package com.example.merabills.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.merabills.model.Payment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileUtils {
    public static void savePaymentsToFile(Context context, ArrayList<Payment> payments) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonPayment = gson.toJson(payments);

        String filename = "payments.txt";
        File file = new File(context.getFilesDir(), filename);
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(jsonPayment.getBytes());
            outputStream.flush();
            Toast.makeText(context, "Payments saved to " + file.getAbsolutePath() + " successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deletePaymentFile(Context context) {
        File file = new File(context.getFilesDir(), "payments.txt");

        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(context, "Payments file deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete payments file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No payments file found", Toast.LENGTH_SHORT).show();
        }
    }


    public static ArrayList<Payment> getPaymentsFromFile(Context context) {
        File file = new File(context.getFilesDir(), "payments.txt");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type paymentListType = new TypeToken<ArrayList<Payment>>() {
            }.getType();
            return gson.fromJson(streamReader, paymentListType);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred. Please try again", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }
}
