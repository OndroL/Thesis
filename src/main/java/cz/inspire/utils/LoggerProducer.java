package cz.inspire.utils;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The LoggerProducer class provides dynamic injection of Logger instances using CDI. Unlike the old static logger
 * approach, this enables better testability by allowing loggers to be mocked during unit tests, addressing issues
 * where logger.error(...) invocations could not be verified.
 */

public class LoggerProducer {
    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return LogManager.getLogger(injectionPoint.getMember().getDeclaringClass());
    }
}