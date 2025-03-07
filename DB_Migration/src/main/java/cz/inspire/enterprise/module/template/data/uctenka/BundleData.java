/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* BundleData.java
* Created on: 29.8.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.template.data.uctenka;

import java.io.Serializable;

/**
* BundleData
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class BundleData implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String eetRezim;
    private String jmenoZakaznika;
    private String stavDepozitu;
    private String casProdeje;
    private String nazevPolozky;
    private String ksPolozky;
    private String cenaZaJednotku;
    private String celkem;
    private String celkemZaPolozku;
    private String dph;
    private String zTohoDph;
    private String fiskalizovano;
    private String pokladna;
    private String provozovna;
    private String danovyDokladCislo;
    private String datumPlneni;
    private String platba;

    public String getEetRezim() {
        return eetRezim;
    }

    public void setEetRezim(String eetRezim) {
        this.eetRezim = eetRezim;
    }

    public String getJmenoZakaznika() {
        return jmenoZakaznika;
    }

    public void setJmenoZakaznika(String jmenoZakaznika) {
        this.jmenoZakaznika = jmenoZakaznika;
    }

    public String getStavDepozitu() {
        return stavDepozitu;
    }

    public void setStavDepozitu(String stavDepozitu) {
        this.stavDepozitu = stavDepozitu;
    }

    public String getCasProdeje() {
        return casProdeje;
    }

    public void setCasProdeje(String casProdeje) {
        this.casProdeje = casProdeje;
    }

    public String getNazevPolozky() {
        return nazevPolozky;
    }

    public void setNazevPolozky(String nazevPolozky) {
        this.nazevPolozky = nazevPolozky;
    }

    public String getKsPolozky() {
        return ksPolozky;
    }

    public void setKsPolozky(String ksPolozky) {
        this.ksPolozky = ksPolozky;
    }

    public String getCenaZaJednotku() {
        return cenaZaJednotku;
    }

    public void setCenaZaJednotku(String cenaZaJednotku) {
        this.cenaZaJednotku = cenaZaJednotku;
    }

    public String getCelkem() {
        return celkem;
    }

    public void setCelkem(String celkem) {
        this.celkem = celkem;
    }

    public String getCelkemZaPolozku() {
        return celkemZaPolozku;
    }

    public void setCelkemZaPolozku(String celkemZaPolozku) {
        this.celkemZaPolozku = celkemZaPolozku;
    }

    public String getDph() {
        return dph;
    }

    public void setDph(String dph) {
        this.dph = dph;
    }

    public String getzTohoDph() {
        return zTohoDph;
    }

    public void setzTohoDph(String zTohoDph) {
        this.zTohoDph = zTohoDph;
    }

    public String getFiskalizovano() {
        return fiskalizovano;
    }

    public void setFiskalizovano(String fiskalizovano) {
        this.fiskalizovano = fiskalizovano;
    }

    public String getPokladna() {
        return pokladna;
    }

    public void setPokladna(String pokladna) {
        this.pokladna = pokladna;
    }

    public String getProvozovna() {
        return provozovna;
    }

    public void setProvozovna(String provozovna) {
        this.provozovna = provozovna;
    }

    public String getDanovyDokladCislo() {
        return danovyDokladCislo;
    }

    public void setDanovyDokladCislo(String danovyDokladCislo) {
        this.danovyDokladCislo = danovyDokladCislo;
    }

    public String getDatumPlneni() {
        return datumPlneni;
    }

    public void setDatumPlneni(String datumPlneni) {
        this.datumPlneni = datumPlneni;
    }

    public String getPlatba() {
        return platba;
    }

    public void setPlatba(String platba) {
        this.platba = platba;
    }
    
}
