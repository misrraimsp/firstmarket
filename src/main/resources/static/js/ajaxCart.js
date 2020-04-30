/*

*/

'use strict';


document.addEventListener("DOMContentLoaded", function() {

    // variables
    let ajaxObject, i, element, action, id, triggerElement, url;
    let idsToRemove = '';
    let baseUrl = "http://localhost:8080/firstmarket/ajaxCart/";
    let snackbar = document.getElementById("snackbar");
    let snackbarTimeout = 3000;

    // functions
    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);

    let onHttpOk = function(action, id, q) {

        let updatePrices = function(id) {

        };

        document.getElementById("cartIcon").setAttribute("data-count", q);
        switch (action) {
            case 'addBook':
                // snackbar
                snackbar.className = "show";
                setTimeout(function() {
                    snackbar.className = snackbar.className.replace("show", "");
                    }, snackbarTimeout);
                break;
            case 'incrementItem':
                document.getElementById("numOfBooks").innerHTML = q;
                document.getElementById("iq-" + id).innerHTML++;
                updatePrices(id);
                break;
            case 'decrementItem':
                document.getElementById("numOfBooks").innerHTML = q;
                let qElement = document.getElementById("iq-" + id);
                if (Number(qElement.innerHTML) > 1){
                    qElement.innerHTML--;
                } else {
                    document.getElementById("i-" + id).style.display = "none";
                }
                break;
            case 'removeItems':
                document.getElementById("numOfBooks").innerHTML = q;
                //document.getElementById("i-" + id).style.display = "none";
                break;
            default:
                console.log("error state within onHttpOk function: no action found");
        }
    };

    let onHttpUnauthorized = function() {
        document.getElementById("loginLink").click();
    };

    // ajax actions
    let ajaxElements = document.querySelectorAll("[ajax]");
    for (i = 0; i < ajaxElements.length; i++) {
        element = ajaxElements[i];
        element.addEventListener("click", function(e) {
            triggerElement = e.currentTarget;
            action = triggerElement.getAttribute("ajax");
            if (action == 'removeItems') {
                id = null;
                url = baseUrl + action +"?ids=" + idsToRemove;
            }
            else {
                id = extractNumber(triggerElement.getAttribute("id"));
                url = baseUrl + action +"/" + id;
            }
            ajaxObject = new XMLHttpRequest();
            ajaxObject.open("GET", url, true);
            ajaxObject.setRequestHeader('isAjaxCartRequested', '1');
            ajaxObject.send();
            ajaxObject.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    onHttpOk(action, id, this.responseText);
                }
                if (this.readyState == 4 && this.status == 401) {
                    onHttpUnauthorized();
                }
            };
        }, false);
    }
}, false);



/*
for (i = 0; i < addBookButtons.length; i++){
    button = addBookButtons[i];
    button.addEventListener("click", function(e) {
        ajaxObject = new XMLHttpRequest();
        let id = extractNumber(e.currentTarget.getAttribute("id"));
        ajaxObject.open(
            "GET",
            baseUrl + "/addBook/" + id,
            true);
        ajaxObject.setRequestHeader('isAjaxCartRequested', '1');
        ajaxObject.send();
        ajaxObject.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                onHttpOk('addBook', id, this.responseText);
                //document.getElementById("cart").setAttribute("data-count", this.responseText);
                // snackbar
                //let sbar = document.getElementById("snackbar");
                //sbar.className = "show";
                //setTimeout(function(){ sbar.className = sbar.className.replace("show", ""); }, 3000);
            }
            if (this.readyState == 4 && this.status == 401) {
                onHttpUnauthorized();
            }
        };
    }, false);
}

 */
/*
    //incrementItem
    let incrementItemButtons = document.getElementsByClassName("incrementItem");
    for (i = 0; i < incrementItemButtons.length; i++){
        element = incrementItemButtons[i];
        element.addEventListener("click", function(e) {
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
                    onHttpOk(id);
                }
                if (this.readyState == 4 && this.status == 401) {
                    document.getElementById("loginLink").click();
                }
            };
        }, false);
    }
*/
/*
    //decrementItem
    let decrementItemButtons = document.getElementsByClassName("decrementItem");
    for (i = 0; i < decrementItemButtons.length; i++){
        element = decrementItemButtons[i];
        element.addEventListener("click", function(e) {
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
*/
/*
    //removeItem
    let removeItemButtons = document.getElementsByClassName("removeItem");
    for (i = 0; i < removeItemButtons.length; i++){
        element = removeItemButtons[i];
        element.addEventListener("click", function(e) {
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
*/