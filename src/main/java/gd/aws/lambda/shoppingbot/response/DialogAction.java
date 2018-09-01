package gd.aws.lambda.shoppingbot.response;

import java.util.HashMap;
import java.util.Map;

public class DialogAction {
    private String type;
    private String fulfillmentState;
    private Message message;
    private String slotToElicit;
    private ResponseCard responseCard;
    private String intentName;
    private Map<String, String> slots = new HashMap<>();

    public class Type {
        public static final String ELICIT_INTENT_TYPE = "ElicitIntent";
        public static final String ELICIT_SLOT_TYPE = "ElicitSlot";
        public static final String CONFIRM_TYPE = "ConfirmIntent";
        public static final String DELEGATE_TYPE = "Delegate";
        public static final String CLOSE_TYPE = "Close";
    }

    public class FulfillmentState {
        public static final String FULFILLMENT_STATE_FULFILLED = "Fulfilled";
        public static final String FULFILLMENT_STATE_FAILED = "Failed";
    }

    public DialogAction(String type, String fulfillmentState, Message message) {
        this.type = type;
        this.fulfillmentState = fulfillmentState;
        this.message = message;
    }

    public DialogAction() {
    }

    public DialogAction(String type, String intentName, Message message, ResponseCard responseCard, String fulfillmentState) {
        this.type = type;
        this.intentName = intentName;
        this.fulfillmentState = fulfillmentState;
        this.message = message;
        this.responseCard = responseCard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFulfillmentState() {
        return fulfillmentState;
    }

    public void setFulfillmentState(String fulfillmentState) {
        this.fulfillmentState = fulfillmentState;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getSlotToElicit() {
        return slotToElicit;
    }

    public void setSlotToElicit(String slotToElicit) {
        this.slotToElicit = slotToElicit;
    }

    public ResponseCard getResponseCard() {
        return responseCard;
    }

    public void setResponseCard(ResponseCard responseCard) {
        this.responseCard = responseCard;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }
}
