package com.campfire.campafar.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campfire.campafar.Entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
