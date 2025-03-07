/*
 * NastaveniInformaciCentra.java
 *
 * Created on 24. listopad 2005, 16:25
 *
 */
package cz.inspire.enterprise.module.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author rosta
 */
public class NastaveniInformaciCentra implements Serializable {
    
    private static final long serialVersionUID = -6790057273183197131L;
    
    private String nazevCenta;
    private String ICO;
    private String DIC;
    private String icDph;
    private String ulice;
    private String uliceProvozovna;
    private String obec;
    private String obecProvozovna;
    private String PSC;
    private String PSCProvozovna;
    private String telefon;
    private String email;
    private String web;
    private String banka;
    private String ucet;
    private String facebook;
    private String facebookTitle;
    private byte[] logo;
    private String instagram;
    private String youtube;
    
    /** Creates a new instance of NastaveniInformaciCentra */
    public NastaveniInformaciCentra() {
    }
    
    public String getNazevCenta() {
        return nazevCenta;
    }
    
    public void setNazevCenta(String nazevCenta) {
        this.nazevCenta = nazevCenta;
    }
    
    public String getICO() {
        return ICO;
    }
    
    public void setICO(String ICO) {
        this.ICO = ICO;
    }
    
    public String getDIC() {
        return DIC;
    }
    
    public void setDIC(String DIC) {
        this.DIC = DIC;
    }
    
    public String getIcDph() {
        return icDph;
    }

    public void setIcDph(String icDph) {
        this.icDph = icDph;
    }
    
    public String getUlice() {
        return ulice;
    }
    
    public void setUlice(String ulice) {
        this.ulice = ulice;
    }
    
    public String getUliceProvozovna() {
        return uliceProvozovna;
    }

    public void setUliceProvozovna(String uliceProvozovna) {
        this.uliceProvozovna = uliceProvozovna;
    }   
    
    public String getObec() {
        return obec;
    }
    
    public void setObec(String obec) {
        this.obec = obec;
    }

    public String getObecProvozovna() {
        return obecProvozovna;
    }

    public void setObecProvozovna(String obecProvozovna) {
        this.obecProvozovna = obecProvozovna;
    }

    public String getPSCProvozovna() {
        return PSCProvozovna;
    }

    public void setPSCProvozovna(String PSCProvozovna) {
        this.PSCProvozovna = PSCProvozovna;
    }   
    
    public String getPSC() {
        return PSC;
    }
    
    public void setPSC(String PSC) {
        this.PSC = PSC;
    }
    
