/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* ObjednavkaData.java
* Created on: 26.8.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.template.data.uctenka;

import java.io.Serializable;

/**
* UcetData
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class UcetData implements Serializable  {

    private static final long serialVersionUID = 1L;
    
    private String zaplacenoDen;
    private String zaplacenoCas;
    private String cisloObjednavky;
    private String soucetSDPH;
    private String celkemDPH;
    private String fiskalizovano;
    private String mena;

    public String getZaplacenoDen() {
        return zaplacenoDen;
    }

    public void setZaplacenoDen(String zaplacenoDen) {
        this.zaplacenoDen = zaplacenoDen;
    }

    public String getZaplacenoCas() {
        return zaplacenoCas;
    }

    public void setZaplacenoCas(String zaplacenoCas) {
        this.zaplacenoCas = zaplacenoCas;
    }

    public String getCisloObjednavky() {
        return cisloObjednavky;
    }

    public void setCisloObjednavky(String cisloObjednavky) {
        this.cisloObjednavky = cisloObjednavky;
    }

    public String getSoucetSDPH() {
        return soucetSDPH;
    }

    public void setSoucetSDPH(String soucetSDPH) {
        this.soucetSDPH = soucetSDPH;
    }

    public String getCelkemDPH() {
        return celkemDPH;
    }

    public void setCelkemDPH(String celkemDPH) {
        this.celkemDPH = celkemDPH;
    }

    public String getFiskalizovano() {
        return fiskalizovano;
    }

    public void setFiskalizovano(String fiskalizovano) {
        this.fiskalizovano = fiskalizovano;
    }

    public String getMena() {
        return mena;
    }

    public void setMena(String mena) {
        this.mena = mena;
    }
    
}