package com.campfire.campafar.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Repository.ArticleRepository;
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
    ObjectMapper objectMapper;
    @Autowired
    private ArticleService articleService;


    int pageSize = 10;
    /**
     * 文章分页列表
     * **/
    @RequestMapping("/articles")
    public String ArticleList(@RequestParam(value = "page",defaultValue = "3")Integer page,//第几页
                              @RequestParam(value = "orderBy",defaultValue = "0")Integer orderBy)throws JsonProcessingException {
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
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
        Article article = new Article(uid, title, "", 0, true, price);
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
        //目前无法检测购买

        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        Integer aid = InfoParser.parseInt(aidStr);
        if(aid == null) {
            //文章id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(!uid.equals(0)) {
            User user = userRepository.selectUserById(uid);
            if (user == null) {
                //目标用户不存在
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
            }
        }

        Article article = articleRepository.selectArticleById(aid);
        if(article == null) {
            //文章不存在或已删除
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, article));
    }
}
