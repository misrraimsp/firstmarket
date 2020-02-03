/*
create table category (id bigint not null, name varchar(255), parent_id bigint, primary key (id));
 */
INSERT INTO category (id,name,parent_id) VALUES ('1','fm','1');
/*
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

 */