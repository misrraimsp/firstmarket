/*
computes the checksum digit and alerts if an error is detected
*/

'use strict';

const isbnChecksum = function() {
    let chars, last, sum, check, i;
    const isbn = document.getElementById("isbn");
    if (isbn.checkValidity()) {
        // Remove non ISBN digits, then split into an array
        chars = isbn.value.replace(/[- ]|^ISBN(?:-1[03])?:?/g, "").split("");
        // Remove the final ISBN digit from `chars`, and assign it to `last`
        last = chars.pop();
        sum = 0;
        if (chars.length == 9) {
            // Compute the ISBN-10 check digit
            chars.reverse();
            for (i = 0; i < chars.length; i++) {
                sum += (i + 2) * parseInt(chars[i], 10);
            }
            check = 11 - (sum % 11);
            if (check == 10) {
                check = "X";
            } else if (check == 11) {
                check = "0";
            }
        } else {
            // Compute the ISBN-13 check digit
            for (i = 0; i < chars.length; i++) {
                sum += (i % 2 * 2 + 1) * parseInt(chars[i], 10);
            }
            check = 10 - (sum % 10);
            if (check == 10) {
                check = "0";
            }
        }
        if (check != last) {
            alert("Error: Invalid ISBN checksum digit (" + last + "). Try with (" + check + ")");
            isbn.focus();
            isbn.value = isbn.value.substring(0, isbn.value.length - 1);
        }
    }
};

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("isbnForm").addEventListener('submit', isbnChecksum, false);
}, false);