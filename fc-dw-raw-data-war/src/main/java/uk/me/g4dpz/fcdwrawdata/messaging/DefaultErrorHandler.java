package uk.me.g4dpz.fcdwrawdata.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler {

    private static Logger log = LoggerFactory.getLogger(DefaultErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        log.error(t.getCause().getMessage());
    }
}
