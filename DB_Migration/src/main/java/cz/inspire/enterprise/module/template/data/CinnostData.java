/**
 * Clubspire, (c) Inspire CZ 2004-2011
 *
 * CinnostData.java
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
public class CinnostData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private int typ;
    private ZboziData zbozi;
    private String nazev;
    private String popis;
    private int pocetHracu;

    public CinnostData() {
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

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public int getTyp() {
        return typ;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public ZboziData getZbozi() {
        return zbozi;
    }

    public void setZbozi(ZboziData zbozi) {
        this.zbozi = zbozi;
    }

    public int getPocetHracu() {
        return pocetHracu;
    }

    public void setPocetHracu(int pocetHracu) {
        this.pocetHracu = pocetHracu;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CinnostData other = (CinnostData) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
}
