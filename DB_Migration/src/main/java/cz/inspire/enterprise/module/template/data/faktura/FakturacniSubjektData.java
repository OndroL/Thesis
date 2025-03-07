
package cz.inspire.enterprise.module.template.data.faktura;

import java.io.Serializable;
/**
 *
 * @author Marek
 */
public class FakturacniSubjektData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String dic;
    private String ico;
    private String icDph;
    
    private String nazevSpolecnosti;
    private String ulice;
    private String obec;
    private String psc;
    
    private String web;
    private String email;
    private String telefon;
    
    private String banka;
    private String ucet;
    private String iban;
    private String swift;
    private String zapisText;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanka() {
        return banka;
    }

    public void setBanka(String banka) {
        this.banka = banka;
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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

    public String getNazevSpolecnosti() {
        return nazevSpolecnosti;
    }

    public void setNazevSpolecnosti(String nazevSpolecnosti) {
        this.nazevSpolecnosti = nazevSpolecnosti;
    }

    public String getObec() {
        return obec;
    }

    public void setObec(String obec) {
        this.obec = obec;
    }

    public String getPsc() {
        return psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
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
    
    public String getZapisText() {
        return zapisText;
    }

    public void setZapisText(String zapisText) {
        this.zapisText = zapisText;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FakturacniSubjektData other = (FakturacniSubjektData) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
        
}
