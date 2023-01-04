package com.campfire.campafar.Controller;

import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
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

@RestController
public class FileController {
    //文件功能相关接口
    @Resource
    ObjectMapper objectMapper;
    @Resource
    UserRepository userRepository;

    /**
     * 上传头像接口
     * @param uidStr uid文本
     * @param file 文件
     * @return 上传结果
     */
    @RequestMapping("/upload/avatar")
    public String uploadAvatar(@RequestParam(value = "uid", defaultValue = "")String uidStr,
                               @RequestBody MultipartFile file) throws JsonProcessingException {
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
            //用户名无效
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
            //用户名无效
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
}
