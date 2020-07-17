'use strict';

document.addEventListener("DOMContentLoaded", function() {

    //env variables
    let hostUrl = document.getElementById("host").getAttribute("host-address");
    let stripePublicKey = document.getElementById("pk").getAttribute("pk");

    //dev
    let mockWebhook = function (paymentIntent) {
        let xmlHttpRequest = new XMLHttpRequest();
        let content;
        switch (paymentIntent.status) {
            case "succeeded":
                content = "payment_intent.succeeded";
                break;
            case "canceled":
                content = "payment_intent.canceled";
                break;
            case "requires_payment_method":
                content = "payment_intent.payment_failed";
                break;
            case "processing":
                content = "payment_intent.processing";
                break;
            case "requires_confirmation":
            case "requires_action":
            case "requires_capture":
            default:
                content = "(status)" + paymentIntent.status;
                break;
        }
        content += "-" + paymentIntent.id;
        xmlHttpRequest.open("POST", hostUrl + 'listener', true);
        xmlHttpRequest.setRequestHeader('Stripe-Signature', 'local-dev');
        xmlHttpRequest.send(content);
        xmlHttpRequest.onreadystatechange = function() {
            if (this.readyState === 4) {
                if (this.status === 200) {
                    console.log('OK');
                }
                else if (this.status === 400) {
                    console.log('BadRequest');
                }
                else {
                    console.log('Other');
                    console.log(this);
                }
            }
        };
    };

    function buildErrorBox(head, body, dismissible) {

        /*
        <div class="alert alert-danger alert-dismissible fade show">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <strong>Payment Error: </strong>
            <span>some error message here, please</span>
        </div>
        */

        let errorBox = document.createElement("div");

        if (dismissible) {
            errorBox.setAttribute("class", "alert alert-danger alert-dismissible fade show");

            let dismissButton = document.createElement("button");
            dismissButton.setAttribute("class", "close");
            dismissButton.setAttribute("type", "button");
            dismissButton.setAttribute("data-dismiss", "alert");
            dismissButton.innerHTML = '&times;';

            errorBox.appendChild(dismissButton);
        }
        else {
            errorBox.setAttribute("class", "alert alert-danger");
        }

        let errorHead = document.createElement("strong");
        errorHead.innerHTML = head;

        let errorBody = document.createElement("span");
        errorBody.innerHTML = body;

        errorBox.appendChild(errorHead);
        errorBox.appendChild(errorBody);

        document.getElementById("error-box-hook").appendChild(errorBox);
    }

    let form = document.getElementById('payment-form');
    let payButton = document.getElementById("pay");
    let spinner = document.getElementById("spinner");
    let clientSecret = payButton.getAttribute("data-secret");
    let displayError = document.getElementById('card-errors');
    let stripe = Stripe(stripePublicKey);
    let elements = stripe.elements();
    let cardElement = elements.create('card');
    cardElement.mount('#card-element');

    cardElement.on('change', function(event) {
        if (event.complete) {
            displayError.textContent = '';
            payButton.disabled = false;
        }
        else if (event.error) {
            payButton.disabled = true;
            displayError.textContent = event.error.message;
        }
        else {
            payButton.disabled = true;
            displayError.textContent = '';
        }
    });


    form.addEventListener('submit', function(event) {
        event.preventDefault();
        event.stopPropagation();
        if (form.checkValidity() === true) {
            spinner.style.visibility = 'visible';
            payButton.disabled = true;
            stripe.confirmCardPayment(clientSecret, {
                payment_method: {
                    card: cardElement,
                    billing_details: {
                        address: {
                            line1: null,
                            city: null,
                            country: null,
                            line2: null,
                            postal_code: null,
                            state: null
                        },
                        name: null,
                        phone: null,
                        email: null
                    }
                },
                receipt_email: document.getElementById("receiptEmail").value,
                shipping: {
                    address: {
                        line1: document.getElementById("line1").value,
                        city: document.getElementById("city").value,
                        country: document.getElementById("country").value,
                        line2: document.getElementById("line2").value,
                        postal_code: document.getElementById("postalCode").value,
                        state: document.getElementById("province").value
                    },
                    name: document.getElementById("name").value,
                    carrier: null,
                    phone: document.getElementById("phone").value,
                    tracking_number: null
                }
            }).then(function(result) {
                if (result.error) {
                    console.log(result.error);
                    switch (result.error.code) {
                        case "card_declined":
                            spinner.style.visibility = 'hidden';
                            payButton.disabled = false;
                            buildErrorBox("Card Declined: ", result.error.message, true);
                            buildErrorBox("Declined Code: ", result.error.decline_code, true);
                            break;
                        case "payment_intent_unexpected_state":
                            if (result.error.payment_intent.status === "canceled") {
                                spinner.style.visibility = 'hidden';
                                payButton.disabled = true;
                                buildErrorBox("TIMEOUT: ", "Your purchase process has EXPIRED. Please, go to your cart and try again", false);
                            }
                            else {
                                spinner.style.visibility = 'hidden';
                                payButton.disabled = false;
                                buildErrorBox("ERROR: ", result.error.code, true);
                                buildErrorBox("ERROR: ", result.error.message, true);
                            }
                            break;
                        default:
                            spinner.style.visibility = 'hidden';
                            payButton.disabled = false;
                            buildErrorBox("ERROR: ", result.error.code, true);
                            buildErrorBox("ERROR: ", result.error.message, true);
                    }
                } else {
                    //local-dev
                     mockWebhook(result.paymentIntent);
                     setTimeout(function() {
                         if (result.paymentIntent.status === 'succeeded') {
                             document.getElementById("successLink").click();
                         }
                         else {
                             console.log(result.paymentIntent);
                             spinner.style.visibility = 'hidden';
                             payButton.disabled = true;
                             buildErrorBox("EXCEPTION: ", "An unexpected situation has took place. Please, contact us at afirstmarket@gmail.com", false);
                         }
                     }, (5 * 1000));
                    /*if (result.paymentIntent.status === 'succeeded') {
                        document.getElementById("successLink").click();
                    }
                    else {
                        console.log(result.paymentIntent);
                        spinner.style.visibility = 'hidden';
                        payButton.disabled = true;
                        buildErrorBox("EXCEPTION: ", "An unexpected situation has took place. Please, contact us at afirstmarket@gmail.com", false);
                    }*/
                }
            });
        }
        form.classList.add('was-validated');
    }, false);


}, false);