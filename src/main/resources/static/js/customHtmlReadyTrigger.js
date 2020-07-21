'use strict';

document.addEventListener("DOMContentLoaded", function() {
    document.dispatchEvent(new CustomEvent('custom-htmlReady'));
}, false);