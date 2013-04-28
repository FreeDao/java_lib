/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\androidhap\\androidhap\\Hap2\\src\\com\\airbiquity\\hap\\IHandsetApplicationProxy.aidl
 */
package com.airbiquity.hap;
/**
 * Interface for the Handset Application Proxy (HAP).
 */
public interface IHandsetApplicationProxy extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.airbiquity.hap.IHandsetApplicationProxy
{
private static final java.lang.String DESCRIPTOR = "com.airbiquity.hap.IHandsetApplicationProxy";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.airbiquity.hap.IHandsetApplicationProxy interface,
 * generating a proxy if needed.
 */
public static com.airbiquity.hap.IHandsetApplicationProxy asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.airbiquity.hap.IHandsetApplicationProxy))) {
return ((com.airbiquity.hap.IHandsetApplicationProxy)iin);
}
return new com.airbiquity.hap.IHandsetApplicationProxy.Stub.Proxy(obj);
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
case TRANSACTION_aqHapInit:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
com.airbiquity.hap.IHapCallback _arg3;
_arg3 = com.airbiquity.hap.IHapCallback.Stub.asInterface(data.readStrongBinder());
int _result = this.aqHapInit(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_aqSendMsg:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
java.lang.String _arg3;
_arg3 = data.readString();
boolean _result = this.aqSendMsg(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.airbiquity.hap.IHandsetApplicationProxy
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
     * Used to register an application with the Handset Application Proxy (HAP).  A single
     * handset application can call this function multiple times, each time a different
     * connection ID will be returned.  In essence, calling this more than once is viewed
     * as the application re-registering.  Any previous connection ID assigned to the
     * handset application is considered to be released. 
     *
     * @param applicationName Unique handset application name.  Java package naming conventions
     * (e.g. com.company.product) are recommended.
     *
     * @param mipSchemaVersion Version of the MIP schema used by the handset application.  The
     * MIP schema version is a version maintained by the handset application vendor that changes
     * when new MIP messages/features are exposed in the handset application.
     *
     * @param baseActivityName Full name of the base activity of the handset application that
     * HAP can use to start the handset application.
     *
     * @param callback Callback interface used by HAP to pass messages to the handset application.
     *
     * @return Upon success, a unique connection ID that HAP uses to identify the handset
     * application, -1 otherwise.
     */
public int aqHapInit(java.lang.String applicationName, java.lang.String mipSchemaVersion, java.lang.String baseActivityName, com.airbiquity.hap.IHapCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(applicationName);
_data.writeString(mipSchemaVersion);
_data.writeString(baseActivityName);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_aqHapInit, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
	 * Used to send a message to the head unit.
	 *
	 * @param connectionId Connection ID provided to the handset application when it
	 * initialized with HAP.
	 *
	 * @param sequenceNumber
	 * @param payload
	 * @param contentType
	 */
public boolean aqSendMsg(int connectionId, int sequenceNumber, byte[] payload, java.lang.String contentType) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(connectionId);
_data.writeInt(sequenceNumber);
_data.writeByteArray(payload);
_data.writeString(contentType);
mRemote.transact(Stub.TRANSACTION_aqSendMsg, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_aqHapInit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_aqSendMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
/**
     * Used to register an application with the Handset Application Proxy (HAP).  A single
     * handset application can call this function multiple times, each time a different
     * connection ID will be returned.  In essence, calling this more than once is viewed
     * as the application re-registering.  Any previous connection ID assigned to the
     * handset application is considered to be released. 
     *
     * @param applicationName Unique handset application name.  Java package naming conventions
     * (e.g. com.company.product) are recommended.
     *
     * @param mipSchemaVersion Version of the MIP schema used by the handset application.  The
     * MIP schema version is a version maintained by the handset application vendor that changes
     * when new MIP messages/features are exposed in the handset application.
     *
     * @param baseActivityName Full name of the base activity of the handset application that
     * HAP can use to start the handset application.
     *
     * @param callback Callback interface used by HAP to pass messages to the handset application.
     *
     * @return Upon success, a unique connection ID that HAP uses to identify the handset
     * application, -1 otherwise.
     */
public int aqHapInit(java.lang.String applicationName, java.lang.String mipSchemaVersion, java.lang.String baseActivityName, com.airbiquity.hap.IHapCallback callback) throws android.os.RemoteException;
/**
	 * Used to send a message to the head unit.
	 *
	 * @param connectionId Connection ID provided to the handset application when it
	 * initialized with HAP.
	 *
	 * @param sequenceNumber
	 * @param payload
	 * @param contentType
	 */
public boolean aqSendMsg(int connectionId, int sequenceNumber, byte[] payload, java.lang.String contentType) throws android.os.RemoteException;
}
