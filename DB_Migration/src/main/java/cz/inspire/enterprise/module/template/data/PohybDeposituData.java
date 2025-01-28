package cz.inspire.enterprise.module.template.data;

import java.io.Serializable;

/**
 *
 * @author Marek
 */
public class PohybDeposituData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String cas;
    private String slozeno;
    private String cerpano;

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getCerpano() {
        return cerpano;
    }

    public void setCerpano(String cerpano) {
        this.cerpano = cerpano;
    }

    public String getSlozeno() {
        return slozeno;
    }

    public void setSlozeno(String slozeno) {
        this.slozeno = slozeno;
    }
    
}
