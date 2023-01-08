use campafar;
drop table if exists `purchase`;
create table purchase (
    purchase_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id int unsigned NOT NULL,
    target_type tinyint(2) NOT NULL, #支付类别，文章、提问、旁听
    target_id int unsigned NOT NULL, #对象id
    purchase_value decimal(7, 2) NOT NULL , #本单价格
    purchase_created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, #支付创建时间
    purchase_state tinyint(2) NOT NULL DEFAULT 0, #支付状态
    purchase_finished_at timestamp default null, #支付完成于
    purchase_serial_id varchar(50) NOT NULL #总感觉支付应该有个生成的字符串很有逼格，不知道怎么搞就先留空
);