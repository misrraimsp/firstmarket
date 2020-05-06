/*
*/

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let authorsHook = document.getElementById("dynamicAuthors");
    let authorButtons = document.getElementById("authorButtons");
    let removeAuthorButton = document.getElementById("removeAuthor");
    let addAuthorButton = document.getElementById("addAuthor");

    // get the pattern from the first author (who is always present)
    let textBasicPattern = authorsHook.firstElementChild.firstElementChild.getAttribute("pattern");

    addAuthorButton.addEventListener("click", function () {

        let createInput = function(name, placeholder) {
            let input = document.createElement("input");
            input.setAttribute("class", "form-control");
            input.setAttribute("type", "text");
            input.setAttribute("name", name);
            input.setAttribute("value", "");
            input.setAttribute("placeholder", placeholder);
            input.setAttribute("pattern", textBasicPattern);
            return input;
        };

        // create author container
        let divAuthor = document.createElement("div");
        divAuthor.setAttribute("class", "input-group mb-3");

        // create inputs
        let inputFirstName = createInput("authorsFirstName", "First Name");
        let inputLastName = createInput("authorsLastName", "Last Name");

        // create front end error container
        let divFrontEndError = document.createElement("div");
        divFrontEndError.setAttribute("class", "invalid-feedback");

        // connections
        divAuthor.appendChild(inputFirstName);
        divAuthor.appendChild(inputLastName);
        divAuthor.appendChild(authorButtons);
        divAuthor.appendChild(divFrontEndError);
        authorsHook.appendChild(divAuthor);

        // show remove button
        removeAuthorButton.style.display = "block";

    }, false);

    removeAuthorButton.addEventListener("click", function () {

        // move the buttons to the penultimate author
        let penultimateAuthor = authorsHook.lastElementChild.previousElementSibling;
        penultimateAuthor.insertBefore(authorButtons, penultimateAuthor.lastElementChild);

        // delete last author
        authorsHook.lastElementChild.remove();

        // hide remove button if just one author remains
        if (authorsHook.childElementCount === 1) {
            removeAuthorButton.style.display = "none";
        }
    }, false);

}, false);