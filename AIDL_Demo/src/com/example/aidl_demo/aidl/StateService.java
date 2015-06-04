package com.example.aidl_demo.aidl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class StateService extends Service {
	public static final String TAG = StateService.class.getSimpleName();

	private StateBinder mStateBinder;
	private Timer mTimer;
	private int mState;
	private ParcelableData mParcelableData;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e(TAG, "onCreate");
		mStateBinder = new StateBinder();
		mTimer = new Timer();
		mParcelableData = new ParcelableData(mState, System.currentTimeMillis());
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mState += 1;
				synchronized (mParcelableData) {
					mParcelableData.setState(mState);
					Date date = mParcelableData.getDate();
					date.setTime(System.currentTimeMillis());
					mParcelableData.setDate(date);
				}
			}
		}, 0, 1000);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy");
		mTimer.cancel();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e(TAG, "onBind mStateBinder: " + mStateBinder.toString());
		return mStateBinder;
	}

	public class StateBinder extends IState.Stub {

		@Override
		public int getState() throws RemoteException {
			return mState;
		}

		@Override
		public ParcelableData getData() throws RemoteException {
			return mParcelableData;
		}
	}
}
