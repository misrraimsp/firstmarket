package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OrderBuilder {

    private static final String BuiltOrderQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtOrderQueries.txt";
    private static final int numOfOrders = 500;
    private static int nextSaleId = 1;

    public static void main(String[] args) throws IOException {
        build();
    }

    private static void build() throws IOException {
        //initialize
        ValueGenerator valueGenerator = new ValueGenerator();
        QueryHolder globalQueryHolder = new QueryHolder();
        QueryHolder shippingQueryHolder = new QueryHolder();
        QueryHolder paymentQueryHolder = new QueryHolder();
        QueryHolder pedidoQueryHolder = new QueryHolder();
        QueryHolder saleQueryHolder = new QueryHolder();
        QueryHolder pedidoSalesQueryHolder = new QueryHolder();
        //build order queries
        shippingQueryHolder.openInsertShippingQuery();
        paymentQueryHolder.openInsertPaymentQuery();
        pedidoQueryHolder.openInsertPedidoQuery();
        saleQueryHolder.openInsertSaleQuery();
        pedidoSalesQueryHolder.openInsertPedidoSalesQuery();
        for (int i = 1; i <= numOfOrders; i++){
            String dateTime = valueGenerator.getRandomDateTime();
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
                    valueGenerator.getRandomAddressId()
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
                    valueGenerator.getRandomPedidoStatus(),
                    String.valueOf(i),
                    String.valueOf(i),
                    valueGenerator.getRandomUserId()
            );
            for (int j = 1; j <= Integer.parseInt(valueGenerator.getRandomNumOfSales()); j++) {
                saleQueryHolder.addSaleValues(
                        String.valueOf(nextSaleId),
                        "1",
                        dateTime,
                        "1",
                        dateTime,
                        valueGenerator.getRandomPrice(),
                        valueGenerator.getRandomQuantity(),
                        valueGenerator.getRandomBookId()
                );
                pedidoSalesQueryHolder.addPedidoSalesValues(
                        String.valueOf(i),
                        String.valueOf(nextSaleId)
                );
                nextSaleId++;
            }
        }
        shippingQueryHolder.closeInsertQuery();
        paymentQueryHolder.closeInsertQuery();
        pedidoQueryHolder.closeInsertQuery();
        saleQueryHolder.closeInsertQuery();
        pedidoSalesQueryHolder.closeInsertQuery();
        //join queries
        globalQueryHolder.addSQL(shippingQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(paymentQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(pedidoQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(saleQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(pedidoSalesQueryHolder.getSql());
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
