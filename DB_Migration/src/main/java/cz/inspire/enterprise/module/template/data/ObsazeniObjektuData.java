package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;
/**
 *
 * @author Marek
 */
public class ObsazeniObjektuData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nazevObjektu;
    private String zacatekDatum;
    private String zacatekCas;
    private String konecCas;
    private String cena;

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getKonecCas() {
        return konecCas;
    }

    public void setKonecCas(String konecCas) {
        this.konecCas = konecCas;
    }

    public String getNazevObjektu() {
        return nazevObjektu;
    }

    public void setNazevObjektu(String nazevObjektu) {
        this.nazevObjektu = nazevObjektu;
    }

    public String getZacatekCas() {
        return zacatekCas;
    }

    public void setZacatekCas(String zacatekCas) {
        this.zacatekCas = zacatekCas;
    }

    public String getZacatekDatum() {
        return zacatekDatum;
    }

    public void setZacatekDatum(String zacatekDatum) {
        this.zacatekDatum = zacatekDatum;
    }
    
}
