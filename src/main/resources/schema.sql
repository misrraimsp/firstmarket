

drop table if exists user_purchases;
drop table if exists purchase_items;
drop table if exists users_roles;
drop table if exists cart_items;
drop table if exists user;
drop table if exists cart;
drop table if exists role;
drop table if exists purchase;
drop table if exists item;
drop table if exists book;
drop table if exists image;
drop table if exists catpath;
drop table if exists category;


drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;

create table book (id bigint not null, isbn varchar(255), title varchar(255), category_id bigint, image_id bigint, primary key (id));
create table cart (id bigint not null, last_modified timestamp, primary key (id));
create table cart_items (cart_id bigint not null, item_id bigint not null);
create table category (id bigint not null, name varchar(255), parent_id bigint, primary key (id));
create table catpath (id bigint not null, size integer not null, ancestor_id bigint, descendant_id bigint, primary key (id));
create table image (id bigint not null, data blob, is_default boolean not null, mime_type varchar(255), name varchar(255), primary key (id));
create table item (id bigint not null, quantity integer not null, book_id bigint, primary key (id));
create table purchase (id bigint not null, created timestamp, primary key (id));
create table purchase_items (purchase_id bigint not null, item_id bigint not null);
create table role (id bigint not null, name varchar(255), primary key (id));
create table user (id bigint not null, email varchar(255), first_name varchar(255), last_name varchar(255), password varchar(255), cart_id bigint, primary key (id));
create table user_purchases (user_id bigint not null, purchase_id bigint not null);
create table users_roles (user_id bigint not null, role_id bigint not null);

alter table cart_items add constraint uk_itemIdOnCartItems unique (item_id);
alter table purchase_items add constraint uk_itemIdOnPurchaseItems unique (item_id);
alter table user_purchases add constraint uk_purchaseIdOnUserPurchases unique (purchase_id);

alter table book add constraint fk_categoryIdOnBook foreign key (category_id) references category(id);
alter table book add constraint fk_imageIdOnBook foreign key (image_id) references image(id);
alter table cart_items add constraint fk_itemIdOnCartItems foreign key (item_id) references item(id);
alter table cart_items add constraint fk_cartIdOnCartItems foreign key (cart_id) references cart(id);
alter table category add constraint fk_parentIdOnCategory foreign key (parent_id) references category(id);
alter table catpath add constraint fk_ancestorIdOnCatpath foreign key (ancestor_id) references category(id);
alter table catpath add constraint fk_descendantIdOnCatpath foreign key (descendant_id) references category(id);
alter table item add constraint fk_bookIdOnItem foreign key (book_id) references book(id);
alter table purchase_items add constraint fk_itemIdOnPurchaseItems foreign key (item_id) references item(id);
alter table purchase_items add constraint fk_purchaseIdOnPurchaseItems foreign key (purchase_id) references purchase(id);
alter table user add constraint fk_cartIdOnUser foreign key (cart_id) references cart(id);
alter table user_purchases add constraint fk_purchaseIdOnUserPurchases foreign key (purchase_id) references purchase(id);
alter table user_purchases add constraint fk_userIdOnUserPurchases foreign key (user_id) references user(id);
alter table users_roles add constraint fk_roleIdOnUsersRoles foreign key (role_id) references role(id);
alter table users_roles add constraint fk_userIdOnUsersRoles foreign key (user_id) references user(id);