package gd.aws.lambda.shoppingbot.processing.strategies;


import gd.aws.lambda.shoppingbot.common.OperationValueResult;
import gd.aws.lambda.shoppingbot.entities.ShoppingCart;
import gd.aws.lambda.shoppingbot.entities.ShoppingCartItem;
import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.ShoppingCartService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class ReviewShoppingCartIntentProcessor extends UserSessionIntentProcessor {
    private ShoppingCartService shoppingCartService;

    public ReviewShoppingCartIntentProcessor(ShoppingCartService shoppingCartService, UserService userService,
                                             Logger logger) {
        super(userService, logger);
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        OperationValueResult<User> gettingUserResult = getUser(lexRequest);
        if(gettingUserResult.isFailed())
            return createLexErrorResponse(lexRequest, gettingUserResult.getErrorsAsString());
        User user = gettingUserResult.getValue();

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(user.getUserId());
        String shoppingCartContent = createShoppingCartContent(shoppingCart);
        return LexResponseHelper.createLexResponse(lexRequest, shoppingCartContent,
                                                    DialogAction.Type.Close,
                                                    DialogAction.FulfillmentState.Fulfilled);
    }

    private String createShoppingCartContent(ShoppingCart shoppingCart) {
        if(shoppingCart == null || shoppingCart.isEmpty())
            return "Shopping cart is empty";

        StringBuilder messageBuilder = new StringBuilder();
        for(ShoppingCartItem cartItem: shoppingCart.getItems()){
            if(cartItem.isEmpty())
                continue;
            messageBuilder.append(String.format("%s; ", cartItem));
        }
        if(messageBuilder.length() > 0){
            messageBuilder.insert(0, "Your shopping cart contains: ");
            messageBuilder.append(String.format("Total sum: %s", shoppingCart.getTotalSum()));
        }
        return messageBuilder.toString();
    }
}
