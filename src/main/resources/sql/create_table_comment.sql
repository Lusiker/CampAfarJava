use campafar;
drop table if exists `comment`;
create table comment (
    comment_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY, #评论id
    user_id int unsigned NOT NULL, #发布者id
    comment_type tinyint(1) NOT NULL, #发布的是文章评论还是问答评论
    target_id int unsigned NOT NULL, #对象id
    comment_detail varchar(255) NOT NULL DEFAULT '', #评论内容
    comment_created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP, #评论创建时间
    comment_state tinyint(2) unsigned DEFAULT 0, #评论状态，具体待定
    comment_visible tinyint(1) DEFAULT '1' #评论可见性
);