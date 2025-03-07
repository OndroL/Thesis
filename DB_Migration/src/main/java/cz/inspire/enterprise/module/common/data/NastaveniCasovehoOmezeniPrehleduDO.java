/**
 * Clubspire, (c) Inspire CZ 2004-2012
 *
 * NastaveniCasovehoOmezeniPrehleduDO.java
 * Created on: 29.8.2012
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.common.data;

import java.io.Serializable;
import java.util.List;

/**
 * NastaveniCasovehoOmezeniPrehleduDO
 *
 * @version 1.0
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public class NastaveniCasovehoOmezeniPrehleduDO implements Serializable {

    private int days;
    private List<String> skupinyList;

    public NastaveniCasovehoOmezeniPrehleduDO(int days, List<String> skupinyList) {
        this.days = days;
        this.skupinyList = skupinyList;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public List<String> getSkupinyList() {
        return skupinyList;
    }

    public void setSkupinyList(List<String> skupinyList) {
        this.skupinyList = skupinyList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NastaveniCasovehoOmezeniPrehleduDO other = (NastaveniCasovehoOmezeniPrehleduDO) obj;
        if (this.days != other.days) {
            return false;
        }
        if (this.skupinyList != other.skupinyList && (this.skupinyList == null || !this.skupinyList.equals(other.skupinyList))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
}
