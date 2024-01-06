package com.webtutsplus.ecommerce.service;


import com.webtutsplus.ecommerce.exceptions.OrderNotFoundException;
import com.webtutsplus.ecommerce.model.Order;
import com.webtutsplus.ecommerce.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private OrderRepository orderRepository;

    public void sendOrderEmail(String email, Integer orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException("Order not found with id : " + orderId);
        }
        Order order = optionalOrder.get();

        String subject = "Підтвердження замовлення";
        String text = "Дякуємо за ваше замовлення! " +
                "\nМи раді повідомити, що ваше замовлення було успішно прийнято." +
                "\nЙого відправка буде здійснена найближчим часом, очікуйте, будь ласка!";

        // 3. Send the email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            logger.error("Failed to send email", e);
        }
    }

    public void sendTokenToEmail(String email, String firstName, String lastName, String token) {
        String subject = "Згенерований токен доступу";
        String text = "Вітаємо Вас " + lastName +  " " + firstName + "!" + "\nДякуємо, що долучилися до нашого сервісу! "
                + "\nВаш згенерований токен доступу: " + token + "." +
                "\nЩоб скористатися ним, перейдіть за наступним посиланням та введіть отриманий токен: " + "\nhttp://localhost:8080/signInToken";

        // 3. Send the email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            logger.error("Failed to send email", e);
        }

    }
}
