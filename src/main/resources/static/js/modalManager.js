/*

 */

'use strict';

document.addEventListener("custom-htmlReady", function() {

    let extractId = (str) => str.slice(str.indexOf("-") + 1);

    let modalTrigger, id;
    let modalYes = document.getElementById("theModalYes");
    let modalTriggers = document.getElementsByClassName("modalTrigger");

    for (let i = 0; i < modalTriggers.length; i++) {
        modalTriggers[i].addEventListener("click", function (e) {
            modalTrigger = e.currentTarget;
            id = extractId(modalTrigger.getAttribute("id"));
            modalYes.setAttribute("href", baseUrl + id);
            $("#theModal").modal();
        }, false);
    }

}, false);