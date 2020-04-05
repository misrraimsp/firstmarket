/*

 */

'use strict';

document.addEventListener("custom-htmlReady", function() {

    // change style if expanded
    let collapsers = document.querySelectorAll('button[collapser="categoryCollapser"]');
    let button;
    for (let i = 0; i < collapsers.length; i++) {
        collapsers[i].addEventListener("click", function (e) {
            button = e.target;
            if (button.getAttribute("aria-expanded") === "true") {
                button.setAttribute("class", "btn btn-sm btn-outline-secondary btn-block text-left");
            } else {
                button.setAttribute("class", "btn btn-sm btn-secondary btn-block text-left");
            }
        }, false);
    }
    
}, false);