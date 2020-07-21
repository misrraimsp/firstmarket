'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let refreshTrigger = document.getElementById("paginationTrigger");

    let paginationDataHolder = document.getElementById("paginationData");
    let totalElements = parseInt(paginationDataHolder.getAttribute("totalElements"));
    let pageIndex = parseInt(paginationDataHolder.getAttribute("pageIndex"));
    let numberOfElements = parseInt(paginationDataHolder.getAttribute("numberOfElements"));
    let pageSize = parseInt(paginationDataHolder.getAttribute("pageSize"));

    //functions
    let appendURLParam = function(url,paramName) {
        const indexOfParamToken = url.indexOf("?");
        if (indexOfParamToken === -1) { //url without params (firstmarket.xyz)
            url += "?" + paramName + "=";

        }
        else {
            if (indexOfParamToken === url.length - 1) { //url ending with '?' (firstmarket.xyz?)
                url += paramName + "=";
            }
            else { // url with well formed params (firstmarket.xyz?pageNo=3)
                url += "&" + paramName + "=";
            }
        }
        return url;
    };

    //sort criteria and page size
    let sortCriteriaSelector = document.getElementById("paginationSortCriteria");
    let pageSizeSelector = document.getElementById("paginationPageSize");
    //legend numbers
    let begins = document.getElementsByClassName("paginationBegin");
    let ends = document.getElementsByClassName("paginationEnd");
    let totals = document.getElementsByClassName("paginationTotal");

    //update legend number
    let i = 0;
    for (; i < begins.length; i++){
        begins[i].innerHTML = (pageIndex * pageSize) + 1;
        ends[i].innerHTML = (pageIndex * pageSize) + numberOfElements;
        totals[i].innerHTML = totalElements;
    }

    //refresh on sort criteria change
    if (sortCriteriaSelector){
        sortCriteriaSelector.addEventListener("change", function () {
            const newUrl = appendURLParam(document.URL.replace(/(&)*(sort=)\w+/g,""), "sort");
            refreshTrigger.setAttribute("href", newUrl + sortCriteriaSelector.value);
            refreshTrigger.click();
        }, false);
    }

    //refresh on page size change
    if (pageSizeSelector){
        pageSizeSelector.addEventListener("change", function () {
            const newUrl = appendURLParam(document.URL.replace(/(&)*(pageSize=)\w+/g,""), "pageSize");
            refreshTrigger.setAttribute("href", newUrl + pageSizeSelector.value);
            refreshTrigger.click();
        }, false);
    }

}, false);