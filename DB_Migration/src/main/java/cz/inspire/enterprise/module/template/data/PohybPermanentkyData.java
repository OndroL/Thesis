package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;
/**
 *
 * @author Marek
 */
public class PohybPermanentkyData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String cas;
    private String jednotek;

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getJednotek() {
        return jednotek;
    }

    public void setJednotek(String jednotek) {
        this.jednotek = jednotek;
    }
    
}
