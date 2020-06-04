'use strict';


document.addEventListener("DOMContentLoaded", function() {

    //general definitions
    let url = document.URL;
    let sendTrigger = document.getElementById("trigger");
    let catLinks = document.getElementsByClassName("categoryLink");

    //check boxes definitions
    let i, j, k, id, ids, fullId, start, end, checkBox, checkBoxes, present;
    const criteriaCheckBox = [
        {key:"priceId=", htmlIdPrefix:"pr-", htmlAccordionId:"priceAcc", htmlClass:"priceCheckBox"},
        {key:"authorId=", htmlIdPrefix:"au-", htmlAccordionId:"authorAcc", htmlClass:"authorCheckBox"},
        {key:"publisherId=", htmlIdPrefix:"pu-", htmlAccordionId:"publisherAcc", htmlClass:"publisherCheckBox"},
        {key:"languageId=", htmlIdPrefix:"la-", htmlAccordionId:"languageAcc", htmlClass:"languageCheckBox"}
    ];

    //text query definitions
    let textBox = document.getElementById("queryTextBox");
    let searchTextBoxButton = document.getElementById("searchTextBoxButton");
    let textQueryAlertBox = document.getElementById("textQueryAlertBox");
    const textKey = "q=";
    const startTextKey = url.indexOf(textKey);
    let endTextQuery, textBoxQuery, textBoxQueryValue, newTextBoxQuery;
    if (startTextKey !== -1){
        endTextQuery = url.indexOf("&", startTextKey);
        textBoxQuery = (endTextQuery === -1) ? url.slice(startTextKey) : url.slice(startTextKey, endTextQuery);
        textBoxQueryValue = textBoxQuery.slice(textKey.length).replace(/\+/g, " ");
    } else {
        textBoxQuery = "";
        textBoxQueryValue = "";
    }

    //function definitions
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
    let removePageNo = function (str) {
        str = str.replace(/(\?pageNo=)\d+$/g,"");//unique
        str = str.replace(/(\?pageNo=)\d+(&)/g,"?");//first
        str = str.replace(/(&pageNo=)\d+$/g,"");//last
        str = str.replace(/(&pageNo=)\d+(&)/g, "&");//middle
        return str;
    };

    //hide-show query alert box and update category links with text box query, if necessary
    if (textBoxQuery !== "") {
        for (i = 0; i < catLinks.length; i++){
            catLinks[i].setAttribute("href", catLinks[i].getAttribute("href") + "&" + textBoxQuery);
        }
        textQueryAlertBox.getElementsByTagName("small")[0].innerHTML = textBoxQueryValue;
    } else {
        textQueryAlertBox.getElementsByTagName("a")[0].click();
    }

    //manage clear button click
    textQueryAlertBox.getElementsByTagName("button")[0].addEventListener("click", function () {
        url = url.
        replace("&" + textBoxQuery, "").
        replace("?" + textBoxQuery + "&", "?").
        replace("?" + textBoxQuery, "");
        url = removePageNo(url);
        sendTrigger.setAttribute("href", url);
        sendTrigger.click();
    }, false);

    //manage search text box button
    searchTextBoxButton.addEventListener("click", function () {
        if (textBox.value !== ""){
            newTextBoxQuery = textKey + textBox.value.replace(/ /g, "+");
            if (textBoxQuery !== ""){
                url = url.replace(textBoxQuery, newTextBoxQuery);
            } else {
                url += (url.indexOf("?") !== -1) ? "&" + newTextBoxQuery : "?" + newTextBoxQuery;
            }
            url = removePageNo(url);
            sendTrigger.setAttribute("href", url);
            sendTrigger.click();
        }
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
                        let regex = new RegExp("," + id + "\$");
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
                url = removePageNo(url);
                sendTrigger.setAttribute("href", url);
                sendTrigger.click();
            }, false);
        }
    }

}, false);