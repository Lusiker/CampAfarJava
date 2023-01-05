package com.campfire.campafar.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileProcessor {
    //用于处理文件相关功能
    public static final String avatarStorePath = "..\\avatar\\";

    public static boolean saveAvatar(int uid, MultipartFile file) {
        //获取文件后缀
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        //根据用户id创建文件名
        String fileName = uid + "." + fileSuffix;
        String dirPath = avatarStorePath + uid + '\\';

        File folder = new File(dirPath);
        if (!folder.exists() && !folder.isDirectory()) {
            //创建目标文件夹
            if(!folder.mkdirs()){
                return false;
            }
        }

        File descFile = new File(folder.getAbsolutePath(), fileName);

        try {
            file.transferTo(descFile);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * 从后端获取base64格式转码的头像信息
     * @param uid 目标uid
     * @return 转码后的图片或出错时返回null
     */
    public static String getAvatarAsBase64(int uid) {
        String dirPath = avatarStorePath + uid + '\\';
        File dir = new File(dirPath);
        if(!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        String[] files = dir.list();
        if(files == null || files.length == 0) {
            return null;
        }

        String target = files[0];
        InputStream in;
        byte[] data;

        try {
            in = new FileInputStream(dir.getAbsolutePath() + '\\' + target);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            return null;
        }

        return Base64.getEncoder().encodeToString(data);
    }
}
