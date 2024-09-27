package com.example.merabills.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.merabills.model.Payment;
import com.example.merabills.model.PaymentMethod;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

public class PaymentViewModel extends ViewModel {


    public MutableLiveData<ArrayList<Payment>> payments = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<PaymentMethod>> availablePaymentMethods=new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MutableLiveData<Double> amount = new MutableLiveData<>();
    public MutableLiveData<PaymentMethod> selectedPaymentMethod = new MutableLiveData<>();
    public MutableLiveData<String> bankName = new MutableLiveData<>();
    public MutableLiveData<String> ifscCode = new MutableLiveData<>();
    public MutableLiveData<String> cardNumber = new MutableLiveData<>();
    public MutableLiveData<String> expiryDate = new MutableLiveData<>();
    public MutableLiveData<String> cvv = new MutableLiveData<>();
    public MutableLiveData<String> upiId = new MutableLiveData<>();


    public PaymentViewModel() {
        updateAvailablePaymentMethods();
    }

    // Update the available payment methods based on already added methods
    public void updateAvailablePaymentMethods() {
        ArrayList<Payment> currentPayments = payments.getValue();
        if (currentPayments != null) {
            ArrayList<PaymentMethod> addedMethods = new ArrayList<>();
            for (Payment payment : currentPayments) {
                addedMethods.add(payment.getPaymentMethod());
            }

            // Get the full set of payment methods and remove the ones that have already been added
            ArrayList<PaymentMethod> availableMethods = new ArrayList<>(EnumSet.allOf(PaymentMethod.class));
            availableMethods.removeAll(addedMethods);

            availablePaymentMethods.postValue(availableMethods);
        }
    }


    private boolean isPaymentMethodAdded(PaymentMethod paymentMethod) {
        ArrayList<Payment> existingPayments = payments.getValue();
        if (existingPayments != null) {
            for (Payment payment : existingPayments) {
                if (payment.getPaymentMethod().equals(paymentMethod)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean validateAndAddPayment() {
        if (selectedPaymentMethod.getValue() == null) {
            errorMessage.setValue("Please select payment method");
            return false;
        }

        PaymentMethod paymentMethod = selectedPaymentMethod.getValue();

        if (isPaymentMethodAdded(paymentMethod)) {
            errorMessage.setValue("Payment method already added");
            return false;
        }

        Payment payment = null;

        if (amount.getValue() == null || amount.getValue() <= 0) {
            errorMessage.setValue("Please enter valid amount");
            return false;
        }

        switch (paymentMethod) {
            case CASH:
                payment = new Payment(amount.getValue(), paymentMethod);
                break;
            case NET_BANKING:
                if (bankName.getValue() == null || bankName.getValue().isEmpty()) {
                    errorMessage.setValue("Please enter bank name");
                    return false;
                }
                if (ifscCode.getValue() == null || ifscCode.getValue().isEmpty()) {
                    errorMessage.setValue("Please enter IFSC code");
                    return false;
                }
                payment = new Payment(amount.getValue(), paymentMethod, bankName.getValue(), ifscCode.getValue());
                break;
            case CREDIT_CARD:
                if (cardNumber.getValue() == null || cardNumber.getValue().isEmpty()) {
                    errorMessage.setValue("Please enter card number");
                    return false;
                }
                if (expiryDate.getValue() == null || expiryDate.getValue().isEmpty()) {
                    errorMessage.setValue("Please enter expiry date");
                    return false;
                }
                if (cvv.getValue() == null || cvv.getValue().isEmpty()) {
                    errorMessage.setValue("Please enter CVV");
                    return false;
                }
                payment = new Payment(amount.getValue(), paymentMethod, cardNumber.getValue(), expiryDate.getValue(), cvv.getValue());
                break;
            case UPI:
                if (upiId.getValue() == null || upiId.getValue().isEmpty()) {
                    errorMessage.setValue("Please enter UPI ID");
                    return false;
                }
                payment = new Payment(amount.getValue(), paymentMethod, upiId.getValue());
                break;
        }

        ArrayList<Payment> updatedPayments = new ArrayList<>(payments.getValue());
        updatedPayments.add(payment);

        payments.setValue(updatedPayments);

        Log.d("Payment", new Gson().toJson(payment));
        errorMessage.setValue("Payment method added successfully");
        updateAvailablePaymentMethods();

        return true;
    }

    public void removePayment(Payment payment){
        ArrayList<Payment> currentPayments = payments.getValue();
        if (currentPayments != null) {
            currentPayments.remove(payment);
            payments.setValue(currentPayments);

            updateAvailablePaymentMethods();
        }
    }


}
