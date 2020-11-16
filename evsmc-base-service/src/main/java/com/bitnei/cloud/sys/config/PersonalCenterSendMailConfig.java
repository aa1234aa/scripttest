package com.bitnei.cloud.sys.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PersonalCenterSendMailConfig {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.protocol}")
    private String protocol;
    @Value("${spring.mail.port}")
    private String port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender getMailSet(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.timeout",30000);
        mailProperties.put("mail.smtp.starttls.enable", true);
        mailProperties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        mailProperties.put("mail.smtp.socketFactory.port",port);
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);
       // mailSender.
        return mailSender;
    }




}
