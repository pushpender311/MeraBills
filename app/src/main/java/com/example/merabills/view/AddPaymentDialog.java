package com.example.merabills.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.merabills.R;
import com.example.merabills.databinding.CustomAddPaymentDialogBinding;
import com.example.merabills.model.PaymentMethod;
import com.example.merabills.viewmodel.PaymentViewModel;

import java.util.ArrayList;

public class AddPaymentDialog extends DialogFragment {

    private CustomAddPaymentDialogBinding binding;
    private PaymentViewModel paymentViewModel;
    private ArrayAdapter<String> paymentMethodAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = CustomAddPaymentDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(requireContext(), R.style.Dialog_No_Border);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());
        setupDialogWindow(dialog);
        setupViewModel();
        initSpinner();
        clickListeners(dialog);

        return dialog;
    }

    private void clickListeners(Dialog dialog) {
        binding.buttonAddPayment.setOnClickListener(v -> {

            updateInputValues();

            if (paymentViewModel.validateAndAddPayment()) {
                clearInputs();
                dialog.dismiss();
            }
        });

        binding.textCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void initSpinner() {
        paymentMethodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(paymentMethodAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPaymentMethod = (String) adapterView.getItemAtPosition(i);
                paymentViewModel.selectedPaymentMethod.setValue(stringToMethod(selectedPaymentMethod));

                updatePaymentMethod(stringToMethod(selectedPaymentMethod));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void updatePaymentMethod(PaymentMethod method) {
        switch (method) {
            case CASH:
                binding.linearCardDetails.setVisibility(View.GONE);
                binding.linearBankDetails.setVisibility(View.GONE);
                binding.editTextUpiId.setVisibility(View.GONE);
                break;
            case CREDIT_CARD:
                binding.linearCardDetails.setVisibility(View.VISIBLE);
                binding.linearBankDetails.setVisibility(View.GONE);
                binding.editTextUpiId.setVisibility(View.GONE);
                break;
            case NET_BANKING:
                binding.linearCardDetails.setVisibility(View.GONE);
                binding.linearBankDetails.setVisibility(View.VISIBLE);
                binding.editTextUpiId.setVisibility(View.GONE);
                break;
            case UPI:
                binding.linearCardDetails.setVisibility(View.GONE);
                binding.linearBankDetails.setVisibility(View.GONE);
                binding.editTextUpiId.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupViewModel() {
        paymentViewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);

        paymentViewModel.availablePaymentMethods.observe(this, availableMethods -> {
            ArrayList<String> methodNames = new ArrayList<>();
            for (PaymentMethod method : availableMethods) {
                methodNames.add(methodToString(method));
            }
            updateSpinner(methodNames);
        });

        paymentViewModel.errorMessage.observe(this, message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSpinner(ArrayList<String> methodNames) {
        if (paymentMethodAdapter == null) {
            paymentMethodAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, methodNames);
            paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner.setAdapter(paymentMethodAdapter);
        } else {
            paymentMethodAdapter.clear();
            paymentMethodAdapter.addAll(methodNames);
            paymentMethodAdapter.notifyDataSetChanged();
        }
    }

    private String methodToString(PaymentMethod method) {
        switch (method) {
            case CASH:
                return "Cash";
            case CREDIT_CARD:
                return "Credit Card";
            case NET_BANKING:
                return "Net Banking";
            case UPI:
                return "UPI";
            default:
                return "";
        }
    }

    private PaymentMethod stringToMethod(String method) {
        switch (method) {
            case "Cash":
                return PaymentMethod.CASH;
            case "Credit Card":
                return PaymentMethod.CREDIT_CARD;
            case "Net Banking":
                return PaymentMethod.NET_BANKING;
            case "UPI":
                return PaymentMethod.UPI;
            default:
                return null;
        }
    }


    private void setupDialogWindow(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    private void updateInputValues() {
        /*Amount*/
        String amountText = binding.editTextAmount.getText().toString();
        if (!amountText.isEmpty()) {
            paymentViewModel.amount.setValue(Double.parseDouble(amountText));
        }

        /*Banking Details*/
        paymentViewModel.bankName.setValue(binding.editTextBankName.getText().toString());
        paymentViewModel.ifscCode.setValue(binding.editTextIfscCode.getText().toString());

        /*Credit Card Details*/
        paymentViewModel.cardNumber.setValue(binding.editTextCardNumber.getText().toString());
        paymentViewModel.expiryDate.setValue(binding.editTextExpireDate.getText().toString());
        paymentViewModel.cvv.setValue(binding.editTextCvv.getText().toString());

        /*UPI Details*/
        paymentViewModel.upiId.setValue(binding.editTextUpiId.getText().toString());
    }

    private void clearInputs() {
        // Reset ViewModel properties
        paymentViewModel.amount.setValue(null);
        paymentViewModel.selectedPaymentMethod.setValue(null);
        paymentViewModel.bankName.setValue(null);
        paymentViewModel.ifscCode.setValue(null);
        paymentViewModel.cardNumber.setValue(null);
        paymentViewModel.expiryDate.setValue(null);
        paymentViewModel.cvv.setValue(null);
        paymentViewModel.upiId.setValue(null);
        paymentViewModel.selectedPaymentMethod.setValue(null);
        paymentViewModel.errorMessage.setValue(null);
    }
}
