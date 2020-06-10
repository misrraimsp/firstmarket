'use strict';

document.addEventListener("DOMContentLoaded", function() {

    // function
    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);

    let collapseControls = document.querySelectorAll('a[data-toggle="collapse"]');
    for (let i = 0; i < collapseControls.length; i++) {
        collapseControls[i].addEventListener("click", function (e) {
            const control = e.currentTarget;
            const orderId = extractNumber(control.getAttribute("href"));
            document.getElementById("orderHead-" + orderId).classList.toggle("order-selected");
            const children = control.children;
            for (let j = 0; j < children.length; j++) {
                let child = children[j];
                child.style.display = (child.style.display === "none") ? "inline" : "none";
            }
        }, false);
    }

}, false);