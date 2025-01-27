package com.eshop.service.impl;

import com.eshop.entity.Constant;
import com.eshop.entity.EmailCode;
import com.eshop.entity.User;
import com.eshop.entity.config.ApplicationConfig;
import com.eshop.exception.BusinessException;
import com.eshop.repository.EmailCodeRepository;
import com.eshop.repository.UserRepository;
import com.eshop.service.EmailCodeService;
import com.eshop.utils.StringTools;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailCodeServiceImpl implements EmailCodeService {
    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

    private UserRepository userRepository;
    private EmailCodeRepository emailCodeRepository;
    private ApplicationConfig applicationConfig;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void disableEmailCode(String email) {
        List<EmailCode> emailCodes = emailCodeRepository.findByEmail(email);
        if(emailCodes.isEmpty()) {
            return;
        }
        emailCodes.stream()
                .filter(ec -> ec.getStatus().equals(EmailCode.Status.NOT_USED))
                .forEach(ec -> ec.setStatus(EmailCode.Status.USED));
        emailCodeRepository.saveAll(emailCodes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer status) {
        if(status == Constant.ZERO) {
            User user = userRepository.findByEmail(email);
            if(user != null) {
                try {
                    throw new BusinessException("This Email is Already Registered");
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        String code = StringTools.getRandomNumber(Constant.LENGTH_6);
        // overload to explicitly send code to email
        sendEmailCode(email, code);
        // disable all previous codes of this email
        this.disableEmailCode(email);
        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setCreateTime(LocalDateTime.now());
        emailCode.setStatus(EmailCode.Status.NOT_USED);
        emailCodeRepository.save(emailCode);
    }

    private void sendEmailCode(String toEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setFrom(applicationConfig.getSendUserName());
            message.setSubject(applicationConfig.getRegisterEmailSubject());
            message.setText(String.format(applicationConfig.getRegisterEmailContent(), code));
            message.setSentDate(new Date());
            mailSender.send(message);
        } catch(Exception e) {
            logger.error("Failed To Send Email", e);
            try {
                throw new BusinessException("Failed To Send Email");
            } catch (BusinessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void verifyEmailCode(String email, String code) {
        EmailCode emailCode = this.emailCodeRepository.findByEmailAndCode(email, code);
        if(null == emailCode) {
            try {
                throw new BusinessException("Email Verification Code Is Not Correct.");
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }

        long timeInMills = emailCode.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if(emailCode.getStatus().equals(EmailCode.Status.USED) || System.currentTimeMillis() - timeInMills> Constant.LENGTH_15 * 1000 * 60) {
            try {
                throw new BusinessException("This Verification Code Already Expired.");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        this.disableEmailCode(email);
    }

}
