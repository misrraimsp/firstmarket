/*
always set newPasswordConfirm pattern equals to newPassword value
*/

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("newPassword").addEventListener("change", function() {
        document.getElementById("newPasswordConfirm").pattern = document.getElementById("newPassword").value;
    }, false);
}, false);