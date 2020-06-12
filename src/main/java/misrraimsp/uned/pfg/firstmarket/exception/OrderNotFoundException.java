package misrraimsp.uned.pfg.firstmarket.exception;

public class OrderNotFoundException extends IdNotFoundException  {

    public OrderNotFoundException(Long orderId) {
        super("There is no Order with id=" + orderId);
    }
}
