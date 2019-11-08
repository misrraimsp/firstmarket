<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page import="pfg.firstmarket.adt.TreeNode"  %>
<%@ page import="pfg.firstmarket.model.Category"  %>
<%@ page import="pfg.firstmarket.model.services.CategoryServer"  %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta charset="UTF-8">
		<title>Category Manager</title>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<style>
			body {
			font-family: "Lato", sans-serif;
			}

			/* Fixed sidenav, full height */
			.sidenav {
			  height: 100%;
			  width: 250px;
			  position: fixed;
			  z-index: 1;
			  top: 0;
			  left: 0;
			  background-color: #111;
			  overflow-x: hidden;
			  padding-top: 20px;
			}

			/* Style the sidenav links and the dropdown button */
			.sidenav a, .dropdown-btn {
			  padding: 6px 8px 6px 16px;
			  text-decoration: none;
			  font-size: 15px;
			  color: #818181;
			  display: block;
			  border: none;
			  background: none;
			  width: 100%;
			  text-align: left;
			  cursor: pointer;
			  outline: none;
			}

			/* On mouse-over */
			.sidenav a:hover, .dropdown-btn:hover {
			  color: #f1f1f1;
			}
	
			/* Main content */
			.main {
			  margin-left: 250px; /* Same as the width of the sidenav */
			  font-size: 20px; /* Increased text to enable scrolling */
			  padding: 0px 10px;
			}
	
			/* Add an active class to the active dropdown button */
			.active {
			  background-color: green;
			  color: white;
			}
	
			/* Dropdown container (hidden by default). Optional: add a lighter background color and some left padding to change the design of the dropdown content */
			.dropdown-container {
			  display: none;
			  background-color: #262626;
			  padding-left: 8px;
			}

			/* Optional: Style the caret down icon */
			.fa-caret-down {
			  float: right;
			  padding-right: 8px;
			}
	
			/* Some media queries for responsiveness */
			@media screen and (max-height: 450px) {
			  .sidenav {padding-top: 15px;}
			  .sidenav a {font-size: 18px;}
			}
		</style>
	</head>
	<body>	
<%!
public String unroll(TreeNode<Category> node) {
	String code = "";
	if (!node.isRoot()){
		code = "<a href='/firstmarket/fc/admin/categories/editCategory?category_id=" + node.getData().getCategory_id() + "'><b>" + node.getData().getName() + "</b></a>";
	}
	for (TreeNode<Category> child : node.getChildren()){
		if (child.isLeaf()){
			code += "<a href='/firstmarket/fc/admin/categories/editCategory?category_id=" + child.getData().getCategory_id() + "'>" + child.getData().getName() + "</a>";
		}
		else {
			//button
			code += "<button class='dropdown-btn'><i>" + child.getData().getName() + "</i>";
				code += "<i class='fa fa-caret-down'></i>";
			code += "</button>";
			//dropdown container
			code += "<div class='dropdown-container'>";
				code += unroll(child);
			code += "</div>";
		}
	}
	return code;
}

public String deployCategories(CategoryServer cs) {
	return unroll(cs.getRootCategoryNode());
}
%>
		<div class="sidenav">
			<%=deployCategories((CategoryServer)application.getAttribute("categoryServer"))%>
		</div>
		
		<div class="main">
			<h2>Category Manager</h2>
			<p><a href="/firstmarket/fc/admin">go back</a></p>
			<p><a href="/firstmarket/fc/admin/categories/newCategory">new category</a></p>
			<p>Please, to edit some category just click on its link</p>
		</div>
		<script>
			/* Loop through all dropdown buttons to toggle between hiding and showing its dropdown content - This allows the user to have multiple dropdowns without any conflict */
			var dropdown = document.getElementsByClassName("dropdown-btn");
			var i;
			
			for (i = 0; i < dropdown.length; i++) {
				dropdown[i].addEventListener("click", function() {
					this.classList.toggle("active");
					var dropdownContent = this.nextElementSibling;
					if (dropdownContent.style.display === "block") {
						dropdownContent.style.display = "none";
					}
					else {
						dropdownContent.style.display = "block";
					}
				});
			}
		</script>
	</body>
</html>