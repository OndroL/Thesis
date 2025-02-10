/**
 * Clubspire, (c) Inspire CZ 2004-2010
 *
 * NastaveniInformaciCentraNew.java
 * Created on: 6.1.2011
 * Author: Radek Matula
 *
 */
package cz.inspire.enterprise.module.common;

/**
 *
 * @version 1.0
 * @author <a href="mailto:radek.matula@inspire.cz">Radek Matula</a>
 */
public class NastaveniInformaciCentraNew extends NastaveniInformaciCentra {
    private String nazevCentra;

    public String getNazevCentra() {
        return nazevCentra;
    }

    public void setNazevCentra(String nazevCentra) {
        this.nazevCentra = nazevCentra;
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                nazevCentra;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NastaveniInformaciCentraNew other = (NastaveniInformaciCentraNew) obj;
        if ((this.nazevCentra == null) ? (other.nazevCentra != null) : !this.nazevCentra.equals(other.nazevCentra)) {
            return false;
        }
        return super.equals(obj);
    }

    
    
    @Override
    public int hashCode() {
        return super.hashCode() + 37 * nazevCentra.hashCode();
    }
}
