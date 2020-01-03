package misrraimsp.uned.pfg.firstmarket.service;

import misrraimsp.uned.pfg.firstmarket.adt.TreeNode;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryViewBuilder {

    public String buildHtml(TreeNode<Category> rootCategoryNode) {
        return unroll(rootCategoryNode);
    }

    private String unroll(TreeNode<Category> node) {
        String html = "";
        if (!node.isRoot()){
            html = "<a href='/firstmarket/admin/editCategory/" + node.getData().getId() + "'><b>" + node.getData().getName() + "</b></a>";
        }
        for (TreeNode<Category> child : node.getChildren()){
            if (child.isLeaf()){
                html += "<a href='/firstmarket/admin/editCategory/" + child.getData().getId() + "'>" + child.getData().getName() + "</a>";
            }
            else {
                //button
                html += "<button class='dropdown-btn'><i>" + child.getData().getName() + "</i>";
                html += "<i class='fa fa-caret-down'></i>";
                html += "</button>";
                //dropdown container
                html += "<div class='dropdown-container'>";
                html += unroll(child);
                html += "</div>";
            }
        }
        return html;
    }
}
