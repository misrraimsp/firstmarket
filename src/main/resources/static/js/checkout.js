'use strict';

document.addEventListener("DOMContentLoaded", function() {

    //dev
    let simulateWebhook = function (result) {
        let xmlHttpRequest = new XMLHttpRequest();
        let content = (result.error) ? result.error.message : result.paymentIntent.status;
        xmlHttpRequest.open("POST", 'http://localhost:8080/firstmarket/listener', true);
        xmlHttpRequest.setRequestHeader('Stripe-Signature', 'localdev');
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


    let stripe = Stripe('pk_test_iLddVaUD1AOnudl6q5v2LEKt00Xk2Yz1R6');
    let elements = stripe.elements();
    let cardElement = elements.create('card');
    cardElement.mount('#card-element');

    cardElement.on('change', function(event) {
        var displayError = document.getElementById('card-errors');
        if (event.complete) {
            displayError.textContent = '';
            // enable payment button
        }
        else if (event.error) {
            // show validation to customer
            displayError.textContent = event.error.message;
        }
        else {
            displayError.textContent = '';
        }
    });

    let clientSecret = document.getElementById("card-button").getAttribute("data-secret");

    document.getElementById('payment-form').addEventListener('submit', function(ev) {
        ev.preventDefault();
        stripe.confirmCardPayment(clientSecret, {
            payment_method: {
                card: cardElement,
                //explore whether or not populate with user info
                billing_details: {
                    name: 'Misrra Test'
                }
            }
        }).then(function(result) {
            //simulate webhook notification
            simulateWebhook(result);
            if (result.error) {
                // Show error to your customer (e.g., insufficient funds)
                console.log(result.error.message);
            } else {
                // The payment has been processed!
                if (result.paymentIntent.status === 'succeeded') {
                    // Show a success message to your customer
                    console.log(result.paymentIntent);
                }
            }
        });
    });


}, false);