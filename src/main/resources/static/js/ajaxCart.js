/*

*/

'use strict';

document.addEventListener("DOMContentLoaded", function() {
    let buttons = document.getElementsByClassName("addBookToCart");
    let i = 0;
    for (; i < buttons.length; i++){
        let button = buttons[i];
        button.addEventListener("click", function() {
            let xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("cart").setAttribute("data-count", this.responseText);
                }
            };
            xhttp.open("GET", "http://localhost:8080/firstmarket/user/addBook/" + button.getAttribute("id"), true);
            xhttp.send();
        }, false);
    }
}, false);