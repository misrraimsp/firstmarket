
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

INSERT INTO image (id,data,is_default,mime_type,name) VALUES (1,LOAD_FILE('/Users/macbookpro/Desktop/EmbajadaMisrra/pfg/firstmarket/img/fm.png'),true,'image/png','fm.png');
INSERT INTO image (id,data,is_default,mime_type,name) VALUES (2,LOAD_FILE('/Users/macbookpro/Desktop/EmbajadaMisrra/pfg/firstmarket/img/rojo.jpg'),false,'image/jpeg','rojo.jpg');
INSERT INTO image (id,data,is_default,mime_type,name) VALUES (3,LOAD_FILE('/Users/macbookpro/Desktop/EmbajadaMisrra/pfg/firstmarket/img/azul.jpg'),false,'image/jpeg','azul.jpg');

INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (1,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (2,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (3,'00003','Traditional Music of Spain',7,1);

INSERT INTO item (id,quantity,book_id) VALUES (1,1,1);
INSERT INTO item (id,quantity,book_id) VALUES (2,2,3);

INSERT INTO cart (id,last_modified) VALUES (1,null);
INSERT INTO cart (id,last_modified) VALUES (2,null);
INSERT INTO cart (id,last_modified) VALUES (3,null);

INSERT INTO cart_items (cart_id,items_id) VALUES (3,1);
INSERT INTO cart_items (cart_id,items_id) VALUES (3,2);

INSERT INTO role (id,name) VALUES (1,'ROLE_ADMIN');
INSERT INTO role (id,name) VALUES (2,'ROLE_USER');

INSERT INTO user (id,email,first_name,last_name,password,cart_id) VALUES (1,'admin@fm.com','afn','aln','$2a$10$b6cNYRchyum58sJw3BrS2OYRm9GiqRYDwlHTLKFY3fdD/kRS2GL0G',1);
INSERT INTO user (id,email,first_name,last_name,password,cart_id) VALUES (2,'peter@fm.com','Peter','Parker','$2a$10$b6cNYRchyum58sJw3BrS2OYRm9GiqRYDwlHTLKFY3fdD/kRS2GL0G',2);
INSERT INTO user (id,email,first_name,last_name,password,cart_id) VALUES (3,'marco@fm.com','Marco','Polo','$2a$10$b6cNYRchyum58sJw3BrS2OYRm9GiqRYDwlHTLKFY3fdD/kRS2GL0G',3);

INSERT INTO users_roles (user_id,role_id) VALUES (1,1);
INSERT INTO users_roles (user_id,role_id) VALUES (2,2);
INSERT INTO users_roles (user_id,role_id) VALUES (3,2);
