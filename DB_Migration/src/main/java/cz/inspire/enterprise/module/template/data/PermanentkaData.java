package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Marek
 */
public class PermanentkaData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nazev;
    private String slozeniPermanentky;
    private String koncovyStav;
    private String platnostDo;
    private List<PohybPermanentkyData> pohybyList;

    public String getKoncovyStav() {
        return koncovyStav;
    }

    public void setKoncovyStav(String koncovyStav) {
        this.koncovyStav = koncovyStav;
    }

    public String getPlatnostDo() {
        return platnostDo;
    }

    public void setPlatnostDo(String platnostDo) {
        this.platnostDo = platnostDo;
    }

    public String getSlozeniPermanentky() {
        return slozeniPermanentky;
    }

    public void setSlozeniPermanentky(String slozeniPermanentky) {
        this.slozeniPermanentky = slozeniPermanentky;
    }

    public List<PohybPermanentkyData> getPohybyList() {
        return pohybyList;
    }

    public void setPohybyList(List<PohybPermanentkyData> pohybyList) {
        this.pohybyList = pohybyList;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }
    
}
