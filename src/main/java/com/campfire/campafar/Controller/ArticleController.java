package com.campfire.campafar.Controller;

import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Repository.ArticleRepository;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Utils.FileProcessor;
import com.campfire.campafar.Utils.InfoParser;
import com.campfire.campafar.Utils.RequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @RequestMapping("/article/create")
    public String createArticle(@RequestParam(name = "uid", defaultValue = "")String uidStr,
                                @RequestParam(name = "title", defaultValue = "")String title,
                                @RequestParam(name = "tags", defaultValue = "")String tagsStr,
                                @RequestParam(name = "isFree", defaultValue = "")String isFreeStr,
                                @RequestParam(name = "price", defaultValue = "")String priceStr,
                                @RequestBody String body) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户名无效
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
}
