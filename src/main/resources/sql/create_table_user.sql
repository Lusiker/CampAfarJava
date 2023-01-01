use campafar;
drop table if exists user;
create table user (
    user_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_name varchar(30) NOT NULL,
    user_email varchar(50) NOT NULL,
    user_password tinytext NOT NULL, #用md5加密的密码
    user_introduction varchar(250) DEFAULT 'No intro',
    created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP, #账号创建时间
    last_login timestamp NULL DEFAULT NULL, #上次登录时间
    total_login int unsigned NOT NULL DEFAULT 0, #总计登录数
    user_has_activated tinyint(1) unsigned DEFAULT 0, #用户是否已进行邮箱激活
    user_state tinyint(2) unsigned DEFAULT 0, #未激活，正常，被封禁，已注销
    user_exp int unsigned DEFAULT 0,#经验值
    user_point decimal(10,2) not null default '0.00', #用户钱包
    user_question_open tinyint(1) default 0, #用户允许其他人提问
    user_question_price decimal(7, 2) NOT NULL default '0.00' #提问费用
);