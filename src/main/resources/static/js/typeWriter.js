/*
*/

'use strict';

document.addEventListener("DOMContentLoaded", function() {

    let i;
    let mainTitleElem = document.getElementById("mainTitle");
    let mainTitleText = mainTitleElem.getAttribute("tw-text");
    let mainTitleSpeed = Number(mainTitleElem.getAttribute("tw-speed"));
    let secondaryTitleElem = document.getElementById("secondaryTitle");
    let secondaryTitle = secondaryTitleElem.getAttribute("tw-text");
    let secondaryTitleSpeed = Number(secondaryTitleElem.getAttribute("tw-speed"));

    let typeWriter = function() {
        if (i < mainTitleText.length) {
            mainTitleElem.innerHTML += mainTitleText.charAt(i);
            i++;
            setTimeout(typeWriter, mainTitleSpeed);
        }
        else if (i < mainTitleText.length + secondaryTitle.length) {
            secondaryTitleElem.innerHTML += secondaryTitle.charAt(i - mainTitleText.length);
            i++;
            setTimeout(typeWriter, secondaryTitleSpeed);
        }
    };

    let typeWriter_v2 = function() {
        if (i < mainTitleText.length) {
            i++;
            setTimeout(typeWriter_v2, mainTitleSpeed);
        }
        else if (i < mainTitleText.length + secondaryTitle.length) {
            secondaryTitleElem.innerHTML += secondaryTitle.charAt(i - mainTitleText.length);
            i++;
            setTimeout(typeWriter_v2, secondaryTitleSpeed);
        }
    };

    i = 0;
    mainTitleElem.innerHTML += mainTitleText;
    typeWriter_v2();

}, false);