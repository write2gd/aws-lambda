package gd.aws.lambda.shoppingbot.processing.strategies;


import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.LexResponse;
import gd.aws.lambda.shoppingbot.response.LexResponseHelper;

public abstract class IntentProcessor {
    protected final Logger logger;

    public IntentProcessor(Logger logger) {
        this.logger = logger;
    }

    public abstract LexResponse Process(LexRequest lexRequest);

    protected LexResponse createLexErrorResponse(LexRequest lexRequest, String message) {
        LexResponse failedLexResponse = LexResponseHelper.createFailedLexResponse(message, lexRequest);
        logger.log("Error: " + failedLexResponse.getDialogAction().getMessage().getContent());
        return failedLexResponse;
    }
}
