/*
Este script permite mostrar u ocultar dinámicamente diferentes autores (máximo 5).
Además permite que cuando se detecte un error en el lado del servidor se muestren
los campos implicados automáticamente (simulando el evento 'click()')

This script allows to dynamically show or hide different authors (maximum 5).
It also allows the fields involved on a server side error to be displayed
automatically by properly simulating the 'click ()' event.
*/

'use strict';

const transitionTime = 300;

const setUp = function() {
    if (document.getElementById('authorFirstName4').value != "" || document.getElementById('authorLastName4').value != "") {
        document.getElementById('showAuthor1').click();
        document.getElementById('showAuthor2').click();
        document.getElementById('showAuthor3').click();
        document.getElementById('showAuthor4').click();
    }
    else {
        if (document.getElementById('authorFirstName3').value != "" || document.getElementById('authorLastName3').value != "") {
            document.getElementById('showAuthor1').click();
            document.getElementById('showAuthor2').click();
            document.getElementById('showAuthor3').click();
        }
        else {
            if (document.getElementById('authorFirstName2').value != "" || document.getElementById('authorLastName2').value != "") {
                document.getElementById('showAuthor1').click();
                document.getElementById('showAuthor2').click();
            }
            else {
                if (document.getElementById('authorFirstName1').value != "" || document.getElementById('authorLastName1').value != "") {
                    document.getElementById('showAuthor1').click();
                }
            }
        }
    }
};

const showAuthor1 = function() {
    $("#showAuthor1").hide(transitionTime);
    $("#author1").show(transitionTime);
    $("#hideAuthor1").show(transitionTime);
    $("#showAuthor2").show(transitionTime);
};

const showAuthor2 = function() {
    $("#showAuthor2").hide(transitionTime);
    $("#hideAuthor1").hide(transitionTime);
    $("#author2").show(transitionTime);
    $("#showAuthor3").show(transitionTime);
    $("#hideAuthor2").show(transitionTime);
};

const showAuthor3 = function() {
    $("#showAuthor3").hide(transitionTime);
    $("#hideAuthor2").hide(transitionTime);
    $("#author3").show(transitionTime);
    $("#showAuthor4").show(transitionTime);
    $("#hideAuthor3").show(transitionTime);
};

const showAuthor4 = function() {
    $("#showAuthor4").hide(transitionTime);
    $("#hideAuthor3").hide(transitionTime);
    $("#author4").show(transitionTime);
    $("#hideAuthor4").show(transitionTime);
};

const hideAuthor1 = function() {
    $("#showAuthor2").hide(transitionTime);
    $("#hideAuthor1").hide(transitionTime);
    $("#author1").hide(transitionTime);
    $("#authorFirstName1").val("");
    $("#authorLastName1").val("");
    $("#showAuthor1").show(transitionTime);
};

const hideAuthor2 = function() {
    $("#showAuthor3").hide(transitionTime);
    $("#hideAuthor2").hide(transitionTime);
    $("#author2").hide(transitionTime);
    $("#authorFirstName2").val("");
    $("#authorLastName2").val("");
    $("#showAuthor2").show(transitionTime);
    $("#hideAuthor1").show(transitionTime);
};

const hideAuthor3 = function() {
    $("#showAuthor4").hide(transitionTime);
    $("#hideAuthor3").hide(transitionTime);
    $("#author3").hide(transitionTime);
    $("#authorFirstName3").val("");
    $("#authorLastName3").val("");
    $("#showAuthor3").show(transitionTime);
    $("#hideAuthor2").show(transitionTime);
};

const hideAuthor4 = function() {
    $("#hideAuthor4").hide(transitionTime);
    $("#author4").hide(transitionTime);
    $("#authorFirstName4").val("");
    $("#authorLastName4").val("");
    $("#showAuthor4").show(transitionTime);
    $("#hideAuthor3").show(transitionTime);
};

document.addEventListener("DOMContentLoaded", function() {
    window.addEventListener('load', setUp, false);
    document.getElementById("showAuthor1").addEventListener('click', showAuthor1, false);
    document.getElementById("hideAuthor1").addEventListener('click', hideAuthor1, false);
    document.getElementById("showAuthor2").addEventListener('click', showAuthor2, false);
    document.getElementById("hideAuthor2").addEventListener('click', hideAuthor2, false);
    document.getElementById("showAuthor3").addEventListener('click', showAuthor3, false);
    document.getElementById("hideAuthor3").addEventListener('click', hideAuthor3, false);
    document.getElementById("showAuthor4").addEventListener('click', showAuthor4, false);
    document.getElementById("hideAuthor4").addEventListener('click', hideAuthor4, false);
}, false);
