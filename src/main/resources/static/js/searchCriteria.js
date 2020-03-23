/*

 */


'use strict';

document.addEventListener("DOMContentLoaded", function() {
    let i, id, ids, start, end, authorCheckBox;
    let authorKey = "authorId=";
    let sendTrigger = document.getElementById("trigger");
    let authors = document.getElementsByClassName("authorCriteria");
    let authorAccordion = document.getElementById("author-acc");
    let url = document.URL;

    start = url.indexOf(authorKey);
    end = url.indexOf("&", start);
    if (start != -1){
        authorAccordion.click();
        ids = (end == -1) ? url.slice(start + authorKey.length).split(",") : url.slice(start + authorKey.length, end).split(",");
        for (i = 0; i < ids.length; i++){
            authorCheckBox = document.getElementById("au-" + ids[i]);
            if (authorCheckBox == null){
                //TODO
            } else {
                authorCheckBox.checked = true;
            }
        }
    }

    for (i = 0; i < authors.length; i++) {
        authors[i].addEventListener("change", function (e) {
            authorCheckBox = e.currentTarget;
            id = authorCheckBox.getAttribute("id");
            id = id.slice(id.indexOf("-") + 1);
            start = url.indexOf(authorKey);
            end = url.indexOf("&", start);
            if (authorCheckBox.checked){
                if (start != -1){
                    ids = (end == -1) ? url.slice(start) : url.slice(start, end);
                    url = url.replace(ids, ids + "," + id);
                } else {
                    url += (url.indexOf("?") != -1) ? "&" + authorKey + id : "?" + authorKey + id;
                }
            } else {
                ids = (end == -1) ? url.slice(start + authorKey.length).split(",") : url.slice(start + authorKey.length, end).split(",");
                if (ids.length > 1){
                    let re = new RegExp("," + id + "\$");
                    url = url.
                    replace("=" + id + ",", "=").
                    replace("," + id + ",", ",").
                    replace("," + id + "&", "&").
                    replace(re, "");
                } else {
                    url = url.
                    replace("&" + "authorId=" + id, "").
                    replace("?" + "authorId=" + id + "&", "?").
                    replace("?" + "authorId=" + id, "");
                }
            }

            sendTrigger.setAttribute("href", url);
            sendTrigger.click();

        }, false);
    }

}, false);