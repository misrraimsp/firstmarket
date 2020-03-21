/*

 */

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let acc = document.getElementsByClassName("accordion");
    let i, panel;

    for (i = 0; i < acc.length; i++) {
        acc[i].addEventListener("click", function() {
            this.classList.toggle("active");
            panel = this.nextElementSibling;
            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
            } else {
                panel.style.maxHeight = panel.scrollHeight + "px";
            }
        });
    }

    document.getElementById("accCategory").click();

}, false);

