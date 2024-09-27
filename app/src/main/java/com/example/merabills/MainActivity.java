package com.example.merabills;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.merabills.databinding.ActivityMainBinding;
import com.example.merabills.model.Payment;
import com.example.merabills.utils.FileUtils;
import com.example.merabills.view.AddPaymentDialog;
import com.example.merabills.viewmodel.PaymentViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private PaymentViewModel paymentViewModel;
    private ArrayList<Payment> payments;
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        payments = new ArrayList<>();
        setupObservers();

        loadSavedPayments();

        binding.textAddPayments.setOnClickListener(v -> {
            AddPaymentDialog dialog = new AddPaymentDialog();
            dialog.show(getSupportFragmentManager(), "AddPaymentDialog");
        });

        binding.textSavePayment.setOnClickListener(v -> {
            ArrayList<Payment> jsonPayments = paymentViewModel.payments.getValue();
            if (jsonPayments != null && !jsonPayments.isEmpty()) {
                FileUtils.savePaymentsToFile(this, jsonPayments);
            } else if (jsonPayments.isEmpty()) {
                FileUtils.deletePaymentFile(this);
            } else {
                Toast.makeText(this, "No payments to save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupObservers() {
        paymentViewModel.payments.observe(this, payments -> {
            this.payments.clear();
            this.payments.addAll(payments);
            inflateChips();
        });
    }

    private void loadSavedPayments() {
        ArrayList<Payment> savedPayments = FileUtils.getPaymentsFromFile(this);

        if (savedPayments != null && !savedPayments.isEmpty()) {
            payments.addAll(savedPayments);
            paymentViewModel.payments.setValue(new ArrayList<>(payments));
            paymentViewModel.updateAvailablePaymentMethods();
        }
    }

    private void inflateChips() {
        binding.linearChips.removeAllViews(); // Clear previous views
        totalAmount = 0;
        for (Payment payment : payments) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_chips, binding.linearChips, false);
            Chip chip = view.findViewById(R.id.chip);
            String chipText = payment.getPaymentMethod().toString().toLowerCase().replace("_", " ") + " = INR " + payment.getAmount();
            chip.setText(chipText);
            totalAmount += payment.getAmount();
            chip.setOnCloseIconClickListener(v -> {
                ArrayList<Payment> updatedPayments = new ArrayList<>(payments);
                updatedPayments.remove(payment);
                paymentViewModel.payments.setValue(updatedPayments);
                paymentViewModel.removePayment(payment);
                totalAmount -= payment.getAmount();
                binding.textTotal.setText("₹" + totalAmount);

                payments = updatedPayments;
                inflateChips();
            });

            binding.linearChips.addView(view);
        }

        Log.v("Payments", totalAmount + "");
        binding.textTotal.setText("₹" + totalAmount);
    }

}