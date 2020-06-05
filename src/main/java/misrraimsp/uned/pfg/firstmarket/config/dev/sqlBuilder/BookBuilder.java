package misrraimsp.uned.pfg.firstmarket.config.dev.sqlBuilder;

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
        NumberGenerator numberGenerator = new NumberGenerator();
        IsbnHolder isbnHolder = new IsbnHolder(numOfBooks);
        QueryHolder queryHolder = new QueryHolder();
        //build insert book query
        queryHolder.openInsertBookQuery();
        for (int i = 1; i <= numOfBooks; i++){
            String status = numberGenerator.getRandomBookStatus();
            String stock = (status.equals("OUT_OF_STOCK")) ? "0" : numberGenerator.getRandomStock();
            queryHolder.addBookValues(
                    String.valueOf(i),
                    isbnHolder.getIsbn(),
                    numberGenerator.getRandomLanguage(),
                    numberGenerator.getRandomNumPages(),
                    numberGenerator.getRandomPrice(),
                    status,
                    stock,
                    "title-" + i,
                    numberGenerator.getRandomYear(),
                    numberGenerator.getRandomCategoryId(),
                    numberGenerator.getRandomImageId(),
                    numberGenerator.getRandomPublisherId()
            );
        }
        queryHolder.closeInsertQuery();
        //new lines
        queryHolder.addNewLine();
        queryHolder.addNewLine();
        //build insert books_authors query
        queryHolder.openInsertBooksAuthorsQuery();
        for (int i = 1; i <= numOfBooks; i++){
            for (int j = 1; j <= numberGenerator.getRandomNumOfAuthors(); j++) {
                queryHolder.addBooksAuthorsValues(
                        String.valueOf(i),
                        numberGenerator.getRandomAuthorId()
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