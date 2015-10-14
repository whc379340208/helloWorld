package dfire.whc.com.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends Activity {
    private String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    @InjectView(R.id.telphone)
    EditText telphoneTxt;
    @InjectView(R.id.code)
    EditText messageCodeTxt;
    @InjectView(R.id.webView)
    WebView webView;
    SMSSuccessReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        /**验证短信是否发送成功*/
        receiver = new SMSSuccessReceiver();
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0, deliverIntent, 0);

        registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress != 100) {
                    Log.d("whc--", getString(R.string.web_loading));
                    messageCodeTxt.setText(getString(R.string.web_loading));
                } else if (newProgress == 100) {
                    Log.d("whc--", getString(R.string.web_load_success));
                    messageCodeTxt.setText(getString(R.string.web_load_success));
                }
            }
        });
        webView.loadUrl("https://www.baidu.com/");
        webView.setWebViewClient(new WebViewClient());
    }

    @OnClick(R.id.send_message_btn)
    public void sendMessage(){
        //发送短信验证码
        String phoneNum = telphoneTxt.getText().toString();
        if(!phoneNum.isEmpty() && !"".equals(phoneNum)){
            String messageStr = "验证码为："+Math.random()*100;
            try{
                messageStr = new String(messageStr.getBytes("GBK"), "UTF-8");
            }catch (UnsupportedEncodingException e){

            }
            doSendSMSTO(phoneNum, messageStr);
        }
    }

    private void doSendSMSTO(String phoneNumber, String message){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    class SMSSuccessReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "短信发送成功并接收", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
