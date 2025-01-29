package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;
/**
 *
 * @author Marek
 */
public class PolozkaUctuData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String datum;
    private String nazev;
    private String pocet;
    private String cenaBezDph;
    private String dph;
    private String cenaDph;

    public String getCenaBezDph() {
        return cenaBezDph;
    }

    public void setCenaBezDph(String cenaBezDph) {
        this.cenaBezDph = cenaBezDph;
    }

    public String getCenaDph() {
        return cenaDph;
    }

    public void setCenaDph(String cenaDph) {
        this.cenaDph = cenaDph;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDph() {
        return dph;
    }

    public void setDph(String dph) {
        this.dph = dph;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getPocet() {
        return pocet;
    }

    public void setPocet(String pocet) {
        this.pocet = pocet;
    }
    
    
}