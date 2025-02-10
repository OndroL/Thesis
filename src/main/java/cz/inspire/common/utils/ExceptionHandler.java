package cz.inspire.common.utils;

import jakarta.ejb.FinderException;
import jakarta.ejb.ObjectNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

import java.util.function.Supplier;

public class ExceptionHandler {
    public static <T> T wrapDBException(Supplier<T> action, String operation) throws FinderException {
        try {
            return action.get();
        } catch (NoResultException e) {
            ObjectNotFoundException ex = new ObjectNotFoundException(
                    "No result found (Operation: " + operation + ") - Cause: " + e.getMessage()
            );
            ex.initCause(e); // Preserve stack trace
            throw ex;
        } catch (NonUniqueResultException e) {
            FinderException ex = new FinderException(
                    "Multiple results found (Operation: " + operation + ") - Cause: " + e.getMessage()
            );
            ex.initCause(e); // Preserve stack trace
            throw ex;
        } catch (Exception e) {
            FinderException ex = new FinderException(
                    "Unexpected error (Operation: " + operation + ") - Cause: " + e.getMessage()
            );
            ex.initCause(e); // Preserve stack trace
            throw ex;
        }
    }
}
