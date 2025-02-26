package UtilTests;

import cz.inspire.common.utils.ExceptionHandler;
import jakarta.ejb.FinderException;
import jakarta.ejb.ObjectNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerTest {

    @Test
    void testWrapDBException_NoResultException() {
        NoResultException cause = new NoResultException("No entity found");
        
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> 
            ExceptionHandler.wrapDBException(() -> { throw cause; }, "Find operation")
        );

        assertEquals("No result found (Operation: Find operation) - Cause: No entity found", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testWrapDBException_NonUniqueResultException() {
        NonUniqueResultException cause = new NonUniqueResultException("More than one entity found");

        FinderException exception = assertThrows(FinderException.class, () -> 
            ExceptionHandler.wrapDBException(() -> { throw cause; }, "Find operation")
        );

        assertEquals("Multiple results found (Operation: Find operation) - Cause: More than one entity found", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testWrapDBException_UnexpectedException() {
        RuntimeException cause = new RuntimeException("Unexpected error occurred");

        FinderException exception = assertThrows(FinderException.class, () -> 
            ExceptionHandler.wrapDBException(() -> { throw cause; }, "Find operation")
        );

        assertEquals("Unexpected error (Operation: Find operation) - Cause: Unexpected error occurred", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testWrapDBException_SuccessfulExecution() {
        String result = assertDoesNotThrow(() -> 
            ExceptionHandler.wrapDBException(() -> "Success", "Find operation")
        );

        assertEquals("Success", result);
    }
}
