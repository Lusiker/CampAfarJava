package com.campfire.campafar.Controller;

import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.ArticleStateEnum;
import com.campfire.campafar.Enum.CommonPageState;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class FileController {
    //文件功能相关接口
    @Resource
    ObjectMapper objectMapper;
    @Resource
    UserRepository userRepository;
    @Resource
    ArticleRepository articleRepository;

    /**
     * 上传头像接口
     * @param uidStr uid文本
     * @param file 文件
     * @return 上传结果
     */
    @RequestMapping("/upload/avatar")
    public String uploadAvatar(@RequestParam(value = "uid", defaultValue = "")String uidStr,
                               @RequestBody MultipartFile file) throws IOException {
        if(file == null) {
            //文件参数为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        double fileSize = file.getSize();
        double sizeAsMB = fileSize / 1024 / 1024;
        if(sizeAsMB > 2.0) {
            //文件长度过大
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(file.isEmpty()) {
            //文件为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if(user == null){
            //目标用户不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        if(!FileProcessor.saveAvatar(uid, file)) {
            //文件创建失败，内部错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
        }

        //文件创建成功
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
    }

    @RequestMapping("/acquire/avatar")
    public String acquireAvatar(@RequestParam(value = "uid", defaultValue = "")String uidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if(user == null){
            //目标用户不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        String result = FileProcessor.getAvatarAsBase64(uid);
        if(result == null) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, ""));
        }

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, result));
    }

    //上传article的banner
    @RequestMapping("/upload/articleBanner")
    public String uploadArticleBanner(@RequestParam(value = "uid", defaultValue = "")String uidStr,
                                      @RequestParam(value = "aid", defaultValue = "")String aidStr,
                                      @RequestBody MultipartFile file) throws IOException {
        if(file == null) {
            //文件参数为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        double fileSize = file.getSize();
        double sizeAsMB = fileSize / 1024 / 1024;
        if(sizeAsMB > 2.0) {
            //文件长度过大
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        Integer aid = InfoParser.parseInt(aidStr);
        if(aid == null){
            //文章id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(file.isEmpty()) {
            //文件为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if(user == null){
            //目标用户不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        Article article = articleRepository.selectArticleById(aid);
        if(article == null || article.getArticleState() == ArticleStateEnum.DELETED) {
            //目标文章不存在或已被删除
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        if(!article.getUserId().equals(uid)) {
            //无权修改
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.ACCESS_DENIED, 1, null));
        }

        if(!FileProcessor.saveBanner(aid, file)) {
            //文件创建失败，内部错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
        }

        //文件创建成功
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
    }

    @RequestMapping("/acquire/articleBanner")
    public String acquireArticleBanner(@RequestParam(value = "aid", defaultValue = "")String aidStr,
                                       @RequestParam(value = "asUrl", defaultValue = "false")String asUrlStr) throws JsonProcessingException  {
        Boolean asUrl = InfoParser.parseBoolean(asUrlStr);
        if(asUrl == null) {
            //参数错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        System.out.println(asUrl);

        Integer aid = InfoParser.parseInt(aidStr);
        if(aid == null){
            //文章id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        Article article = articleRepository.selectArticleById(aid);
        if(article == null || article.getArticleState() == ArticleStateEnum.DELETED) {
            //目标文章不存在或已被删除
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        if(asUrl) {
            String url = FileProcessor.getArticleBannerAsUrl(aid);
            if(url == null) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
            } else {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,url));
            }
        } else {
            String result = FileProcessor.getArticleBannerAsBase64(aid);
            if(result == null) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
            } else {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,result));
            }
        }
    }
}
