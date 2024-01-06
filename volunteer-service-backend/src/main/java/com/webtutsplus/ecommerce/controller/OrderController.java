package com.webtutsplus.ecommerce.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.webtutsplus.ecommerce.common.ApiResponse;
import com.webtutsplus.ecommerce.dto.checkout.CheckoutItemDto;
import com.webtutsplus.ecommerce.dto.checkout.StripeResponse;
import com.webtutsplus.ecommerce.dto.order.OrderEmailSendDto;
import com.webtutsplus.ecommerce.dto.order.OrderMilitaryDto;
import com.webtutsplus.ecommerce.exceptions.AuthenticationFailException;
import com.webtutsplus.ecommerce.exceptions.OrderNotFoundException;
import com.webtutsplus.ecommerce.model.Order;
import com.webtutsplus.ecommerce.model.User;
import com.webtutsplus.ecommerce.service.AuthenticationService;
import com.webtutsplus.ecommerce.service.EmailService;
import com.webtutsplus.ecommerce.service.OrderService;
import org.springframework.mail.MailException;
//import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    // stripe create session API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        // create the stripe session
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        // send the stripe session id in response
        return new ResponseEntity<StripeResponse>(stripeResponse, HttpStatus.OK);
    }

    // place order after checkout
    @PostMapping("/ad")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {
        // validate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        // place the order
        orderService.placeOrder(user, sessionId);
        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody OrderMilitaryDto orderRequest,
                                                  @RequestHeader(value="Authorization") String token)
            throws AuthenticationFailException {
        // validate token
        token = token.replace("Bearer ", "");
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        // place the order
        Order order = orderService.addOrder(user, orderRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed", order.getId()), HttpStatus.CREATED);
    }

    @PostMapping("/email")
    public ResponseEntity<ApiResponse> sendOrderEmail(@RequestBody OrderEmailSendDto emailRequest,
                                                      @RequestHeader(value="Authorization") String token)
            throws AuthenticationFailException {
        // validate token
//        token = token.replace("Bearer ", "");
//        authenticationService.authenticate(token);
        // send the email
//        emailService.sendOrderEmail(emailRequest.getEmail(), emailRequest.getOrderId());
//        return new ResponseEntity<>(new ApiResponse(true, "Email has been sent"), HttpStatus.OK);
        try {
            // send the email
            emailService.sendOrderEmail(emailRequest.getEmail(), emailRequest.getOrderId());
            return new ResponseEntity<>(new ApiResponse(true, "Email has been sent"), HttpStatus.OK);
        } catch (MailException ex) {
            // log the exception
            logger.error("Failed to send email", ex);
            return new ResponseEntity<>(new ApiResponse(false, "Failed to send email"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get all orders
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {
        // validate token
        authenticationService.authenticate(token);
        // retrieve user
        User user = authenticationService.getUser(token);
        // get orders
        List<Order> orderDtoList = orderService.listOrders(user);

        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }


    // get orderitems for an order
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
            throws AuthenticationFailException {
        // validate token
        authenticationService.authenticate(token);
        try {
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order,HttpStatus.OK);
        }
        catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

}
