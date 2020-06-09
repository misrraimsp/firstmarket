'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let collapseControls = document.querySelectorAll('a[data-toggle="collapse"]');
    for (let i = 0; i < collapseControls.length; i++) {
        collapseControls[i].addEventListener("click", function (e) {
            const control = e.target;
            control.innerHTML = (control.innerHTML === "+ info") ? "- info" : "+ info";
        }, false);
    }

}, false);