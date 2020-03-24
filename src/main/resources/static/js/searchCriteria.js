/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {

    //definitions
    let i, j, k, id, ids, fullId, start, end, checkBox, checkBoxes, textBoxQuery, present;
    const transitionTime = 300;
    let sendTrigger = document.getElementById("trigger");
    let catLinks = document.getElementsByClassName("categoryLink");
    let textBox = document.getElementById("queryTextBox");
    let clearTextBoxButton = document.getElementById("clearTextBoxButton");
    let searchTextBoxButton = document.getElementById("searchTextBoxButton");
    let url = document.URL;

    const textBoxQueryKey = "q=";
    const criteriaCheckBox = [
        {key:"priceId=", htmlIdPrefix:"pr-", htmlAccordionId:"priceAcc", htmlClass:"priceCheckBox"},
        {key:"authorId=", htmlIdPrefix:"au-", htmlAccordionId:"authorAcc", htmlClass:"authorCheckBox"},
        {key:"publisherId=", htmlIdPrefix:"pu-", htmlAccordionId:"publisherAcc", htmlClass:"publisherCheckBox"},
        {key:"languageId=", htmlIdPrefix:"la-", htmlAccordionId:"languageAcc", htmlClass:"languageCheckBox"}
        ];

    let regex = new RegExp("," + id + "\$");

    let extractId = (str) => str.slice(str.indexOf("-") + 1);

    let extractPrefix = (str) => str.slice(0, str.indexOf("-") + 1);

    let extractItems = function (str, start, end, separator) {
        return (end === -1) ? str.slice(start).split(separator) : str.slice(start, end).split(separator);
    };

    let getCriteriaIndex = function(prefix){
        for (i = 0; i < criteriaCheckBox.length; i++){
            if (criteriaCheckBox[i].htmlIdPrefix === prefix) {
                return i;
            }
        }
        return -1;
    };


    //populate text box, show clear button and update category links with text box query, if necessary
    start = url.indexOf(textBoxQueryKey);
    if (start !== -1) {
        end = url.indexOf("&", start);
        textBoxQuery = (end === -1) ? url.slice(start) : url.slice(start, end);
        for (i = 0; i < catLinks.length; i++){
            catLinks[i].setAttribute("href", catLinks[i].getAttribute("href") + "&" + textBoxQuery);
        }
        textBox.value = textBoxQuery.slice(textBoxQueryKey.length).replace(/\+/g, " ");
        $("#clearTextBoxButton").show(transitionTime);
    }

    //manage clear button click
    clearTextBoxButton.addEventListener("click", function () {
        start = url.indexOf(textBoxQueryKey);
        if (start !== -1) {
            end = url.indexOf("&", start);
            textBoxQuery = (end === -1) ? url.slice(start) : url.slice(start, end);
            url = url.
            replace("&" + textBoxQuery, "").
            replace("?" + textBoxQuery + "&", "?").
            replace("?" + textBoxQuery, "");
            sendTrigger.setAttribute("href", url);
            sendTrigger.click();
        } else {
            textBox.value = "";
            $("#clearTextBoxButton").hide(transitionTime);
        }
    }, false);

    //manage text box change (show or hide clear button)
    textBox.addEventListener("input", function () {
        if (textBox.value !== ""){
            $("#clearTextBoxButton").show(transitionTime);
        } else {
            $("#clearTextBoxButton").hide(transitionTime);
        }
    }, false);

    //manage search text box button
    searchTextBoxButton.addEventListener("click", function () {

    }, false);

    //set all criteria checkboxes checked state based on url info
    for (i = 0; i < criteriaCheckBox.length; i++){
        present = false;
        start = url.indexOf(criteriaCheckBox[i].key);
        end = url.indexOf("&", start);
        if (start !== -1){
            ids = extractItems(url, start + criteriaCheckBox[i].key.length, end, ",");
            for (j = 0; j < ids.length; j++){
                checkBox = document.getElementById(criteriaCheckBox[i].htmlIdPrefix + ids[j]);
                if (checkBox != null){
                    checkBox.checked = true;
                    present = true;
                }
            }
            if (present) {
                document.getElementById(criteriaCheckBox[i].htmlAccordionId).click();
            }
        }
    }

    //set all criteria checkboxes 'change' event listener
    for (i = 0; i < criteriaCheckBox.length; i++){
        checkBoxes = document.getElementsByClassName(criteriaCheckBox[i].htmlClass);
        for (j = 0; j < checkBoxes.length; j++) {
            checkBoxes[j].addEventListener("change", function (e) {
                checkBox = e.currentTarget;
                fullId = checkBox.getAttribute("id");
                id = extractId(fullId);
                k = getCriteriaIndex(extractPrefix(fullId));
                start = url.indexOf(criteriaCheckBox[k].key);
                end = url.indexOf("&", start);
                if (checkBox.checked){
                    if (start !== -1){
                        ids = (end === -1) ? url.slice(start) : url.slice(start, end);
                        url = url.replace(ids, ids + "," + id);
                    } else {
                        url += (url.indexOf("?") !== -1) ? "&" + criteriaCheckBox[k].key + id : "?" + criteriaCheckBox[k].key + id;
                    }
                } else {
                    ids = extractItems(url, start + criteriaCheckBox[k].key.length, end, ",");
                    if (ids.length > 1){
                        url = url.
                        replace("=" + id + ",", "=").
                        replace("," + id + ",", ",").
                        replace("," + id + "&", "&").
                        replace(regex, "");
                    } else {
                        url = url.
                        replace("&" + criteriaCheckBox[k].key + id, "").
                        replace("?" + criteriaCheckBox[k].key + id + "&", "?").
                        replace("?" + criteriaCheckBox[k].key + id, "");
                    }
                }
                sendTrigger.setAttribute("href", url);
                sendTrigger.click();
            }, false);
        }
    }

}, false);