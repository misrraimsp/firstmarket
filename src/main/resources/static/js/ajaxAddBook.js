/*

*/

'use strict';


document.addEventListener("DOMContentLoaded", function() {

    // env variable
    let hostUrl = document.getElementById("host").getAttribute("host-address");

    // uninitialized variables
    let xmlHttpRequest, i, id;

    // initialized variables
    let baseUrl = hostUrl + "ajaxCart/addBook/";
    let snackbar = document.getElementById("snackbar");
    let cartIcon = document.getElementById("cartIcon");
    let loginLink = document.getElementById("loginLink");
    let snackbarTimeout = 3000;
    let addBookButtons = document.querySelectorAll("[ajax]");

    // functions
    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);


    // ajax elements event attachment
    for (i = 0; i < addBookButtons.length; i++) {
        addBookButtons[i].addEventListener("click", function(e) {
            id = extractNumber(e.currentTarget.getAttribute("id"));
            xmlHttpRequest = new XMLHttpRequest();
            xmlHttpRequest.open("GET", baseUrl + id, true);
            xmlHttpRequest.setRequestHeader('isAjaxCartRequested', '1');
            xmlHttpRequest.send();
            xmlHttpRequest.onreadystatechange = function() {
                if (this.readyState === 4) {
                    switch (this.status) {
                        case 200:
                            cartIcon.setAttribute("data-count", this.responseText);
                            // snackbar
                            snackbar.className = "show";
                            setTimeout(function() {
                                snackbar.className = snackbar.className.replace("show", "");
                            }, snackbarTimeout);
                            break;
                        case 401:
                            loginLink.click();
                            break;
                        default:
                            console.log(this);
                    }
                }
            };
        }, false);
    }

}, false);