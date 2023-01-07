use campafar;
drop table if exists `mail_record`;
create table mail_record(
    record_id int unsigned not null auto_increment primary key,
    uid int unsigned not null,
    record_type tinyint(2) not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    record_code char(4)
);