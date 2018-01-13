package com.example.retar.justjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    int cost = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * method called when the order button is pressed
     */
    public void submitOrder(View view) {
        String priceMessage = "Total $" + cost * quantity + "\nThank you!";
        displayMessage(priceMessage);
    }

    /**
     * this method increments the counter and is called when the + button is pressed
     */
    public void increment(View view) {
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * this method decrements the counter and is called when the - button is pressed
     */
    public void decrement(View view) {
        quantity = quantity - 1;

        if (quantity < 0) {
            quantity = 0;
        }
        display(quantity);
    }

    /**
     * this displays the current count of coffee's
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * this displayes the calculated price
     */
    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * this also changes the price field
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }
}
