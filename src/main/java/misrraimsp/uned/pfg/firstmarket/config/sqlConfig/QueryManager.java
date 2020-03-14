package misrraimsp.uned.pfg.firstmarket.config.sqlConfig;

public class QueryManager {

    private static final String categoryStaticPart = "INSERT INTO category (id,name,parent_id) VALUES ";
    private static final String catpathStaticPart = "INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES ";

    private String sql;

    public QueryManager(){
        this.reset();
    }

    public void addCategoryQuery(String id, String name, String parent_id) {
        sql += categoryStaticPart + "(" + id + ",'" + name + "'," + parent_id + ")";
    }

    public void addNewLine() {
        sql += "\\n";
    }

    public void addCatpathQuery(String id, String size, String ancestor_id, String descendant_id) {
        sql += catpathStaticPart + "(" + id + "," + size + "," + ancestor_id + "," + descendant_id + ")";
    }

    public String getSql(){
        return sql;
    }

    public void reset(){
        sql = "";
    }
}
