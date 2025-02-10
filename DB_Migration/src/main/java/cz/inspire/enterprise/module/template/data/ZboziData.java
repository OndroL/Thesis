/**
 * Clubspire, (c) Inspire CZ 2004-2011
 *
 * ZboziData.java
 * Created on: 17.1.2013
 * Author: Tomáš Kramec
 *
 */
package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;

/**
 * TODO description
 *
 * @version 1.0
 * @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
 */
public class ZboziData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum TypZbozi {
        SLUZBA, ZBOZI, SLOZENE_ZBOZI, SLOZENA_SLUZBA
    }
    
    private String id;
    private String nazev;
    private TypZbozi typZbozi;
    private int mnozstvi;

    public ZboziData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public TypZbozi getTypZbozi() {
        return typZbozi;
    }

    public void setTypZbozi(TypZbozi typZbozi) {
        this.typZbozi = typZbozi;
    }

    public int getMnozstvi() {
        return mnozstvi;
    }

    public void setMnozstvi(int mnozstvi) {
        this.mnozstvi = mnozstvi;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ZboziData other = (ZboziData) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
}
