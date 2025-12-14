package com.spring_ecom.order.services;

import com.spring_ecom.order.dtos.CartItemRequest;
import com.spring_ecom.order.models.CartItem;

import com.spring_ecom.order.repositories.CartItemRepository;

import lombok.RequiredArgsConstructor;
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
    public boolean addToCart(String userId, CartItemRequest request) {

//        Optional<Product> productOpt = productRepository.findById(request.getProductId());
//        if(productOpt.isEmpty())
//            return false;
//
//        Product product= productOpt.get();
//
//        if(product.getStockQuantity()< request.getQuantity()){
//            return false;
//        }
//
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
            cartItem.setUserId(Long.valueOf(userId));
            cartItem.setProductId(Long.valueOf(request.getProductId()));
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(100));
            cartItemRepository.save(cartItem);
        }

        return true;
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
