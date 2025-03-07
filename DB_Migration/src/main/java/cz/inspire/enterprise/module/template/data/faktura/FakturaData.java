package cz.inspire.enterprise.module.template.data.faktura;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Marek
 */
public class FakturaData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String cisloPlatby;
    private Date datumVystaveniFaktury;
    private Date datumSplatnosti;
    private Date datumZdanPln;
    private String konstSymbol;
    private String varSymbol;
    private String poznamka;
    
    private Locale locale;
    private String menaSymbol;
    private BigDecimal cenaBezDph;
    private BigDecimal dphSuma;
    private BigDecimal cenaSDph;
    private BigDecimal cenaCelkem;
    private BigDecimal zaloha;
    
    private List<PolozkaData> polozky;

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getCisloPlatby() {
        return cisloPlatby;
    }

    public void setCisloPlatby(String cisloPlatby) {
        this.cisloPlatby = cisloPlatby;
    }

    public Date getDatumSplatnosti() {
        return datumSplatnosti == null ? null : (Date) datumSplatnosti.clone();
    }

    public void setDatumSplatnosti(Date datumSplatnosti) {
        this.datumSplatnosti = datumSplatnosti == null ? null : (Date) datumSplatnosti.clone();
    }

    public Date getDatumVystaveniFaktury() {
        return datumVystaveniFaktury == null ? null : (Date) datumVystaveniFaktury.clone();
    }

    public void setDatumVystaveniFaktury(Date datumVystaveniFaktury) {
        this.datumVystaveniFaktury = datumVystaveniFaktury == null ? null : (Date) datumVystaveniFaktury.clone();
    }

    public Date getDatumZdanPln() {
        return datumZdanPln == null ? null : (Date) datumZdanPln.clone();
    }

    public void setDatumZdanPln(Date datumZdanPln) {
        this.datumZdanPln = datumZdanPln == null ? null : (Date) datumZdanPln.clone();
    }

    public String getKonstSymbol() {
        return konstSymbol;
    }

    public void setKonstSymbol(String konstSymbol) {
        this.konstSymbol = konstSymbol;
    }

    public String getVarSymbol() {
        return varSymbol;
    }

    public void setVarSymbol(String varSymbol) {
        this.varSymbol = varSymbol;
    }

    public List<PolozkaData> getPolozky() {
        return polozky;
    }

    public void setPolozky(List<PolozkaData> polozky) {
        this.polozky = polozky;
    }

    public BigDecimal getCenaBezDph() {
        return cenaBezDph;
    }

    public void setCenaBezDph(BigDecimal cenaBezDph) {
        this.cenaBezDph = cenaBezDph;
    }

    public BigDecimal getCenaCelkem() {
        return cenaCelkem;
    }

    public void setCenaCelkem(BigDecimal cenaCelkem) {
        this.cenaCelkem = cenaCelkem;
    }

    public BigDecimal getCenaSDph() {
        return cenaSDph;
    }

    public void setCenaSDph(BigDecimal cenaSDph) {
        this.cenaSDph = cenaSDph;
    }

    public BigDecimal getDphSuma() {
        return dphSuma;
    }

    public void setDphSuma(BigDecimal dphSuma) {
        this.dphSuma = dphSuma;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getMenaSymbol() {
        return menaSymbol;
    }

    public void setMenaSymbol(String menaSymbol) {
        this.menaSymbol = menaSymbol;
    }

    public BigDecimal getZaloha() {
        return zaloha;
    }

    public void setZaloha(BigDecimal zaloha) {
        this.zaloha = zaloha;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 11 * hash + (this.cisloPlatby != null ? this.cisloPlatby.hashCode() : 0);
        hash = 11 * hash + (this.datumVystaveniFaktury != null ? this.datumVystaveniFaktury.hashCode() : 0);
        hash = 11 * hash + (this.datumSplatnosti != null ? this.datumSplatnosti.hashCode() : 0);
        hash = 11 * hash + (this.datumZdanPln != null ? this.datumZdanPln.hashCode() : 0);
        hash = 11 * hash + (this.konstSymbol != null ? this.konstSymbol.hashCode() : 0);
        hash = 11 * hash + (this.varSymbol != null ? this.varSymbol.hashCode() : 0);
        hash = 11 * hash + (this.poznamka != null ? this.poznamka.hashCode() : 0);
        hash = 11 * hash + (this.locale != null ? this.locale.hashCode() : 0);
        hash = 11 * hash + (this.menaSymbol != null ? this.menaSymbol.hashCode() : 0);
        hash = 11 * hash + (this.cenaBezDph != null ? this.cenaBezDph.hashCode() : 0);
        hash = 11 * hash + (this.dphSuma != null ? this.dphSuma.hashCode() : 0);
        hash = 11 * hash + (this.cenaSDph != null ? this.cenaSDph.hashCode() : 0);
        hash = 11 * hash + (this.cenaCelkem != null ? this.cenaCelkem.hashCode() : 0);
        hash = 11 * hash + (this.zaloha != null ? this.zaloha.hashCode() : 0);
        hash = 11 * hash + (this.polozky != null ? this.polozky.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FakturaData other = (FakturaData) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.cisloPlatby == null) ? (other.cisloPlatby != null) : !this.cisloPlatby.equals(other.cisloPlatby)) {
            return false;
        }
        if (this.datumVystaveniFaktury != other.datumVystaveniFaktury && (this.datumVystaveniFaktury == null || !this.datumVystaveniFaktury.equals(other.datumVystaveniFaktury))) {
            return false;
        }
        if (this.datumSplatnosti != other.datumSplatnosti && (this.datumSplatnosti == null || !this.datumSplatnosti.equals(other.datumSplatnosti))) {
            return false;
        }
        if (this.datumZdanPln != other.datumZdanPln && (this.datumZdanPln == null || !this.datumZdanPln.equals(other.datumZdanPln))) {
            return false;
        }
        if ((this.konstSymbol == null) ? (other.konstSymbol != null) : !this.konstSymbol.equals(other.konstSymbol)) {
            return false;
        }
        if ((this.varSymbol == null) ? (other.varSymbol != null) : !this.varSymbol.equals(other.varSymbol)) {
            return false;
        }
        if ((this.poznamka == null) ? (other.poznamka != null) : !this.poznamka.equals(other.poznamka)) {
            return false;
        }
        if (this.locale != other.locale && (this.locale == null || !this.locale.equals(other.locale))) {
            return false;
        }
        if ((this.menaSymbol == null) ? (other.menaSymbol != null) : !this.menaSymbol.equals(other.menaSymbol)) {
            return false;
        }
        if (this.cenaBezDph != other.cenaBezDph && (this.cenaBezDph == null || !this.cenaBezDph.equals(other.cenaBezDph))) {
            return false;
        }
        if (this.dphSuma != other.dphSuma && (this.dphSuma == null || !this.dphSuma.equals(other.dphSuma))) {
            return false;
        }
        if (this.cenaSDph != other.cenaSDph && (this.cenaSDph == null || !this.cenaSDph.equals(other.cenaSDph))) {
            return false;
        }
        if (this.cenaCelkem != other.cenaCelkem && (this.cenaCelkem == null || !this.cenaCelkem.equals(other.cenaCelkem))) {
            return false;
        }
        if (this.zaloha != other.zaloha && (this.zaloha == null || !this.zaloha.equals(other.zaloha))) {
            return false;
        }
        if (this.polozky != other.polozky && (this.polozky == null || !this.polozky.equals(other.polozky))) {
            return false;
        }
        return true;
    }
        
}