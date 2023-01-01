use campafar;
drop table if exists user_follow;
create table user_follow (
    uid_from int unsigned NOT NULL, #关注人的uid
    uid_follow int unsigned NOT NULL, #被关注人的uid
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP #此记录创建时间
);