package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
import misrraimsp.uned.pfg.firstmarket.data.CatPathRepository;
import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CategoryServer {

    private CategoryRepository categoryRepository;
    private CatPathRepository catPathRepository;

    private TreeNode<Category> rootCategoryNode; //root node of category tree
    private Set<CatPath> directPaths; //set of first-order relations among categories

    @Autowired
    public CategoryServer(CategoryRepository categoryRepository, CatPathRepository catPathRepository) {
        this.categoryRepository = categoryRepository;
        this.catPathRepository = catPathRepository;
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

    private Set<Category> getChildren(Category c){
        Set<Category> children = new HashSet<Category>();
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
}