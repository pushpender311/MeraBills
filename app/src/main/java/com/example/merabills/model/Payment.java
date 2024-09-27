package com.example.merabills.model;

public class Payment {


    private Double amount;
    private PaymentMethod paymentMethod;
    private String bankName;
    private String bankIFSC;
    private String creditCardNumber;
    private String creditCardExpiryDate;
    private String creditCardCVV;
    private String upiId;

    public Payment(Double amount, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public Payment(Double amount, PaymentMethod paymentMethod, String bankName, String bankIFSC) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.bankName = bankName;
        this.bankIFSC = bankIFSC;
    }

    public Payment(Double amount, PaymentMethod paymentMethod, String creditCardNumber, String creditCardExpiryDate, String creditCardCVV) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiryDate = creditCardExpiryDate;
        this.creditCardCVV = creditCardCVV;
    }

    public Payment(Double amount, PaymentMethod paymentMethod, String upiId) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.upiId = upiId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankIFSC() {
        return bankIFSC;
    }

    public void setBankIFSC(String bankIFSC) {
        this.bankIFSC = bankIFSC;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardExpiryDate() {
        return creditCardExpiryDate;
    }

    public void setCreditCardExpiryDate(String creditCardExpiryDate) {
        this.creditCardExpiryDate = creditCardExpiryDate;
    }

    public String getCreditCardCVV() {
        return creditCardCVV;
    }

    public void setCreditCardCVV(String creditCardCVV) {
        this.creditCardCVV = creditCardCVV;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}
