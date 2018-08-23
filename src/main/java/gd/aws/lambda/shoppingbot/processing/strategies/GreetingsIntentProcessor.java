package gd.aws.lambda.shoppingbot.processing.strategies;

import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.request.LexRequestAttribute;
import gd.aws.lambda.shoppingbot.response.DialogAction;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;
import gd.aws.lambda.shoppingbot.services.UserService;

public class GreetingsIntentProcessor extends IntentProcessor {
    private UserService userService;

    public GreetingsIntentProcessor(UserService userService, Logger logger) {
        super(logger);
        this.userService = userService;
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        String welcomeMessage = processNames(lexRequest);
        logger.log("Welcome Message is: " + welcomeMessage);
        return LexResponseHelper.createLexResponse(lexRequest, welcomeMessage, DialogAction.Type.Close, DialogAction.FulfillmentState.Fulfilled);
    }

    private String processNames(LexRequest lexRequest) {
        String welcomeMessage = "";
        if (lexRequest.getUserName() == null || lexRequest.getUserName()
                                                          .isEmpty()) {
            return "You are welcome. Can you tell your name please";
        }

        User user = null;
        String userName = lexRequest.getUserName();
        if (areSameNamesAsInSession(lexRequest, userName)) {
            return String.format("Welcome %s, what can I do for you?", userName);
        } else {
            user = userService.getUserByName(userName);
        }
        if (user == null) {
            user = addNewUser(userName, lexRequest);
            welcomeMessage = String.format("Nice to meet you %s. What would you like to buy?", userName);
            String newUserMessage = String.format("A new user has been registered: %s (UserId:\"%s\")", userName, user.getUserId());
            if (lexRequest.hasValidUserId())
                newUserMessage += String.format(";%s:\"%s\"", lexRequest.getUserIdType(), lexRequest.getUserId());
            logger.log(newUserMessage);
        } else {
            welcomeMessage = String.format("It's nice to see you again %s. What would you like to buy today?", userName);
            logger.log(String.format("An existing user has been recognized: %s (%s)", userName, user.getUserId()));
        }
        overrideSessionAttributesWithNonEmptyNames(lexRequest, user);
        return welcomeMessage;
    }

    private boolean areSameNamesAsInSession(LexRequest lexRequest, String userName) {
        String sessionUserName = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserName);
        logger.log(String.format("Session user name is: %s and user name is: %s", sessionUserName, userName));
        return (!isEmpty(userName) && userName.equals(sessionUserName));
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim()
                                     .length() == 0;
    }

    private User addNewUser(String userName, LexRequest lexRequest) {
        User user = new User();
        user.setUserName(userName);
        user.setUserId(lexRequest.getUserId());
        user.setAddress("Mock Address for Testing");
        userService.save(user);
        return user;
    }

    private void overrideSessionAttributesWithNonEmptyNames(LexRequest lexRequest, User user) {
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.UserId, user.getUserId());
        lexRequest.setSessionAttribute(LexRequestAttribute.SessionAttribute.UserName, user.getUserName());
    }
}
