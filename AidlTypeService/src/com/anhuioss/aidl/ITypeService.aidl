package com.anhuioss.aidl;
import com.anhuioss.aidl.Dog;
import com.anhuioss.aidl.ITypeServiceListener;

interface ITypeService {
    void basicTypes(int anInt, long aLong, char aChar, boolean aBoolean, float aFloat, double aDouble, String aString);
    void objectType(in Dog aDog);
    Dog getDogWithType(int type);
    void request(ITypeServiceListener lintener);
}