package misrraimsp.uned.pfg.firstmarket.exception;


public class PaymentIntentProcessingTimeout extends Exception {

    public PaymentIntentProcessingTimeout(String piId) {
        super("PaymentIntent with 'processing' status has timeout. (piId=" + piId + ")");
    }
}
