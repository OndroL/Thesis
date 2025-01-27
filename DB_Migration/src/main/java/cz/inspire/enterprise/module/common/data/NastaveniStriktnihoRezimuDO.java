/*
 * Clubspire, (c) Inspire CZ 2004-2006
 *
 * NastaveniStriktnihoRezimuDO.java
 * Created: 24. srpen 2006, 14:59
 * Author: <a href="rosta.svoboda@inspire.cz">Rostislav Svoboda</a>
 *
 */

package cz.inspire.enterprise.module.common.data;

import java.io.Serializable;

/**
 * Datovy objekt pro nastaveni striktniho rezimu
 *
 * @version 1.0
 * @author <a href="rosta.svoboda@inspire.cz">Rostislav Svoboda</a>
 */
public class NastaveniStriktnihoRezimuDO implements Serializable {
    
    /** Creates a new instance of NastaveniStriktnihoRezimuDO */
    public NastaveniStriktnihoRezimuDO() {
    }
    
    public NastaveniStriktnihoRezimuDO(boolean striktniRezimZapnut, String poplatkoveZboziId) {
        this.poplatkoveZboziId = poplatkoveZboziId;
        this.striktniRezimZapnut = striktniRezimZapnut;
    }

    /**
     * Holds value of property striktniRezimZapnut.
     */
    private boolean striktniRezimZapnut;

    /**
     * Getter for property striktniRezimZapnut.
     * @return Value of property striktniRezimZapnut.
     */
    public boolean isStriktniRezimZapnut() {
        return this.striktniRezimZapnut;
    }

    /**
     * Setter for property striktniRezimZapnut.
     * @param striktniRezimZapnut New value of property striktniRezimZapnut.
     */
    public void setStriktniRezimZapnut(boolean striktniRezimZapnut) {
        this.striktniRezimZapnut = striktniRezimZapnut;
    }

    /**
     * Holds value of property poplatkoveZboziId.
     * Pokud je nastaven poplatek za vidavani karty...
     */
    private String poplatkoveZboziId;

    /**
     * Getter for property poplatkoveZboziId.
     * @return Value of property poplatkoveZboziId.
     */
    public String getPoplatkoveZboziId() {
        return this.poplatkoveZboziId;
    }

    /**
     * Setter for property poplatkoveZboziId.
     * @param poplatkoveZboziId New value of property poplatkoveZboziId.
     */
    public void setPoplatkoveZboziId(String poplatkoveZboziId) {
        this.poplatkoveZboziId = poplatkoveZboziId;
    }
    
}
