/*

 */

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    // function definitions
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

        // Control
        divControl = document.createElement("div");
        divControl.setAttribute("class", "d-flex flex-row-reverse mt-1 bg-light");

        // Category name
        divCatName = document.createElement("div");
        divCatName.setAttribute("class", "flex-grow-1 order-3");

        catName = document.createElement("button");
        catName.setAttribute("class", "btn btn-sm btn-outline-secondary btn-block text-left");
        if (category.children.length !== 0) {
            catName.setAttribute("collapser", "categoryCollapser");
            catName.setAttribute("data-toggle", "collapse");
            catName.setAttribute("data-target", "#" + childrenId);
        }
        catName.innerHTML = category.name;

        // Category edition
        divCatEdit = document.createElement("div");
        divCatEdit.setAttribute("class", "mx-1 order-2");

        editCat = document.createElement("a");
        editCat.setAttribute("class", "btn btn-sm btn-outline-secondary");
        editCat.setAttribute("href", "http://localhost:8080/firstmarket/admin/editCategory/" + category.id);

        editIcon = document.createElement("i");
        editIcon.setAttribute("class", "fas fa-pen");

        // Category deletion
        divCatDelete = document.createElement("div");
        divCatDelete.setAttribute("class", "order-1");

        deleteCat = document.createElement("button");
        deleteCat.setAttribute("class", "modalTrigger btn btn-sm btn-outline-secondary");
        deleteCat.setAttribute("id", "del-" + category.id);

        deleteIcon = document.createElement("i");
        deleteIcon.setAttribute("class", "fas fa-trash-alt");

        // Connexions
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
        divCollapse.setAttribute("class", "collapse ml-5");
        divCollapse.setAttribute("id", childrenId);

        divNewHook = document.createElement("div");
        divNewHook.setAttribute("class", "d-flex flex-column");
        divNewHook.setAttribute("id", newHookId);

        divCollapse.appendChild(divNewHook);

        hook.appendChild(divCollapse);
    };

    // build html
    let categories = JSON.parse(jsonStringCategories);
    for (let i = 0; i < categories.children.length; i++){
        build(categories.children[i],"root-hook");
    }
    document.dispatchEvent(new CustomEvent('custom-htmlReady'));

}, false);