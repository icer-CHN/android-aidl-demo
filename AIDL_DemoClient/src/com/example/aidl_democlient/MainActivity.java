package com.example.aidl_democlient;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.example.aidl_demo.aidl.IState;
import com.example.aidl_demo.aidl.ParcelableData;

public class MainActivity extends Activity {
	public static final String TAG = MainActivity.class.getSimpleName();

	private TextView text;
	private AsyncTask<String, String, String> mTask;
	private IState remoteState;
	private boolean isAlive;
	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			initUpdateThread();
			return true;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isAlive = true;
		initService();
		handler.sendEmptyMessageDelayed(998, 100);
	}

	private void initService() {
		// Log.e(TAG, "initService");
		if (AIDLService.getAIDLBinder() == null) {
			Intent service = new Intent(this, AIDLService.class);
			startService(service);
		}
	}

	private void initUpdateThread() {
		// Log.e(TAG, "initUpdateThread");
		if (mTask == null) {
			// Log.e(TAG, "mTask == null");
			mTask = new AsyncTask<String, String, String>() {
				@Override
				protected String doInBackground(String... params) {
					// Log.e(TAG, "doInBackground");
					while (remoteState == null) {
						remoteState = AIDLService.getAIDLBinder();
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					while (isAlive)
						try {
							Thread.sleep(100);
							if (remoteState != null) {
								ParcelableData data = remoteState.getData();
								if (data != null) {
									int tempState = data.getState();
									Date tempDate = data.getDate();
									publishProgress(tempState + "  "
											+ tempDate.getTime());
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					return null;
				};

				@Override
				protected void onProgressUpdate(String... values) {
					text.setText(values[0]);
					// Log.e(TAG, "onProgressUpdate " + values[0]);
				};

			};
			// Log.e(TAG, "mTask.execute();");
			mTask.execute();
		} else if (mTask.getStatus() == AsyncTask.Status.FINISHED
				|| mTask.getStatus() == AsyncTask.Status.PENDING)
			// Log.e(TAG, "AsyncTask.Status.FINISHED / PENDING");
			mTask.execute();
	}

	private void initView() {
		text = (TextView) findViewById(R.id.text);
	}

	@Override
	protected void onDestroy() {
		isAlive = false;
		super.onDestroy();
	}
}
