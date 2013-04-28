package com.sam.aidlservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Dog implements Parcelable{
	private String name;
	private int age;
	private String corlor;
	// Constructor
	public Dog(String name,int age ,String corlor){
		this.name = name;
		this.age = age;
		this.corlor = corlor;
	}

	public Dog(Parcel pl){
		name = pl.readString();
		age = pl.readInt();
		corlor = pl.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(age);
		dest.writeString(corlor);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{Name:"+name+" Age: "+age+" Corlor "+corlor+" }";
	}
	
	public static final Parcelable.Creator<Dog> CREATOR = new Creator<Dog>(){

		@Override
		public Dog createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Dog(source);
		}

		@Override
		public Dog[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Dog[size];
		}
		
	};

}
