package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

public class QueryHolder {

    private static final String categoryStaticPart = "INSERT INTO category (id,created_by,created_date,last_modified_by,last_modified_date,name,parent_id) VALUES ";
    private static final String catpathStaticPart = "INSERT INTO catpath (id,created_by,created_date,last_modified_by,last_modified_date,size,ancestor_id,descendant_id) VALUES ";
    private static final String bookStaticPart = "INSERT INTO book (id,created_by,created_date,last_modified_by,last_modified_date,description,isbn,language,pages,price,status,stock,title,year,category_id,image_id,publisher_id) VALUES ";
    private static final String booksAuthorsStaticPart = "INSERT INTO books_authors (book_id,author_id) VALUES ";
    private static final String itemStaticPart = "INSERT INTO item (id,created_by,created_date,last_modified_by,last_modified_date,quantity,book_id) VALUES ";
    private static final String cartItemsStaticPart = "INSERT INTO cart_items (cart_id,items_id) VALUES ";
    private static final String shippingStaticPart = "INSERT INTO shipping_info (id,created_by,created_date,last_modified_by,last_modified_date,carrier,name,phone,tracking_number,address_id) VALUES ";
    private static final String paymentStaticPart = "INSERT INTO payment (id,created_by,created_date,last_modified_by,last_modified_date,amount,currency,description,stripe_payment_intent_id) VALUES ";
    private static final String pedidoStaticPart = "INSERT INTO pedido (id,created_by,created_date,last_modified_by,last_modified_date,status,payment_id,shipping_info_id,user_id) VALUES ";
    private static final String saleStaticPart = "INSERT INTO sale (id,created_by,created_date,last_modified_by,last_modified_date,price,quantity,book_id) VALUES ";
    private static final String pedidoSalesStaticPart = "INSERT INTO pedido_sales (order_id,sales_id) VALUES ";


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

    public void openInsertSaleQuery() {
        sql += saleStaticPart;
    }

    public void openInsertPedidoSalesQuery() {
        sql += pedidoSalesStaticPart;
    }

    private String buildAuditoryPart(String creatorId,
                                     String creationDate,
                                     String modifierId,
                                     String modificationDate) {

        return "'" + creatorId + "',timestamp '" + creationDate + "','" + modifierId + "',timestamp '" + modificationDate + "'";
    }

    public void addCategoryValues(String id,
                                  String creatorId,
                                  String creationDate,
                                  String modifierId,
                                  String modificationDate,
                                  String name,
                                  String parent_id) {

        sql += "(" + id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += ",'" + name + "'," + parent_id + ")" + ",";
    }

    public void addCatpathValues(String id,
                                 String creatorId,
                                 String creationDate,
                                 String modifierId,
                                 String modificationDate,
                                 String size,
                                 String ancestor_id,
                                 String descendant_id) {

        sql += "(" + id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += "," + size + "," + ancestor_id + "," + descendant_id + ")" + ",";
    }

    public void addBookValues(String id,
                              String creatorId,
                              String creationDate,
                              String modifierId,
                              String modificationDate,
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

        sql += "(" + id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += ",'" + description + "','" + isbn + "','" + language + "',";
        sql += pages + "," + price + ",'" + status + "'," + stock + ",'" + title + "',";
        sql += year + "," + category_id + "," + image_id + "," + publisher_id;
        sql += ")" + ",";
    }

    public void addBooksAuthorsValues(String book_id, String author_id) {
        sql += "(" + book_id + "," + author_id + ")" + ",";
    }

    public void addItemValues(String id,
                              String creatorId,
                              String creationDate,
                              String modifierId,
                              String modificationDate,
                              String quantity,
                              String book_id) {

        sql += "(" + id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += "," + quantity + "," + book_id + ")" + ",";
    }

    public void addCartItemsValues(String cart_id, String items_id) {
        sql += "(" + cart_id + "," + items_id + ")" + ",";
    }

    public void addShippingValues(String id,
                                  String creatorId,
                                  String creationDate,
                                  String modifierId,
                                  String modificationDate,
                                  String carrier,
                                  String name,
                                  String phone,
                                  String tracking_number,
                                  String address_id) {

        sql += "(";
        sql += id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += ",'" + carrier + "','" + name + "','" + phone + "','";
        sql += tracking_number + "'," + address_id;
        sql += ")" + ",";
    }

    public void addPaymentValues(String id,
                                 String creatorId,
                                 String creationDate,
                                 String modifierId,
                                 String modificationDate,
                                 String amount,
                                 String currency,
                                 String description,
                                 String stripe_payment_intent_id) {

        sql += "(";
        sql += id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += "," + amount + ",'" + currency + "','" + description + "','" + stripe_payment_intent_id + "'";
        sql += ")" + ",";
    }

    public void addPedidoValues(String id,
                                String creatorId,
                                String creationDate,
                                String modifierId,
                                String modificationDate,
                                String status,
                                String payment_id,
                                String shipping_info_id,
                                String user_id) {

        sql += "(";
        sql += id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += ",'" + status + "'," + payment_id + "," + shipping_info_id + "," + user_id;
        sql += ")" + ",";
    }

    //(id,created_by,created_date,last_modified_by,last_modified_date,price,quantity,book_id)
    public void addSaleValues(String id,
                              String creatorId,
                              String creationDate,
                              String modifierId,
                              String modificationDate,
                              String price,
                              String quantity,
                              String book_id) {

        sql += "(" + id + ",";
        sql += buildAuditoryPart(creatorId,creationDate,modifierId,modificationDate);
        sql += "," + price + "," + quantity + "," + book_id + ")" + ",";
    }

    public void addPedidoSalesValues(String order_id,
                                     String sales_id) {

        sql += "(";
        sql += order_id + "," + sales_id;
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
