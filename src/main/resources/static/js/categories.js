/*

 */

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let div, a;

    let jsonString = '{"name":"cat1", "id":1, "children":[{"name":"cat2", "id":2, "children":[{"name":"cat2-1", "id":3, "children":[]}, {"name":"cat2-2", "id":4, "children":[]}]}, {"name":"cat3", "id":5, "children":[{"name":"cat3-1", "id":6, "children":[]}, {"name":"cat3-2", "id":7, "children":[]}]}, {"name":"cat4", "id":8, "children":[{"name":"cat4-1", "id":9, "children":[]}, {"name":"cat4-2", "id":10, "children":[]}]}]}'

    let categories = JSON.parse(jsonString);

    //alert(categories.name);

    /*
    for (let j = 0; j < categories.children.length; j++){
        let child = categories.children[j];
        alert(child.name);
    }
     */

    var para = document.createElement("p");
    var node = document.createTextNode("This is a new paragraph.");
    para.appendChild(node);

    var input = document.getElementById("input");

    input.appendChild(para);


    var button = document.createElement("a");
    button.setAttribute("class", "btn btn-outline-info btn-block");
    button.setAttribute("href", "http://localhost:8080/firstmarket/home");
    button.innerHTML = "Dynamic Home Button";

    input.insertBefore(button, para);


    let divControl = document.createElement("div");
    divControl.setAttribute("class", "d-flex align-items-baseline my-1 p-1 border rounded shadow-sm");

    let divCatName = document.createElement("div");
    divCatName.setAttribute("class", "mr-auto");

    //<a class="text-dark" href="#cat1" data-toggle="collapse" style="text-decoration: none">
    let aCatName = document.createElement("a");
    aCatName.setAttribute("class", "text-dark");
    aCatName.setAttribute("href", "#jsonCat2");
    aCatName.setAttribute("data-toggle", "collapse");
    aCatName.setAttribute("style", "text-decoration: none");
    aCatName.innerHTML = "jsonCat2";

    let divCatEdit = document.createElement("div");
    divCatEdit.setAttribute("class", "mr-1");

    //<a class="text-dark" href="#cat1" data-toggle="collapse" style="text-decoration: none">
    let aEditCat = document.createElement("a");
    aCatName.setAttribute("class", "text-dark");
    aCatName.setAttribute("href", "#jsonCat2");
    aCatName.setAttribute("data-toggle", "collapse");
    aCatName.setAttribute("style", "text-decoration: none");
    aCatName.innerHTML = "jsonCat2";


    input.appendChild(divControl);
    //input.appendChild(div);


}, false);