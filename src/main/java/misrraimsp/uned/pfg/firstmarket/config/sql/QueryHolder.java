package misrraimsp.uned.pfg.firstmarket.config.sql;

public class QueryHolder {

    private static final String categoryStaticPart = "INSERT INTO category (id,name,parent_id) VALUES ";
    private static final String catpathStaticPart = "INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES ";
    private static final String bookStaticPart = "INSERT INTO book (id,description,isbn,language,num_pages,price,stock,title,year,category_id,image_id,publisher_id) VALUES ";

    private static final String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
            "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private String sql;

    public QueryHolder(){
        this.reset();
    }

    public void addInsertCategoryQuery(String id, String name, String parent_id) {
        sql += categoryStaticPart + "(" + id + ",'" + name + "'," + parent_id + ")" + ";";
    }

    public void addInsertCatpathQuery(String id, String size, String ancestor_id, String descendant_id) {
        sql += catpathStaticPart + "(" + id + "," + size + "," + ancestor_id + "," + descendant_id + ")" + ";";
    }

    public void addInsertBookQuery(String id,
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

        sql += bookStaticPart + "(";
        sql += id + ",'" + description + "','" + isbn + "'," + language + ",";
        sql += num_pages + "," + price + "," + stock + ",'" + title + "',";
        sql += year + "," + category_id + "," + image_id + "," + publisher_id;
        sql += ")" + ";";
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
