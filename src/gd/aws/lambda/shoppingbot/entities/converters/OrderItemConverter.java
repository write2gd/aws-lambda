package gd.aws.lambda.shoppingbot.entities.converters;


import com.fasterxml.jackson.core.type.TypeReference;
import gd.aws.lambda.shoppingbot.entities.OrderItem;

import java.util.List;

public class OrderItemConverter extends OrderItemInfoConverter<OrderItem> {
    @Override
    protected TypeReference<List<OrderItem>> createTypeReference() {
        return new TypeReference<List<OrderItem>>(){};
    }
}
