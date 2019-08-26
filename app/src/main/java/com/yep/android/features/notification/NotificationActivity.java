package com.yep.android.features.notification;

import android.support.v7.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

//    private MFPPush push; // Push client
//    private MFPPushNotificationListener notificationListener; // Notification listener to handle a push sent to the phone
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification);
//
//
//        // initialize core SDK with IBM Bluemix application Region, TODO: Update region if not using Bluemix US SOUTH
//        BMSClient.getInstance().initialize(this, BMSClient.REGION_SYDNEY);
//
//        // Grabs push client sdk instance
//        push = MFPPush.getInstance();
//        // Initialize Push client
//        // You can find your App Guid and Client Secret by navigating to the Configure section of your Push dashboard, click Mobile Options (Upper Right Hand Corner)
//        // TODO: Please replace <APP_GUID> and <CLIENT_SECRET> with a valid App GUID and Client Secret from the Push dashboard Mobile Options
//        push.initialize(this, "79550f8d-8a22-44c8-b893-80ad983095ac", "98d19e42-f20b-48e7-bcbd-c2b51743c35f");
//
//        // Create notification listener and enable pop up notification when a message is received
//        notificationListener = message -> {
//            d("Received a Push Notification: " + message.toString());
//            runOnUiThread(() -> new android.app.AlertDialog.Builder(NotificationActivity.this)
//                    .setTitle("Received a Push Notification")
//                    .setMessage(message.getAlert())
//                    .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
//                    })
//                    .show());
//        };
//
//        push.setNotificationStatusListener((messageId, status) -> {
//            d("Notification Listener " + status.name());
//        });
//
//    }
//
//    /**
//     * Called when the register device button is pressed.
//     * Attempts to register the device with your push service on Bluemix.
//     * If successful, the push client sdk begins listening to the notification listener.
//     * Also includes the example option of UserID association with the registration for very targeted Push notifications.
//     *
//     * @param view the button pressed
//     */
//    public void registerDevice(View view) {
//
//        // Checks for null in case registration has failed previously
//        if(push==null){
//            push = MFPPush.getInstance();
//        }
//
//        // Make register button unclickable during registration and show registering text
//        TextView buttonText = (TextView) findViewById(R.id.button_text);
//        buttonText.setClickable(false);
//        TextView responseText = (TextView) findViewById(R.id.response_text);
//        responseText.setText("Registering");
//        d("Registering for notifications");
//
//        // Creates response listener to handle the response when a device is registered.
//        MFPPushResponseListener registrationResponselistener = new MFPPushResponseListener<String>() {
//            @Override
//            public void onSuccess(String response) {
//                // Split response and convert to JSON object to display User ID confirmation from the backend
//                String[] resp = response.split("Text: ");
//                try {
//                    JSONObject responseJSON = new JSONObject(resp[1]);
//                    setStatus("Device Registered Successfully with USER ID " + responseJSON.getString("userId"), true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                d( "Successfully registered for push notifications, " + response);
//                // Start listening to notification listener now that registration has succeeded
//                push.listen(notificationListener);
//            }
//
//            @Override
//            public void onFailure(MFPPushException exception) {
//                String errLog = "Error registering for push notifications: ";
//                String errMessage = exception.getErrorMessage();
//                int statusCode = exception.getStatusCode();
//
//                // Set error log based on response code and error message
//                if(statusCode == 401){
//                    errLog += "Cannot authenticate successfully with Bluemix Push instance, ensure your CLIENT SECRET was set correctly.";
//                } else if(statusCode == 404 && errMessage.contains("Push GCM Configuration")){
//                    errLog += "Push GCM Configuration does not exist, ensure you have configured GCM Push credentials on your Bluemix Push dashboard correctly.";
//                } else if(statusCode == 404 && errMessage.contains("PushApplication")){
//                    errLog += "Cannot find Bluemix Push instance, ensure your APPLICATION ID was set correctly and your phone can successfully connect to the internet.";
//                } else if(statusCode >= 500){
//                    errLog += "Bluemix and/or your Push instance seem to be having problems, please try again later.";
//                } else {
//                    errLog += " " + statusCode;
//                }
//
//                setStatus(errLog, false);
//                d(errLog);
//                // make push null since registration failed
//                push = null;
//            }
//        };
//
//        // Attempt to register device using response listener created above
//        // Include unique sample user Id instead of Sample UserId in order to send targeted push notifications to specific users
//        push.registerDeviceWithUserId("AndroidOw",registrationResponselistener);
//    }
//
//    // If the device has been registered previously, hold push notifications when the app is paused
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        if (push != null) {
//            push.hold();
//        }
//    }
//
//    // If the device has been registered previously, ensure the client sdk is still using the notification listener from onCreate when app is resumed
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (push != null) {
//            push.listen(notificationListener);
//        }
//    }
//
//    /**
//     * Manipulates text fields in the UI based on initialization and registration events
//     * @param messageText String main text view
//     * @param wasSuccessful Boolean dictates top 2 text view texts
//     */
//    private void setStatus(final String messageText, boolean wasSuccessful){
//        final TextView responseText = (TextView) findViewById(R.id.response_text);
//        final TextView topText = (TextView) findViewById(R.id.top_text);
//        final TextView bottomText = (TextView) findViewById(R.id.bottom_text);
//        final TextView buttonText = (TextView) findViewById(R.id.button_text);
//        final String topStatus = wasSuccessful ? "Yay!" : "Bummer";
//        final String bottomStatus = wasSuccessful ? "You Are Connected" : "Something Went Wrong";
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                buttonText.setClickable(true);
//                responseText.setText(messageText);
//                topText.setText(topStatus);
//                bottomText.setText(bottomStatus);
//            }
//        });
//    }
}
