package gd.aws.lambda.shoppingbot.processing.strategies;


import gd.aws.lambda.shoppingbot.common.OperationValueResult;
import gd.aws.lambda.shoppingbot.entities.Invoice;
import gd.aws.lambda.shoppingbot.entities.InvoiceItem;
import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.InvoiceService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class InvoiceIntentProcessor extends UserSessionIntentProcessor {
    private InvoiceService invoiceService;

    public InvoiceIntentProcessor(InvoiceService invoiceService, UserService userService,
                                             Logger logger) {
        super(userService, logger);
        this.invoiceService = invoiceService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        OperationValueResult<User> gettingUserResult = getUser(lexRequest);
        if(gettingUserResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUserResult.getErrorsAsString());
        User user = gettingUserResult.getValue();

        Invoice invoice = invoiceService.getShoppingCartByUserId(user.getUserId());
        String shoppingCartContent = createShoppingCartContent(invoice);
        return LexResponseHelper.createLexResponse(lexRequest, shoppingCartContent,
                                                    DialogAction.Type.CLOSE_TYPE,
                                                    DialogAction.FulfillmentState.FULFILLMENT_STATE_FULFILLED);
    }

    private String createShoppingCartContent(Invoice invoice) {
        if(invoice == null || invoice.isEmpty())
            return "Shopping cart is empty";

        StringBuilder messageBuilder = new StringBuilder();
        for(InvoiceItem cartItem: invoice.getItems()){
            if(cartItem.isEmpty())
                continue;
            messageBuilder.append(String.format("%s; ", cartItem));
        }
        if(messageBuilder.length() > 0){
            messageBuilder.insert(0, "Your shopping cart contains: ");
            messageBuilder.append(String.format("Total sum: %s", invoice.getTotalSum()));
        }
        return messageBuilder.toString();
    }
}
