/**
* Clubspire, (c) Inspire CZ 2004-2013
*
* CenterInfoData.java
* Created on: 25.9.2013
* Author: Tomáš Kramec
*
*/
package cz.inspire.enterprise.module.template.data;
 
import java.io.Serializable;

/**
* 
* @version 1.0
* @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
*/
public class CenterInfoData implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String centerName;
    
    private String email;
    
    private String phone;
    
    private String mobile;
    
    private String web;
    
    private String facebook;
    
    private String facebookTitle;
    
    private String street;
    
    private String city;
    
    private String zip;
    
    private String bankAccount;
    
    private String ico;
    
    private String dic;
    
    private String establishmentStreet;
    
    private String establishmentCity;
    
    private String establishmentZip;

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getDic() {
        return dic;
    }

    public void setDic(String dic) {
        this.dic = dic;
    }

    public String getFacebookTitle() {
        return facebookTitle;
    }

    public void setFacebookTitle(String facebookTitle) {
        this.facebookTitle = facebookTitle;
    }

    public String getEstablishmentStreet() {
        return establishmentStreet;
    }

    public void setEstablishmentStreet(String establishmentStreet) {
        this.establishmentStreet = establishmentStreet;
    }

    public String getEstablishmentCity() {
        return establishmentCity;
    }

    public void setEstablishmentCity(String establishmentCity) {
        this.establishmentCity = establishmentCity;
    }

    public String getEstablishmentZip() {
        return establishmentZip;
    }

    public void setEstablishmentZip(String establishmentZip) {
        this.establishmentZip = establishmentZip;
    }
    
    

}