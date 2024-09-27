# **MeraBills**

This is an Android application designed to manage payments through various methods, including Cash,
Credit Card, UPI, and Net Banking. The app allows users to add, view, and delete payment methods,
and it saves this data to internal storage. Payments are displayed using dynamically created chips
for better user experience.

## **Features**

* **Add Payments:** Users can add payments using different methods (Cash, Credit Card, UPI, Net
  Banking).

* **View Payments:** Payments are displayed as chips in the UI, showing the payment method and amount.

* **Delete Payments:** Users can remove specific payments by deleting the corresponding chip.

* **Persist Payments:** Payments are saved to internal storage in JSON format (payments.txt), so they can be restored after the app is closed.

* **Validation:** Payment details are validated before being added (e.g., ensuring that all required
  fields for Credit Card or UPI are filled out).

* **No Duplicate Payment Methods:** Once a payment method (e.g., Cash) is added, it is removed from the available options in the spinner dropdown to prevent duplicates.

## **Technology Stack**

**Language**: Java

**Architecture**: MVVM (Model-View-ViewModel)

**Data** Persistence: JSON file stored in internal storage

**UI Components**: Chips, Spinners, Custom Dialogs

**Libraries**: ViewModel and LiveData for managing UI-related data in a lifecycle-conscious way

Gson for serializing and deserializing payments to JSON

## **Project Structure**

~~~ merabills/
├── model/ # Model classes (e.g., Payment, PaymentMethod)
├── utils/ # Utility classes (e.g., Payment file handling)
├── viewmodel/ # ViewModel classes for managing UI state and data
├── view/ # DialogFragment classes
├── res/ # Resources (layouts, strings, etc.)
└── MainActivity.java # Main entry point of the app
~~~

## **Core File**

**MainActivity.java**: Main screen where payments are listed, added, and deleted.

**PaymentViewModel.java**: ViewModel for managing payments data and validation logic.

**Payment.java**: Model class representing a payment (with amount and payment method details).

**PaymentMethod.java**: Enum class listing the available payment methods (Cash, Credit Card, UPI,
Net Banking).

**AddPaymentDialog.java**: Custom dialog for adding new payments, with validation for each payment
method's required fields.

**payments.txt**: JSON file where payment data is saved and restored from.

## **Steps**

1. Clone the repository.
   ~~~
    git clone https://github.com/pushpender311/MeraBills.git
    ~~~
2. Open the project in Android Studio.
3. Sync the gradle files.
4. Build and run the app on an emulator or physical device.

## **Usage**

1. **Adding the Payments:**
    * Open the app and click the "Add Payment" button.

    * A dialog will appear to input payment details, including the method (Cash, Credit Card, UPI, or Net Banking).

    * Fill in the details, and click "Add Payment". The payment method will appear as a chip in the main UI. And a toast will be displayed for the payment.

2. **Deleting the Payments:**
    * To remove a payment, click the close (x) icon on the respective chip.

3. **Save and Load Payments:**
    * When the save button on MainActivity clicked the payment(if there are any payment methods added) saved automatically to payments.txt in the app's internal storage in json format.

    * File will be saved at the location: /data/data/com.example.merabills/files/payments.txt

    * When the app is opened, the payments are loaded from the file and displayed as chips in the UI.

    * To clear all the payments, Clear the chips and save the empty list of payments(Which simply deletes the file in internal storage).

## **Future Improvements**

**Database Integration:** Moving from internal storage to a Room database for better data management
and query support.
