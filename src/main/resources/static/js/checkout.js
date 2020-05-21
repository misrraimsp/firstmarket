'use strict';

document.addEventListener("DOMContentLoaded", function() {

    //dev
    let simulateWebhook = function (result) {
        let xmlHttpRequest = new XMLHttpRequest();
        let content;
        if (result.error) {
            content = result.error.message + "-" + result.error.message;
        }
        else {
            switch (result.paymentIntent.status) {
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
                    content = "(status)" + result.paymentIntent.status;
                    break;
            }
            content += "-" + result.paymentIntent.id;
        }
        xmlHttpRequest.open("POST", 'http://localhost:8080/firstmarket/listener', true);
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
                }
            }
        };
    };


    let payButton = document.getElementById("pay");
    let spinner = document.getElementById("spinner");
    let clientSecret = payButton.getAttribute("data-secret");
    let displayError = document.getElementById('card-errors');

    let stripe = Stripe('pk_test_iLddVaUD1AOnudl6q5v2LEKt00Xk2Yz1R6');
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

    document.getElementById('payment-form').addEventListener('submit', function(event) {
        event.preventDefault();
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
            simulateWebhook(result);
            if (result.error) {
                //TODO
                // Show error to your customer (e.g., insufficient funds)
                console.log(result.error.message);
            } else {
                if (result.paymentIntent.status === 'succeeded') {
                    //TODO
                    // Show a success message to your customer
                    console.log(result.paymentIntent);
                }
                else {
                    //TODO
                }
            }
        });
    }, false);


}, false);