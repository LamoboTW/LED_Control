package com.kortide.led_control;


import com.kortide.remote.ConnectionType;
import com.kortide.remote.RemoteGpio;
import com.kortide.remote.RemoteManager;
import com.kortide.remote.RemoteMessage;
import com.kortide.remote.ResultCallback;
import com.kortide.remote.ResultMsg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
	
	private static final String LOG_TAG = "LamoboCar";
	
	private RemoteManager mRemoteManager;
	private RemoteMessage mRemoteMessage;
	private RemoteGpio mRemoteGpio;
	private TextView txtReturn;
	private Button connectButton;
	private ToggleButton PH21bButton;
	public EditText mailETx;
	public EditText pwdETx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mRemoteManager = new RemoteManager(this);
		mRemoteMessage = new RemoteMessage(this);
		mRemoteGpio = new RemoteGpio(this);
		txtReturn =(TextView) findViewById(R.id.txtReturn);
		connectButton =(Button)findViewById(R.id.connect);
		PH21bButton =(ToggleButton)findViewById(R.id.PH21Button);
		mailETx = (EditText) findViewById(R.id.user_id);
		pwdETx = (EditText) findViewById(R.id.user_password);
		
		
		connectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String mail = mailETx.getText().toString();
				String pwd = pwdETx.getText().toString();
				mRemoteManager.login(mail, pwd, new CBclazz("login"));
			}
		});
		
		PH21bButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (PH21bButton.isChecked()) { 
					mRemoteGpio.writeGpio('h', 21, 1);
                   }   
               
                else {              
					mRemoteGpio.writeGpio('h', 21, 0);
                }
				
				int ret = mRemoteGpio.readGpio('h', 21);
				txtReturn.setText(Integer.toString(ret));
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.txtReturn) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class CBclazz implements ResultCallback {
		private String mStr;

		CBclazz(String str) {
			mStr = str;
		}

		/** before execute */
		public void onPreExecute() {
			Log.d(LOG_TAG, "[onPreExecute] " + mStr);
		}

		/** on executing */
		public void doInBackground() {
			Log.d(LOG_TAG, mStr + "doInBackground ");
		}

		/** after executed */
		public void onSuccess(String result) {
			ConnectionType connectionType = mRemoteManager.getCurrentConnType();
			Log.d(LOG_TAG, mStr + " onSuccess, ConectionType = "
					+ connectionType + ", Result = " + result);
			txtReturn.setText(result);
		}

		public void onFail(ResultMsg result) {
			ConnectionType connectionType = mRemoteManager.getCurrentConnType();
			Log.d(LOG_TAG, mStr + " onFail " + result.getResult());
			txtReturn.setText(result.getResult());
		}
	}
}
