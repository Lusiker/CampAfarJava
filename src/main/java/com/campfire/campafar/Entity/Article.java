package com.campfire.campafar.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.campfire.campafar.Enum.ArticleStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TableName(schema = "campafar",value = "article")
public class Article {
    Integer userId;
    @TableId(type = IdType.AUTO)
    Integer articleId;
    String articleTitle;
    Date articleCreatedAt;
    Integer articleTag;
    Boolean articleIsFree;
    BigDecimal articlePrice;
    String articleDetail;
    Integer articleViewCount;
    ArticleStateEnum articleState;
    Boolean articleVisible;
    Date articleLastModified;

    public Article(int uid, String newTitle, String newDetail, int tags, boolean isFree, BigDecimal newPrice) {
        this.userId = uid;
        this.articleTitle = newTitle;
        this.articleDetail = newDetail;
        this.articleTag = tags;
        this.articleIsFree = isFree;
        this.articlePrice = newPrice;
    }
}
