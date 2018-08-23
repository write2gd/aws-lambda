package gd.aws.lambda.shoppingbot.processing.strategies;


import gd.aws.lambda.shoppingbot.log.Logger;
import gd.aws.lambda.shoppingbot.request.LexRequest;
import gd.aws.lambda.shoppingbot.response.LexResponse;

public class UnsupportedIntentProcessor extends IntentProcessor {
    public UnsupportedIntentProcessor(Logger logger) {
        super(logger);
    }

    @Override
    public LexResponse Process(LexRequest lexRequest) {
        return createLexErrorResponse(lexRequest, "Can you repeat again..");
    }
}
