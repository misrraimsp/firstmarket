/*

*/

'use strict';


document.addEventListener("DOMContentLoaded", function() {
    let ajaxObject, i, button;

    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);

    //addBook
    let addBookButtons = document.getElementsByClassName("addBook");
    for (i = 0; i < addBookButtons.length; i++){
        button = addBookButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/ajaxCart/addBook/" + id,
                true);
            ajaxObject.setRequestHeader('isAjaxCartRequested', '1');
            ajaxObject.send();
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                    // snackbar
                    let sbar = document.getElementById("snackbar");
                    sbar.className = "show";
                    setTimeout(function(){ sbar.className = sbar.className.replace("show", ""); }, 3000);
                }
                if (this.readyState == 4 && this.status == 401) {
                    document.getElementById("loginLink").click();
                }
            };
        }, false);
    }

    //incrementItem
    let incrementItemButtons = document.getElementsByClassName("incrementItem");
    for (i = 0; i < incrementItemButtons.length; i++){
        button = incrementItemButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/ajaxCart/incrementItem/" + id,
                true);
            ajaxObject.setRequestHeader('isAjaxCartRequested', '1');
            ajaxObject.send();
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                    document.getElementById("iq-" + id).innerHTML++;
                }
                if (this.readyState == 4 && this.status == 401) {
                    document.getElementById("loginLink").click();
                }
            };
        }, false);
    }

    //decrementItem
    let decrementItemButtons = document.getElementsByClassName("decrementItem");
    for (i = 0; i < decrementItemButtons.length; i++){
        button = decrementItemButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/ajaxCart/decrementItem/" + id,
                true);
            ajaxObject.setRequestHeader('isAjaxCartRequested', '1');
            ajaxObject.send();
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
                if (this.readyState == 4 && this.status == 401) {
                    document.getElementById("loginLink").click();
                }
            };
        }, false);
    }

    //removeItem
    let removeItemButtons = document.getElementsByClassName("removeItem");
    for (i = 0; i < removeItemButtons.length; i++){
        button = removeItemButtons[i];
        button.addEventListener("click", function(e) {
            ajaxObject = new XMLHttpRequest();
            let id = extractNumber(e.currentTarget.getAttribute("id"));
            ajaxObject.open(
                "GET",
                "http://localhost:8080/firstmarket/ajaxCart/removeItem/" + id,
                true);
            ajaxObject.setRequestHeader('isAjaxCartRequested', '1');
            ajaxObject.send();
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                    document.getElementById("i-" + id).style.display = "none";
                }
                if (this.readyState == 4 && this.status == 401) {
                    document.getElementById("loginLink").click();
                }
            };
        }, false);
    }

}, false);