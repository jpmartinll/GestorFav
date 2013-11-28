# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table url (
  id                        bigint not null,
  texto                     varchar(255),
  autor_id                  bigint,
  constraint pk_url primary key (id))
;

create table usuario (
  id                        bigint not null,
  nombre                    varchar(255),
  constraint pk_usuario primary key (id))
;

create sequence url_seq;

create sequence usuario_seq;

alter table url add constraint fk_url_autor_1 foreign key (autor_id) references usuario (id) on delete restrict on update restrict;
create index ix_url_autor_1 on url (autor_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists url;

drop table if exists usuario;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists url_seq;

drop sequence if exists usuario_seq;

