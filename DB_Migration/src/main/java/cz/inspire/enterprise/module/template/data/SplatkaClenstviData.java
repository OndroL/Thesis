/**
* Clubspire, (c) Inspire CZ 2004-2013
*
* SplatkaClenstviData.java
* Created on: 18.9.2014
* Author: Tomáš Kramec
*
*/
package cz.inspire.enterprise.module.template.data;
 
import java.math.BigDecimal;
import java.util.Date;

/**
* 
*
* @author <a href="mailto:tomas.kramec@inspire.cz">Tomáš Kramec</a>
*/
public class SplatkaClenstviData {
 
    private Integer number;
    private BigDecimal amount;
    private String amountFormatted;
    private Date dueDate;
    private Date paidDate;
    private Date paymentCanceledDate;
    private Date periodFrom;
    private Date periodTo;
    private String repaymentType;
    private String customerGroup;
    private Integer daysAfterDueDate;
    private String specificSymbol;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountFormatted() {
        return amountFormatted;
    }

    public void setAmountFormatted(String amountFormatted) {
        this.amountFormatted = amountFormatted;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public Date getPaymentCanceledDate() {
        return paymentCanceledDate;
    }

    public void setPaymentCanceledDate(Date paymentCanceledDate) {
        this.paymentCanceledDate = paymentCanceledDate;
    }

    public Date getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(Date periodFrom) {
        this.periodFrom = periodFrom;
    }

    public Date getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(Date periodTo) {
        this.periodTo = periodTo;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }

    public Integer getDaysAfterDueDate() {
        return daysAfterDueDate;
    }

    public void setDaysAfterDueDate(Integer daysAfterDueDate) {
        this.daysAfterDueDate = daysAfterDueDate;
    }

    public String getSpecificSymbol() {
        return specificSymbol;
    }

    public void setSpecificSymbol(String specificSymbol) {
        this.specificSymbol = specificSymbol;
    }
    
}
