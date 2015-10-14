package dfire.whc.com.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2015/10/14.
 */
public class SmsReciver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                Date date = new Date(msg.getTimestampMillis());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String receiveTime = format.format(date);
                System.out.println("number:" + msg.getOriginatingAddress()
                        + "   body:" + msg.getDisplayMessageBody() + "  time:"
                        + msg.getTimestampMillis());

                //
                if (msg.getOriginatingAddress().equals("13588835122")) {
                    //TODO

                }

            }
        }
    }
}
