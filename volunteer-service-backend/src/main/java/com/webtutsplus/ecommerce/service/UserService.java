package com.webtutsplus.ecommerce.service;


import com.webtutsplus.ecommerce.config.MessageStrings;
import com.webtutsplus.ecommerce.dto.*;
import com.webtutsplus.ecommerce.dto.user.*;
import com.webtutsplus.ecommerce.enums.ResponseStatus;
import com.webtutsplus.ecommerce.enums.Role;
import com.webtutsplus.ecommerce.enums.TokenStatus;
import com.webtutsplus.ecommerce.exceptions.AuthenticationFailException;
import com.webtutsplus.ecommerce.exceptions.CustomException;
import com.webtutsplus.ecommerce.model.AuthenticationToken;
import com.webtutsplus.ecommerce.model.Military;
import com.webtutsplus.ecommerce.model.User;
//import com.webtutsplus.ecommerce.repository.MilitaryRepository;
import com.webtutsplus.ecommerce.repository.MilitaryRepository;
import com.webtutsplus.ecommerce.repository.UserRepository;
import com.webtutsplus.ecommerce.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import static com.webtutsplus.ecommerce.config.MessageStrings.USER_CREATED;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MilitaryRepository militaryRepository;

    @Autowired
    AuthenticationService authenticationService;

    Logger logger = LoggerFactory.getLogger(UserService.class);


    public ResponseDto signUp(SignupDto signupDto)  throws CustomException {
        // Check to see if the current email address has already been registered.
        if (Helper.notNull(userRepository.findByEmail(signupDto.getEmail()))) {
            // If the email address has been registered then throw an exception.
            throw new CustomException("User already exists");
        }
        // first encrypt the password
        String encryptedPassword = signupDto.getPassword();
        try {
            encryptedPassword = hashPassword(signupDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }


        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), Role.user, encryptedPassword );

        User createdUser;
        try {
            // save the User
            createdUser = userRepository.save(user);
            // generate token for user
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            // save token in database
            authenticationService.saveConfirmationToken(authenticationToken);
            // success in creating
            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws CustomException {
        // first find User by email
        User user = userRepository.findByEmail(signInDto.getEmail());
        if(!Helper.notNull(user)){
            throw  new AuthenticationFailException("user not present");
        }
        try {
            // check if password is right
            if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
                // passowrd doesnot match
                throw  new AuthenticationFailException(MessageStrings.WRONG_PASSWORD);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }

        AuthenticationToken token = authenticationService.getToken(user);

        if(!Helper.notNull(token)) {
            // token not present
            throw new CustomException("token not present");
        }

        return new SignInResponseDto ("success", token.getToken());
    }


    String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

//    public ResponseDto mySignUp(SignupDto signupDto) throws CustomException {
//        // If the user is a volunteer, process a standard registration
//        if (signupDto.getRole().equals(Role.VOLUNTEER)) {
//            // Same as your original code...
//        }
//        // If the user is military, the sign-up process only involves generating a token
//        else if (signupDto.getRole().equals(Role.MILITARY)) {
//            User user = new User(signupDto.getFirstName(), signupDto.getLastName(), Role.MILITARY, null);
//            User createdUser;
//            try {
//                // save the User
//                createdUser = userRepository.save(user);
//                // generate token for user
//                final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
//                // save token in database
//                authenticationService.saveConfirmationToken(authenticationToken);
//                // success in creating
//                return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
//            } catch (Exception e) {
//                // handle signup error
//                throw new CustomException(e.getMessage());
//            }
//        }
//        else {
//            throw new CustomException("Invalid role");
//        }
//    }
//
//    public SignInResponseDto mySignIn(SignInDto signInDto) throws CustomException {
//        // If the user is a volunteer, process a standard sign-in
//        if (signInDto.getRole().equals(Role.VOLUNTEER)) {
//            // Same as your original code...
//        }
//        // If the user is military, they sign in using a token instead of a password
//        else if (signInDto.getRole().equals(Role.MILITARY)) {
//            // Get the token from the SignInDto
//            AuthenticationToken token = authenticationService.getToken(signInDto.getToken());
//            if (!Helper.notNull(token)) {
//                // token not present
//                throw new CustomException("Invalid token");
//            }
//            return new SignInResponseDto ("success", token.getToken());
//        }
//        else {
//            throw new CustomException("Invalid role");
//        }
//    }

    public ResponseDtoWithToken militarySignUp(MilitarySignUpDto militarySignUpDto) throws CustomException {
        // Check to see if the current email address has already been registered.
//        if (Helper.notNull(militaryRepository.findByEmail(militarySignUpDto.getEmail()))) {
//            // If the email address has been registered then throw an exception.
//            throw new CustomException("User already exists");
//        }
        // first encrypt the password
//        String encryptedPassword = signupDto.getPassword();
//        try {
//            encryptedPassword = hashPassword(signupDto.getPassword());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            logger.error("hashing password failed {}", e.getMessage());
//        }

        User user = new User(militarySignUpDto.getFirstName(), militarySignUpDto.getLastName(), Role.military);
        Military military = new Military(user, user.getFirstName(), user.getLastName(), militarySignUpDto.getRank());


        User createdUser;
        Military createdMilitary;
        try {
            // save the User
            createdUser = userRepository.save(user);
            createdMilitary = militaryRepository.save(military);
            // generate token for user
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            //set expiration date
            // Set expiration date to 24 hours from now
            Calendar cal = Calendar.getInstance(); // gets the current time
            cal.add(Calendar.HOUR, 24); // adds 24 hours
            Date expirationDate = cal.getTime();

            authenticationToken.setExpirationDate(expirationDate);
            authenticationToken.setStatus(TokenStatus.unused);
            // save token in database
            authenticationService.saveConfirmationToken(authenticationToken);
            // success in creating
            // add returning token in ResponseDto
            ResponseDtoWithToken responseDtoWithToken = new ResponseDtoWithToken(ResponseStatus.success.toString(), USER_CREATED, authenticationToken.getToken());
            System.out.println(responseDtoWithToken);
            return responseDtoWithToken;
        } catch (Exception e) {
            // handle signup error
            throw new CustomException(e.getMessage());
        }
    }

    public SignInResponseDto signInWithToken(SignInWithTokenDto signInWithTokenDto) throws CustomException {
        // first find User by token
        //AuthenticationToken authToken = authenticationService.getToken(signInWithTokenDto.getToken());




        // Check if token is not null and if the token hasn't expired.
//        if(!Helper.notNull(authToken)) {
//            throw new CustomException("token not present");
//        }
        AuthenticationToken authToken = authenticationService.getTokenByToken(signInWithTokenDto.getToken());

        if (!Helper.notNull(authToken)){
            throw new CustomException("token not found");
        }

        if(authToken.getExpirationDate().before(new Date())) {
            throw new AuthenticationFailException("token expired");
        }

        if(!authToken.getStatus().equals(TokenStatus.unused)) {
            throw new AuthenticationFailException("token already used");
        }

        User user = authenticationService.getUser(signInWithTokenDto.getToken());
//        if (!Helper.notNull(user)){
//            throw new CustomException("token not present");
//        }
//        User user = authToken.getUser();
//
//        if(!Helper.notNull(user)){
//            throw  new AuthenticationFailException("user not present");
//        }

        // At this point, the user has been successfully authenticated by their token.
        // Update the token status to USED.
        authToken.setStatus(TokenStatus.used);
        authenticationService.saveConfirmationToken(authToken);

        String userName = authToken.getUser().getFirstName();

        return new SignInResponseDto ("success", authToken.getToken(), userName);

        //return new SignInResponseDto ("success", signInWithTokenDto.getToken());
    }

    public ResponseDto createUser(String token, UserCreateDto userCreateDto) throws CustomException, AuthenticationFailException {
        User creatingUser = authenticationService.getUser(token);
        if (!canCrudUser(creatingUser.getRole())) {
            // user can't create new user
            throw  new AuthenticationFailException(MessageStrings.USER_NOT_PERMITTED);
        }
        String encryptedPassword = userCreateDto.getPassword();
        try {
            encryptedPassword = hashPassword(userCreateDto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("hashing password failed {}", e.getMessage());
        }

        User user = new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getEmail(), userCreateDto.getRole(), encryptedPassword );
        User createdUser;
        try {
            createdUser = userRepository.save(user);
            final AuthenticationToken authenticationToken = new AuthenticationToken(createdUser);
            authenticationService.saveConfirmationToken(authenticationToken);
            return new ResponseDto(ResponseStatus.success.toString(), USER_CREATED);
        } catch (Exception e) {
            // handle user creation fail error
            throw new CustomException(e.getMessage());
        }

    }

    boolean canCrudUser(Role role) {
        if (role == Role.admin || role == Role.manager) {
            return true;
        }
        return false;
    }

    boolean canCrudUser(User userUpdating, Integer userIdBeingUpdated) {
        Role role = userUpdating.getRole();
        // admin and manager can crud any user
        if (role == Role.admin || role == Role.manager) {
            return true;
        }
        // user can update his own record, but not his role
        if (role == Role.user && userUpdating.getId() == userIdBeingUpdated) {
            return true;
        }
        return false;
    }
}
