package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookBuilder {

    private static final String BuiltBookQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtBookQueries.txt";
    private static final int numOfBooks = 1000; // link with NumberGenerator's MAX_BOOK_ID constant

    public static void main(String[] args) throws IOException {
        configure();
    }

    private static void configure() throws IOException {
        //initialize
        ValueGenerator valueGenerator = new ValueGenerator();
        IsbnHolder isbnHolder = new IsbnHolder(numOfBooks);
        QueryHolder queryHolder = new QueryHolder();
        //build insert book query
        queryHolder.openInsertBookQuery();
        for (int i = 1; i <= numOfBooks; i++){
            String status = valueGenerator.getRandomBookStatus();
            String stock = (status.equals("OUT_OF_STOCK")) ? "0" : valueGenerator.getRandomStock();
            String dateTime = valueGenerator.getRandomDateTime();
            queryHolder.addBookValues(
                    String.valueOf(i),
                    "1",
                    dateTime,
                    "1",
                    dateTime,
                    isbnHolder.getIsbn(),
                    valueGenerator.getRandomLanguage(),
                    valueGenerator.getRandomNumPages(),
                    valueGenerator.getRandomPrice(),
                    status,
                    stock,
                    "title-" + i,
                    valueGenerator.getRandomYear(),
                    valueGenerator.getRandomCategoryId(),
                    valueGenerator.getRandomImageId(),
                    valueGenerator.getRandomPublisherId()
            );
        }
        queryHolder.closeInsertQuery();
        //new lines
        queryHolder.addTwoNewLines();
        //build insert books_authors query
        queryHolder.openInsertBooksAuthorsQuery();
        for (int i = 1; i <= numOfBooks; i++){
            for (int j = 1; j <= valueGenerator.getRandomNumOfAuthors(); j++) {
                queryHolder.addBooksAuthorsValues(
                        String.valueOf(i),
                        valueGenerator.getRandomAuthorId()
                );
            }
        }
        queryHolder.closeInsertQuery();
        //output
        outputSQL(queryHolder.getSql(), BuiltBookQueriesPath);
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
