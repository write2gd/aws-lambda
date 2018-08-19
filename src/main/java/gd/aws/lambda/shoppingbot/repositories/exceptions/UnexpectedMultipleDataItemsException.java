package gd.aws.lambda.shoppingbot.repositories.exceptions;


public class UnexpectedMultipleDataItemsException extends Exception {
    public UnexpectedMultipleDataItemsException(String message) {
        super(message);
    }
}
