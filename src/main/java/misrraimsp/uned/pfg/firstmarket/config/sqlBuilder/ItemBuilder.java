package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ItemBuilder {

    private static final String BuiltItemCartQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtItemQueries.txt";
    private static final int numOfItems = 200;

    public static void main(String[] args) throws IOException {
        build();
    }

    private static void build() throws IOException {
        //initialize
        ValueGenerator valueGenerator = new ValueGenerator();
        QueryHolder queryHolder = new QueryHolder();
        //build insert item query
        queryHolder.openInsertItemQuery();
        for (int i = 1; i <= numOfItems; i++){
            String dateTime = valueGenerator.getRandomDateTime();
            queryHolder.addItemValues(
                    String.valueOf(i),
                    "1",
                    dateTime,
                    "1",
                    dateTime,
                    valueGenerator.getRandomQuantity(),
                    valueGenerator.getRandomBookId()
            );
        }
        queryHolder.closeInsertQuery();
        //new lines
        queryHolder.addTwoNewLines();
        //build insert cart_items query
        queryHolder.openInsertCartItemsQuery();
        for (int i = 1; i <= numOfItems; i++){
            queryHolder.addCartItemsValues(
                    valueGenerator.getRandomCartId(),
                    String.valueOf(i)
            );
        }
        queryHolder.closeInsertQuery();
        //output
        outputSQL(queryHolder.getSql(), BuiltItemCartQueriesPath);
    }

    /**
     * Este método escribe el input parameter sql en un fichero
     * @param sql
     * @param fileName
     * @throws IOException
     */
    private static void outputSQL(String sql, String fileName) throws IOException {
        Files.write(Paths.get(fileName), sql.getBytes());
        //System.out.println(sql);
    }
}
