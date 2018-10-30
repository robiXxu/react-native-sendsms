
package com.robixxu;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;


public class RNSendsmsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private enum Keys {
    SENT,
    DELIVERY
  }

  private Callback _out = null;

  public RNSendsmsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNSendsms";
  }


  private void sendResponse(final Integer msgId, final String status) {
    if( _out != null ){
      _out.invoke(msgId, status);
      _out = null;
    }
  }

  @ReactMethod
  public void send( final Integer msgId, final String phoneNumber, final String message, final Callback out ) {
    this._out = out;

    try {
      PendingIntent sentIntent = smsSentIntent(Keys.SENT.toString(), msgId);
      PendingIntent deliveryIntent = smsDeliveryIntent(Keys.DELIVERY.toString(), msgId);

      SmsManager sms = SmsManager.getDefault();

      sms.sendTextMessage(phoneNumber, null, message, sentIntent, deliveryIntent);


    } catch (Exception e) {
      sendResponse(msgId, "Error ".concat(e.getMessage()));
    }
  }

  private PendingIntent smsSentIntent(final String SENT_KEY, final Integer msgId) {

    BroadcastReceiver br = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
          case Activity.RESULT_OK:
            sendResponse(msgId, "SMS_SENT");
            break;
          case SmsManager.RESULT_ERROR_NO_SERVICE:
            sendResponse(msgId, "NO_SERVICE");
            break;
          case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
            sendResponse(msgId, "GENERIC_FAILURE");
            break;
        }
      }
    };

    IntentFilter intentFilter = new IntentFilter(SENT_KEY);

    reactContext.registerReceiver(br,intentFilter);

    Intent sentIntent = new Intent(SENT_KEY);

    return PendingIntent.getBroadcast(reactContext, 0, sentIntent, 0);
  }

  private PendingIntent smsDeliveryIntent(final String DELIVERY_KEY, final Integer msgId) {

    BroadcastReceiver br = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
          case Activity.RESULT_OK:
            sendResponse(msgId, "SMS_DELIVERED");
            break;
          case Activity.RESULT_CANCELED:
            sendResponse(msgId, "SMS_NOT_DELIVERED");
            break;
        }
      }
    };

    IntentFilter intentFilter = new IntentFilter(DELIVERY_KEY);

    reactContext.registerReceiver(br,intentFilter);

    Intent statusIntent = new Intent(DELIVERY_KEY);

    return PendingIntent.getBroadcast(reactContext, 0, statusIntent, 0);
  }
}