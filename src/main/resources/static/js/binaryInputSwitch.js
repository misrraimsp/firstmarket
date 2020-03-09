/*
Checkbox based switch to enable/disable two input tags mutexly.
checkbox.checked TRUE:  input1 disabled, input2 enabled
otherwise:              input1 enabled, input2 disabled
*/

'use strict';

const binaryInputSwitch = function() {
    if (document.getElementById("switch").checked == true) {
        document.getElementById("input1").disabled = true;
        document.getElementById("input2").disabled = false;
    } else {
        document.getElementById("input1").disabled = false;
        document.getElementById("input2").disabled = true;
    }
};

document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("switch").addEventListener('change', binaryInputSwitch, false);
}, false);