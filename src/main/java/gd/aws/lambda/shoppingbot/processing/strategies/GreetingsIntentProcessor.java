package gd.aws.lambda.shoppingbot.processing.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.request.LexRequestAttribute;
import gd.aws.lambda.shoppingbot.response.Attachments;
import gd.aws.lambda.shoppingbot.response.Button;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.response.Message;
import gd.aws.lambda.shoppingbot.response.ResponseCard;
import gd.aws.lambda.shoppingbot.services.ProductService;
import gd.aws.lambda.shoppingbot.services.UserService;

public class GreetingsIntentProcessor extends IntentProcessor {
    private UserService userService;
    private ProductService productService;

    public GreetingsIntentProcessor(UserService userService, ProductService productService, Logger logger) {
        super(logger);
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        if (!lexRequest.firstNameIsSet() || !lexRequest.lastNameIsSet())
            return createLexErrorResponse(lexRequest, "First name or last name are not specified.");

        String welcomeMessage = processNames(lexRequest);
        Map<Integer, List<String>> products = productService.getAvailableProducts();
        if (products.isEmpty()) {
            return LexResponseHelper.createLexResponse(lexRequest, welcomeMessage, DialogAction.Type.CLOSE_TYPE,
                                                       DialogAction.FulfillmentState.FULFILLMENT_STATE_FULFILLED);
        }

        DialogAction action = new DialogAction();
        ResponseCard responseCard = new ResponseCard();
        responseCard.setContentType("application/vnd.amazonaws.card.generic");
        Attachments[] attachments = getAttachments(products);
        responseCard.setGenericAttachments(attachments);
        responseCard.setVersion(1);
        action.setResponseCard(responseCard);
        Message message = new Message(Message.ContentType.PlainText, welcomeMessage);
        action.setMessage(message);
        action.setType(DialogAction.Type.CLOSE_TYPE);
        action.setFulfillmentState(DialogAction.FulfillmentState.FULFILLMENT_STATE_FULFILLED);

        return LexResponseHelper.createButtonLexResponse(lexRequest, action);
    }

    private Attachments[] getAttachments(Map<Integer, List<String>> products) {
        List<Attachments> attachments = new ArrayList<>();
        int attachmentSize = 0;
        for (Map.Entry<Integer, List<String>> listEntry : products.entrySet()) {
            if (attachmentSize == 10) {
                break;
            }
            attachmentSize++;
            List<String> pList = listEntry.getValue();
            Button[] buttons = getProductButtons(pList);
            Attachments attachment = new Attachments();
            attachment.setButtons(buttons);
            if (listEntry.getKey() == 1) {
                attachment.setTitle("Volvo Products");
                attachments.add(attachment);
            } else if (listEntry.getKey() == 2) {
                attachment.setTitle("Claims");
                attachments.add(attachment);
            } else if (listEntry.getKey() == 3) {
                attachment.setTitle("Campaigns");
                attachments.add(attachment);
            }
        }

        return attachments.toArray(new Attachments[attachments.size()]);
    }

    private Button[] getProductButtons(List<String> products) {
        List<Button> buttons = new ArrayList<>();
        int buttonSize = 0;
        for (String p : products) {
            if (buttonSize == 5) {
                break;
            }
            buttonSize++;
            Button button = new Button(p, p);
            buttons.add(button);
        }
        return buttons.toArray(new Button[buttons.size()]);
    }

    private String processNames(LexRequest lexRequest) {
        String firstName = lexRequest.getFirstName();
        String lastName = lexRequest.getLastName();

        if (areSameNamesAsInSession(lexRequest, firstName)) {
            return String.format("Yes %s, what can I do for you?", firstName);
        }

        String welcomeMessage = "";
        User user = userService.getUserByName(firstName);

        if (user == null) {
            user = addNewUser(firstName, lastName, lexRequest);
            welcomeMessage = String.format("Nice to meet you  %s.", firstName);
        } else {
            welcomeMessage = String.format("Nice to meet you again %s.", firstName);
        }
        welcomeMessage += String.format("What would you like to buy?");
        overrideSessionAttributesWithNonEmptyNames(lexRequest, user);
        return welcomeMessage;
    }

    private boolean areSameNamesAsInSession(LexRequest lexRequest, String userName) {
        String sessionUserName = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserName);
        return (!isEmpty(userName) && userName.equals(sessionUserName));
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim()
                                     .length() == 0;
    }

    private User addNewUser(String userName, String lastName, LexRequest lexRequest) {
        User user = new User();
        user.setUserName(userName);
        user.setLastName(lastName);
        user.setUserId(lexRequest.getUserId());
        user.setAddress("Vijaya Enclave");
        userService.save(user);
        return user;
    }

    private void overrideSessionAttributesWithNonEmptyNames(LexRequest lexRequest, User user) {
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.UserName, user.getUserName());
    }
}
