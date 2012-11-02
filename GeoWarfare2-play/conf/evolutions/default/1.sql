# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ally (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  constraint pk_ally primary key (id))
;

create table city (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  min_latitude              double,
  min_longitude             double,
  max_latitude              double,
  max_longitude             double,
  constraint pk_city primary key (id))
;

create table device_info (
  id                        bigint auto_increment not null,
  player_id                 bigint,
  url                       varchar(255),
  type                      varchar(255),
  constraint pk_device_info primary key (id))
;

create table geo_event (
  id                        bigint auto_increment not null,
  player_id                 bigint,
  title                     varchar(255),
  content                   varchar(255),
  date_publication          datetime,
  geo_event_type            integer,
  constraint ck_geo_event_geo_event_type check (geo_event_type in (0,1,2,3,4)),
  constraint pk_geo_event primary key (id))
;

create table geo_message (
  id                        bigint auto_increment not null,
  destinataire_id           bigint,
  expediteur                varchar(255),
  sujet                     varchar(255),
  content                   varchar(255),
  date                      datetime,
  is_read                   tinyint(1) default 0,
  constraint pk_geo_message primary key (id))
;

create table news (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  content                   longtext,
  date_publication          datetime,
  author                    varchar(255),
  path_image                varchar(255),
  constraint pk_news primary key (id))
;

create table player (
  id                        bigint auto_increment not null,
  login                     varchar(255),
  password                  varchar(255),
  date_inscription          datetime,
  date_connexion            datetime,
  faction                   integer,
  units                     integer,
  production                integer,
  avatar                    varchar(255),
  biography                 varchar(255),
  success                   varchar(255),
  spies_nb                  integer,
  alliance_id               bigint,
  message_candidature       varchar(255),
  constraint pk_player primary key (id))
;

create table sector (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  venue_id                  varchar(255),
  longitude                 double,
  latitude                  double,
  city_name                 varchar(255),
  owner                     varchar(255),
  category                  varchar(255),
  units                     integer,
  influence                 integer,
  development               integer,
  constraint pk_sector primary key (id))
;

alter table device_info add constraint fk_device_info_player_1 foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_device_info_player_1 on device_info (player_id);
alter table geo_event add constraint fk_geo_event_player_2 foreign key (player_id) references player (id) on delete restrict on update restrict;
create index ix_geo_event_player_2 on geo_event (player_id);
alter table geo_message add constraint fk_geo_message_destinataire_3 foreign key (destinataire_id) references player (id) on delete restrict on update restrict;
create index ix_geo_message_destinataire_3 on geo_message (destinataire_id);
alter table player add constraint fk_player_alliance_4 foreign key (alliance_id) references ally (id) on delete restrict on update restrict;
create index ix_player_alliance_4 on player (alliance_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table ally;

drop table city;

drop table device_info;

drop table geo_event;

drop table geo_message;

drop table news;

drop table player;

drop table sector;

SET FOREIGN_KEY_CHECKS=1;

