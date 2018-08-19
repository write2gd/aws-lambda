package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.Order;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.repositories.OrderRepository;

import java.util.List;

public class OrderServiceImpl extends Service implements OrderService {
    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository, Logger logger) {
        super(logger);
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getByOrderId(String orderId) {
        return orderRepository.getByOrderId(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    @Override
    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.getOrdersByUserId(userId);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }
}
