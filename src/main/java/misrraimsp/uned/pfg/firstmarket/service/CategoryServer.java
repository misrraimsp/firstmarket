package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.data.CatPathRepository;
import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServer {

    private CategoryRepository categoryRepository;
    private CatPathRepository catPathRepository;
    private BookRepository bookRepository;

    private TreeNode<Category> rootCategoryNode; //root node of category tree
    private List<CatPath> directPaths; //set of first-order relations among categories

    @Autowired
    public CategoryServer(CategoryRepository categoryRepository,
                          CatPathRepository catPathRepository,
                          BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.catPathRepository = catPathRepository;
        this.bookRepository = bookRepository;
    }

    public void loadCategories() {
        rootCategoryNode = new TreeNode<>(this.getRootCategory());
        directPaths = catPathRepository.getDirectPaths();
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
                indentedCategory.setName("|" +
                        getIndent(node.getLevel()) +
                        "> " +
                        node.getData().getName());
            }
            list.add(indentedCategory);
        }
        return list;
    }

    /**
     * ---- Main way of getting root category: Delegating on crafted SQL query
     *      (see categoryRepository interface)
     */
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

    @Transactional
    public void persistCategory(Category category) {
        //Update Category info
        Category savedCategory =  categoryRepository.save(category);

        //Update CatPath info
        for (CatPath ancestorCP : catPathRepository.getCatPathsByDescendant(category.getParent())){
            CatPath nuevoCP = new CatPath();
            nuevoCP.setAncestor(ancestorCP.getAncestor());
            nuevoCP.setDescendant(savedCategory);
            nuevoCP.setPath_length(1 + ancestorCP.getPath_length());
            catPathRepository.save(nuevoCP);
        }
        CatPath nuevoCP = new CatPath();
        nuevoCP.setAncestor(savedCategory);
        nuevoCP.setDescendant(savedCategory);
        nuevoCP.setPath_length(0);
        catPathRepository.save(nuevoCP);
    }

    @Transactional
    public void editCategory(Category modifiedCategory) {
        if (hasCategoryTreeModification(modifiedCategory)){
            //Update Category info
            categoryRepository.updateName(modifiedCategory.getId(), modifiedCategory.getName());
            categoryRepository.updateParentById(modifiedCategory.getId(), modifiedCategory.getParent().getId());

            //Update CatPath info
            catPathRepository.deleteCatPathsFromAncestorsToDescendantsOf(modifiedCategory.getId());
            for (CatPath ancestorCP : catPathRepository.getCatPathsByDescendant(modifiedCategory.getParent())){
                for (CatPath descendantCP : catPathRepository.getCatPathsByAncestor(modifiedCategory)){
                    CatPath nuevoCP = new CatPath();
                    nuevoCP.setAncestor(ancestorCP.getAncestor());
                    nuevoCP.setDescendant(descendantCP.getDescendant());
                    nuevoCP.setPath_length(1 + ancestorCP.getPath_length() + descendantCP.getPath_length());
                    catPathRepository.save(nuevoCP);
                }
            }
        }
        else {
            //Update Category info
            categoryRepository.updateName(modifiedCategory.getId(), modifiedCategory.getName());
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        //reducir -1 los catpath de los ancestros a los descendientes de la categoría a eliminar
        catPathRepository.reduceCatPathsFromAncestorsToDescendantsOf(id);
        //eliminar los catpath que tienen a la categoría a eliminar
        catPathRepository.deleteCatPathsOf(id);
        //el abuelo de las categorías hijas de la categoría a eliminar pasa a ser su padre
        Category deletingCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + id));
        categoryRepository.updateParentByParentId(id, deletingCategory.getParent().getId());
        //los libros con la categoría a eliminar pasan a estar vinculados a la categoría del padre
        bookRepository.updateCategoryByCategoryId(id, deletingCategory.getParent().getId());
        //eliminar la categoría
        categoryRepository.delete(deletingCategory);
    }

    private void populate(TreeNode<Category> node) {
        for (Category c : getChildren(node.getData())) {
            populate(node.addChild(c));
        }
    }

    private List<Category> getChildren(Category c){
        List<Category> children = new ArrayList<>();
        for (CatPath cp : directPaths) {
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