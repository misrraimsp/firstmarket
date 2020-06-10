'use strict';

document.addEventListener("DOMContentLoaded", function() {

    // function
    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);

    $(".collapse").on('show.bs.collapse', function(e){
        const orderId = extractNumber(e.target.getAttribute("id"));
        document.getElementById("orderHead-" + orderId).classList.add("order-selected");
        document.getElementById("plus-" + orderId).style.display = "none";
        document.getElementById("minus-" + orderId).style.display = "inline";
    });

    $(".collapse").on('hide.bs.collapse', function(e){
        const orderId = extractNumber(e.target.getAttribute("id"));
        document.getElementById("orderHead-" + orderId).classList.remove("order-selected");
    });

    $(".collapse").on('hidden.bs.collapse', function(e){
        const orderId = extractNumber(e.target.getAttribute("id"));
        document.getElementById("plus-" + orderId).style.display = "inline";
        document.getElementById("minus-" + orderId).style.display = "none";
    });

}, false);