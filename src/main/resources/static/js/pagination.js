/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {
    let i, pageButton, pageButtons;

    pageButtons = document.getElementById("pagination").children;

    for (i = 0; i < pageButtons.length; i++){
        pageButton = pageButtons[i];
        pageButton.addEventListener("click", function() {
            alert(pageButton.toString());
        }, false);
    }

}, false);