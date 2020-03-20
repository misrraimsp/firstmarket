/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {

    document.getElementById("begin").innerHTML = (pageIndex * pageSize) + 1;
    document.getElementById("end").innerHTML = (pageIndex * pageSize) + numberOfElements;
    document.getElementById("total").innerHTML = totalElements;

}, false);