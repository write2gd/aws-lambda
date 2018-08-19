package gd.aws.lambda.shoppingbot.common;

public class OperationResultImpl implements OperationResult {
    private StringBuilder errors = new StringBuilder();

    @Override
    public void addError(String message) {
        errors.append(message + " \n");
    }

    @Override
    public boolean isFailed() {
        return errors.length() > 0;
    }

    @Override
    public String getErrorsAsString() {
        return errors.toString();
    }
}
