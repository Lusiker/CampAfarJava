package com.campfire.campafar.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileProcessor {
    //用于处理文件相关功能
    private static final String avatarStorePath = "..\\avatar\\";

    public static boolean saveAvatar(int uid, MultipartFile file) {
        //获取文件后缀
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        //根据用户id创建文件名
        String fileName = uid + "." + fileSuffix;

        File folder = new File(avatarStorePath);
        if (!folder.exists() && !folder.isDirectory()) {
            if(!folder.setWritable(true, false)){
                return false;
            }
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
}
