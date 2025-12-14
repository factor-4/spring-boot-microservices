package com.spring_ecom.order.services;

import com.spring_ecom.order.dtos.OrderItemDto;
import com.spring_ecom.order.dtos.OrderResponse;

import com.spring_ecom.order.repositories.OrderRepository;
import com.spring_ecom.order.models.OrderStatus;
import com.spring_ecom.order.models.CartItem;
import com.spring_ecom.order.models.Order;
import com.spring_ecom.order.models.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId) {

        //validate cart items
        List <CartItem> cartItems = cartService.getCart(userId);
        if(cartItems.isEmpty()){

            return  Optional.empty();
        }

        // validate for user

//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if(userOptional.isEmpty()){
//
//            return Optional.empty();
//        }
//
//        User user = userOptional.get();

        // calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order
        Order order = new Order();
        order.setUserId(Long.valueOf(userId));
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);


        List<OrderItem> orderItems = cartItems.stream()
                .map(item-> new OrderItem(
                        null,
                        String.valueOf(item.getProductId()),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        // clear the cart
        cartService.clearCart(userId);
        
        return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDto(
                                orderItem.getId(),
                                orderItem.getProductId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
