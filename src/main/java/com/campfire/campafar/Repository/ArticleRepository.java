package com.campfire.campafar.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Mapper.ArticleMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ArticleRepository {
    //文章相关数据库操作
    @Resource
    ArticleMapper articleMapper;

    public boolean insertArticle(Article newArticle){
        return articleMapper.insert(newArticle) == 1;
    }

    public boolean updateArticle(Article article){
        UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
        wrapper.eq("article_id",article.getArticleId());

        return articleMapper.update(article, wrapper) == 1;
    }

    public boolean updateArticleDetail(Article article) {
        UpdateWrapper<Article> wrapper = new UpdateWrapper<>();
        wrapper.eq("article_id", article.getUserId())
               .set("article_detail", article.getArticleDetail());

        return articleMapper.update(null, wrapper) == 1;
    }

    public int createArticle(Article article){
        if(!insertArticle(article)){
            return -1;
        }

        return 0;
    }

    public Article selectArticleById(int aid) {
        QueryWrapper<Article> wrapper = new QueryWrapper<Article>().eq("article_id", aid);

        return articleMapper.selectOne(wrapper);
    }
}
