package gd.aws.lambda.shoppingbot;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import gd.aws.lambda.shoppingbot.log.CompositeLogger;
import gd.aws.lambda.shoppingbot.processing.ShoppingBotProcessor;
import gd.aws.lambda.shoppingbot.repositories.RepositoryFactory;
import gd.aws.lambda.shoppingbot.repositories.RepositoryFactoryImpl;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.request.LexRequestFactory;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.OrderService;
import gd.aws.lambda.shoppingbot.services.OrderServiceImpl;
import gd.aws.lambda.shoppingbot.services.ProductService;
import gd.aws.lambda.shoppingbot.services.ProductServiceImpl;
import gd.aws.lambda.shoppingbot.services.ShoppingCartService;
import gd.aws.lambda.shoppingbot.services.ShoppingCartServiceImpl;
import gd.aws.lambda.shoppingbot.services.UserService;
import gd.aws.lambda.shoppingbot.services.UserServiceImpl;

public class GrobotLambda implements RequestHandler<Map<String, Object>, LexResponse> {

    private final RepositoryFactory repositoryFactory;
    private ShoppingBotProcessor shoppingBotProcessor;
    private CompositeLogger logger = new CompositeLogger();

    public GrobotLambda() {
        repositoryFactory = new RepositoryFactoryImpl();
        UserServiceImpl userService = new UserServiceImpl(repositoryFactory.createUserRepository(), logger);
        init(userService,
             new ShoppingCartServiceImpl(repositoryFactory.createShoppingCartRepository(), userService, this.logger),
             new ProductServiceImpl(repositoryFactory.createProductRepository(), this.logger),
             new OrderServiceImpl(repositoryFactory.createOrderRepository(), this.logger));
    }

    public GrobotLambda(RepositoryFactory repositoryFactory, UserService userService, ShoppingCartService shoppingCartService, ProductService productService) {
        this.repositoryFactory = repositoryFactory;
        init(userService, shoppingCartService, productService, new OrderServiceImpl(repositoryFactory.createOrderRepository(), this.logger));
    }

    private void init(UserService userService, ShoppingCartService shoppingCartService, ProductService productService, OrderService orderService) {
        this.shoppingBotProcessor = new ShoppingBotProcessor(userService, shoppingCartService, productService, orderService, logger);
    }

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        if(context != null)
            logger.setLambdaLogger(context.getLogger());

        LexRequest lexRequest = null;
        try {
            lexRequest = LexRequestFactory.createFromMap(input);
            String json = new ObjectMapper().writeValueAsString(input);
            logger.log(String.format("Input: %s", json));
            logger.log(String.format("Input UserId: %s", lexRequest.getUserId()));
            LexResponse lexRespond = shoppingBotProcessor.Process(lexRequest);
            logStatus(lexRespond);
            return lexRespond;
        } catch (Exception e) {
            logger.log(e);
            return LexResponseHelper.createFailedLexResponse("Error: " + e.getMessage(), lexRequest);
        }
    }

    private void logStatus(LexResponse lexRespond) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("FulfillmentState: %s\n", lexRespond.getDialogAction().getFulfillmentState()));
        builder.append("Session:\n");
        Map<String, Object> sessionAttributes = lexRespond.getSessionAttributes();
        for (String key: sessionAttributes.keySet())
            builder.append(String.format("  %s:\"%s\"\n", key, sessionAttributes.get(key)));
        logger.log(builder.toString());
    }

    @Override
    public void finalize() {
        repositoryFactory.shutdown();
    }
}

