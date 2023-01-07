package com.campfire.campafar.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Mapper.ArticleMapper;
import com.campfire.campafar.Service.ArticleService;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {
}
