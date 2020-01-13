    /**
     * ---- Alternative way of getting root category: Fetching all categories
     *      and iterating through them until meeting root condition
     *
    private Category getRootCategory(){
        for (Category category : categoryRepository.findAll()){
            if (category.isRootCategory()){
               return category;
            }
        }
        return null;
    }
    */


    /**
    * alternative way of determining the root category, without using self-parenthood
    */
    @Query(value = "SELECT id,name,parent_id FROM (" +
                        "SELECT c.id,name,parent_id,COUNT(c.id) AS count " +
                        "FROM category AS c, cat_path AS cp " +
                        "WHERE c.id=cp.descendant_id " +
                        "GROUP BY c.id" +
                    ") AS aux " +
                    "WHERE aux.count=1",
            nativeQuery = true)
    Category getRootCategory();

     /**
     * Elimina los caminos que parten desde cada ancestro de la categoría a editar (INcluída ella misma)
     * hasta cada uno de sus descendientes (INcluída ella misma), salvo su propio autocamino
     *
     * @param id
     */
     @Modifying(clearAutomatically = true)
     @Query(value = "DELETE FROM CatPath cp WHERE " +
     "cp.ancestor.id IN (SELECT cp1.ancestor.id FROM CatPath cp1 WHERE cp1.descendant.id = :id) AND " +
     "cp.descendant.id IN (SELECT cp2.descendant.id FROM CatPath cp2 WHERE cp2.ancestor.id = :id) AND " +
     "cp.ancestor.id <> cp.descendant.id")
     void deletePaths(@Param("id") Long id);


*********************************

CREATE DATABASE fm;
USE fm;

CREATE TABLE categories (category_id INT UNSIGNED AUTO_INCREMENT, name VARCHAR(60) NOT NULL, PRIMARY KEY(category_id));
CREATE TABLE catpaths (ancestor INT UNSIGNED, descendant INT UNSIGNED, path_length INT UNSIGNED NOT NULL, PRIMARY KEY (ancestor, descendant), FOREIGN KEY (ancestor) REFERENCES categories(category_id), FOREIGN KEY (descendant) REFERENCES categories(category_id));
CREATE TABLE books (isbn VARCHAR(60), title VARCHAR(60) NOT NULL, category_id INT UNSIGNED, PRIMARY KEY (isbn), FOREIGN KEY (category_id) REFERENCES categories(category_id));

INSERT INTO categories (name) VALUES ('fm');
INSERT INTO categories (name) VALUES ('Computers & Tech');
INSERT INTO categories (name) VALUES ('Databases');
INSERT INTO categories (name) VALUES ('PostgreSQL');
INSERT INTO categories (name) VALUES ('MySQL');
INSERT INTO categories (name) VALUES ('Arts & Music');
INSERT INTO categories (name) VALUES ('Music');
INSERT INTO categories (name) VALUES ('Painting');

INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,1,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (2,2,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (3,3,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (4,4,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (5,5,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (6,6,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (7,7,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (8,8,0);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,2,1);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,3,2);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,4,3);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,5,3);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (2,3,1);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (2,4,2);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (2,5,2);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (3,4,1);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (3,5,1);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,6,1);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,7,2);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (1,8,2);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (6,7,1);
INSERT INTO catpaths (ancestor, descendant, path_length) VALUES (6,8,1);

INSERT INTO books (isbn,title,category_id) VALUES ('00001','JavaWeb','2');
INSERT INTO books (isbn,title,category_id) VALUES ('00002','Java Programming','2');

/*query para actualizar los enlaces tras una inserción de una categoría*/
INSERT INTO catpaths (ancestor,descendant,path_length) SELECT ancestor,(SELECT MAX(category_id) FROM categories),path_length+1 FROM catpaths where descendant=2 UNION ALL SELECT (SELECT MAX(category_id) FROM categories),(SELECT MAX(category_id) FROM categories),(SELECT 0)

