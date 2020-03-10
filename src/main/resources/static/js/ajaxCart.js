/*

*/

'use strict';

let extractNumber = (str) => str.slice(str.indexOf("-") + 1);

document.addEventListener("DOMContentLoaded", function() {
    let ajaxObject, i, button;

    //addBook
    let addBookButtons = document.getElementsByClassName("addBook");
    for (i = 0; i < addBookButtons.length; i++){
        button = addBookButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                }
            };
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/user/addBook/" + id,
                true);
            ajaxObject.send();
        }, false);
    }

    //incrementItem
    let incrementItemButtons = document.getElementsByClassName("incrementItem");
    for (i = 0; i < incrementItemButtons.length; i++){
        button = incrementItemButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                    document.getElementById("iq-" + id).innerHTML++;
                }
            };
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/user/incrementItem/" + id,
                true);
            ajaxObject.send();
        }, false);
    }

    //decrementItem
    let decrementItemButtons = document.getElementsByClassName("decrementItem");
    for (i = 0; i < decrementItemButtons.length; i++){
        button = decrementItemButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                    let qElement = document.getElementById("iq-" + id);
                    if (Number(qElement.innerHTML) > 1){
                        qElement.innerHTML--;
                    } else {
                        document.getElementById("i-" + id).style.display = "none";
                    }
                }
            };
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/user/decrementItem/" + id,
                true);
            ajaxObject.send();
        }, false);
    }

    //removeItem
    let removeItemButtons = document.getElementsByClassName("removeItem");
    for (i = 0; i < removeItemButtons.length; i++){
        button = removeItemButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                    document.getElementById("i-" + id).style.display = "none";
                }
            };
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/user/removeItem/" + id,
                true);
            ajaxObject.send();
        }, false);
    }

}, false);