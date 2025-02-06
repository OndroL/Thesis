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
            throw new ObjectNotFoundException("No results found during " + operation + ": " + e.getMessage() + " Error stack :" + e);
        } catch (NonUniqueResultException e) {
            throw new FinderException("Multiple results found during " + operation + ": " + e.getMessage() + " Error stack :" + e);
        } catch (Exception e) {
            throw new FinderException("Unexpected error during " + operation + ": " + e.getMessage() + " Error stack :" + e);
        }
    }
}
