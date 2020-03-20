/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let i, pageLinks, pageLinki, pageLink1, pageLink2, pageLink3, pageLink4, pageLink5, pageLink6, pageLink7;

    let url = document.URL.replace(/(pageNo=)\d+/g,"");
    let indexOfParamToken = url.indexOf("?");
    if (indexOfParamToken != -1) {
        if (indexOfParamToken != url.length - 1) {
            url += "&pageNo=";
        } else {
            url += "pageNo=";
        }
    } else {
        url += "?pageNo=";
    }

    if (totalPages <= 7) {
        pageLinks = document.getElementById("pagination").children;
        for (i = 1; i <= 7; i++) {
            pageLinki = pageLinks[i - 1];
            if (i > totalPages || ((i == 1) && (totalPages == 1))) {
                pageLinki.style.display = "none";
            } else {
                pageLinki.innerHTML = i;
                pageLinki.setAttribute("href", url + (i - 1));
                if (pageIndex == (i - 1)) {
                    pageLinki.setAttribute("class", "btn btn-info");
                }
            }
        }
    } else {
        pageLink1 = document.getElementById("pl-1");
        pageLink2 = document.getElementById("pl-2");
        pageLink3 = document.getElementById("pl-3");
        pageLink4 = document.getElementById("pl-4");
        pageLink5 = document.getElementById("pl-5");
        pageLink6 = document.getElementById("pl-6");
        pageLink7 = document.getElementById("pl-7");

        pageLink7.innerHTML = totalPages;
        pageLink7.setAttribute("href", url + (totalPages - 1));
        if (pageIndex == (totalPages - 1)) {pageLink7.setAttribute("class", "btn btn-info");}
        if (pageIndex < 3) {
            pageLink6.innerHTML = "...";
            pageLink6.setAttribute("href", url + "4");
            pageLink5.innerHTML = "4";
            pageLink5.setAttribute("href", url + "3");
            pageLink4.style.display = "none";
            pageLink3.innerHTML = "3";
            pageLink3.setAttribute("href", url + "2");
            if (pageIndex == 2) {pageLink3.setAttribute("class", "btn btn-info");}
            pageLink2.innerHTML = "2";
            pageLink2.setAttribute("href", url + "1");
            if (pageIndex == 1) {pageLink2.setAttribute("class", "btn btn-info");}
        } else if (pageIndex > (totalPages - 4)) {
            pageLink6.innerHTML = totalPages - 1;
            pageLink6.setAttribute("href", url + (totalPages - 2));
            if (pageIndex == (totalPages - 2)) {pageLink6.setAttribute("class", "btn btn-info");}
            pageLink5.innerHTML = totalPages - 2;
            pageLink5.setAttribute("href", url + (totalPages - 3));
            if (pageIndex == (totalPages - 3)) {pageLink5.setAttribute("class", "btn btn-info");}
            pageLink4.style.display = "none";
            pageLink3.innerHTML = totalPages - 3;
            pageLink3.setAttribute("href", url + (totalPages - 4));
            pageLink2.innerHTML = "...";
            pageLink2.setAttribute("href", url + (totalPages - 5));
        } else {
            pageLink6.innerHTML = "...";
            pageLink6.setAttribute("href", url + (pageIndex + 3));
            pageLink5.innerHTML = pageIndex + 2;
            pageLink5.setAttribute("href", url + (pageIndex + 1));
            pageLink4.innerHTML = pageIndex + 1;
            pageLink4.setAttribute("href", url + pageIndex);
            pageLink4.setAttribute("class", "btn btn-info");
            pageLink3.innerHTML = pageIndex;
            pageLink3.setAttribute("href", url + (pageIndex - 1));
            pageLink2.innerHTML = "...";
            pageLink2.setAttribute("href", url + (pageIndex - 3));
        }
        pageLink1.innerHTML = "1";
        pageLink1.setAttribute("href", url + "0");
        if (pageIndex == 0) {pageLink1.setAttribute("class", "btn btn-info");}
    }

}, false);