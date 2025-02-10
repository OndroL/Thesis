
package cz.inspire.enterprise.module.template.data.faktura;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 *
 * @author Marek
 */
public class PolozkaData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String nazev;
    private BigDecimal jednCena;
    private BigDecimal mnozstvi;
    private BigDecimal cenaBezDph;
    private BigDecimal dphProc;
    private BigDecimal dphCena;
    private BigDecimal cenaCelkem;
    
    
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

    public BigDecimal getDphCena() {
        return dphCena;
    }

    public void setDphCena(BigDecimal dphCena) {
        this.dphCena = dphCena;
    }

    public BigDecimal getDphProc() {
        return dphProc;
    }

    public void setDphProc(BigDecimal dphProc) {
        this.dphProc = dphProc;
    }

    public BigDecimal getJednCena() {
        return jednCena;
    }

    public void setJednCena(BigDecimal jednCena) {
        this.jednCena = jednCena;
    }

    public BigDecimal getMnozstvi() {
        return mnozstvi;
    }

    public void setMnozstvi(BigDecimal mnozstvi) {
        this.mnozstvi = mnozstvi;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PolozkaData other = (PolozkaData) obj;
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
