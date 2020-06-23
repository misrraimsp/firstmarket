/*

*/

'use strict';


document.addEventListener("DOMContentLoaded", function() {

    //env variable
    let hostUrl = document.getElementById("host").getAttribute("host-address");

    // uninitialized variables
    let xmlHttpRequest, i, action, id, target, url, itemCheckbox, increment, itemQuantity, itemPrice;

    // functions
    let extractNumber = (str) => str.slice(str.indexOf("-") + 1);
    let getSetOfDisplayedItemIds = function() {
        let displayedItemIds = new Set();
        let items = document.querySelectorAll('div[id|="i"]');
        for (i = 0; i < items.length; i++) {
            if (items[i].style.display !== "none") displayedItemIds.add(extractNumber(items[i].getAttribute("id")));
        }
        return displayedItemIds;
    };
    let onHttpOk = function(action, id, q) {

        // local functions
        function updateGlobalPrices(increment) {
            subtotal.innerHTML = (parseFloat(subtotal.innerHTML) + increment).toFixed(2);
            total.innerHTML = (parseFloat(total.innerHTML) + increment).toFixed(2);
        }
        function updatePrices(id, increment) {
            itemPrice = document.getElementById("ip-" + id);
            itemPrice.innerHTML = (parseFloat(itemPrice.innerHTML) + increment).toFixed(2);
            updateGlobalPrices(increment);
        }
        function hideItem(id) {
            document.getElementById("i-" + id).style.display = "none";
            setOfItemIdsToRemove.delete(id);
            setOfDisplayedItemIds.delete(id);
            setAppearance();
        }
        function hideItems() {
            increment = 0;
            for (const id of setOfItemIdsToRemove.keys()) {
                increment -= (parseFloat(document.getElementById("iq-" + id).innerHTML)) * (parseFloat(document.getElementById("ibp-" + id).innerHTML));
                document.getElementById("i-" + id).style.display = "none";
                setOfDisplayedItemIds.delete(id);
            }
            setOfItemIdsToRemove.clear();
            setAppearance();
            updateGlobalPrices(increment);
        }
        function setAppearance() {
            removeButton.disabled = (setOfItemIdsToRemove.size === 0);
            masterCheckbox.disabled = (setOfDisplayedItemIds.size === 0);
            purchase.disabled = (setOfDisplayedItemIds.size === 0);
            masterCheckbox.checked = (setOfDisplayedItemIds.size > 0 && setOfItemIdsToRemove.size === setOfDisplayedItemIds.size);
        }

        cartIcon.setAttribute("data-count", q);
        numOfBooks.innerHTML = q;
        switch (action) {
            case 'incrementItem':
                document.getElementById("iq-" + id).innerHTML++;
                updatePrices(id, parseFloat(document.getElementById("ibp-" + id).innerHTML));
                break;
            case 'decrementItem':
                itemQuantity = document.getElementById("iq-" + id);
                if (parseFloat(itemQuantity.innerHTML) > 1) {
                    itemQuantity.innerHTML--;
                    const item = document.getElementById("i-" + id);
                    item.classList.remove("out-of-stock");
                    item.classList.add("bg-white");
                }
                else{
                    hideItem(id);
                }
                updatePrices(id, 0 - parseFloat(document.getElementById("ibp-" + id).innerHTML));
                break;
            case 'removeItems':
                hideItems();
                break;
        }
    };
    let onHttpUnauthorized = function() {
        loginLink.click();
    };

    // initialized variables
    let cartIcon = document.getElementById("cartIcon");
    let loginLink = document.getElementById("loginLink");
    let baseUrl = hostUrl + "ajaxCart/";
    let numOfBooks = document.getElementById("numOfBooks");
    let itemCheckboxes = document.querySelectorAll('input[id|="icb"]');
    let masterCheckbox = document.getElementById("selectAll");
    let ajaxElements = document.querySelectorAll("[ajax]");
    let removeButton = document.querySelector("[ajax='removeItems']");
    let setOfDisplayedItemIds = getSetOfDisplayedItemIds();
    let setOfItemIdsToRemove = new Set();
    let subtotal = document.getElementById("subtotal");
    //let discount = document.getElementById("discount");
    let total = document.getElementById("total");
    let purchase = document.getElementById("purchase");

    // itemCheckbox event attachment
    for (i = 0; i < itemCheckboxes.length; i++) {
        itemCheckboxes[i].addEventListener("click", function (e) {
            itemCheckbox = e.target;
            id = extractNumber(itemCheckbox.getAttribute("id"));
            (itemCheckbox.checked) ? setOfItemIdsToRemove.add(id) : setOfItemIdsToRemove.delete(id);
            removeButton.disabled = (setOfItemIdsToRemove.size === 0);
            masterCheckbox.checked = (setOfItemIdsToRemove.size === setOfDisplayedItemIds.size);
        }, false);
    }

    // masterCheckbox event attachment
    masterCheckbox.addEventListener("click", function () {
        if (masterCheckbox.checked) {
            for (const id of setOfDisplayedItemIds.keys()) {
                setOfItemIdsToRemove.add(id);
            }
        }
        else {
            setOfItemIdsToRemove.clear();
        }
        removeButton.disabled = (setOfItemIdsToRemove.size === 0);
        for (i = 0; i < itemCheckboxes.length; i++) {
            itemCheckboxes[i].checked = masterCheckbox.checked;
        }
    }, false);

    // ajax elements event attachment
    for (i = 0; i < ajaxElements.length; i++) {
        ajaxElements[i].addEventListener("click", function(e) {
            target = e.currentTarget;
            action = target.getAttribute("ajax");
            id = extractNumber(target.getAttribute("id"));
            url = baseUrl + action;
            url += (action === "removeItems") ? "?ids=" + [...setOfItemIdsToRemove.keys()].toString() : "/" + id;
            xmlHttpRequest = new XMLHttpRequest();
            xmlHttpRequest.open("GET", url, true);
            xmlHttpRequest.setRequestHeader('isAjaxCartRequested', '1');
            xmlHttpRequest.send();
            xmlHttpRequest.onreadystatechange = function() {
                if (this.readyState === 4 && this.status === 200) {
                    onHttpOk(action, id, this.responseText);
                }
                if (this.readyState === 4 && this.status === 401) {
                    onHttpUnauthorized();
                }
            };
        }, false);
    }

    // at beginning there is no item selected, so removeItems button should be disabled
    document.getElementById("removeItems").disabled = true;

    // if there is no item then disable masterCheckbox and purchaseButton
    if (setOfDisplayedItemIds.size === 0) {
        masterCheckbox.disabled = true;
        purchase.disabled = true;
    }

}, false);