/*
 * Common-Server, (c) Inspire CZ 2004-2006
 *
 * SystemException.java
 * Vytvoreno: 9.4.2003
 * Autor: <a href="dominik.pospisil@inspire.cz">Dominik Pospíšil</a>
 */

package cz.inspire.exception;

/**
 * Systémová chyba. Lze použít pro zapouzdření chyb vyvolaných EJB kontajnerem.
 *
 * @author <a href="dominik.pospisil@inspire.cz">Dominik Pospíšil</a>
 * @version 1.0
 */
public class SystemException extends Exception {
    
    /** Creates a new instance of InvalidOperationException */
    public SystemException() {
    }
    public SystemException(String msg) {
        super(msg);
    } 
    public SystemException(String msg, Throwable cause) {
        super(msg, cause);
    } 
    
}

