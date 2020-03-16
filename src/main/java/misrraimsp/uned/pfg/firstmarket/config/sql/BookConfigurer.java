package misrraimsp.uned.pfg.firstmarket.config.sql;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookConfigurer {

    private static final String BuiltBookQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtBookQueries.txt";
    private static final int numBooks = 1000;

    public static void main(String[] args) throws IOException {
        configure();
    }
//(id,description,isbn,language,num_pages,price,stock,title,year,category_id,image_id,publisher_id)
    private static void configure() throws IOException {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator();
        IsbnHolder isbnHolder = new IsbnHolder();
        QueryHolder queryHolder = new QueryHolder();

        for (int i = 1; i <= numBooks; i++){
            queryHolder.addInsertBookQuery(
                    String.valueOf(i),
                    isbnHolder.getNextIsbn(),
                    randomNumberGenerator.getRandomLanguage(),
                    randomNumberGenerator.getRandomNumPages(),
                    randomNumberGenerator.getRandomPrice(),
                    randomNumberGenerator.getRandomStock(),
                    "title" + i,
                    randomNumberGenerator.getRandomYear(),
                    randomNumberGenerator.getRandomCategoryId(),
                    randomNumberGenerator.getRandomImageId(),
                    randomNumberGenerator.getRandomPublisherId()
            );
            queryHolder.addNewLine();
        }

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
