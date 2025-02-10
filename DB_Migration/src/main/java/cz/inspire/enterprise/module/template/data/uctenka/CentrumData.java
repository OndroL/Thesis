/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* CentrumData.java
* Created on: 26.8.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.template.data.uctenka;

import java.io.Serializable;

/**
* CentrumData
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class CentrumData implements Serializable  {

    private static final long serialVersionUID = 1L;

    private String nazev;
    private String adresa;
    private String logoUrl;
    private String ico;
    private String dic;
    private String telefon;

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    
}