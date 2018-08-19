package gd.aws.lambda.shoppingbot.entities.converters;


import com.fasterxml.jackson.core.type.TypeReference;
import gd.aws.lambda.shoppingbot.entities.ShoppingCartItem;

import java.util.List;

public class ShoppingCartItemConverter extends OrderItemInfoConverter<ShoppingCartItem> {
    @Override
    protected TypeReference<List<ShoppingCartItem>> createTypeReference() {
        return new TypeReference<List<ShoppingCartItem>>(){};
    }
}
