package misrraimsp.uned.pfg.firstmarket.service;

import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.model.Address;
import misrraimsp.uned.pfg.firstmarket.model.Payment;
import misrraimsp.uned.pfg.firstmarket.model.ShippingInfo;
import org.springframework.stereotype.Service;


@Service
public class ConversionServer {

    public Address convertStripeAddressToAddress(@NonNull com.stripe.model.Address stripeAddress) {
        misrraimsp.uned.pfg.firstmarket.model.Address address = new Address();
        address.setCountry(stripeAddress.getCountry());
        address.setProvince(stripeAddress.getState());
        address.setCity(stripeAddress.getCity());
        address.setLine1(stripeAddress.getLine1());
        address.setLine2(stripeAddress.getLine2());
        address.setPostalCode(stripeAddress.getPostalCode());
        return address;
    }

    public ShippingInfo convertStripeShippingDetailsToShippingInfo(@NonNull ShippingDetails shippingDetails) {
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setName(shippingDetails.getName());
        shippingInfo.setPhone(shippingDetails.getPhone());
        shippingInfo.setCarrier(shippingDetails.getCarrier());
        shippingInfo.setTrackingNumber(shippingDetails.getTrackingNumber());
        return shippingInfo;
    }

    public Payment convertStripePaymentIntentToPayment(@NonNull PaymentIntent paymentIntent) {
        Payment payment = new Payment();
        payment.setStripePaymentIntentId(paymentIntent.getId());
        payment.setAmount(paymentIntent.getAmount());
        payment.setCurrency(paymentIntent.getCurrency());
        payment.setDescription(paymentIntent.getDescription());
        return payment;
    }
}
