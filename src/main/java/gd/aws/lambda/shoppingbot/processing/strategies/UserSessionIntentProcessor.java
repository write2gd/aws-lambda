package gd.aws.lambda.shoppingbot.processing.strategies;

import static org.apache.http.util.TextUtils.isEmpty;

import gd.aws.lambda.shoppingbot.common.OperationValueResult;
import gd.aws.lambda.shoppingbot.common.OperationValueResultImpl;
import gd.aws.lambda.shoppingbot.entities.User;
import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.request.LexRequestAttribute;
import gd.aws.lambda.shoppingbot.services.UserService;

public abstract class UserSessionIntentProcessor extends IntentProcessor {
    protected final UserService userService;
    private static final String ERROR_MESSAGE = "Hi could you please tell your full name?";

    public UserSessionIntentProcessor(UserService userService, Logger logger) {
        super(logger);
        this.userService = userService;
    }

    protected OperationValueResult<User> getUser(LexRequest lexRequest) {
        OperationValueResultImpl<User> operationResult = new OperationValueResultImpl<>();
        String sessionUserId = (String) lexRequest.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserId);
        boolean userIdIsEmpty = isEmpty(lexRequest.getUserId());
        boolean sessionUserIdIsEmpty = isEmpty(sessionUserId);
        if (sessionUserIdIsEmpty && userIdIsEmpty) {
            operationResult.addError(ERROR_MESSAGE);
            return operationResult;
        }
        User user = null;
        if (!sessionUserIdIsEmpty)
            user = userService.getUserById(sessionUserId);
        if (user == null && lexRequest.hasValidUserId()) {
            user = userService.getUserById(lexRequest.getUserId());
        }

        if (user == null) {
            operationResult.addError(ERROR_MESSAGE);
            return operationResult;
        }

        operationResult.setValue(user);
        return operationResult;
    }
}
