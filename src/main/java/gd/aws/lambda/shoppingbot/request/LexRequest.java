package gd.aws.lambda.shoppingbot.request;


import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.http.util.TextUtils.isEmpty;

public class LexRequest {
    private String botName;
    private ConfirmationStatus confirmationStatus;
    private String intentName;
    private String type;
    private String id;
    private String unit;
    private InvocationSource invocationSource = InvocationSource.FulfillmentCodeHook;
    private OutputDialogMode outputDialogMode = OutputDialogMode.Text;
    private String error;
    private String firstName;
    private String lastName;
    private String address;
    private Map<String, Object> sessionAttributes = new LinkedHashMap<>();
    private String userId;
    private String inputTranscript;
    private UserIdType userIdType;

    public void setProduct(String requestedProduct) {
        this.type = requestedProduct;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUnit() { return unit; }

    public void setInvocationSource(InvocationSource invocationSource) {
        this.invocationSource = invocationSource;
    }

    public void setOutputDialogMode(OutputDialogMode outputDialogMode) {
        this.outputDialogMode = outputDialogMode;
    }

    public boolean requestedProductIsSet() {
        return type != null && type.length() > 0;
    }
    public boolean requestedAmountIsSet() { return id != null && id.length() > 0; }
    public boolean requestedUnitsIsSet() { return unit != null && unit.length() > 0; }
    public boolean firstNameIsSet() { return firstName != null && firstName.length() > 0; }
    public boolean lastNameIsSet() { return lastName != null && lastName.length() > 0; }

    public String getBotName() {
        return botName;
    }

    public ConfirmationStatus getConfirmationStatus() {
        return confirmationStatus;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setConfirmationStatus(ConfirmationStatus confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return getError() != null;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSessionAttributes(Map<String, Object> sessionAttributes) {
        this.sessionAttributes = sessionAttributes != null ? sessionAttributes : new LinkedHashMap<>();
    }

    public Map<String, Object> getSessionAttributes() {
        return sessionAttributes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getSessionAttribute(String attributeName) {
        return sessionAttributes.containsKey(attributeName) ? sessionAttributes.get(attributeName) : null;
    }

    public void setSessionAttribute(String attributeName, Object value) {
        sessionAttributes.put(attributeName, value);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setInputTranscript(String inputTranscript) {
        this.inputTranscript = inputTranscript;
    }

    public String getInputTranscript() {
        return inputTranscript;
    }

    public void setUserIdType(UserIdType userIdType) {
        this.userIdType = userIdType;
    }

    public UserIdType getUserIdType() {
        return userIdType;
    }

    public boolean hasValidUserId() {
        return !isEmpty(getUserId()) && getUserIdType() != UserIdType.Undefined;
    }
}
