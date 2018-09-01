package gd.aws.lambda.shoppingbot.entities.converters;


import com.fasterxml.jackson.core.type.TypeReference;
import gd.aws.lambda.shoppingbot.entities.InvoiceItem;

import java.util.List;

public class InvoiceItemConverter extends OrderItemInfoConverter<InvoiceItem> {
    @Override
    protected TypeReference<List<InvoiceItem>> createTypeReference() {
        return new TypeReference<List<InvoiceItem>>(){};
    }
}
