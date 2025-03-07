/**
 * Clubspire, (c) Inspire CZ 2004-2009
 *
 * OtviraciDoba.java
 * Created on: 24.8.2004
 * Author: Dominik Pospisil
 *
 */
package cz.inspire.enterprise.module.sport;

import java.util.SortedMap;
import java.util.Date;
import java.io.Serializable;

/**
 * Reprezentuje otviraci dobu.
 *
 * @version 1.0
 * @author Dominik Pospisil
 */
public interface OtviraciDoba extends Serializable {

    /**
     * Dotaz na otviraci dobu jednoho dne.
     *
     * @param day den, na jehoz otviraci dobu se ptam
     * @return vrati Mapu. Klic je interval oteviraci doby. Hodnota je provozovany sport v dany interval.
     */
    public SortedMap getOtevreno(Date day);
    
    SortedMap getOtevreno(Date day, boolean svatkyJakoNormalniDen);
    
}

