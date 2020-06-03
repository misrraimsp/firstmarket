package misrraimsp.uned.pfg.firstmarket.config.dev.sqlBuilder;

public class QueryHolder {

    private static final String categoryStaticPart = "INSERT INTO category (id,name,parent_id) VALUES ";
    private static final String catpathStaticPart = "INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES ";
    private static final String bookStaticPart = "INSERT INTO book (id,description,isbn,language,pages,price,status,stock,title,year,category_id,image_id,publisher_id) VALUES ";
    private static final String booksAuthorsStaticPart = "INSERT INTO books_authors (book_id,author_id) VALUES ";
    private static final String itemStaticPart = "INSERT INTO item (id,quantity,book_id) VALUES ";
    private static final String cartItemsStaticPart = "INSERT INTO cart_items (cart_id,items_id) VALUES ";

    //id bigint not null, carrier varchar(255), name varchar(255), phone varchar(255), tracking_number varchar(255), address_id bigint
    private static final String shippingStaticPart = "INSERT INTO shipping_info (id,carrier,name,phone,tracking_number,address_id) VALUES ";
    //id bigint not null, amount bigint, currency varchar(255), description varchar(255), stripe_payment_intent_id varchar(255)
    private static final String paymentStaticPart = "INSERT INTO payment (id,amount,currency,description,stripe_payment_intent_id) VALUES ";
    //id bigint not null, date varchar(255), payment_id bigint, shipping_info_id bigint, user_id bigint
    private static final String pedidoStaticPart = "INSERT INTO pedido (id,date,payment_id,shipping_info_id,user_id) VALUES ";
    //order_id bigint not null, items_id
    private static final String pedidoItemsStaticPart = "INSERT INTO pedido_items (order_id,items_id) VALUES ";


    private static final String description = "Lorem ipsum dolor sit amet";

    private String sql;

    public QueryHolder(){
        this.reset();
    }

    public void openInsertCategoryQuery() {
        sql += categoryStaticPart;
    }

    public void openInsertCatpathQuery() {
        sql += catpathStaticPart;
    }

    public void openInsertBookQuery() {
        sql += bookStaticPart;
    }

    public void openInsertBooksAuthorsQuery() {
        sql += booksAuthorsStaticPart;
    }

    public void openInsertItemQuery() {
        sql += itemStaticPart;
    }

    public void openInsertCartItemsQuery() {
        sql += cartItemsStaticPart;
    }

    public void openInsertShippingQuery() {
        sql += shippingStaticPart;
    }

    public void openInsertPaymentQuery() {
        sql += paymentStaticPart;
    }

    public void openInsertPedidoQuery() {
        sql += pedidoStaticPart;
    }

    public void openInsertPedidoItemsQuery() {
        sql += pedidoItemsStaticPart;
    }

    public void addCategoryValues(String id, String name, String parent_id) {
        sql += "(" + id + ",'" + name + "'," + parent_id + ")" + ",";
    }

    public void addCatpathValues(String id, String size, String ancestor_id, String descendant_id) {
        sql += "(" + id + "," + size + "," + ancestor_id + "," + descendant_id + ")" + ",";
    }

    public void addBookValues(String id,
                              String isbn,
                              String language,
                              String pages,
                              String price,
                              String status,
                              String stock,
                              String title,
                              String year,
                              String category_id,
                              String image_id,
                              String publisher_id) {

        sql += "(";
        sql += id + ",'" + description + "','" + isbn + "','" + language + "',";
        sql += pages + "," + price + ",'" + status + "'," + stock + ",'" + title + "',";
        sql += year + "," + category_id + "," + image_id + "," + publisher_id;
        sql += ")" + ",";
    }

    public void addBooksAuthorsValues(String book_id, String author_id) {
        sql += "(" + book_id + "," + author_id + ")" + ",";
    }

    public void addItemValues(String id, String quantity, String book_id) {
        sql += "(" + id + "," + quantity + "," + book_id + ")" + ",";
    }

    public void addCartItemsValues(String cart_id, String items_id) {
        sql += "(" + cart_id + "," + items_id + ")" + ",";
    }

    //id bigint not null, carrier varchar(255), name varchar(255), phone varchar(255), tracking_number varchar(255), address_id bigint
    public void addShippingValues(String id,
                                  String carrier,
                                  String name,
                                  String phone,
                                  String tracking_number,
                                  String address_id) {

        sql += "(";
        sql += id + ",'" + carrier + "','" + name + "','" + phone + "','";
        sql += tracking_number + "'," + address_id;
        sql += ")" + ",";
    }

    //id bigint not null, amount bigint, currency varchar(255), description varchar(255), stripe_payment_intent_id varchar(255)
    public void addPaymentValues(String id,
                                 String amount,
                                 String currency,
                                 String description,
                                 String stripe_payment_intent_id) {

        sql += "(";
        sql += id + "," + amount + ",'" + currency + "','" + description + "','" + stripe_payment_intent_id + "'";
        sql += ")" + ",";
    }

    //id bigint not null, created_at timestamp, payment_id bigint, shipping_info_id bigint, user_id bigint
    public void addPedidoValues(String id,
                                String date,
                                String payment_id,
                                String shipping_info_id,
                                String user_id) {

        sql += "(";
        sql += id + ",'" + date + "'," + payment_id + "," + shipping_info_id + "," + user_id;
        sql += ")" + ",";
    }

    //order_id bigint not null, items_id
    public void addPedidoItemsValues(String order_id,
                                     String items_id) {

        sql += "(";
        sql += order_id + "," + items_id;
        sql += ")" + ",";
    }

    public void closeInsertQuery() {
        sql = sql.substring(0, sql.length() - 1) + ";"; //remove last ',' and append ';'
    }

    public void addNewLine() {
        sql += "\n";
    }

    public void addTwoNewLines() {
        this.addNewLine();
        this.addNewLine();
    }

    public void addSQL(String sql) {
        this.sql += sql;
    }

    public String getSql(){
        return sql;
    }

    public void reset(){
        sql = "";
    }
}
