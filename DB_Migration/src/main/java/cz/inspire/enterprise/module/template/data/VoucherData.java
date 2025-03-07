/**
 * Clubspire, (c) Inspire CZ 2004-2011
 *
 * VoucherData.java
 * Created on: 17.1.2013
 * Author: Tomáš Kramec
 *
 */
package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO description
 *
 * @version 1.0
 * @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
 */
public class VoucherData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private Date datumVytvoreni;
    private String zadavatel;
    
    private CinnostData cinnost;
    private ZboziData sluzba;
    private ZboziData zbozi;
    private BigDecimal cenaCelkem;
    
    private BigDecimal cena;  
    private String mena;
    private String menaSymbol;
    
    private Date obdobiOd;
    private Date obdobiDo;
    private Integer platnostDni;
    
    private String jmeno;
    private ZakaznikData zakaznik;
    private String poznamka;

    public VoucherData() {
    }
    
    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public BigDecimal getCenaCelkem() {
        return cenaCelkem;
    }

    public void setCenaCelkem(BigDecimal cenaCelkem) {
        this.cenaCelkem = cenaCelkem;
    }

    public CinnostData getCinnost() {
        return cinnost;
    }

    public void setCinnost(CinnostData cinnost) {
        this.cinnost = cinnost;
    }

    public Date getDatumVytvoreni() {
        return datumVytvoreni;
    }

    public void setDatumVytvoreni(Date datumVytvoreni) {
        this.datumVytvoreni = datumVytvoreni;
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

    public String getMena() {
        return mena;
    }

    public void setMena(String mena) {
        this.mena = mena;
    }

    public String getMenaSymbol() {
        return menaSymbol;
    }

    public void setMenaSymbol(String menaSymbol) {
        this.menaSymbol = menaSymbol;
    }

    public Date getObdobiDo() {
        return obdobiDo;
    }

    public void setObdobiDo(Date obdobiDo) {
        this.obdobiDo = obdobiDo;
    }

    public Date getObdobiOd() {
        return obdobiOd;
    }

    public void setObdobiOd(Date obdobiOd) {
        this.obdobiOd = obdobiOd;
    }

    public Integer getPlatnostDni() {
        return platnostDni;
    }

    public void setPlatnostDni(Integer platnostDni) {
        this.platnostDni = platnostDni;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public String getZadavatel() {
        return zadavatel;
    }

    public void setZadavatel(String zadavatel) {
        this.zadavatel = zadavatel;
    }

    public ZakaznikData getZakaznik() {
        return zakaznik;
    }

    public void setZakaznik(ZakaznikData zakaznik) {
        this.zakaznik = zakaznik;
    }

    public ZboziData getZbozi() {
        return zbozi;
    }

    public void setZbozi(ZboziData zbozi) {
        if (zbozi != null && (!ZboziData.TypZbozi.ZBOZI.equals(zbozi.getTypZbozi()) 
                                && !ZboziData.TypZbozi.SLOZENE_ZBOZI.equals(zbozi.getTypZbozi()))) {
            throw new IllegalArgumentException("Only ZBOZI and SLOZENE_ZBOZI is allowed for zbozi data."
                    + " Type " + zbozi.getTypZbozi() + " not allowed!");
        } 
        this.zbozi = zbozi;
    }

    public ZboziData getSluzba() {
        return sluzba;
    }

    public void setSluzba(ZboziData sluzba) {
        if (sluzba != null && (!ZboziData.TypZbozi.SLUZBA.equals(sluzba.getTypZbozi())
                                && !ZboziData.TypZbozi.SLOZENA_SLUZBA.equals(sluzba.getTypZbozi()))) {
            throw new IllegalArgumentException("Only SLUZBA and SLOZENA_SLUZBA is allowed for sluzba data."
                    + " Type " + sluzba.getTypZbozi() + " not allowed!");
        }
        this.sluzba = sluzba;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VoucherData other = (VoucherData) obj;
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
