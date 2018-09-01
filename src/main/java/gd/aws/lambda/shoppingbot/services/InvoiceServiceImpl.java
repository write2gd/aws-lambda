package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.entities.Invoice;
import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.repositories.InvoiceRepository;


public class InvoiceServiceImpl extends Service implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private UserService userService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, UserService userService, Logger logger) {
        super(logger);
        this.invoiceRepository = invoiceRepository;
        this.userService = userService;
    }

    @Override
    public Invoice getShoppingCartByUserId(String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
           getLogger().log(String.format("User not found by UserId: %s", userId));
            return null;
        }
        Invoice invoice = invoiceRepository.getShoppingCartByUserId(userId);
        if(invoice != null)
            invoice.setUser(user);
        return invoice;
    }

    @Override
    public void save(Invoice cart) {
        invoiceRepository.save(cart);
    }

    @Override
    public void delete(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }
}
