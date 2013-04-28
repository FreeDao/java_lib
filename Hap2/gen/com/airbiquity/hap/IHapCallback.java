/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\androidhap\\androidhap\\Hap2\\src\\com\\airbiquity\\hap\\IHapCallback.aidl
 */
package com.airbiquity.hap;
/**
 * Callback interface implemented by handset applications.  These methods are
 * invoked by the Handset Application Proxy (HAP).  The callback interface is
 * supplied to HAP in the aqHapInit() method of the IHandsetApplicationProxy
 * interface.
 */
public interface IHapCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.airbiquity.hap.IHapCallback
{
private static final java.lang.String DESCRIPTOR = "com.airbiquity.hap.IHapCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.airbiquity.hap.IHapCallback interface,
 * generating a proxy if needed.
 */
public static com.airbiquity.hap.IHapCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.airbiquity.hap.IHapCallback))) {
return ((com.airbiquity.hap.IHapCallback)iin);
}
return new com.airbiquity.hap.IHapCallback.Stub.Proxy(obj);
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
case TRANSACTION_onHapConnectionStateChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.onHapConnectionStateChange(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onHapCommandReceived:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
byte[] _arg1;
_arg1 = data.createByteArray();
java.lang.String _arg2;
_arg2 = data.readString();
this.onHapCommandReceived(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.airbiquity.hap.IHapCallback
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
/**
     * Notifies handset application when the head unit connection state changes.
     *
     * @param connectionState 0: head unit and handset got connected.
     *                        1: head unit and handset got disconnected.
     */
public void onHapConnectionStateChange(int connectionState) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(connectionState);
mRemote.transact(Stub.TRANSACTION_onHapConnectionStateChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Passes a message from the head unit to a handset application.
     *
     * @param sequenceNumber Non-zero sequence number of the request.  This sequence
     * number must be echoed back in the response sent to HAP by the application in order
     * to match the request and response.
     *
     * @param payload Payload sent from the head unit to the handset application.
     *
     * @param contentType HTTP 1.1 Content Type of the payload. 
     */
public void onHapCommandReceived(int sequenceNumber, byte[] payload, java.lang.String contentType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(sequenceNumber);
_data.writeByteArray(payload);
_data.writeString(contentType);
mRemote.transact(Stub.TRANSACTION_onHapCommandReceived, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onHapConnectionStateChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onHapCommandReceived = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
     * Notifies handset application when the head unit connection state changes.
     *
     * @param connectionState 0: head unit and handset got connected.
     *                        1: head unit and handset got disconnected.
     */
public void onHapConnectionStateChange(int connectionState) throws android.os.RemoteException;
/**
     * Passes a message from the head unit to a handset application.
     *
     * @param sequenceNumber Non-zero sequence number of the request.  This sequence
     * number must be echoed back in the response sent to HAP by the application in order
     * to match the request and response.
     *
     * @param payload Payload sent from the head unit to the handset application.
     *
     * @param contentType HTTP 1.1 Content Type of the payload. 
     */
public void onHapCommandReceived(int sequenceNumber, byte[] payload, java.lang.String contentType) throws android.os.RemoteException;
}
