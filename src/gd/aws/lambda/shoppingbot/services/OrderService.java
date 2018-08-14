package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.Order;

import java.util.List;

public interface OrderService {
    Order getByOrderId(String orderId);
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(String userId);
    void save(Order order);
    void delete(Order order);
}