    public String getTelefon() {
        return telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getWeb() {
        return web;
    }
    
    public void setWeb(String web) {
        this.web = web;
    }
    
    public String getBanka() {
        return banka;
    }
    
    public void setBanka(String banka) {
        this.banka = banka;
    }
    
    public String getUcet() {
        return ucet;
    }
    
    public void setUcet(String ucet) {
        this.ucet = ucet;
    }
    
    public byte[] getLogo() {
        return logo == null ? null : (byte[]) logo.clone();
    }
    
    public void setLogo(byte[] logo) {
        this.logo = logo == null ? null : (byte[]) logo.clone();
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFacebookTitle() {
        return facebookTitle;
    }

    public void setFacebookTitle(String facebookTitle) {
        this.facebookTitle = facebookTitle;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.nazevCenta != null ? this.nazevCenta.hashCode() : 0);
        hash = 89 * hash + (this.ICO != null ? this.ICO.hashCode() : 0);
        hash = 89 * hash + (this.DIC != null ? this.DIC.hashCode() : 0);
        hash = 89 * hash + (this.icDph != null ? this.icDph.hashCode() : 0);
        hash = 89 * hash + (this.ulice != null ? this.ulice.hashCode() : 0);
        hash = 89 * hash + (this.uliceProvozovna != null ? this.uliceProvozovna.hashCode() : 0);
        hash = 89 * hash + (this.obec != null ? this.obec.hashCode() : 0);
        hash = 89 * hash + (this.obecProvozovna != null ? this.obecProvozovna.hashCode() : 0);
        hash = 89 * hash + (this.PSC != null ? this.PSC.hashCode() : 0);
        hash = 89 * hash + (this.PSCProvozovna != null ? this.PSCProvozovna.hashCode() : 0);
        hash = 89 * hash + (this.telefon != null ? this.telefon.hashCode() : 0);
        hash = 89 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 89 * hash + (this.web != null ? this.web.hashCode() : 0);
        hash = 89 * hash + (this.banka != null ? this.banka.hashCode() : 0);
        hash = 89 * hash + (this.ucet != null ? this.ucet.hashCode() : 0);
        hash = 89 * hash + (this.facebook != null ? this.facebook.hashCode() : 0);
        hash = 89 * hash + (this.facebookTitle != null ? this.facebookTitle.hashCode() : 0);
        hash = 89 * hash + Arrays.hashCode(this.logo);
        hash = 89 * hash + (this.instagram != null ? this.instagram.hashCode() : 0);
        hash = 89 * hash + (this.youtube != null ? this.youtube.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NastaveniInformaciCentra other = (NastaveniInformaciCentra) obj;
        if ((this.nazevCenta == null) ? (other.nazevCenta != null) : !this.nazevCenta.equals(other.nazevCenta)) {
            return false;
        }
        if ((this.ICO == null) ? (other.ICO != null) : !this.ICO.equals(other.ICO)) {
            return false;
        }
        if ((this.DIC == null) ? (other.DIC != null) : !this.DIC.equals(other.DIC)) {
            return false;
        }
        if ((this.icDph == null) ? (other.icDph != null) : !this.icDph.equals(other.icDph)) {
            return false;
        }
        if ((this.ulice == null) ? (other.ulice != null) : !this.ulice.equals(other.ulice)) {
            return false;
        }
        if ((this.uliceProvozovna == null) ? (other.uliceProvozovna != null) : !this.uliceProvozovna.equals(other.uliceProvozovna)) {
            return false;
        }
        if ((this.obec == null) ? (other.obec != null) : !this.obec.equals(other.obec)) {
            return false;
        }
        if ((this.obecProvozovna == null) ? (other.obecProvozovna != null) : !this.obecProvozovna.equals(other.obecProvozovna)) {
            return false;
        }
        if ((this.PSC == null) ? (other.PSC != null) : !this.PSC.equals(other.PSC)) {
            return false;
        }
        if ((this.PSCProvozovna == null) ? (other.PSCProvozovna != null) : !this.PSCProvozovna.equals(other.PSCProvozovna)) {
            return false;
        }
        if ((this.telefon == null) ? (other.telefon != null) : !this.telefon.equals(other.telefon)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.web == null) ? (other.web != null) : !this.web.equals(other.web)) {
            return false;
        }
        if ((this.banka == null) ? (other.banka != null) : !this.banka.equals(other.banka)) {
            return false;
        }
        if ((this.ucet == null) ? (other.ucet != null) : !this.ucet.equals(other.ucet)) {
            return false;
        }
        if ((this.facebook == null) ? (other.facebook != null) : !this.facebook.equals(other.facebook)) {
            return false;
        }
        if ((this.facebookTitle == null) ? (other.facebookTitle != null) : !this.facebookTitle.equals(other.facebookTitle)) {
            return false;
        }
        if ((this.instagram == null) ? (other.instagram != null) : !this.instagram.equals(other.instagram)) {
            return false;
        }
        if ((this.youtube == null) ? (other.youtube != null) : !this.youtube.equals(other.youtube)) {
            return false;
        }
        if (!Arrays.equals(this.logo, other.logo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NastaveniInformaciCentra{" + "nazevCenta=" + nazevCenta + ", ICO=" + ICO + ", DIC=" + DIC +
                ", icDph=" + icDph + ", ulice=" + ulice + ", uliceProvozovna=" + uliceProvozovna + ", obec=" + obec +
                ", obecProvozovna=" + obecProvozovna + ", PSC=" + PSC + ", PSCProvozovna=" + PSCProvozovna +
                ", telefon=" + telefon + ", email=" + email + ", web=" + web +
                ", banka=" + banka + ", ucet=" + ucet + ", facebook=" + facebook + ", facebookTitle=" + facebookTitle +
                ", logo=" + logo + ", instagram=" + instagram + ", youtube=" + youtube + '}';
    }    
    
}
