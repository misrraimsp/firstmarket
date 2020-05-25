package misrraimsp.uned.pfg.firstmarket.config.dev.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookConfigurer {

    private static final String BuiltBookQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtBookQueries.txt";
    private static final int numBooks = 1000; //1k

    public static void main(String[] args) throws IOException {
        configure();
    }

    private static void configure() throws IOException {
        //initialize
        NumberGenerator numberGenerator = new NumberGenerator();
        IsbnHolder isbnHolder = new IsbnHolder(numBooks);
        QueryHolder queryHolder = new QueryHolder();
        //build insert book query
        queryHolder.openInsertBookQuery();
        for (int i = 1; i <= numBooks; i++){
            queryHolder.addBookValues(
                    String.valueOf(i),
                    isbnHolder.getIsbn(),
                    numberGenerator.getRandomLanguage(),
                    numberGenerator.getRandomNumPages(),
                    numberGenerator.getRandomPrice(),
                    numberGenerator.getRandomStock(),
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
        for (int i = 1; i <= numBooks; i++){
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
