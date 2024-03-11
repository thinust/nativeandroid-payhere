package com.oriensolutions.payhere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getName();
        private static final int PAYHERE_REQUEST = 11001;


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.paybutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                InitRequest req = new InitRequest();
                req.setMerchantId("1225224");       // Merchant ID
                req.setCurrency("LKR");             // Currency code LKR/USD/GBP/EUR/AUD
                req.setAmount(1000.00);             // Final Amount to be charged
                req.setOrderId("230000123");        // Unique Reference ID
                req.setItemsDescription("Door bell wireless");  // Item description title
                req.setCustom1("This is the custom message 1");
                req.setCustom2("This is the custom message 2");
                req.getCustomer().setFirstName("Thinuka");
                req.getCustomer().setLastName("Ravindith");
                req.getCustomer().setEmail("thinuka1@gmail.com");
                req.getCustomer().setPhone("+94760584888");
                req.getCustomer().getAddress().setAddress("No.1, Galle Road");
                req.getCustomer().getAddress().setCity("Colombo");
                req.getCustomer().getAddress().setCountry("Sri Lanka");

                req.setHoldOnCardEnabled(true);

//Optional Params
//                req.setNotifyUrl(“xxxx”);           // Notifiy Url
//                req.getCustomer().getDeliveryAddress().setAddress("No.2, Kandy Road");
//                req.getCustomer().getDeliveryAddress().setCity("Kadawatha");
//                req.getCustomer().getDeliveryAddress().setCountry("Sri Lanka");
//                req.getItems().add(new Item(null, "Door bell wireless", 1, 1000.0));

                Intent intent = new Intent(MainActivity.this, PHMainActivity.class);
                intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
                PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
                startActivityForResult(intent, PAYHERE_REQUEST); //unique request ID e.g. "11001"
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        textView = findViewById(R.id.textView);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (resultCode == Activity.RESULT_OK) {
                String msg;
                if (response != null)
                    if (response.isSuccess())
                        msg = "Activity result:" + response.getData().toString();
                    else
                msg = "Result:" + response.toString();
                else
                msg = "Result: no response";
                Log.d(TAG, msg);
                textView.setText(msg);
//                Log.i(TAG,response.toString());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response != null)
                    textView.setText(response.toString());
                else
                    textView.setText("User canceled the request");
            }
        }
    }
}