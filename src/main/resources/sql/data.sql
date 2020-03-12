
/*
don't forget update sequence initial number XXX on schema.sql:
**create sequence hibernate_sequence start with XXX increment by 1;**
 */


INSERT INTO category (id,name,parent_id) VALUES (1,'fm',1);
    INSERT INTO category (id,name,parent_id) VALUES (2,'Arte',1);
        INSERT INTO category (id,name,parent_id) VALUES (3,'Arquitectura',2);
            INSERT INTO category (id,name,parent_id) VALUES (4,'Arquitectos españoles',3);
            INSERT INTO category (id,name,parent_id) VALUES (5,'Arquitectos extranjeros',3);
            INSERT INTO category (id,name,parent_id) VALUES (6,'Decoración e interiorismo',3);
            INSERT INTO category (id,name,parent_id) VALUES (7,'Feng shui',3);
            INSERT INTO category (id,name,parent_id) VALUES (8,'Historia de la arquitectura',3);
            INSERT INTO category (id,name,parent_id) VALUES (9,'Modelos arquitectónicos',3);
            INSERT INTO category (id,name,parent_id) VALUES (10,'Paisajismo y diseño de jardines',3);
            INSERT INTO category (id,name,parent_id) VALUES (11,'Teoría y crítica de arquitectura',3);
            INSERT INTO category (id,name,parent_id) VALUES (12,'Urbanismo',3);
        INSERT INTO category (id,name,parent_id) VALUES (13,'Artes Escénicas',2);
            INSERT INTO category (id,name,parent_id) VALUES (14,'Bailes de salón',13);
            INSERT INTO category (id,name,parent_id) VALUES (15,'Bailes populares',13);
            INSERT INTO category (id,name,parent_id) VALUES (16,'Danza',13);
            INSERT INTO category (id,name,parent_id) VALUES (17,'Escenografía',13);
            INSERT INTO category (id,name,parent_id) VALUES (18,'Formación del actor',13);
            INSERT INTO category (id,name,parent_id) VALUES (19,'Géneros dramáticos',13);
            INSERT INTO category (id,name,parent_id) VALUES (20,'Historia y estudios de arte dramático',13);
            INSERT INTO category (id,name,parent_id) VALUES (21,'Técnicas de interpretación',13);
        INSERT INTO category (id,name,parent_id) VALUES (22,'Bellas Artes y Aplicadas',2);
            INSERT INTO category (id,name,parent_id) VALUES (23,'Alfombras',22);
            INSERT INTO category (id,name,parent_id) VALUES (24,'Bellas Artes',22);
            INSERT INTO category (id,name,parent_id) VALUES (25,'Carteles',22);
            INSERT INTO category (id,name,parent_id) VALUES (26,'Cerámica y porcelana',22);
            INSERT INTO category (id,name,parent_id) VALUES (27,'Glíptica',22);
            INSERT INTO category (id,name,parent_id) VALUES (28,'Historia y estudios de Artes Aplicadas',22);
            INSERT INTO category (id,name,parent_id) VALUES (29,'Historia y estudios de Bellas Artes',22);
            INSERT INTO category (id,name,parent_id) VALUES (30,'Joyería',22);
            INSERT INTO category (id,name,parent_id) VALUES (31,'Marfil',22);
            INSERT INTO category (id,name,parent_id) VALUES (32,'Metal y cerrajería artística',22);
            INSERT INTO category (id,name,parent_id) VALUES (33,'Muebles',22);
            INSERT INTO category (id,name,parent_id) VALUES (34,'Pieles',22);
            INSERT INTO category (id,name,parent_id) VALUES (35,'Relojería',22);
            INSERT INTO category (id,name,parent_id) VALUES (36,'Restauración',22);
            INSERT INTO category (id,name,parent_id) VALUES (37,'Tapices',22);
            INSERT INTO category (id,name,parent_id) VALUES (38,'Vidrieras y vidrio',22);
        INSERT INTO category (id,name,parent_id) VALUES (39,'Cine',2);
        INSERT INTO category (id,name,parent_id) VALUES (7,'Dibujo',2);
        INSERT INTO category (id,name,parent_id) VALUES (8,'Diseño y moda',2);
        INSERT INTO category (id,name,parent_id) VALUES (9,'Historia del Arte',2);
        INSERT INTO category (id,name,parent_id) VALUES (10,'Museología y museos',2);
        INSERT INTO category (id,name,parent_id) VALUES (11,'Pintores y escultores',2);
        INSERT INTO category (id,name,parent_id) VALUES (12,'Tauromaquia',2);

/*  */
/*********************************************************************************** ROOT -> subTree Arte ************/
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (1,0,1,1);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (2,1,1,2);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (3,2,1,3);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (4,2,1,4);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (5,2,1,5);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (6,2,1,6);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (7,2,1,7);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (8,2,1,8);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (9,2,1,9);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (10,2,1,10);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (11,2,1,11);
INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (12,2,1,12);

    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (13,0,2,2);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (14,1,2,3);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (15,1,2,4);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (16,1,2,5);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (17,1,2,6);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (18,1,2,7);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (19,1,2,8);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (20,1,2,9);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (21,1,2,10);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (22,1,2,11);
    INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (23,1,2,12);

        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (24,0,3,3);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (25,0,4,4);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (26,0,5,5);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (27,0,6,6);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (28,0,7,7);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (29,0,8,8);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (30,0,9,9);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (31,0,10,10);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (32,0,11,11);
        INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (33,0,12,12);

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
