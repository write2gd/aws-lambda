package gd.aws.lambda.shoppingbot.common;


public interface OperationResult {
    void addError(String message);
    boolean isFailed();
    String getErrorsAsString();
}
