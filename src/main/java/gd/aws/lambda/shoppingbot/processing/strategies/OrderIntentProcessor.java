package gd.aws.lambda.shoppingbot.processing.strategies;

import gd.aws.lambda.shoppingbot.common.OperationResult;
import gd.aws.lambda.shoppingbot.common.OperationResultImpl;
import gd.aws.lambda.shoppingbot.common.OperationValueResult;
import gd.aws.lambda.shoppingbot.entities.*;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.OrderService;
import gd.aws.lambda.shoppingbot.services.InvoiceService;
import gd.aws.lambda.shoppingbot.services.UserService;

import java.util.List;

public class OrderIntentProcessor extends UserSessionIntentProcessor {
    private InvoiceService invoiceService;
    private OrderService orderService;

    public OrderIntentProcessor(InvoiceService invoiceService, OrderService orderService, UserService userService, Logger logger) {
        super(userService, logger);
        this.invoiceService = invoiceService;
        this.orderService = orderService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        OperationValueResult<User> gettingUserResult = getUser(lexRequest);
        if(gettingUserResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUserResult.getErrorsAsString());
        User user = gettingUserResult.getValue();

        Invoice invoice = invoiceService.getShoppingCartByUserId(user.getUserId());
        if(invoice == null || invoice.isEmpty())
            return createLexErrorResponse(lexRequest, "I'm sorry, shopping cart is empty yet.");

        Order order = createOrderFrom(invoice);
        try {
            orderService.save(order);
            OperationResult savingOrderResult = validateSavedOrder(order);
            if(savingOrderResult.isFailed())
                return createLexErrorResponse(lexRequest, savingOrderResult.getErrorsAsString());
            invoiceService.delete(invoice);
        } catch (Exception e) {
            logger.log(e);
            return createLexErrorResponse(lexRequest, "An error occurred while creating an order. Please try again.");
        }
        String orderContent = createOrderContent(order);
        return LexResponseHelper.createLexResponse(lexRequest, orderContent,
                DialogAction.Type.CLOSE_TYPE,
                DialogAction.FulfillmentState.FULFILLMENT_STATE_FULFILLED);
    }

    private OperationResult validateSavedOrder(Order order) {
        OperationResult operationResult = new OperationResultImpl();
        Order savedOrder = orderService.getByOrderId(order.getOrderId());
        if(savedOrder == null) {
            operationResult.addError("The order has noot been saved.");
        } else if(!order.toString().equals(savedOrder.toString())) {
            operationResult.addError(String.format("Saved order is different than original order.\n" +
                            "Original order:\n%s\nSaved order:\n%s.",
                             createOrderContent(order), createOrderContent(savedOrder)));
        }
        return operationResult;
    }

    private Order createOrderFrom(Invoice invoice) {
        Order order = new Order();
        order.setUser(invoice.getUser());
        List<OrderItem> orderItems = order.getItems();
        for(InvoiceItem cartItem : invoice.getItems())
            orderItems.add(createOrderItemFrom(cartItem));
        return order;
    }

    private OrderItem createOrderItemFrom(InvoiceItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setAmount(cartItem.getAmount());
        orderItem.setUnit(cartItem.getUnit());
        orderItem.setPrice(cartItem.getPrice());
        return orderItem;
    }

    private String createOrderContent(Order order) {
        if(order == null || order.isEmpty())
            return "Order is empty";

        StringBuilder messageBuilder = new StringBuilder();
        for(OrderItem cartItem: order.getItems()){
            if(cartItem.isEmpty())
                continue;
            messageBuilder.append(String.format("%s; ", cartItem));
        }
        if(messageBuilder.length() > 0){
            messageBuilder.insert(0, "Order contains: ");
            messageBuilder.append(String.format("Total sum: %s", order.getTotalSum()));
        }
        return messageBuilder.toString();
    }
}
