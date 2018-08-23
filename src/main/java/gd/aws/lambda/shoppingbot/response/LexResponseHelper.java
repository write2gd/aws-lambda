package gd.aws.lambda.shoppingbot.response;

import java.util.LinkedHashMap;
import java.util.Map;

import gd.aws.lambda.shoppingbot.log.CompositeLogger;
import gd.aws.lambda.shoppingbot.request.LexRequest;

public final class LexResponseHelper {
    private static CompositeLogger logger = new CompositeLogger();

    public static LexResponse createFailedLexResponse(String message, LexRequest lexRequest) {
        logger.log("Success Response helper processor: " + message);
        Map<String, Object> sessionAttributes = lexRequest != null ? lexRequest.getSessionAttributes() : new LinkedHashMap<>();
        return new LexResponse(
                   new DialogAction(DialogAction.Type.Close, DialogAction.FulfillmentState.Failed, new Message(Message.ContentType.PlainText, message)),
                   sessionAttributes);
    }

    public static LexResponse createLexResponse(LexRequest lexRequest, String content, String type, String fulfillmentState) {
        logger.log("Success Response helper processor: " + content);
        Message message = new Message(Message.ContentType.PlainText, content);
        DialogAction dialogAction = new DialogAction(type, fulfillmentState, message);
        return new LexResponse(dialogAction, lexRequest.getSessionAttributes());
    }

}
