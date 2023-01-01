use campafar;
drop table if exists `purchase`;
create table purchase (
    purchase_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id int unsigned NOT NULL,
    target_type tinyint(2) NOT NULL, #支付类别，文章、提问、旁听
    target_id int unsigned NOT NULL, #对象id
    purchase_created_at timestamp NOT NULL, #支付创建时间
    purchase_serial_id varchar(50) NOT NULL #总感觉支付应该有个生成的字符串很有逼格，不知道怎么搞就先留空
);