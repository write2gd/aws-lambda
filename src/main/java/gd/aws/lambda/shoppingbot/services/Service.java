package gd.aws.lambda.shoppingbot.services;


import gd.aws.lambda.shoppingbot.log.Logger;


public class Service {
    private Logger logger;

    protected Service(Logger logger) {
        this.logger = logger;
    }

    protected Logger getLogger() {
        return logger;
    }
}
