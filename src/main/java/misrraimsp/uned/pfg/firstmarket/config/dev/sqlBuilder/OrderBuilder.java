package misrraimsp.uned.pfg.firstmarket.config.dev.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OrderBuilder {

    private static final String BuiltOrderQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtOrderQueries.txt";
    private static final int numOfOrders = 1000;

    public static void main(String[] args) throws IOException {
        build();
    }

    private static void build() throws IOException {
        //initialize
        NumberGenerator numberGenerator = new NumberGenerator();
        QueryHolder shippingQueryHolder = new QueryHolder();
        QueryHolder paymentQueryHolder = new QueryHolder();
        QueryHolder pedidoQueryHolder = new QueryHolder();
        QueryHolder itemQueryHolder = new QueryHolder();
        QueryHolder pedidoItemsQueryHolder = new QueryHolder();
        //build order queries
        shippingQueryHolder.openInsertShippingQuery();
        paymentQueryHolder.openInsertPaymentQuery();
        pedidoQueryHolder.openInsertPedidoQuery();
        itemQueryHolder.openInsertItemQuery();
        pedidoItemsQueryHolder.openInsertPedidoItemsQuery();
        for (int i = 1; i <= numOfOrders; i++){
            shippingQueryHolder.addShippingValues(
                    String.valueOf(i),
                    "Arrecife",
                    "España",
                    "calle Galicia, 13",
                    "puerta 2",
                    "35500",
                    "Las Palmas"
            );
        }
        queryHolder.closeInsertQuery();
        //new lines
        queryHolder.addNewLine();
        queryHolder.addNewLine();
        //build insert cart_items query
        queryHolder.openInsertCartItemsQuery();
        for (int i = 1; i <= numOfOrders; i++){
            queryHolder.addCartItemsValues(
                    numberGenerator.getRandomCartId(),
                    String.valueOf(i)
            );
        }
        queryHolder.closeInsertQuery();
        //output
        outputSQL(queryHolder.getSql(), BuiltOrderQueriesPath);
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
