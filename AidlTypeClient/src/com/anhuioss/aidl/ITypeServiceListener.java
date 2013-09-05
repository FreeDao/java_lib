/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/oss/workspace/AidlTypeService/src/com/anhuioss/aidl/ITypeServiceListener.aidl
 */
package com.anhuioss.aidl;
public interface ITypeServiceListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.anhuioss.aidl.ITypeServiceListener
{
private static final java.lang.String DESCRIPTOR = "com.anhuioss.aidl.ITypeServiceListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.anhuioss.aidl.ITypeServiceListener interface,
 * generating a proxy if needed.
 */
public static com.anhuioss.aidl.ITypeServiceListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.anhuioss.aidl.ITypeServiceListener))) {
return ((com.anhuioss.aidl.ITypeServiceListener)iin);
}
return new com.anhuioss.aidl.ITypeServiceListener.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
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
case TRANSACTION_requestCompleted:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.requestCompleted(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.anhuioss.aidl.ITypeServiceListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void requestCompleted(java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_requestCompleted, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_requestCompleted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void requestCompleted(java.lang.String message) throws android.os.RemoteException;
}
