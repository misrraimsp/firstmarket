/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let begins = document.getElementsByClassName("paginationBegin");
    let ends = document.getElementsByClassName("paginationEnd");
    let totals = document.getElementsByClassName("paginationTotal");

    let i = 0;
    for (; i < begins.length; i++){
        begins[i].innerHTML = (pageIndex * pageSize) + 1;
        ends[i].innerHTML = (pageIndex * pageSize) + numberOfElements;
        totals[i].innerHTML = totalElements;
    }

}, false);