/**
* Clubspire, (c) Inspire CZ 2004-2022
*
* ZakaznikData.java
* Created on: 26.8.2022
* Author: Lukas Ondrak
*
*/
package cz.inspire.enterprise.module.template.data.uctenka;

import java.io.Serializable;

/**
* ZakaznikData
*
* @author <a href="lukas.ondrak@inspire.cz">Lukas Ondrak</a>
*/
public class ZakaznikData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jmenoPrijmeni;
    private String depozit;

    public String getJmenoPrijmeni() {
        return jmenoPrijmeni;
    }

    public void setJmenoPrijmeni(String jmenoPrijmeni) {
        this.jmenoPrijmeni = jmenoPrijmeni;
    }

    public String getDepozit() {
        return depozit;
    }

    public void setDepozit(String depozit) {
        this.depozit = depozit;
    }
}