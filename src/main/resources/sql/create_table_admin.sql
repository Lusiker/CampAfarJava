use campafar;
drop table if exists `admin`;
create table admin (
    admin_id tinyint unsigned not null primary key,
    admin_password varchar(15) not null
);
-- 管理员后台是另一个页面，用一个简单的表存储管理员信息，直接通过接口或者数据库修改