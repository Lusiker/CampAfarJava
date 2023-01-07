package com.campfire.campafar.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;

public class FileProcessor {
    //用于处理文件相关功能
    public static final String avatarStorePath = "..\\avatar\\";
    public static final String articleStorePath = "..\\article\\";
    public static final String jpgHeader = "data:image/jpeg;base64,";
    public static final String pngHeader = "data:image/png;base64,";
    public static final String svgHeader = "data:image/svg;base64,";
    public static final String gifHeader = "data:image/gif;base64,";
    public static Base64.Encoder encoder = Base64.getEncoder();
    public static Base64.Decoder decoder = Base64.getDecoder();

    public static boolean saveAvatar(int uid, MultipartFile file) throws IOException {
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
        } else {
            String[] files = folder.list();
            if(files != null) {
                for(String f : files) {
                    Files.delete(Path.of(folder.getAbsolutePath() + "\\" + f));
                }
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

        //获取文件后缀
        String target = files[0];
        String suffix = target.split("\\.")[1];

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

        return switch (suffix) {
            case "jpg", "jpeg" -> jpgHeader + encoder.encodeToString(data);
            case "png" -> pngHeader + encoder.encodeToString(data);
            case "svg" -> svgHeader + encoder.encodeToString(data);
            case "gif" -> gifHeader + encoder.encodeToString(data);
            default -> null;
        };
    }

    private static String getSuffix(String header) {
        return switch (header) {
            case jpgHeader, "data:image/jpg;base64," -> ".jpg";
            case pngHeader -> ".png";
            case svgHeader -> ".svg";
            default -> "";
        };
    }

    public static String handleRawArticle(int aid, String rawArticle) {
        String dirPath = articleStorePath + aid + '\\';
        File folder = new File(dirPath);
        //如果该文件夹不存在，创建
        if (!folder.exists() && !folder.isDirectory()) {
            //创建目标文件夹
            if(!folder.mkdirs()){
                return "";
            }
        }

        rawArticle = rawArticle.replaceAll("\\\\\"", "\"");
        Document doc = Jsoup.parse(rawArticle);
        Elements images = doc.select("img");

        int imageCount = 1;
        String currentSrc;
        String filename;
        String url;
        OutputStream out;
        for(Element elem : images) {
            //获取当前src字符串
            currentSrc = elem.attr("src");
            currentSrc = currentSrc.replaceAll("\\\\\"", "");

            //将头部和信息分离
            String[] infos = currentSrc.split(",");
            filename = imageCount + getSuffix(infos[0] + ",");
            try {
                //创建文件输入流
                out = new FileOutputStream(folder.getAbsolutePath() + "\\" + filename);
                out.write(decoder.decode(infos[1].getBytes()));
                out.close();
            } catch (IOException e) {
                return null;
            }

            //将信息中的图片改写为地址
            url = "http://localhost:8080/files/article/" + aid + "/" + filename;
            elem.attr("src", url);

            imageCount++;
        }

        String tree = doc.toString();
        return tree.substring(tree.indexOf('"') + 1,tree.lastIndexOf('"'));
    }

    public static boolean saveBanner(int aid, MultipartFile file) {
        //根据用户id创建文件名
        String fileSuffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        String fileName = "banner." + fileSuffix;
        String dirPath = articleStorePath + aid + '\\';

        File folder = new File(dirPath);
        //如果该文件夹不存在，创建
        if (!folder.exists() && !folder.isDirectory()) {
            //创建目标文件夹
            if(!folder.mkdirs()){
                return false;
            }
        }

        //创建banner
        File descFile = new File(folder.getAbsolutePath(), fileName);
        try {
            file.transferTo(descFile);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static String getArticleBannerAsUrl(int aid) {
        String dirPath = articleStorePath + aid + '\\';
        File dir = new File(dirPath);
        if(!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        String[] files = dir.list();
        if(files == null || files.length == 0) {
            return null;
        }

        String currentName;
        boolean found = false;
        String target = null;
        for(String filename : files) {
            currentName = filename.split("\\.")[0];
            if(currentName.equals("banner")) {
                target = filename;
                found = true;
            }
        }

        if(!found) {
            return null;
        } else {
            return "url:" + dir.getAbsolutePath() + '\\' + target;
        }
    }

    public static String getArticleBannerAsBase64(int aid) {
        String dirPath = articleStorePath + aid + '\\';
        File dir = new File(dirPath);
        if(!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        String[] files = dir.list();
        if(files == null || files.length == 0) {
            return null;
        }

        String currentName;
        String suffix = null;
        boolean found = false;
        String target = null;
        for(String filename : files) {
            currentName = filename.split("\\.")[0];
            if(currentName.equals("banner")) {
                target = filename;
                suffix = filename.split("\\.")[1];
                found = true;
            }
        }

        if(!found) {
            return null;
        }

        if(suffix == null) {
            return null;
        }

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

        return switch (suffix) {
            case "jpg", "jpeg" -> jpgHeader + encoder.encodeToString(data);
            case "png" -> pngHeader + encoder.encodeToString(data);
            case "svg" -> svgHeader + encoder.encodeToString(data);
            case "gif" -> gifHeader + encoder.encodeToString(data);
            default -> null;
        };
    }
}
