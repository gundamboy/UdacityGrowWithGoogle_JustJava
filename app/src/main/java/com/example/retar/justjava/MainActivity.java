package com.example.retar.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;


public class MainActivity extends AppCompatActivity {

    int quantity;
    int baseCostPerCup = 5;
    TextView quantityTextView;
    CheckBox hasWhippedCream;
    CheckBox hasChocolate;
    EditText customerName;

    private static final String KEY_QUANTITY = "quantity";
    private static final String WHIPPED_TOPPING = "whippedcream";
    private static final String CHOCOLATE_TOPPING = "chocolate";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        hasWhippedCream = (CheckBox) findViewById(R.id.checkbox_whippedcream);
        hasChocolate = (CheckBox) findViewById(R.id.checkbox_chocolate);
        customerName = (EditText) findViewById(R.id.customer_name);

        // set the quantity from the xml
        quantity = Integer.parseInt(quantityTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String coffee_quantity = savedInstanceState.getString(KEY_QUANTITY);
        Boolean whippedcream = savedInstanceState.getBoolean(WHIPPED_TOPPING);
        Boolean chocolate = savedInstanceState.getBoolean(CHOCOLATE_TOPPING);
        String customer_name = savedInstanceState.getString(CUSTOMER_NAME);

        if (savedInstanceState != null) {
            quantityTextView.setText(coffee_quantity);
            hasWhippedCream.setChecked(whippedcream);
            hasChocolate.setChecked(chocolate);
            customerName.setText(customer_name);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String quantity_to_save = quantityTextView.getText().toString();
        String customer_name = customerName.getText().toString();
        Boolean whippedcream = hasWhippedCream.isChecked();
        Boolean chocolate = hasChocolate.isChecked();

        outState.putString(KEY_QUANTITY, quantity_to_save);
        outState.putBoolean(WHIPPED_TOPPING, whippedcream);
        outState.putBoolean(CHOCOLATE_TOPPING, chocolate);
        outState.putString(CUSTOMER_NAME, customer_name);
    }

    /**
     * method called when the order button is pressed
     */
    public void submitOrder(View view) {
        int price = calculatePrice();
        String name = customerName.getText().toString();
        Boolean whippedcream = hasWhippedCream.isChecked();
        Boolean chocolate = hasChocolate.isChecked();

        String orderSummary = createOrderSummary(name, price, whippedcream, chocolate);

        if (!TextUtils.isEmpty(orderSummary)) {
            String emailSubject = getString(R.string.order_summary_email_subject, name);
            String mailto[] = {"orders@justjava.com"};
            composeEmail(mailto, emailSubject, orderSummary);
        }
    }

    /**
     *
     * @param addresses array of emails to send to
     * @param subject the email subject line
     * @param orderSummary the email body copy
     */
    public void composeEmail(String[] addresses, String subject, String orderSummary) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order
     * @return the price
     */
    private int calculatePrice() {
        int price = baseCostPerCup;

        if (hasWhippedCream.isChecked()) {
            price = price + 1;
        }

        if (hasChocolate.isChecked()) {
            price = price + 2;
        }

        return quantity * price;
    }

    /**
     *
     * @param name is the customers name
     * @param price the price of the order
     * @param whippedCream does the customer want whipped cream?
     * @param chocolate does the customer want chocolate?
     * @return the order summary
     */
    private String createOrderSummary(String name, int price, boolean whippedCream, boolean chocolate) {
        // check for no name
        if (TextUtils.isEmpty(name)) {
            customerName.setError(getString(R.string.name_error));
            return "";
        }

        String orderSummary = getString(R.string.order_summary_name, name);
        orderSummary += "\n" + getString(R.string.order_summary_whipped_cream, whippedCream);
        orderSummary += "\n" + getString(R.string.order_summary_chocolate, chocolate);
        orderSummary += "\n" + getString(R.string.order_summary_quantity, quantity);
        orderSummary += "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price));
        orderSummary += "\n" + getString(R.string.thank_you);
        return orderSummary;
    }

    /**
     * this method increments the counter and is called when the + button is pressed
     */
    public void increment(View view) {

        if (quantity == 100) {
            Toast.makeText(this, getString(R.string.too_many_error), Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * this method decrements the counter and is called when the - button is pressed
     */
    public void decrement(View view) {

        if (quantity == 1) {
            Toast.makeText(this, getString(R.string.too_few_error), Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * this displays the current count of coffee's
     */
    private void displayQuantity(int number) {
        quantityTextView.setText("" + number);
    }

}
