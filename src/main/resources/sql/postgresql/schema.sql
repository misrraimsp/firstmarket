
drop table if exists user_deletion;
drop table if exists security_token;
drop table if exists pedido_items;
drop table if exists pedido;
drop table if exists payment;
drop table if exists shipping_info;
drop table if exists address;
drop table if exists usuarios_roles;
drop table if exists usuario;
drop table if exists role;
drop table if exists profile;
drop table if exists cart_items;
drop table if exists cart;
drop table if exists item;
drop table if exists books_authors;
drop table if exists book;
drop table if exists author;
drop table if exists publisher;
drop table if exists image;
drop table if exists catpath;
drop table if exists category;

drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 5001 increment by 1;

create table address (id bigint not null, city varchar(255), country varchar(255), line1 varchar(255), line2 varchar(255), postal_code varchar(255), province varchar(255), primary key (id));
create table author (id bigint not null, first_name varchar(255), last_name varchar(255), primary key (id));
create table books_authors (book_id bigint not null, author_id bigint not null);
create table profile (id bigint not null, birth_date date, first_name varchar(255), gender varchar(255), last_name varchar(255), phone varchar(255), primary key (id));
create table publisher (id bigint not null, name varchar(255), primary key (id));
create table book (id bigint not null, description varchar(510), isbn varchar(255), language integer, pages integer, price decimal(19,2), stock integer, title varchar(255), year integer, category_id bigint, image_id bigint, publisher_id bigint, primary key (id));
create table cart (id bigint not null, committed_at timestamp, is_committed boolean not null, last_modified timestamp, pi_client_secret varchar(255), pi_id varchar(255), primary key (id));
create table cart_items (cart_id bigint not null, items_id bigint not null, primary key (cart_id, items_id));
create table category (id bigint not null, name varchar(255), parent_id bigint, primary key (id));
create table catpath (id bigint not null, size integer not null, ancestor_id bigint, descendant_id bigint, primary key (id));
create table image (id bigint not null, data bytea, is_default boolean not null, mime_type varchar(255), name varchar(255), primary key (id));
create table item (id bigint not null, quantity integer not null, book_id bigint, primary key (id));
create table role (id bigint not null, name varchar(255), primary key (id));
create table usuario (id bigint not null, email varchar(255), completed boolean not null, suspended boolean not null, password varchar(255), cart_id bigint, profile_id bigint, primary key (id));
create table user_deletion (id bigint not null, comment varchar(255), date timestamp, deletion_reason integer, user_id bigint, primary key (id));
create table security_token (id bigint not null, edited_email varchar(255), expiry_date timestamp, security_event varchar(255), token varchar(255), user_id bigint, primary key (id));
create table usuarios_roles (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id));
create table payment (id bigint not null, amount bigint, currency varchar(255), description varchar(255), stripe_payment_intent_id varchar(255), primary key (id));
create table pedido (id bigint not null, created_at timestamp, payment_id bigint, shipping_info_id bigint, user_id bigint, primary key (id));
create table pedido_items (order_id bigint not null, items_id bigint not null, primary key (order_id, items_id));
create table shipping_info (id bigint not null, carrier varchar(255), name varchar(255), phone varchar(255), tracking_number varchar(255), address_id bigint, primary key (id));

alter table cart_items add constraint uk_itemIdOnCartItems unique (items_id);
alter table pedido_items add constraint uk_itemIdOnPedidoItems unique (items_id);

alter table book add constraint fk_categoryIdOnBook foreign key (category_id) references category(id);
alter table book add constraint fk_imageIdOnBook foreign key (image_id) references image(id);
alter table book add constraint fk_publisherIdOnBook foreign key (publisher_id) references publisher(id);
alter table books_authors add constraint fk_authorIdOnBooksAuthors foreign key (author_id) references author(id);
alter table books_authors add constraint fk_bookIdOnBooksAuthors foreign key (book_id) references book(id);
alter table cart_items add constraint fk_itemIdOnCartItems foreign key (items_id) references item(id);
alter table cart_items add constraint fk_cartIdOnCartItems foreign key (cart_id) references cart(id);
alter table category add constraint fk_parentIdOnCategory foreign key (parent_id) references category(id);
alter table catpath add constraint fk_ancestorIdOnCatpath foreign key (ancestor_id) references category(id);
alter table catpath add constraint fk_descendantIdOnCatpath foreign key (descendant_id) references category(id);
alter table item add constraint fk_bookIdOnItem foreign key (book_id) references book(id);
alter table pedido add constraint fk_paymentIdOnPedido foreign key (payment_id) references payment(id);
alter table pedido add constraint fk_shippingInfoIdOnPedido foreign key (shipping_info_id) references shipping_info(id);
alter table pedido add constraint fk_userIdOnPedido foreign key (user_id) references usuario(id);
alter table pedido_items add constraint fk_itemsIdOnPedidoItems foreign key (items_id) references item(id);
alter table pedido_items add constraint fk_orderIdOnPedidoItems foreign key (order_id) references pedido(id);
alter table shipping_info add constraint fk_addressIdOnShippingInfo foreign key (address_id) references address(id);
alter table usuario add constraint fk_cartIdOnUsuario foreign key (cart_id) references cart(id);
alter table usuario add constraint fk_profileIdOnUsuario foreign key (profile_id) references profile(id);
alter table user_deletion add constraint fk_userIdOnUserDeletion foreign key (user_id) references usuario(id);
alter table security_token add constraint fk_userIdOnSecurityToken foreign key (user_id) references usuario(id);
alter table usuarios_roles add constraint fk_roleIdOnUsuriosRoles foreign key (role_id) references role(id);
alter table usuarios_roles add constraint fk_userIdOnUsuariosRoles foreign key (user_id) references usuario(id);