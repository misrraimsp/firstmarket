/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {

    //definitions
    let i, j, k, id, ids, fullId, start, end, checkBox, checkBoxes, present;
    let sendTrigger = document.getElementById("trigger");
    let url = document.URL;
    const criteria = [
        {key:"priceId=", prefix:"pr-", accId:"priceAcc", class:"priceCheckBox"},
        {key:"authorId=", prefix:"au-", accId:"authorAcc", class:"authorCheckBox"},
        {key:"publisherId=", prefix:"pu-", accId:"publisherAcc", class:"publisherCheckBox"},
        {key:"languageId=", prefix:"la-", accId:"languageAcc", class:"languageCheckBox"}
        ];
    let regex = new RegExp("," + id + "\$");
    let extractId = (str) => str.slice(str.indexOf("-") + 1);
    let extractPrefix = (str) => str.slice(str.search("-") + 1);
    let extractIds = function (url, key, start, end) {
        return (end === -1) ? url.slice(start + key.length).split(",") : url.slice(start + key.length, end).split(",");
    };
    let extractCriteriaIndex = function(str){
        for (i = 0; i < criteria.length; i++){
            if (str.indexOf(criteria[i].prefix) !== -1) {
                return i;
            }
        }
        return -1;
    };

    //set all criteria checkboxes checked state based on url info
    for (i = 0; i < criteria.length; i++){
        present = false;
        start = url.indexOf(criteria[i].key);
        end = url.indexOf("&", start);
        if (start !== -1){
            ids = extractIds(url, criteria[i].key, start, end);
            for (j = 0; j < ids.length; j++){
                checkBox = document.getElementById(criteria[i].prefix + ids[j]);
                if (checkBox != null){
                    checkBox.checked = true;
                    present = true;
                }
            }
            if (present) {
                document.getElementById(criteria[i].accId).click();
            }
        }
    }

    //set all criteria checkboxes 'change' event listener
    for (i = 0; i < criteria.length; i++){
        checkBoxes = document.getElementsByClassName(criteria[i].class);
        for (j = 0; j < checkBoxes.length; j++) {
            checkBoxes[j].addEventListener("change", function (e) {
                checkBox = e.currentTarget;
                fullId = checkBox.getAttribute("id");
                id = extractId(fullId);
                k = extractCriteriaIndex(fullId);
                start = url.indexOf(criteria[k].key);
                end = url.indexOf("&", start);
                if (checkBox.checked){
                    if (start !== -1){
                        ids = (end === -1) ? url.slice(start) : url.slice(start, end);
                        url = url.replace(ids, ids + "," + id);
                    } else {
                        url += (url.indexOf("?") !== -1) ? "&" + criteria[k].key + id : "?" + criteria[k].key + id;
                    }
                } else {
                    ids = extractIds(url, criteria[k].key, start, end);
                    if (ids.length > 1){
                        url = url.
                        replace("=" + id + ",", "=").
                        replace("," + id + ",", ",").
                        replace("," + id + "&", "&").
                        replace(regex, "");
                    } else {
                        url = url.
                        replace("&" + criteria[k].key + id, "").
                        replace("?" + criteria[k].key + id + "&", "?").
                        replace("?" + criteria[k].key + id, "");
                    }
                }
                sendTrigger.setAttribute("href", url);
                sendTrigger.click();
            }, false);
        }
    }

}, false);