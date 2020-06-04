package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

public enum PageSize {

    XS(5),
    S(10),
    M(25),
    L(50),
    XL(100);

    private final int size;

    PageSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
