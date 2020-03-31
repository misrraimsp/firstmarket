/*

 */

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let divControl, divCatName, divCatEdit, divCatDelete;
    let aCatName, aEditCat, aDeleteCat;
    let editIcon, deleteIcon;

    let jsonString = '{"name":"cat1", "id":1, "children":[{"name":"cat2", "id":2, "children":[{"name":"cat2-1", "id":3, "children":[]}, {"name":"cat2-2", "id":4, "children":[]}]}, {"name":"cat3", "id":5, "children":[{"name":"cat3-1", "id":6, "children":[]}, {"name":"cat3-2", "id":7, "children":[]}]}, {"name":"cat4", "id":8, "children":[{"name":"cat4-1", "id":9, "children":[]}, {"name":"cat4-2", "id":10, "children":[]}]}]}'

    let categories = JSON.parse(jsonString);

    let input = document.getElementById("input");


    for (let i = 0; i < categories.children.length; i++){
        let child = categories.children[i];

        divControl = document.createElement("div");
        divControl.setAttribute("class", "d-flex align-items-baseline my-1 p-1 border rounded shadow-sm");

        divCatName = document.createElement("div");
        divCatName.setAttribute("class", "mr-auto");

        //<a class="text-dark" href="#cat1" data-toggle="collapse" style="text-decoration: none">
        aCatName = document.createElement("a");
        aCatName.setAttribute("class", "text-dark");
        aCatName.setAttribute("href", "#" + child.name);
        aCatName.setAttribute("data-toggle", "collapse");
        aCatName.setAttribute("style", "text-decoration: none");
        aCatName.innerHTML = child.name;

        divCatEdit = document.createElement("div");
        divCatEdit.setAttribute("class", "mr-1");

        //<a class="btn btn-outline-info btn-sm" th:href="@{/home}"></a>
        aEditCat = document.createElement("a");
        aEditCat.setAttribute("class", "btn btn-outline-info btn-sm");
        aEditCat.setAttribute("href", "http://localhost:8080/firstmarket/admin/editCategory/" + child.id);

        //<i class="fas fa-pen"></i>
        editIcon = document.createElement("i");
        editIcon.setAttribute("class", "fas fa-pen");

        divCatDelete = document.createElement("div");

        //<a class="btn btn-outline-danger btn-sm" th:href="@{/home}"></a>
        aDeleteCat = document.createElement("a");
        aDeleteCat.setAttribute("class", "btn btn-outline-danger btn-sm");
        aDeleteCat.setAttribute("href", "http://localhost:8080/firstmarket/admin/deleteCategory/" + child.id);

        //<i class="fas fa-trash-alt"></i>
        deleteIcon = document.createElement("i");
        deleteIcon.setAttribute("class", "fas fa-trash-alt");

        divCatName.appendChild(aCatName);

        aEditCat.appendChild(editIcon);
        divCatEdit.appendChild(aEditCat);

        aDeleteCat.appendChild(deleteIcon);
        divCatDelete.appendChild(aDeleteCat);

        divControl.appendChild(divCatName);
        divControl.appendChild(divCatEdit);
        divControl.appendChild(divCatDelete);

        input.appendChild(divControl);

    }


}, false);