package com.diplom.marketplace.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;


@Component
public class Helper {

    public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    public static DecimalFormat dec = new DecimalFormat("#0.0");

    MessageSource messageSource;

    Environment NonEnv;

    static Environment env;

    public Helper() {
    }

    @Autowired
    public Helper(MessageSource messageSource, Environment nonEnv) {
        this.messageSource = messageSource;
        NonEnv = nonEnv;
    }

    public static void sendEmail(String body, String email, String subject) {
        final String username = env.getProperty("spring.mail.username");
        final String password = env.getProperty("spring.mail.password");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", env.getProperty("spring.mail.host"));
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.ssl.trust", env.getProperty("spring.mail.host"));
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            assert username != null;
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject(subject);
            message.setContent(body, "text/html;charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Mail sent");
    }

    public static void sendPhone(String to, String otp) throws UnsupportedEncodingException {

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(env.getProperty("call.pro.url") + "send")
                .queryParam("key", env.getProperty("call.pro.key"))
                .queryParam("from", env.getProperty("call.pro.num"))
                .queryParam("to", to)
                .queryParam("text", URLEncoder.encode("Tanii batalgaajuulah code " + otp + ". Tibi", "UTF-8"));

        restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);
    }

    //TODO TEST DB BACKUP
    public static boolean backup(String outputFile)

            throws IOException, InterruptedException {
        String command = String.format("mysqldump -u%s -p%s --add-drop-table --databases %s -r %s",
                env.getProperty("spring.datasource.username"),
                env.getProperty("spring.datasource.password"),
                "user_v1_" + new Date() + "_" + System.currentTimeMillis(),
                outputFile);

        Process process = Runtime.getRuntime().exec(command);
        int processComplete = process.waitFor();
        return processComplete == 0;
    }

    @PostConstruct
    private void init() {
        env = this.NonEnv;
    }
}