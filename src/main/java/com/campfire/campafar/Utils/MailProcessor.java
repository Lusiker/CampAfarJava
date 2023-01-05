package com.campfire.campafar.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@PropertySource("classpath:application.yaml")
public class MailProcessor {
    //处理邮件发送相关
    @Value("${spring.mail.username}")
    private String from;
    @Resource
    JavaMailSenderImpl javaMailSender;

    @Async
    public void sendRegistryCode(String code, String userName, String targetEmail, int targetUid){
        //this function is slow, thus set to async
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(targetEmail);
        message.setSubject("您的远行营地账户激活码");
        message.setText("为了您能正常使用远行营地，\n"
                + "请在5分钟内为用户\"" + userName + "(" + targetUid + ")"
                + "\"使用以下代码\n"
                + code);

        javaMailSender.send(message);
    }
}
