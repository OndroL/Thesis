/**
 * SazbaStorna.java
 *
 * Created on 18. listopad 2005, 11:19
 */
package cz.inspire.sport.utils;

import java.io.Serializable;

/**
 *
 * @author rosta
 */
public class SazbaStorna implements Serializable {
    private static final long serialVersionUID = 5715822563537788278l;
    
    private int dobaPredZacatkemHry;
    private String typDoby;
    private double pocetJednotek;
    private boolean procentualne; // jinak fixni cena
    
    /** Creates a new instance of SazbaStorna */
    public SazbaStorna() {
    }

    public SazbaStorna(int dobaPredZacatkemHry, String typDoby, double pocetJednotek, boolean procentualne) {
        this.procentualne = procentualne;
        this.pocetJednotek = pocetJednotek;
        this.dobaPredZacatkemHry = dobaPredZacatkemHry;
        this.typDoby = typDoby;
    }
    
    public int getDobaPredZacatkemHry() {
        return dobaPredZacatkemHry;
    }

    public void setDobaPredZacatkemHry(int dobaPredZacatkemHry) {
        this.dobaPredZacatkemHry = dobaPredZacatkemHry;
    }

    public double getPocetJednotek() {
        return pocetJednotek;
    }

    public void setPocetJednotek(double pocetJednotek) {
        this.pocetJednotek = pocetJednotek;
    }

    public boolean isProcentualne() {
        return procentualne;
    }

    public void setProcentualne(boolean procentualne) {
        this.procentualne = procentualne;
    }

    public String getTypDoby() {
        return typDoby;
    }

    public void setTypDoby(String typDoby) {
        this.typDoby = typDoby;
    }
    

    @Override
    public String toString() {
        return "SazbaStorna{" + "dobaPredZacatkemHry=" + dobaPredZacatkemHry + ", typDoby=" + typDoby + ", pocetJednotek=" + pocetJednotek + ", procentualne=" + procentualne + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.dobaPredZacatkemHry;
        hash = 97 * hash + (this.typDoby != null ? this.typDoby.hashCode() : 0);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.pocetJednotek) ^ (Double.doubleToLongBits(this.pocetJednotek) >>> 32));
        hash = 97 * hash + (this.procentualne ? 1 : 0);
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
        final SazbaStorna other = (SazbaStorna) obj;
        if (this.dobaPredZacatkemHry != other.dobaPredZacatkemHry) {
            return false;
        }
        if ((this.typDoby == null) ? (other.typDoby != null) : !this.typDoby.equals(other.typDoby)) {
            return false;
        }
        if (Double.doubleToLongBits(this.pocetJednotek) != Double.doubleToLongBits(other.pocetJednotek)) {
            return false;
        }
        if (this.procentualne != other.procentualne) {
            return false;
        }
        return true;
    }
}
