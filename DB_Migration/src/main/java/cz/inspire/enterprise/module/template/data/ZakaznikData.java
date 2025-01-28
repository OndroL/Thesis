/**
 * Clubspire, (c) Inspire CZ 2004-2011
 *
 * ZakaznikData.java
 * Created on: 17.1.2013
 * Author: Tomáš Kramec
 *
 */
package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @version 1.0
 * @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
 */
public class ZakaznikData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
//    Atributy ze zakaznika
    private String id; 
    private boolean firma;
    
//    Attributy z osoby
    private String cisloPrefix;
    private String cisloSuffix;
    
    private String jmeno;
    private String prijmeni;
    private String titul;
    private String rc;
    private Date datumNarozeni;
    private Boolean pohlavi;
    
    private String predvolba;
    private String telefon;
    private String mobile;
    private String email;
    private boolean emailAktivni;
    private String kontaktniOsoba;
    
    private String ulice;
    private String psc;
    private String mesto;
    
    private String poznamka;
    private String dic;
    private String ico;
    private String icDph;
    private String web;
    private String banka;
    private String ucet;
    private Double handicap;

    public ZakaznikData() {
    }

    public String getBanka() {
        return banka;
    }

    public void setBanka(String banka) {
        this.banka = banka;
    }

    public String getCisloPrefix() {
        return cisloPrefix;
    }

    public void setCisloPrefix(String cisloPrefix) {
        this.cisloPrefix = cisloPrefix;
    }

    public String getCisloSuffix() {
        return cisloSuffix;
    }

    public void setCisloSuffix(String cisloSuffix) {
        this.cisloSuffix = cisloSuffix;
    }

    public Date getDatumNarozeni() {
        return datumNarozeni;
    }

    public void setDatumNarozeni(Date datumNarozeni) {
        this.datumNarozeni = datumNarozeni;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailAktivni() {
        return emailAktivni;
    }

    public void setEmailAktivni(boolean emailAktivni) {
        this.emailAktivni = emailAktivni;
    }

    public boolean isFirma() {
        return firma;
    }

    public void setFirma(boolean firma) {
        this.firma = firma;
    }

    public Double getHandicap() {
        return handicap;
    }

    public void setHandicap(Double handicap) {
        this.handicap = handicap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public String getKontaktniOsoba() {
        return kontaktniOsoba;
    }

    public void setKontaktniOsoba(String kontaktniOsoba) {
        this.kontaktniOsoba = kontaktniOsoba;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getPohlavi() {
        return pohlavi;
    }

    public void setPohlavi(Boolean pohlavi) {
        this.pohlavi = pohlavi;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public String getPredvolba() {
        return predvolba;
    }

    public void setPredvolba(String predvolba) {
        this.predvolba = predvolba;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public void setPrijmeni(String prijmeni) {
        this.prijmeni = prijmeni;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getTitul() {
        return titul;
    }

    public void setTitul(String titul) {
        this.titul = titul;
    }

    public String getUcet() {
        return ucet;
    }

    public void setUcet(String ucet) {
        this.ucet = ucet;
    }

    public String getUlice() {
        return ulice;
    }

    public void setUlice(String ulice) {
        this.ulice = ulice;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getIcDph() {
        return icDph;
    }

    public void setIcDph(String icDph) {
        this.icDph = icDph;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ZakaznikData other = (ZakaznikData) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
}
