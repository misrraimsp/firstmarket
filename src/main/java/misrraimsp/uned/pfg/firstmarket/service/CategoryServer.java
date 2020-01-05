package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
import misrraimsp.uned.pfg.firstmarket.data.CatPathRepository;
import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServer {

    private CategoryRepository categoryRepository;
    private CatPathRepository catPathRepository;

    private TreeNode<Category> rootCategoryNode; //root node of category tree
    private List<CatPath> directPaths; //set of first-order relations among categories

    private CategoryViewBuilder categoryViewBuilder;

    @Autowired
    public CategoryServer(CategoryRepository categoryRepository,
                          CatPathRepository catPathRepository,
                          CategoryViewBuilder categoryViewBuilder) {
        this.categoryRepository = categoryRepository;
        this.catPathRepository = catPathRepository;
        this.categoryViewBuilder = categoryViewBuilder;
    }

    public TreeNode<Category> getRootCategoryNode() {
        return rootCategoryNode;
    }

    public void loadCategories() {
        rootCategoryNode = new TreeNode<Category>(this.getRootCategory());
        directPaths = catPathRepository.getDirectPaths();
        populate(rootCategoryNode);
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
        for (int i = 0; i < depth; i++) {
            sb.append('-');
        }
        return sb.toString();
    }

    public String getCategoriesOnHtml() {
        return categoryViewBuilder.buildHtml(rootCategoryNode);
    }

    public List<Category> getIndentedCategories(){
        List<Category> list = new ArrayList<>();
        for (TreeNode<Category> node : rootCategoryNode) {
            Category indentedCategory = new Category();
            indentedCategory.setId(node.getData().getId());
            indentedCategory.setName(getIndent(node.getLevel()) + node.getData().getName());
            list.add(indentedCategory);
        }
        return list;
    }

    public void persistCategory(Category category) {
        //update category table on database
        Category savedCategory =  categoryRepository.save(category);
        //update cat_path table on database
        List<CatPath> templateCatPaths = catPathRepository.getCatPathsByDescendant(category.getParent());
        List<CatPath> newCatPaths = new ArrayList<>();
        CatPath catPath = null;
            //links with ancestors
        for (CatPath template : templateCatPaths){
            catPath = new CatPath();
            catPath.setAncestor(template.getAncestor());
            catPath.setDescendant(savedCategory);
            catPath.setPath_length(1 + template.getPath_length());
            newCatPaths.add(catPath);
        }
            //self link
        catPath = new CatPath();
        catPath.setAncestor(savedCategory);
        catPath.setDescendant(savedCategory);
        catPath.setPath_length(0);
        newCatPaths.add(catPath);
            //persist
        catPathRepository.saveAll(newCatPaths);
    }

    /**
     * ---- Main way of getting root category: Delegating on crafted SQL query
     *      (see categoryRepository interface)
     */
    private Category getRootCategory(){
        return categoryRepository.getRootCategory();
    }

    /**
     * ---- Alternative way of getting root category: Fetching all categories
     *      and iterating through them until meeting root condition
     *
    private Category getRootCategory(){
        for (Category category : categoryRepository.findAll()){
            if (category.isRootCategory()){
               return category;
            }
        }
        return null;
    }
    */

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

    public void editCategory(Category modifiedCategory) {
        if (hasCategoryTreeModification(modifiedCategory)){
            System.out.println("modificación compleja");
        }
        else {
            System.out.println("modificación simple");
            categoryRepository.save(modifiedCategory);
        }
    }

    private boolean hasCategoryTreeModification(Category modifiedCategory) {
        Category originalCategory = categoryRepository.findById(modifiedCategory.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + modifiedCategory.getId()));
        return !(originalCategory.getParent().getId().equals(modifiedCategory.getParent().getId()));
    }
}