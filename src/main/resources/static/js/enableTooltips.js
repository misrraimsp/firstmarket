/*
Enable all tooltips in the document
*/

'use strict';

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip({delay: {show: 1000, hide: 200}});
});