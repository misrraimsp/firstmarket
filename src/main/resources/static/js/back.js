'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let i;
    let backButtons = document.getElementsByClassName("backButton");

    for (i = 0; i < backButtons.length; i++) {
        backButtons[i].addEventListener("click", function () {window.history.back();});
    }

}, false);