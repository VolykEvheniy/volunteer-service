package com.webtutsplus.ecommerce.controller;


import com.webtutsplus.ecommerce.common.ApiResponse;
import com.webtutsplus.ecommerce.dto.*;
import com.webtutsplus.ecommerce.dto.order.OrderEmailSendDto;
import com.webtutsplus.ecommerce.dto.user.*;
import com.webtutsplus.ecommerce.exceptions.AuthenticationFailException;
import com.webtutsplus.ecommerce.exceptions.CustomException;
import com.webtutsplus.ecommerce.model.User;
import com.webtutsplus.ecommerce.repository.UserRepository;
import com.webtutsplus.ecommerce.service.AuthenticationService;
import com.webtutsplus.ecommerce.service.EmailService;
import com.webtutsplus.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/all")
    public List<User> findAllUser(@RequestParam("token") String token) throws AuthenticationFailException {
        authenticationService.authenticate(token);
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseDto Signup(@RequestBody SignupDto signupDto) throws CustomException {
        return userService.signUp(signupDto);
    }

    @PostMapping("/militarySignup")
    public ResponseDtoWithToken Signup(@RequestBody MilitarySignUpDto militarySignUpDto) throws CustomException {
        return userService.militarySignUp(militarySignUpDto);
    }

    @PostMapping("/militarySignin")
    public SignInResponseDto signIn(@RequestBody SignInWithTokenDto signInWithTokenDto) throws CustomException {
        return userService.signInWithToken(signInWithTokenDto);
    }

    @PostMapping("/email")
    public ResponseEntity<ApiResponse> sendOrderEmail(@RequestBody UserEmailSendDto userEmailSendDto)
            throws AuthenticationFailException {
        try {

            // send the email
            emailService.sendTokenToEmail(userEmailSendDto.getEmail(), userEmailSendDto.getFirstName(), userEmailSendDto.getLastName(), userEmailSendDto.getToken());
            return new ResponseEntity<>(new ApiResponse(true, "Email has been sent"), HttpStatus.OK);
        } catch (MailException ex) {
            // log the exception
            logger.error("Failed to send email", ex);
            return new ResponseEntity<>(new ApiResponse(false, "Failed to send email"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO token should be updated
    @PostMapping("/signIn")
    public SignInResponseDto Signup(@RequestBody SignInDto signInDto) throws CustomException {
        return userService.signIn(signInDto);
    }

//    @PostMapping("/updateUser")
//    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserUpdateDto userUpdateDto) {
//        authenticationService.authenticate(token);
//        return userService.updateUser(token, userUpdateDto);
//    }


//    @PostMapping("/createUser")
//    public ResponseDto updateUser(@RequestParam("token") String token, @RequestBody UserCreateDto userCreateDto)
//            throws CustomException, AuthenticationFailException {
//        authenticationService.authenticate(token);
//        return userService.createUser(token, userCreateDto);
//    }
}
