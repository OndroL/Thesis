/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* PokladnaData.java
* Created on: 26.8.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.template.data.uctenka;

import java.io.Serializable;

/**
* PokladnaData
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class PokladnaData implements Serializable {

    private static final long serialVersionUID = 1L;
        
    private String id;
    private String bkp;
    private String fik;
    private String typPlatby;
    private String provozovnaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getBkp() {
        return bkp;
    }

    public void setBkp(String bkp) {
        this.bkp = bkp;
    }

    public String getFik() {
        return fik;
    }

    public void setFik(String fik) {
        this.fik = fik;
    }

    public String getTypPlatby() {
        return typPlatby;
    }

    public void setTypPlatby(String typPlatby) {
        this.typPlatby = typPlatby;
    }

    public String getProvozovnaId() {
        return provozovnaId;
    }

    public void setProvozovnaId(String provozovnaId) {
        this.provozovnaId = provozovnaId;
    }
}