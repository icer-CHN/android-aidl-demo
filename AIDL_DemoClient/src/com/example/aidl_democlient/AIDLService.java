package com.example.aidl_democlient;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.aidl_demo.aidl.IState;

public class AIDLService extends Service
{
	public static final String TAG = AIDLService.class.getSimpleName();
	private static IState remoteState;
	private Intent service;
	private ServiceConnection conn;

	@Override
	public void onCreate()
	{
		super.onCreate();
		initAIDL();
		foreground();
	}

	/**
	 * 前台通知栏显示,保证service存活(不设置icon,保证不会弹出,但同时保证存活)
	 */
	private void foreground()
	{
		Notification notification = new Notification();
		// notification.icon = R.drawable.ic_launcher;
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(this, "AIDL", "", pendingIntent);
		startForeground(Notification.FLAG_ONGOING_EVENT, notification);
	}

	private void initAIDL()
	{
		service = new Intent("com.example.aidl_demo.aidl.STATE");
		conn = new ServiceConnection()
		{

			@Override
			public void onServiceDisconnected(ComponentName name)
			{
				remoteState = null;
				Log.e(TAG, "onServiceDisconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				remoteState = IState.Stub.asInterface(service);
				Log.e(TAG, "onServiceConnected service: " + (service == null ? "null" : remoteState.toString()));
			}
		};
		bindService(service, conn, Service.BIND_AUTO_CREATE);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	public static IState getAIDLBinder()
	{
		Log.e(TAG, "remoteState: " + (remoteState == null ? "null" : remoteState.toString()));
		return remoteState;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		remoteState = null;
		unbindService(conn);
		super.onDestroy();
	}
}