/********************queries para reubicar un subarbol
/* 1-eliminar enlaces antiguos
DELETE FROM catpaths WHERE descendant IN (SELECT descendant FROM catpaths WHERE ancestor=3) AND ancestor IN (SELECT ancestor FROM catpaths WHERE descendant=3 AND ancestor != descendant);
/* 2-crear enlaces nuevos
INSERT INTO catpaths (ancestor, descendant, path_length) SELECT supertree.ancestor, subtree.descendant, supertree.path_length+subtree.path_length+1 FROM catpaths AS supertree CROSS JOIN catpaths AS subtree WHERE supertree.descendant=7 AND subtree.ancestor=3;

/********************queries para eliminar una categoria (SIN subarbol)
/* 1-disminuir en una unidad el camino de todos los descendientes del nodo que se quiere eliminar
update catpaths set path_length=path_length-1 where (ancestor,descendant) IN (select aux1.ancestor, aux2.descendant from (select * from catpaths where descendant=? and ancestor!=?) as aux1,(select * from catpaths where descendant!=? and ancestor=?) as aux2);
/* 2-eliminar conexiones con la categoria a suprimir
delete from catpaths where (ancestor,descendant) in (select ancestor,descendant from catpaths where ancestor=? or descendant=?)
/* 3-eliminar la categoria en cuestion de la tabla de categorias
delete from categories where category_id=?

/********************queries para eliminar una(s) categoria(s) (CON subarbol)
/* 1-recuperar y guardar en VARIABLE la lista de category_id de las categorias a eliminar
VARIABLE <-- SELECT descendant FROM catpaths WHERE ancestor=?
/* 2-eliminar todos los caminos que entran o salen de alguna categoria a eliminar
DELETE FROM catpaths WHERE (ancestor,descendant) IN (SELECT ancestor,descendant FROM catpaths WHERE ancestor IN (VARIABLE) OR descendant IN (VARIABLE))
/* 3-eliminar las categorias en cuestion de la tabla de categorias
DELETE FROM categories WHERE category_id IN (VARIABLE)


/********************cambiar la categoria de un grupo de libros (en el contexto de la eliminacion de un nodo del arbol de categorias)
UPDATE books SET category_id=? WHERE isbn IN (SELECT isbn FROM books WHERE category_id=?)

/********************cambiar la categoria de un grupo de libros (en el contexto de la eliminacion de un subarbol del arbol de categorias)
UPDATE books SET category_id=? WHERE isbn IN (SELECT isbn FROM books WHERE category_id IN (SELECT descendant FROM catpaths WHERE ancestor=?))


*********************************************************************************************************************************************************************













INSERT INTO catpaths (ancestor, descendant, path_length) ((SELECT (t.ancestor, (SELECT MAX(category_id) FROM categories)) FROM catpaths AS t WHERE t.descendant = 2) UNION ALL (SELECT (SELECT MAX(category_id) FROM categories), (SELECT MAX(category_id) FROM categories)));



INSERT INTO catpaths (ancestor, descendant, path_length) SELECT p.ancestor, c.descendant, p.path_length+c.path_length+1 FROM catpaths AS p, catpaths AS c WHERE p.descendant=2




DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';


CREATE TABLE category (ID INT NOT NULL AUTO_INCREMENT,CATEGORY_DESCRIPTION VARCHAR(20) NOT NULL,PRIMARY KEY (ID));

CREATE TABLE book (ID INT NOT NULL AUTO_INCREMENT,CATEGORY_ID INT NOT NULL,BOOK_TITLE VARCHAR(60) NOT NULL,PUBLISHER VARCHAR(60) NOT NULL,PRIMARY KEY (ID),CONSTRAINT FK_BOOK_1 FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID));

CREATE TABLE author (ID INT NOT NULL AUTO_INCREMENT,BOOK_ID INT NOT NULL,FIRST_NAME VARCHAR(20) NOT NULL,LAST_NAME VARCHAR(20) NOT NULL,PRIMARY KEY (ID),CONSTRAINT FK_AUTHOR_1 FOREIGN KEY (BOOK_ID) REFERENCES BOOK (ID));


insert into category (category_description) values ('Clojure');
insert into category (category_description) values ('Groovy');
insert into category (category_description) values ('Java');
insert into category (category_description) values ('Scala');


insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (1, 'Practical Clojure', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (2, 'Beginning Groovy, Grails and Griffon', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (2, 'Definitive Guide to Grails 2', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (2, 'Groovy and Grails Recipes', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Modern Java Web Development', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Java 7 Recipes', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Java EE 7 Recipes', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Beginning Java 7 ', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Pro Java 7 NIO.2', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Java 7 for Absolute Beginners', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (3, 'Oracle Certified Java Enterprise Architect Java EE7', 'Apress');
insert into book (CATEGORY_ID, BOOK_TITLE, PUBLISHER) values (4, 'Beginning Scala', 'Apress');


insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (1, 'Luke', 'VanderHart');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (2, 'Vishal', 'Layka');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (3, 'Jeff', 'Brown');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (4, 'Bashar', 'Jawad');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (5, 'Vishal', 'Layka');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (6, 'Josh',  'Juneau');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (7, 'Josh', 'Juneau');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (8, 'Jeff', 'Friesen');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (9, 'Anghel', 'Leonard');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (10, 'Jay',  'Bryant');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (11, 'B V', 'Kumar');
insert into author (BOOK_ID, FIRST_NAME, LAST_NAME) values (12, 'David', 'Pollak');



CREATE TABLE Comments (comment_id SERIAL PRIMARY KEY,bug_id BIGINT UNSIGNED NOT NULL,author BIGINT UNSIGNED NOT NULL,comment_date DATETIME NOT NULL,comment TEXT NOT NULL,FOREIGN KEY (bug_id) REFERENCES Bugs(bug_id),FOREIGN KEY (author) REFERENCES Accounts(account_id));

CREATE TABLE TreePaths (ancestor BIGINT UNSIGNED NOT NULL,descendant BIGINT UNSIGNED NOT NULL,PRIMARY KEY(ancestor, descendant),FOREIGN KEY (ancestor) REFERENCES Comments(comment_id),FOREIGN KEY (descendant) REFERENCES Comments(comment_id));