package gd.aws.lambda.shoppingbot.request;

import java.util.HashMap;
import java.util.Map;

import gd.aws.lambda.shoppingbot.intents.ClaimIntent;
import gd.aws.lambda.shoppingbot.intents.GreetingsIntent;
import gd.aws.lambda.shoppingbot.intents.CampaignIntent;
import gd.aws.lambda.shoppingbot.intents.ProductIntent;
import gd.aws.lambda.shoppingbot.request.strategies.intentloading.ClaimIntentLoadingStrategy;
import gd.aws.lambda.shoppingbot.request.strategies.intentloading.GreetingsIntentLoadingStrategy;
import gd.aws.lambda.shoppingbot.request.strategies.intentloading.IntentLoaderStrategy;
import gd.aws.lambda.shoppingbot.request.strategies.intentloading.CampaignIntentLoadingStrategy;
import gd.aws.lambda.shoppingbot.request.strategies.intentloading.UnsupportedIntentLoaderStrategy;
import gd.aws.lambda.shoppingbot.request.strategies.intentloading.ProductIntentLoadingStrategy;

public class LexRequestFactory {

    private final static Map<String, IntentLoaderStrategy> intentLoaderStrategies = new HashMap<>();
    private final static IntentLoaderStrategy unsupportedIntentLoaderStrategy = new UnsupportedIntentLoaderStrategy();

    static {
        intentLoaderStrategies.put(GreetingsIntent.Name, new GreetingsIntentLoadingStrategy());
        intentLoaderStrategies.put(ClaimIntent.Name, new ClaimIntentLoadingStrategy());
        intentLoaderStrategies.put(CampaignIntent.Name, new CampaignIntentLoadingStrategy());
        intentLoaderStrategies.put(ProductIntent.Name, new ProductIntentLoadingStrategy());
    }

    public static LexRequest createFromMap(Map<String, Object> input) {
        LexRequest request = new LexRequest();
        if (input == null)
            return request;

        loadMainAttributes(input, request);
        loadBotName(input, request);
        loadSessionAttributes(input, request);


        Map<String, Object> currentIntent = loadCurrentIntent(input);
        if (currentIntent != null)
            loadIntentParameters(currentIntent, request);

        return request;
    }

    private static void loadMainAttributes(Map<String, Object> input, LexRequest request) {
        loadUserId(input, request);
        request.setInputTranscript((String) input.get(LexRequestAttribute.InputTranscript));
        request.setInvocationSource(getInvocationSource(input));
        request.setOutputDialogMode(getOutputDialogMode(input));
        request.setFirstName((String) request.getSessionAttribute(LexRequestAttribute.SessionAttribute.UserName));
        request.setLastName((String) request.getSessionAttribute(LexRequestAttribute.SessionAttribute.LastName));
    }

    private static void loadUserId(Map<String, Object> input, LexRequest request) {
        String userId = (String) input.get(LexRequestAttribute.UserId);
        request.setUserId(userId);
    }

    private static OutputDialogMode getOutputDialogMode(Map<String, Object> input) {
        return LexRequestAttribute.OutputDialogModeValue.Voice.equals((String) input.get(LexRequestAttribute.OutputDialogMode)) ?
                   OutputDialogMode.Voice :
                   OutputDialogMode.Text;
    }

    private static InvocationSource getInvocationSource(Map<String, Object> input) {
        return LexRequestAttribute.InvocationSourceValue.DialogCodeHook.equals((String) input.get(LexRequestAttribute.InvocationSource)) ?
                   InvocationSource.DialogCodeHook :
                   InvocationSource.FulfillmentCodeHook;
    }

    private static void loadSessionAttributes(Map<String, Object> input, LexRequest request) {
        Map<String, Object> sessionAttrs = (Map<String, Object>) input.get(LexRequestAttribute.SessionAttributes);
        if (sessionAttrs != null)
            request.setSessionAttributes(sessionAttrs);
    }

    private static void loadIntentParameters(Map<String, Object> currentIntent, LexRequest request) {
        request.setConfirmationStatus(getConfirmationStatus(currentIntent));
        request.setIntentName((String) currentIntent.get(LexRequestAttribute.CurrentIntentName));

        loadIntentSlots(currentIntent, request);
    }

    private static ConfirmationStatus getConfirmationStatus(Map<String, Object> currentIntent) {
        String confirmationStatus = (String) currentIntent.get(LexRequestAttribute.InvocationSource);
        return LexRequestAttribute.ConfirmationStatusValue.Confirmed.equals(confirmationStatus) ?
                   ConfirmationStatus.Confirmed :
                   LexRequestAttribute.ConfirmationStatusValue.Denied.equals(confirmationStatus) ? ConfirmationStatus.Denied : ConfirmationStatus.None;
    }

    private static Map<String, Object> loadCurrentIntent(Map<String, Object> input) {
        return (Map<String, Object>) input.get(LexRequestAttribute.CurrentIntent);
    }

    private static void loadBotName(Map<String, Object> input, LexRequest request) {
        Map<String, Object> bot = (Map<String, Object>) input.get(LexRequestAttribute.Bot);
        if (bot != null)
            request.setBotName((String) bot.get(LexRequestAttribute.BotName));
    }

    private static void loadIntentSlots(Map<String, Object> currentIntent, LexRequest request) {
        IntentLoaderStrategy strategy = getIntentLoadingStrategyBy(request.getIntentName());
        Map<String, Object> slots = (Map<String, Object>) currentIntent.get(LexRequestAttribute.Slots);
        strategy.load(request, slots);
    }

    public static IntentLoaderStrategy getIntentLoadingStrategyBy(String intentName) {
        return intentLoaderStrategies.containsKey(intentName) ? intentLoaderStrategies.get(intentName) : unsupportedIntentLoaderStrategy;
    }

}
