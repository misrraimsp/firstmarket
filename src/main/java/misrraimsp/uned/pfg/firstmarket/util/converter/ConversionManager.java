package misrraimsp.uned.pfg.firstmarket.converter;

import com.stripe.model.PaymentIntent;
import com.stripe.model.ShippingDetails;
import lombok.NonNull;
import misrraimsp.uned.pfg.firstmarket.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.exception.BookFormAuthorsConversionException;
import misrraimsp.uned.pfg.firstmarket.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConversionManager {

    private final BookConverter bookConverter;
    private final ProfileConverter profileConverter;

    @Autowired
    public ConversionManager(BookConverter bookConverter,
                             ProfileConverter profileConverter) {

        this.bookConverter = bookConverter;
        this.profileConverter = profileConverter;
    }

    public Book convertBookFormToBook(BookForm bookForm) throws BookFormAuthorsConversionException {
        return bookConverter.convertBookFormToBook(bookForm);
    }

    public BookForm convertBookToBookForm(Book book) {
        return bookConverter.convertBookToBookForm(book);
    }

    public ProfileForm convertProfileToProfileForm(Profile profile) {
        return profileConverter.convertProfileToProfileForm(profile);
    }

    public Profile convertProfileFormToProfile(ProfileForm profileForm) {
        return profileConverter.convertProfileFormToProfile(profileForm);
    }

    public Address convertStripeAddressToAddress(@NonNull com.stripe.model.Address stripeAddress) {
        misrraimsp.uned.pfg.firstmarket.core.model.Address address = new Address();
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
