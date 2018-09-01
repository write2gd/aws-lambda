package gd.aws.lambda.shoppingbot.repositories;


import gd.aws.lambda.shoppingbot.entities.Invoice;

import java.util.List;

public interface InvoiceRepository extends Repository {
    List<Invoice> getAllShoppingCarts();
    Invoice getShoppingCartByUserId(String userId);
    void save(Invoice cart);
    void delete(Invoice cart);
}
