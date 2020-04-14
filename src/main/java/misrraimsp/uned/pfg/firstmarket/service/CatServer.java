package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
import misrraimsp.uned.pfg.firstmarket.adt.dto.CategoryForm;
import misrraimsp.uned.pfg.firstmarket.converter.CategoryConverter;
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
    private CategoryConverter categoryConverter;

    private TreeNode<Category> rootCategoryNode; //root node of category tree
    private List<Catpath> directPaths; //set of first-order relations among categories

    @Autowired
    public CatServer(CategoryRepository categoryRepository,
                     CatpathRepository catpathRepository,
                     CategoryConverter categoryConverter) {

        this.categoryRepository = categoryRepository;
        this.catpathRepository = catpathRepository;
        this.categoryConverter = categoryConverter;
    }

    public void loadCategories() {
        rootCategoryNode = new TreeNode<>(this.getRootCategory());
        directPaths = catpathRepository.getDirectPaths();
        populate(rootCategoryNode);
    }

    public List<Category> getChildren(Category c) {
        List<Category> children = new ArrayList<>();
        for (Catpath cp : directPaths) {
            if (c.equals(cp.getAncestor())) {
                children.add(cp.getDescendant());
            }
        }
        return children;
    }

    public List<Category> getMainCategories() {
        return this.getChildren(rootCategoryNode.getData());
    }

    public List<Category> getIndentedCategories(){
        List<Category> list = new ArrayList<>();
        for (TreeNode<Category> node : rootCategoryNode) {
            Category indentedCategory = new Category();
            indentedCategory.setId(node.getData().getId());
            indentedCategory.setParent(node.getData().getParent());
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

    public Category getRootCategory(){
        return categoryRepository.getRootCategory();
    }

    /**
     * This method returns the category tree sequence from root category to parameter category (without including it)
     * @param category
     * @return
     */
    public List<Category> getCategorySequence(Category category) {
        List<Category> sequence = new ArrayList<>();
        catpathRepository.findByDescendantIdAndSizeIsGreaterThanOrderBySizeDesc(category.getId(), 0).forEach(
                catpath -> sequence.add(catpath.getAncestor())
        );
        return sequence;
    }

    public List<Category> getDescendants(Long categoryId) {
        List<Category> list = new ArrayList<>();
        TreeNode<Category> subtreeRoot = null;
        for (TreeNode<Category> node : rootCategoryNode) {
            if (node.getData().getId() == categoryId) {
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

    // dev method
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public void persist(Category category) {
        //Update Category info
        Category savedCategory =  categoryRepository.save(category);

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

    public String getJSONStringCategories() {
        return this.jsonStringify(this.getRootCategory());
    }

    private String jsonStringify(Category category) {
        String localString = "";
        localString += "{ \"name\":\"" + category.getName() + "\", \"id\":" + category.getId() + ", \"children\":[";
        for (Category child : this.getChildren(category)) {
            localString += jsonStringify(child) + ",";
        }
        if (localString.charAt(localString.length() - 1) == ',') {
            localString = localString.substring(0, localString.length() - 1);
        }
        localString += "]}";
        return localString;
    }

    public CategoryForm convertCategoryToCategoryForm(Category category) {
        return categoryConverter.convertCategoryToCategoryForm(category);
    }

    public Category convertCategoryFormToCategory(CategoryForm categoryForm) {
        return categoryConverter.convertCategoryFormToCategory(categoryForm);
    }
}