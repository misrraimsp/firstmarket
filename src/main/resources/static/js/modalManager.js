'use strict';

document.addEventListener("custom-htmlReady", function() {

    //env variable
    let hostUrl = document.getElementById("host").getAttribute("host-address");

    let extractId = (str) => str.slice(str.indexOf("-") + 1);

    let modalTrigger, id;
    let modalYes = document.getElementById("theModalYes");
    let modalTriggers = document.getElementsByClassName("modalTrigger");
    let action = document.getElementById("action").getAttribute("modal-action");

    for (let i = 0; i < modalTriggers.length; i++) {
        modalTriggers[i].addEventListener("click", function (e) {
            modalTrigger = e.currentTarget;
            id = extractId(modalTrigger.getAttribute("id"));
            modalYes.setAttribute("href", hostUrl + 'admin/' + action + '/' + id);
            $("#theModal").modal();
        }, false);
    }

}, false);