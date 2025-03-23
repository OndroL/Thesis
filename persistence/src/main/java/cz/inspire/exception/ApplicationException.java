/*
 * Common-Server, (c) Inspire CZ 2004-2006
 *
 * ApplicationException.java
 * Vytvoreno: 29.1.2004
 * Autor: <a href="dominik.pospisil@inspire.cz">Dominik Pospíšil</a>
 */

package cz.inspire.exception;

/**
 * Aplikační chyba. Tato chyba je vyvolána, dojde-li k předvídatelné chybě v aplikační logice.
 * Předpokládá se, že tuto chybu je klient schopen zpracovat. Jedná se kupříkladu o nepovolené
 * vstupní parametry, chybná data, atp. Veškeré aplikační chyby by měly být potomkem této třídy.
 *
 * @author <a href="dominik.pospisil@inspire.cz">Dominik Pospíšil</a>
 * @version 1.0
 */
public class ApplicationException extends Exception implements AppExceptionInterface {
    
    /** Creates a new instance of InvalidOperationException */
    public ApplicationException() {
    }
    public ApplicationException(String msg) {
        super(msg);
    } 
    public ApplicationException(String msg, Throwable cause) {
        super(msg, cause);
    } 
    
}
