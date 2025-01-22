package RepositoryTests.Common;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;


public class LogManagerTest {
    @Test
    public void testLogManager() {
        String logManager = System.getProperty("java.util.logging.manager");
        System.out.println("LogManager: " + logManager);
        assert "org.jboss.logmanager.LogManager".equals(logManager);
    }
}