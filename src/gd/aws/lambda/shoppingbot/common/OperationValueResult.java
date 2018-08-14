package gd.aws.lambda.shoppingbot.common;

public interface OperationValueResult<T> extends OperationResult {
    T getValue();
    void setValue(T value);
}
