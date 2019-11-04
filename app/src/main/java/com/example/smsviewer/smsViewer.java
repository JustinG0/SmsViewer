package com.example.smsviewer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsMessage;
import android.util.Log;

public class smsViewer extends BroadcastReceiver {

    public static final String pdu_type = "pdus";


    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {

                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);

//                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
//                strMessage += " :" + msgs[i].getMessageBody() + "\n";
//                // Log and display the SMS message.
//                Log.d("#a", "onReceive: " + strMessage);

                String number = msgs[i].getOriginatingAddress();


                if(number.equals("+12244779054")){
                    strMessage += " :" + msgs[i].getMessageBody() + "\n";
                    if(msgs[i].getMessageBody().equals("safeword")) {
                        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent intentAlarm = new Intent(context,AlarmReceiver.class);
                        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
                        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),pi);
                        Log.d("#a","End");
                    }
                }

            }
        }
    }
}



