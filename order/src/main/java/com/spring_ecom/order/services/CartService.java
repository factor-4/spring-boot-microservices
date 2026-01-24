package com.spring_ecom.order.services;

import com.spring_ecom.order.clients.ProductServiceClient;
import com.spring_ecom.order.clients.UserServiceClient;
import com.spring_ecom.order.dtos.CartItemRequest;
import com.spring_ecom.order.dtos.ProductResponse;
import com.spring_ecom.order.dtos.UserResponse;
import com.spring_ecom.order.models.CartItem;

import com.spring_ecom.order.repositories.CartItemRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.spring6.fallback.FallbackMethod;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    int attempt = 0;

   // @CircuitBreaker(name = "productService", fallbackMethod = "addToCartFallBack")
    @Retry(name = "retryBreaker", fallbackMethod = "addToCartFallBack")

    public boolean addToCart(String userId, CartItemRequest request) {

        System.out.println("Attempt count: "+ ++attempt);

        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        System.out.println("before user productresponse = " + productResponse);
        System.out.println("before user request = " + request);

        if(productResponse == null || productResponse.getStockQuantity()< request.getQuantity())
            return false;


        System.out.println("Calling user-service with userId = " + userId);

        UserResponse userResponse = userServiceClient.getUserDetails(userId);

        System.out.println("UserResponse = " + userResponse);
        if(userResponse== null)
            return  false;


//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()){
//            return false;
//        }
//        User user= userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(100));
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(Long.valueOf(request.getProductId()));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(100));
            cartItemRepository.save(cartItem);
        }

        return true;
    }


    public boolean addToCartFallBack(String userId, CartItemRequest request,
                                     Exception exception) {



        System.out.println("Fallback called");
        return false;
    }





    public boolean deleteItemFromCart(String userId, String productId) {

      CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem!=null){
            cartItemRepository.delete(cartItem);
            return true;
        }

        return false;


    }


    public List<CartItem> getCart(String userId) {


        return cartItemRepository.findByUserId(userId);

    }

    public void clearCart(String userId) {
         cartItemRepository.deleteByUserId(userId);
    }
}
