package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.Invoice;

public interface InvoiceService {
    Invoice getShoppingCartByUserId(String userId);
    void save(Invoice cart);
    void delete(Invoice invoice);
}
