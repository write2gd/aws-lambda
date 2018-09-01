package gd.aws.lambda.shoppingbot;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import gd.aws.lambda.shoppingbot.log.CompositeLogger;
import gd.aws.lambda.shoppingbot.processing.UchpBotProcessor;
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
import gd.aws.lambda.shoppingbot.services.InvoiceService;
import gd.aws.lambda.shoppingbot.services.InvoiceServiceImpl;
import gd.aws.lambda.shoppingbot.services.UserService;
import gd.aws.lambda.shoppingbot.services.UserServiceImpl;

public class UchpRequestHandler implements RequestHandler<Map<String, Object>, LexResponse> {

    private final RepositoryFactory repositoryFactory;
    private UchpBotProcessor botProcessor;
    private CompositeLogger logger = new CompositeLogger();

    public UchpRequestHandler() {
        repositoryFactory = new RepositoryFactoryImpl();
        UserServiceImpl userService = new UserServiceImpl(repositoryFactory.createUserRepository(), logger);
        init(userService, new InvoiceServiceImpl(repositoryFactory.createShoppingCartRepository(), userService, this.logger),
             new ProductServiceImpl(repositoryFactory.createProductRepository(), this.logger),
             new OrderServiceImpl(repositoryFactory.createOrderRepository(), this.logger));
    }

    public UchpRequestHandler(RepositoryFactory repositoryFactory, UserService userService, InvoiceService invoiceService,
               ProductService productService) {
        this.repositoryFactory = repositoryFactory;
        init(userService, invoiceService, productService, new OrderServiceImpl(repositoryFactory.createOrderRepository(), this.logger));
    }

    private void init(UserService userService, InvoiceService invoiceService, ProductService productService, OrderService orderService) {
        this.botProcessor = new UchpBotProcessor(userService, invoiceService, productService, orderService, logger);
    }

    @Override
    public LexResponse handleRequest(Map<String, Object> input, Context context) {
        if (context != null)
            logger.setLambdaLogger(context.getLogger());

        LexRequest lexRequest = null;
        try {
            lexRequest = LexRequestFactory.createFromMap(input);
            String json = new ObjectMapper().writeValueAsString(input);
            logger.log(String.format("Input: %s", json));
            LexResponse lexRespond = botProcessor.Process(lexRequest);
            logStatus(lexRespond);
            return lexRespond;
        } catch (Exception e) {
            logger.log("Exception occurred is: " + e.getMessage());
            return LexResponseHelper.createFailedLexResponse("Exception occurred: " + e.getMessage(), lexRequest);
        }
    }

    private void logStatus(LexResponse lexRespond) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("FulfillmentState: %s\n", lexRespond.getDialogAction()
                                                                         .getFulfillmentState()));
        builder.append("Session:\n");
        Map<String, Object> sessionAttributes = lexRespond.getSessionAttributes();
        for (String key : sessionAttributes.keySet())
            builder.append(String.format("  %s:\"%s\"\n", key, sessionAttributes.get(key)));
        //logger.log(builder.toString());
    }

    @Override
    public void finalize() {
        repositoryFactory.shutdown();
    }
}

