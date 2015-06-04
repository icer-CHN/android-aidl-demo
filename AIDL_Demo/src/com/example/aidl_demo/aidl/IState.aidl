package com.example.aidl_demo.aidl;

import com.example.aidl_demo.aidl.ParcelableData;

interface IState
{
	int getState();
	
	ParcelableData getData();
}
