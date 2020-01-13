package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
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
        rootCategoryNode = new TreeNode<>(this.getRootCategory());
        directPaths = catPathRepository.getDirectPaths();
        populate(rootCategoryNode);
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
            categoryRepository.updateParent(modifiedCategory.getId(), modifiedCategory.getParent().getId());

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

    public void deleteCategory(Category category) {
        System.out.println("delete category TODO");

        //en la tabla catpath -1 en los caminos de sus ancestros a sus descendientes
        //en la tabla catpath eliminar todos los caminos que la tienen como descendiente
        //en la tabla catpath eliminar todos los caminos que la tienen como ancestro

        //en la tabla category: a todos sus hijos cambiar el padre a su padre
        //quitarla de la tabla de categorias

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

    private boolean hasCategoryTreeModification(Category modifiedCategory) {
        Category originalCategory = categoryRepository.findById(modifiedCategory.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + modifiedCategory.getId()));
        return !(originalCategory.getParent().getId().equals(modifiedCategory.getParent().getId()));
    }

}