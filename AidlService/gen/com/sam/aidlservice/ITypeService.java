/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/sam/work/java/AidlService/src/com/sam/aidlservice/ITypeService.aidl
 */
package com.sam.aidlservice;
public interface ITypeService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.sam.aidlservice.ITypeService
{
private static final java.lang.String DESCRIPTOR = "com.sam.aidlservice.ITypeService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.sam.aidlservice.ITypeService interface,
 * generating a proxy if needed.
 */
public static com.sam.aidlservice.ITypeService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.sam.aidlservice.ITypeService))) {
return ((com.sam.aidlservice.ITypeService)iin);
}
return new com.sam.aidlservice.ITypeService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_basicTypes:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
long _arg1;
_arg1 = data.readLong();
char _arg2;
_arg2 = (char)data.readInt();
boolean _arg3;
_arg3 = (0!=data.readInt());
java.lang.String _arg4;
_arg4 = data.readString();
this.basicTypes(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
return true;
}
case TRANSACTION_objectType:
{
data.enforceInterface(DESCRIPTOR);
Dog _arg0;
if ((0!=data.readInt())) {
_arg0 = Dog.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.objectType(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getDogWithType:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
Dog _result = this.getDogWithType(_arg0);
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_request:
{
data.enforceInterface(DESCRIPTOR);
com.sam.aidlservice.ITypeServiceListener _arg0;
_arg0 = com.sam.aidlservice.ITypeServiceListener.Stub.asInterface(data.readStrongBinder());
this.request(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.sam.aidlservice.ITypeService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void basicTypes(int anInt, long aLong, char aChar, boolean aBoolean, java.lang.String aString) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(anInt);
_data.writeLong(aLong);
_data.writeInt(((int)aChar));
_data.writeInt(((aBoolean)?(1):(0)));
_data.writeString(aString);
mRemote.transact(Stub.TRANSACTION_basicTypes, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void objectType(Dog aDog) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((aDog!=null)) {
_data.writeInt(1);
aDog.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_objectType, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public Dog getDogWithType(int type) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
Dog _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
mRemote.transact(Stub.TRANSACTION_getDogWithType, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = Dog.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void request(com.sam.aidlservice.ITypeServiceListener lintener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((lintener!=null))?(lintener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_request, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_basicTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_objectType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getDogWithType = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_request = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
public void basicTypes(int anInt, long aLong, char aChar, boolean aBoolean, java.lang.String aString) throws android.os.RemoteException;
public void objectType(Dog aDog) throws android.os.RemoteException;
public Dog getDogWithType(int type) throws android.os.RemoteException;
public void request(com.sam.aidlservice.ITypeServiceListener lintener) throws android.os.RemoteException;
}
