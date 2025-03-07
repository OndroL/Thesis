/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* ObjednavkaPolozkyData.java
* Created on: 26.8.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.template.data.uctenka;

import java.io.Serializable;

/**
* ObjednavkaPolozkyData
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class ObjednavkaPolozkyData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String nazevPolozky;
    private String mnozstvi;
    private String cenaPolozkySDPH;
    private String dph;
    private String cenaCelkem;

    public String getNazevPolozky() {
        return nazevPolozky;
    }

    public void setNazevPolozky(String nazevPolozky) {
        this.nazevPolozky = nazevPolozky;
    }

    public String getMnozstvi() {
        return mnozstvi;
    }

    public void setMnozstvi(String mnozstvi) {
        this.mnozstvi = mnozstvi;
    }

    public String getCenaPolozkySDPH() {
        return cenaPolozkySDPH;
    }

    public void setCenaPolozkySDPH(String cenaPolozkySDPH) {
        this.cenaPolozkySDPH = cenaPolozkySDPH;
    }

    public String getCenaCelkem() {
        return cenaCelkem;
    }

    public void setCenaCelkem(String cenaCelkem) {
        this.cenaCelkem = cenaCelkem;
    }

    public String getDph() {
        return dph;
    }

    public void setDph(String dph) {
        this.dph = dph;
    }
    
}