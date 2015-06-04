package com.example.aidl_demo.aidl;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableData implements Parcelable {
	private int state;
	private Date date;

	public ParcelableData(int state, long millis) {
		this.state = state;
		date = new Date(millis);
	}

	private ParcelableData(Parcel parcel) {
		state = parcel.readInt();
		date = new Date(parcel.readLong());
	}

	public static final Parcelable.Creator<ParcelableData> CREATOR = new Creator<ParcelableData>() {

		@Override
		public ParcelableData[] newArray(int size) {
			return new ParcelableData[size];
		}

		@Override
		public ParcelableData createFromParcel(Parcel source) {
			return new ParcelableData(source);
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(state);
		dest.writeLong(date.getTime());
	}

	public int getState() {
		return state;
	}

	public Date getDate() {
		return date;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
