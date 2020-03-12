
/*
don't forget update sequence initial number XXX on schema.sql:
**create sequence hibernate_sequence start with XXX increment by 1;**
 */


INSERT INTO category (id,name,parent_id) VALUES (1,'fm',1);
INSERT INTO category (id,name,parent_id) VALUES (2,'Computers & Tech',1);
INSERT INTO category (id,name,parent_id) VALUES (3,'Databases',2);
INSERT INTO category (id,name,parent_id) VALUES (4,'PostgreSQL',3);
INSERT INTO category (id,name,parent_id) VALUES (5,'MySQL',3);
INSERT INTO category (id,name,parent_id) VALUES (6,'Arts & Music',1);
INSERT INTO category (id,name,parent_id) VALUES (7,'Music',6);
INSERT INTO category (id,name,parent_id) VALUES (8,'Painting',6);

INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (1,0,1,1);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (2,1,1,2);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (3,1,1,6);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (4,2,1,3);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (5,2,1,7);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (6,2,1,8);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (7,3,1,4);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (8,3,1,5);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (9,0,2,2);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (10,1,2,3);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (11,2,2,4);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (12,2,2,5);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (13,0,3,3);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (14,1,3,4);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (15,1,3,5);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (16,0,4,4);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (17,0,5,5);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (18,0,6,6);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (19,1,6,7);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (20,1,6,8);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (21,0,7,7);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (22,0,8,8);

INSERT INTO image (id,data,is_default,mime_type,name) VALUES (1,LOAD_FILE('/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/img/fm.png'),true,'image/png','fm.png');
INSERT INTO image (id,data,is_default,mime_type,name) VALUES (2,LOAD_FILE('/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/img/rojo.jpg'),false,'image/jpeg','rojo.jpg');
INSERT INTO image (id,data,is_default,mime_type,name) VALUES (3,LOAD_FILE('/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/img/azul.jpg'),false,'image/jpeg','azul.jpg');

INSERT INTO publisher (id,name) VALUES (1,'Anaya');

INSERT INTO book (id,description,isbn,language,num_pages,price,stock,title,year,category_id,image_id,publisher_id) VALUES (1,'this is the first test book','isbn00001',1,199,24.99,10,'JavaWeb',2020,2,2,1);

INSERT INTO author (id,first_name,last_name) VALUES (1,'Antonio','Escohotado');
INSERT INTO author (id,first_name,last_name) VALUES (2,'Pablo','Iglesias');
INSERT INTO author (id,first_name,last_name) VALUES (3,'Pepe','Damaso');

INSERT INTO books_authors (book_id,author_id) VALUES (1,1);
INSERT INTO books_authors (book_id,author_id) VALUES (1,2);
INSERT INTO books_authors (book_id,author_id) VALUES (1,3);

INSERT INTO item (id,quantity,book_id) VALUES (1,1,1);

INSERT INTO cart (id,last_modified) VALUES (1,null);
INSERT INTO cart (id,last_modified) VALUES (2,null);
INSERT INTO cart (id,last_modified) VALUES (3,null);

INSERT INTO cart_items (cart_id,items_id) VALUES (3,1);

INSERT INTO profile (id,first_name,last_name) VALUES (1,'afn','aln');
INSERT INTO profile (id,first_name,last_name) VALUES (2,'Peter','Parker');
INSERT INTO profile (id,first_name,last_name) VALUES (3,'Marco','Polo');

INSERT INTO user (id,email,password,cart_id,profile_id) VALUES (1,'admin@fm.com','$2a$10$b6cNYRchyum58sJw3BrS2OYRm9GiqRYDwlHTLKFY3fdD/kRS2GL0G',1,1);
INSERT INTO user (id,email,password,cart_id,profile_id) VALUES (2,'peter@fm.com','$2a$10$b6cNYRchyum58sJw3BrS2OYRm9GiqRYDwlHTLKFY3fdD/kRS2GL0G',2,2);
INSERT INTO user (id,email,password,cart_id,profile_id) VALUES (3,'marco@fm.com','$2a$10$b6cNYRchyum58sJw3BrS2OYRm9GiqRYDwlHTLKFY3fdD/kRS2GL0G',3,3);

INSERT INTO role (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO role (id,name) VALUES (2,'ROLE_USER');

INSERT INTO users_roles (user_id,role_id) VALUES (1,1);
INSERT INTO users_roles (user_id,role_id) VALUES (2,2);
INSERT INTO users_roles (user_id,role_id) VALUES (3,2);
