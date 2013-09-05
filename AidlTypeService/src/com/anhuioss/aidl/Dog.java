package com.anhuioss.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Dog implements Parcelable {
	
	private String name;
	private int age;
	private String color;
	
	public static final Parcelable.Creator<Dog> CREATOR = new Creator<Dog>() {
		
		@Override
		public Dog[] newArray(int size) {
			return new Dog[size];
		}
		
		@Override
		public Dog createFromParcel(Parcel source) {
			return new Dog(source);
		}
	};
	
	public Dog(String name, int age, String color) {
		this.name = name;
		this.age = age;
		this.color = color;
	}
	
	public Dog(Parcel pl) {
		age = pl.readInt();
		name = pl.readString();
		color = pl.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(age);
		dest.writeString(name);
		dest.writeString(color);
	}
	
	@Override
	public String toString() {
		return "{Name:" + name + " Age:" + age + " Color:" + color +  "}";
	}

}
