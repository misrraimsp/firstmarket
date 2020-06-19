'use strict';

document.addEventListener("DOMContentLoaded", function() {

    // id extractor
    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);

    $(".collapse").on('show.bs.collapse', function(e){
        const id = extractNumber(e.target.getAttribute("id"));
        document.getElementById("collapsible-" + id).classList.add("left-bordered");
        document.getElementById("plus-" + id).style.display = "none";
        document.getElementById("minus-" + id).style.display = "inline";
    });

    $(".collapse").on('hidden.bs.collapse', function(e){
        const id = extractNumber(e.target.getAttribute("id"));
        document.getElementById("collapsible-" + id).classList.remove("left-bordered");
        document.getElementById("plus-" + id).style.display = "inline";
        document.getElementById("minus-" + id).style.display = "none";
    });

}, false);