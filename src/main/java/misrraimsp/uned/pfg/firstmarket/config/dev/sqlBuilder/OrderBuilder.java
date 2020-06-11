package misrraimsp.uned.pfg.firstmarket.config.dev.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OrderBuilder {

    private static final String BuiltOrderQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtOrderQueries.txt";
    private static final int numOfOrders = 500;
    private static int nextItemId = 201; // link with ItemCartBuilder's numOfItems property

    public static void main(String[] args) throws IOException {
        build();
    }

    private static void build() throws IOException {
        //initialize
        NumberGenerator numberGenerator = new NumberGenerator();
        QueryHolder globalQueryHolder = new QueryHolder();
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
            String dateTime = numberGenerator.getRandomDate();
            shippingQueryHolder.addShippingValues(
                    String.valueOf(i),
                    "1",
                    dateTime,
                    "1",
                    dateTime,
                    "carrier",
                    "name",
                    "phone",
                    "tracking_number",
                    numberGenerator.getRandomAddressId()
            );
            paymentQueryHolder.addPaymentValues(
                    String.valueOf(i),
                    "1",
                    dateTime,
                    "1",
                    dateTime,
                    "1", //actual value set on app startup
                    "eur",
                    "test payment number " + i,
                    "stripe_payment_intent_id number " + i
            );
            pedidoQueryHolder.addPedidoValues(
                    String.valueOf(i),
                    String.valueOf(i),
                    dateTime,
                    String.valueOf(i),
                    dateTime,
                    numberGenerator.getRandomPedidoStatus(),
                    String.valueOf(i),
                    String.valueOf(i),
                    numberGenerator.getRandomUserId()
            );
            for (int j = 1; j <= Integer.parseInt(numberGenerator.getRandomNumOfItems()); j++) {
                itemQueryHolder.addItemValues(
                        String.valueOf(nextItemId),
                        "1",
                        dateTime,
                        "1",
                        dateTime,
                        numberGenerator.getRandomQuantity(),
                        numberGenerator.getRandomBookId()
                );
                pedidoItemsQueryHolder.addPedidoItemsValues(
                        String.valueOf(i),
                        String.valueOf(nextItemId)
                );
                nextItemId++;
            }
        }
        shippingQueryHolder.closeInsertQuery();
        paymentQueryHolder.closeInsertQuery();
        pedidoQueryHolder.closeInsertQuery();
        itemQueryHolder.closeInsertQuery();
        pedidoItemsQueryHolder.closeInsertQuery();
        //join queries
        globalQueryHolder.addSQL(shippingQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(paymentQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(pedidoQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(itemQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(pedidoItemsQueryHolder.getSql());
        globalQueryHolder.addNewLine();
        //output
        outputSQL(globalQueryHolder.getSql());
    }

    /**
     * Este método escribe el input parameter sql en un fichero
     * @param sql
     * @throws IOException
     */
    private static void outputSQL(String sql) throws IOException {
        Files.write(Paths.get(OrderBuilder.BuiltOrderQueriesPath), sql.getBytes());
        //System.out.println(sql);
    }
}
