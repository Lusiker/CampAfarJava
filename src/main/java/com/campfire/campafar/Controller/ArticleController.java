package com.campfire.campafar.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Enum.ArticleStateEnum;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Repository.ArticleRepository;
import com.campfire.campafar.Repository.PurchaseRepository;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Service.ArticleService;
import com.campfire.campafar.Utils.FileProcessor;
import com.campfire.campafar.Utils.InfoParser;
import com.campfire.campafar.Utils.RequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
public class ArticleController {
    @Resource
    ArticleRepository articleRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    PurchaseRepository purchaseRepository;
    @Resource
    ObjectMapper objectMapper;
    @Autowired
    private ArticleService articleService;

    /**
     * 文章分页列表
     * **/
    @RequestMapping("/articles")
    public String ArticleList(@RequestParam(value = "page",defaultValue = "3")Integer page,//第几页
                              @RequestParam(value = "orderBy",defaultValue = "0")Integer orderBy,
                              @RequestParam(value = "free", defaultValue = "false")String freeStr) throws JsonProcessingException {
        //构造分页构造器
        int pageSize = 5;
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        Boolean free = InfoParser.parseBoolean(freeStr);
        if(free == null) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(free) {
            queryWrapper.eq(Article::getArticleIsFree, true);
        }

        queryWrapper.select(
                Article::getArticleTitle,
                Article::getArticleId,
                Article::getUserId,
                Article::getArticleCreatedAt,
                Article::getArticleIsFree,
                Article::getArticlePrice,
                Article::getArticleTitle,
                Article::getArticleViewCount
        );
        switch(orderBy){
            case 0 :
                queryWrapper.orderByAsc(Article::getArticleId);
                break;
            case 1 :
                queryWrapper.orderByDesc(Article::getArticleId);
                break;
            case 2:
                queryWrapper.orderByAsc(Article::getArticleViewCount);
                break;
            case 3:
                queryWrapper.orderByDesc(Article::getArticleViewCount);
                break;
            default :
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        //执行查询
        articleService.page(pageInfo,queryWrapper);

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,pageInfo));
    }

    /**
     * 获取文章总数方便前端分页
     */
    @RequestMapping("articles/articleCount")
    public String getArticleTotalCount(@RequestParam(value = "free",defaultValue = "false")String freeStr) throws JsonProcessingException {
        Boolean free = InfoParser.parseBoolean(freeStr);
        if(free == null) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        long result;
        if(free){
            result = articleRepository.getFreeArticleCount();
        } else {
            result = articleRepository.getArticleCount();
        }

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0, result));
    }

    @RequestMapping("/article/create")
    public String createArticle(@RequestParam(name = "uid", defaultValue = "")String uidStr,
                                @RequestParam(name = "title", defaultValue = "")String title,
                                @RequestParam(name = "tags", defaultValue = "")String tagsStr,
                                @RequestParam(name = "isFree", defaultValue = "")String isFreeStr,
                                @RequestParam(name = "price", defaultValue = "")String priceStr,
                                @RequestBody String body) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(title.isEmpty() || title.length() > 50) {
            //标题无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        String detail = objectMapper.reader().readTree(body).get("body").toString();
        if(detail.isEmpty()) {
            //正文无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        Integer tags = InfoParser.parseInt(tagsStr);
        if(tags == null){
            //标签无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        Boolean isFree = InfoParser.parseBoolean(isFreeStr);
        if(isFree == null) {
            //付费设定无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(priceStr == null) {
            //费用参数为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        BigDecimal price;
        try{
            if(isFree){
                price = BigDecimal.ZERO;
            } else {
                price = new BigDecimal(priceStr);
            }
        } catch (NumberFormatException e) {
            //数字格式错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if(user == null){
            //目标用户不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        if(user.getUserState() == UserStateEnum.LOGOFF || user.getUserState() == UserStateEnum.BANNED) {
            //用户无权限进行此操作
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        //创建文章
        Article article = new Article(uid, title, "", 0, isFree, price);
        if(articleRepository.createArticle(article) != 0){
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
        }

        //将富文本的img中的base64映射为本地文件后更改成url
        String processedDetail = FileProcessor.handleRawArticle(article.getArticleId(), detail);
        //更新文章
        article.setArticleDetail(processedDetail);
        if(!articleRepository.updateArticle(article)) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
        }

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0, article.getArticleId()));
    }

    @RequestMapping("/article")
    public String getArticleInfo(@RequestParam(value = "uid", defaultValue = "")String uidStr,
                                 @RequestParam(value = "aid", defaultValue = "")String aidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if (uid == null) {
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        Integer aid = InfoParser.parseInt(aidStr);
        if (aid == null) {
            //文章id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        Article article = articleRepository.selectArticleById(aid);
        if (article == null) {
            //文章不存在或已删除
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        if (uid != 0) {
            User user = userRepository.selectUserById(uid);
            if (user == null) {
                //目标用户不存在
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
            }

            if (article.getUserId().equals(uid)) {
                //阅读者与创建者相同

                //在不更新阅读量的情况下返回文章
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, article));
            } else if (purchaseRepository.checkUserHasBoughtArticle(uid, aid)) {
                //文章阅读时+1，返回文章
                if (!articleRepository.updateArticleReadCount(article)) {
                    //更新阅读数失败
                    return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
                }
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, article));
            } else {
                //尚未购买
                if (article.getArticleIsFree()) {
                    //但是文章是免费的
                    if (!articleRepository.updateArticleReadCount(article)) {
                        //更新阅读数失败
                        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
                    }
                    return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, article));
                }

                //不展示文章详情
                article.setArticleDetail("");
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 1, article));
            }
        } else {
            //游客
            if (article.getArticleIsFree()) {
                //但是文章是免费的
                if (!articleRepository.updateArticleReadCount(article)) {
                    //更新阅读数失败
                    return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
                }
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, article));
            }

            //不展示文章详情
            article.setArticleDetail("");
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 1, article));
        }
    }
    
    /**
     * 用户删除文章
     **/
    @RequestMapping("/article/delete")
    public String UerDeleteArticle(@RequestParam(value = "uid",defaultValue = "-1")Integer uid,
                                   @RequestParam(value = "aid",defaultValue = "-1")Integer aid) throws JsonProcessingException {
        if (uid == -1 || aid == -1) {
            //参数错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        User user = userRepository.selectUserById(uid);
        Article article;
        if (articleRepository.selectArticleById(aid) == null) {
            //文章不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        } else {
            article = articleRepository.selectArticleById(aid);
        }
        if (!uid.equals(article.getUserId())) {
            //执行操作用户不是文章作者
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.ACCESS_DENIED, 1, null));
        } else {
            if (user == null || user.getUserState() == UserStateEnum.LOGOFF) {
                //用户不存在或已注销
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
            }
            article.setArticleState(ArticleStateEnum.DELETED);
            articleRepository.updateArticle(article);
        }
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, null));
    }
    
    /**
     * 首页文章展示
     **/
    @RequestMapping("/popularArticles")//最高浏览量三篇文章，降序
    public String PopularArticles()throws JsonProcessingException{
        //构造分页构造器
        int size = 3;
        int page = 1;
        Page pageInfo = new Page(page,size);
        //构造条件构造器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.select(Article::getArticleTitle,
                Article::getArticleId,
                Article::getArticleCreatedAt,
                Article::getArticleIsFree,
                Article::getArticlePrice,
                Article::getArticleTitle,
                Article::getArticleViewCount
        );
        queryWrapper.orderByDesc(Article::getArticleViewCount);
        //执行查询
        articleService.page(pageInfo,queryWrapper);

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,pageInfo));
    }

    @RequestMapping("/latestArticles")//最新发布三篇文章，降序
    public String LatestArticles()throws JsonProcessingException{
        //构造分页构造器
        int size = 3;
        int page = 1;
        Page pageInfo = new Page(page,size);
        //构造条件构造器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.select(Article::getArticleTitle,
                Article::getArticleId,
                Article::getArticleCreatedAt,
                Article::getArticleIsFree,
                Article::getArticlePrice,
                Article::getArticleTitle,
                Article::getArticleViewCount
        );
        queryWrapper.orderByDesc(Article::getArticleCreatedAt);
        //执行查询
        articleService.page(pageInfo,queryWrapper);

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,pageInfo));
    }
}
