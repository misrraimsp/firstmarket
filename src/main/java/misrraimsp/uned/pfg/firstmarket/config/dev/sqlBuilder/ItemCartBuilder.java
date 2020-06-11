package misrraimsp.uned.pfg.firstmarket.config.dev.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ItemCartBuilder {

    private static final String BuiltItemCartQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtItemCartQueries.txt";
    private static final int numOfItems = 200; // link with OrderBuilder's firstItemIdAvailable property

    public static void main(String[] args) throws IOException {
        build();
    }

    private static void build() throws IOException {
        //initialize
        NumberGenerator numberGenerator = new NumberGenerator();
        QueryHolder queryHolder = new QueryHolder();
        //build insert item query
        queryHolder.openInsertItemQuery();
        for (int i = 1; i <= numOfItems; i++){
            String dateTime = numberGenerator.getRandomDate();
            queryHolder.addItemValues(
                    String.valueOf(i),
                    "1",
                    dateTime,
                    "1",
                    dateTime,
                    numberGenerator.getRandomQuantity(),
                    numberGenerator.getRandomBookId()
            );
        }
        queryHolder.closeInsertQuery();
        //new lines
        queryHolder.addTwoNewLines();
        //build insert cart_items query
        queryHolder.openInsertCartItemsQuery();
        for (int i = 1; i <= numOfItems; i++){
            queryHolder.addCartItemsValues(
                    numberGenerator.getRandomCartId(),
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
