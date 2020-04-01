/*

 */

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let categories = JSON.parse(jsonStringCategories);

    let build = function (category, hookId) {
        let hook = document.getElementById(hookId);
        let childrenId = "cat-" + category.id + "-children";
        let newHookId = childrenId + "-hook";

        buildControl(category, hook, childrenId);
        if (category.children.length !== 0) {
            buildCollapsible(category, hook, childrenId, newHookId);
            for (let i = 0; i < category.children.length; i++) {
                build(category.children[i], newHookId);
            }
        }
    };

    let buildControl = function (category, hook, childrenId) {

        let divControl, divCatName, divCatEdit, divCatDelete;
        let catName, editCat, deleteCat;
        let editIcon, deleteIcon;

        divControl = document.createElement("div");
        divControl.setAttribute("class", "d-flex align-items-baseline my-1 p-1 border rounded shadow-sm");

        divCatName = document.createElement("div");
        divCatName.setAttribute("class", "mr-auto");

        if (category.children.length !== 0) {
            //<a class="text-dark" href="#cat1" data-toggle="collapse" style="text-decoration: none">
            catName = document.createElement("a");
            catName.setAttribute("class", "text-dark");
            catName.setAttribute("href", "#" + childrenId);
            catName.setAttribute("data-toggle", "collapse");
            catName.setAttribute("style", "text-decoration: none");
        } else {
            //<span class="text-dark"/>
            catName = document.createElement("span");
            catName.setAttribute("class", "text-dark");
        }
        catName.innerHTML = category.name;

        divCatEdit = document.createElement("div");
        divCatEdit.setAttribute("class", "mr-1");

        //<a class="btn btn-outline-info btn-sm" th:href="@{/home}"></a>
        editCat = document.createElement("a");
        editCat.setAttribute("class", "btn btn-outline-info btn-sm");
        editCat.setAttribute("href", "http://localhost:8080/firstmarket/admin/editCategory/" + category.id);

        //<i class="fas fa-pen"></i>
        editIcon = document.createElement("i");
        editIcon.setAttribute("class", "fas fa-pen");

        divCatDelete = document.createElement("div");

        //<a class="btn btn-outline-danger btn-sm" th:href="@{/home}"></a>
        deleteCat = document.createElement("a");
        deleteCat.setAttribute("class", "btn btn-outline-danger btn-sm");
        deleteCat.setAttribute("href", "http://localhost:8080/firstmarket/admin/deleteCategory/" + category.id);

        //<i class="fas fa-trash-alt"></i>
        deleteIcon = document.createElement("i");
        deleteIcon.setAttribute("class", "fas fa-trash-alt");

        divCatName.appendChild(catName);

        editCat.appendChild(editIcon);
        divCatEdit.appendChild(editCat);

        deleteCat.appendChild(deleteIcon);
        divCatDelete.appendChild(deleteCat);

        divControl.appendChild(divCatName);
        divControl.appendChild(divCatEdit);
        divControl.appendChild(divCatDelete);

        hook.appendChild(divControl);

    };

    let buildCollapsible = function (category, hook, childrenId, newHookId) {

        let divCollapse, divNewHook;

        divCollapse = document.createElement("div");
        divCollapse.setAttribute("class", "collapse m-3");
        divCollapse.setAttribute("id", childrenId);

        divNewHook = document.createElement("div");
        divNewHook.setAttribute("class", "d-flex flex-column");
        divNewHook.setAttribute("id", newHookId);

        divCollapse.appendChild(divNewHook);

        hook.appendChild(divCollapse);
    };


    for (let i = 0; i < categories.children.length; i++){
        build(categories.children[i],"root-hook");
    }


}, false);