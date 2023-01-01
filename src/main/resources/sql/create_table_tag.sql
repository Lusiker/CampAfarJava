use campafar;
drop table if exists `tag`;
create table tag (
    tag_id tinyint(6) unsigned NOT NULL AUTO_INCREMENT, #标签id
    tag_name varchar(25) NOT NULL, #标签名
    tag_type tinyint NOT NULL #标签类别，方便显示上分组
);