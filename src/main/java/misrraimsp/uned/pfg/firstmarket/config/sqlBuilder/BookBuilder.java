package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

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
        QueryHolder globalQueryHolder = new QueryHolder();
        QueryHolder bookQueryHolder = new QueryHolder();
        QueryHolder bookAuthorsQueryHolder = new QueryHolder();
        Set<String> authorIds = new HashSet<>();
        bookQueryHolder.openInsertBookQuery();
        bookAuthorsQueryHolder.openInsertBooksAuthorsQuery();
        for (int i = 1; i <= numOfBooks; i++) {
            //build insert book queries
            String status = valueGenerator.getRandomBookStatus();
            String stock = (status.equals("OUT_OF_STOCK")) ? "0" : valueGenerator.getRandomStock();
            String dateTime = valueGenerator.getRandomDateTime();
            bookQueryHolder.addBookValues(
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
            //build insert book_authors queries
            authorIds.clear();
            while (authorIds.size() < valueGenerator.getRandomNumOfAuthors()) {
                authorIds.add(valueGenerator.getRandomAuthorId());
            }
            int finalI = i;
            authorIds.forEach(authorId -> bookAuthorsQueryHolder.addBooksAuthorsValues(
                    String.valueOf(finalI),
                    authorId)
            );
        }
        bookQueryHolder.closeInsertQuery();
        bookAuthorsQueryHolder.closeInsertQuery();
        //join queries
        globalQueryHolder.addSQL(bookQueryHolder.getSql());
        globalQueryHolder.addTwoNewLines();
        globalQueryHolder.addSQL(bookAuthorsQueryHolder.getSql());
        globalQueryHolder.addNewLine();
        //output
        outputSQL(globalQueryHolder.getSql());

    }

    private static void outputSQL(String sql) throws IOException {
        Files.write(Paths.get(BookBuilder.BuiltBookQueriesPath), sql.getBytes());
        //System.out.println(sql);
    }

}
