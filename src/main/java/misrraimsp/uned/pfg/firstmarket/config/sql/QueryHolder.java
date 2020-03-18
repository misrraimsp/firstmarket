package misrraimsp.uned.pfg.firstmarket.config.sql;

public class QueryHolder {

    private static final String categoryStaticPart = "INSERT INTO category (id,name,parent_id) VALUES ";
    private static final String catpathStaticPart = "INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES ";
    private static final String bookStaticPart = "INSERT INTO book (id,description,isbn,language,num_pages,price,stock,title,year,category_id,image_id,publisher_id) VALUES ";
    private static final String booksAuthorsStaticPart = "INSERT INTO books_authors (book_id,author_id) VALUES ";

    private static final String description = "@description";

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

    public void addCategoryValues(String id, String name, String parent_id) {
        sql += "(" + id + ",'" + name + "'," + parent_id + ")" + ",";
    }

    public void addCatpathValues(String id, String size, String ancestor_id, String descendant_id) {
        sql += "(" + id + "," + size + "," + ancestor_id + "," + descendant_id + ")" + ",";
    }

    public void addBookValues(String id,
                              String isbn,
                              String language,
                              String num_pages,
                              String price,
                              String stock,
                              String title,
                              String year,
                              String category_id,
                              String image_id,
                              String publisher_id) {

        sql += "(";
        sql += id + "," + description + ",'" + isbn + "'," + language + ",";
        sql += num_pages + "," + price + "," + stock + ",'" + title + "',";
        sql += year + "," + category_id + "," + image_id + "," + publisher_id;
        sql += ")" + ",";
    }

    public void addBooksAuthorsValues(String book_id, String author_id) {
        sql += "(" + book_id + "," + author_id + ")" + ",";
    }

    public void closeInsertQuery() {
        sql = sql.substring(0, sql.length() - 1) + ";"; //remove last ',' and append ';'
    }

    public void addNewLine() {
        sql += "\n";
    }

    public String getSql(){
        return sql;
    }

    public void reset(){
        sql = "";
    }
}
