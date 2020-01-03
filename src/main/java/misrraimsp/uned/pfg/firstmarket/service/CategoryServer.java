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
        rootCategoryNode = new TreeNode<Category>(categoryRepository.getRootCategory());
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

    public String getIndent(int depth) {
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

    public void saveNewCategory(Long parent_id, String name) {
        //update category table on database
        Category newCategory = new Category();
        newCategory.setName(name);
        Category savedCategory =  categoryRepository.save(newCategory);

        //update catpath table on database
        Category parentCategory = new Category();
        parentCategory.setId(parent_id);
        List<CatPath> catPaths = catPathRepository.getCatPathsByDescendant(parentCategory);
        List<CatPath> newCatPaths = new ArrayList<>();
        CatPath newCatPath = null;
            //links with ancestors
        for (CatPath catPath : catPaths){
            newCatPath = new CatPath();
            newCatPath.setAncestor(catPath.getAncestor());
            newCatPath.setDescendant(savedCategory);
            newCatPath.setPath_length(1 + catPath.getPath_length());
            newCatPaths.add(newCatPath);
        }
            //self link
        newCatPath = new CatPath();
        newCatPath.setAncestor(savedCategory);
        newCatPath.setDescendant(savedCategory);
        newCatPath.setPath_length(0);
        newCatPaths.add(newCatPath);
            //persist
        catPathRepository.saveAll(newCatPaths);
    }
}