
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

INSERT INTO image (id,data,is_default,mime_type,name) VALUES (1,LOAD_FILE('/Users/macbookpro/Desktop/EmbajadaMisrra/pfg/firstmarket/img/fm.png'),true,'image/png','fm.png');
INSERT INTO image (id,data,is_default,mime_type,name) VALUES (2,LOAD_FILE('/Users/macbookpro/Desktop/EmbajadaMisrra/pfg/firstmarket/img/rojo.jpg'),false,'image/jpeg','rojo.jpg');
INSERT INTO image (id,data,is_default,mime_type,name) VALUES (3,LOAD_FILE('/Users/macbookpro/Desktop/EmbajadaMisrra/pfg/firstmarket/img/azul.jpg'),false,'image/jpeg','azul.jpg');

INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (1,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (2,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (3,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (4,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (5,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (6,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (7,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (8,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (9,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (10,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (11,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (12,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (13,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (14,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (15,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (16,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (17,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (18,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (19,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (20,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (21,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (22,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (23,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (24,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (25,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (26,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (27,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (28,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (29,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (30,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (31,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (32,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (33,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (34,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (35,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (36,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (37,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (38,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (39,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (40,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (41,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (42,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (43,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (44,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (45,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (46,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (47,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (48,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (49,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (50,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (51,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (52,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (53,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (54,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (55,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (56,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (57,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (58,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (59,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (60,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (61,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (62,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (63,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (64,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (65,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (66,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (67,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (68,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (69,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (70,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (71,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (72,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (73,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (74,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (75,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (76,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (77,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (78,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (79,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (80,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (81,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (82,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (83,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (84,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (85,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (86,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (87,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (88,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (89,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (90,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (91,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (92,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (93,'00003','Traditional Music of Spain',7,1);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (94,'00001','JavaWeb',2,2);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (95,'00002','Java Programming',2,3);
INSERT INTO book (id,isbn,title,category_id,image_id) VALUES (96,'00003','Traditional Music of Spain',7,1);

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
