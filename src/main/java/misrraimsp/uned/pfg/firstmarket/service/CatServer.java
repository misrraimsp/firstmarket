package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.data.CatpathRepository;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.model.Catpath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements CATegory and CATpath service layer abstraction
 */
@Service
public class CatServer {

    private CategoryRepository categoryRepository;
    private CatpathRepository catpathRepository;

    private TreeNode<Category> rootCategoryNode; //root node of category tree
    private List<Catpath> directPaths; //set of first-order relations among categories

    @Autowired
    public CatServer(CategoryRepository categoryRepository, CatpathRepository catpathRepository) {
        this.categoryRepository = categoryRepository;
        this.catpathRepository = catpathRepository;
    }

    public void loadCategories() {
        rootCategoryNode = new TreeNode<>(this.getRootCategory());
        directPaths = catpathRepository.getDirectPaths();
        populate(rootCategoryNode);
    }

    public List<Category> getIndentedCategories(){
        List<Category> list = new ArrayList<>();
        for (TreeNode<Category> node : rootCategoryNode) {
            Category indentedCategory = new Category();
            indentedCategory.setId(node.getData().getId());
            if (node == rootCategoryNode){
                indentedCategory.setName(node.getData().getName());
            }
            else{
                indentedCategory.setName("" +
                        getIndent(node.getLevel()) +
                        "" +
                        node.getData().getName());
            }
            list.add(indentedCategory);
        }
        return list;
    }

    private Category getRootCategory(){
        return categoryRepository.getRootCategory();
    }

    public List<Category> getDescendants(Category category) {
        List<Category> list = new ArrayList<>();
        TreeNode<Category> subtreeRoot = null;
        for (TreeNode<Category> node : rootCategoryNode) {
            if (node.getData().equals(category)) {
                subtreeRoot = node;
                break;
            }
        }
        if (subtreeRoot == null) return null;
        for (TreeNode<Category> node : subtreeRoot) {
            list.add(node.getData());
        }
        return list;
    }

    public Catpath save(Catpath catpath) {
        return catpathRepository.save(catpath);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void persist(Category category) {
        //Update Category info
        Category savedCategory =  this.save(category);

        //Update Catpath info
        for (Catpath ancestorCP : catpathRepository.getCatpathsByDescendant(category.getParent())){
            Catpath nuevoCP = new Catpath();
            nuevoCP.setAncestor(ancestorCP.getAncestor());
            nuevoCP.setDescendant(savedCategory);
            nuevoCP.setSize(1 + ancestorCP.getSize());
            this.save(nuevoCP);
        }
        Catpath nuevoCP = new Catpath();
        nuevoCP.setAncestor(savedCategory);
        nuevoCP.setDescendant(savedCategory);
        nuevoCP.setSize(0);
        this.save(nuevoCP);
    }

    @Transactional
    public void edit(Category modifiedCategory) {
        if (hasCategoryTreeModification(modifiedCategory)){
            //Update Category info
            categoryRepository.updateName(modifiedCategory.getId(), modifiedCategory.getName());
            categoryRepository.updateParentById(modifiedCategory.getId(), modifiedCategory.getParent().getId());

            //Update Catpath info
            catpathRepository.deleteCatpathsFromAncestorsToDescendantsOf(modifiedCategory.getId());
            for (Catpath ancestorCP : catpathRepository.getCatpathsByDescendant(modifiedCategory.getParent())){
                for (Catpath descendantCP : catpathRepository.getCatpathsByAncestor(modifiedCategory)){
                    Catpath nuevoCP = new Catpath();
                    nuevoCP.setAncestor(ancestorCP.getAncestor());
                    nuevoCP.setDescendant(descendantCP.getDescendant());
                    nuevoCP.setSize(1 + ancestorCP.getSize() + descendantCP.getSize());
                    this.save(nuevoCP);
                }
            }
        }
        else {
            //Update Category info
            categoryRepository.updateName(modifiedCategory.getId(), modifiedCategory.getName());
        }
    }

    @Transactional
    public void deleteById(Long id) {
        //reducir -1 los catpath de los ancestros a los descendientes de la categoría a eliminar
        catpathRepository.reduceCatpathsFromAncestorsToDescendantsOf(id);
        //eliminar los catpath que tienen a la categoría a eliminar
        catpathRepository.deleteCatpathsOf(id);
        //el abuelo de las categorías hijas de la categoría a eliminar pasa a ser su padre
        Category deletingCategory = this.findCategoryById(id);
        categoryRepository.updateParentByParentId(id, deletingCategory.getParent().getId());
        //eliminar la categoría
        categoryRepository.delete(deletingCategory);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + id));
    }

    private void populate(TreeNode<Category> node) {
        for (Category c : getChildren(node.getData())) {
            populate(node.addChild(c));
        }
    }

    private List<Category> getChildren(Category c){
        List<Category> children = new ArrayList<>();
        for (Catpath cp : directPaths) {
            if (c.equals(cp.getAncestor())) {
                children.add(cp.getDescendant());
            }
        }
        return children;
    }

    private String getIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.pow(2,depth); i++) {
            sb.append("-");
        }
        return sb.toString();
    }

    private boolean hasCategoryTreeModification(Category modifiedCategory) {
        Category originalCategory = categoryRepository.findById(modifiedCategory.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + modifiedCategory.getId()));
        return !(originalCategory.getParent().getId().equals(modifiedCategory.getParent().getId()));
    }

}