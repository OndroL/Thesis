/**
 * Clubspire, (c) Inspire CZ 2004-2009
 *
 * MomentOfTime.java
 * Created on: 20.1.2004
 * Author: Dominik Pospisil
 *
 */
package cz.inspire.enterprise.module.sport;

/**
 * Interface pro tridy, ktere reprezentuji cas.
 * Slouzi predevsim pro zjistovani intervalu mezi 2ma momenty casu.
 *
 * @version 1.0
 * @author  Dominik Pospisil
 */
public abstract class MomentOfTime implements java.io.Serializable, Comparable {
    public abstract int getMinuteDifference(MomentOfTime difMoment);
    public abstract MomentOfTime addMinutes(int minutes);
}
