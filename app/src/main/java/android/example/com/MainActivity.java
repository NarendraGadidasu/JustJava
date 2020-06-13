package android.example.com;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**set up the layout defined in activity_main.xml file on start up*/
        setContentView(R.layout.activity_main);
    }

    int quantity = 2;

    /**
     * This method is called when the order button is clicked.
     */

    public void submitOrder(View view) {

        /**Check if Chocolate or WhippedCream Topping is needed*/
        boolean needWhipped = addWhipped();
        boolean needChocolate = addChocolate();

        /**Get the name of the customer*/
        EditText nameText = (EditText) findViewById(R.id.name);
        String name = nameText.getText().toString();

        /**Calculate the total price*/
        int price = calculatePrice(quantity,needWhipped,needChocolate);

        /**create the order summary*/
        String priceMessage = createOrderSummary(price, needWhipped,needChocolate, name);

        /** display order summary in the app*/
        displayMessage(priceMessage);

        /** send order summary to an email app*/
        sendEmail("JustJava Order for "+name,priceMessage);

    }

    /**this method checks whether whipped cream should be added*/
    private boolean addWhipped(){
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean addWhipped = whippedCreamCheckBox.isChecked();
        return addWhipped;
    }

    /**this method checks whether chocolate should be added*/

    private boolean addChocolate(){
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean addChocolate = chocolateCheckBox.isChecked();
        return addChocolate;
    }


    /**this method decreases the order quantity when the - button is pressed*/
    private void decrement(View view) {
        if (quantity>0)
        {quantity = quantity - 1;}
        else {
            /**If the user tries to decrease the quantity below minimum quantity 0 then a message is shown*/
            Context context = getApplicationContext();
            CharSequence text = "Maximum allowed quantity is 100";
            int duration = Toast.LENGTH_SHORT;
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();
        };
        displayQuantity(quantity);
    }

    /**this method increases the order quantity when the + button is pressed*/
    private void increment(View view) {
        if (quantity<100){
        quantity = quantity + 1;}
        else {
            /**If the user tries to increase the quantity over maximum quantity 100 then a message is shown*/
            Context context = getApplicationContext();
            CharSequence text = "Maximum allowed quantity is 100";
            int duration = Toast.LENGTH_SHORT;
            Toast toast= Toast.makeText(context, text, duration);
            toast.show();
        }
        displayQuantity(quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**this method calculates the total price for the order*/
    private int calculatePrice(int quantity, boolean needWhipped, boolean needChocolate)
    {
        int price_per_each = 5;
        if (needWhipped) {price_per_each += 1;};
        if (needChocolate) {price_per_each += 2;};
        return quantity*price_per_each;
    }

    /**This method creates the order summary*/
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String nameEntered) {
        String needWhipped = Boolean.toString(hasWhippedCream);
        String needChocolate = Boolean.toString(hasChocolate);
        String out = "Name: "+nameEntered
        out += "\nAdd whipped cream? ";
        out += needWhipped;
        out += "\nAdd Chocolate? ";
        out += needChocolate;
        out += "\nQuantity: ";
        out += quantity;
        out += "\nTotal: ";
        out += price;
        out += "\n";
        out += getString(R.string.thank_you);
        return out;
    }

    /**
     * This method displays the given order summary on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**This method opens an E-mail app and fill in the subject and order summary*/
    private void sendEmail(String subject, String body){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}