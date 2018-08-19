package gd.aws.lambda.shoppingbot.log;


public interface Logger {
    void log(String message);
    void log(Exception e);
}
