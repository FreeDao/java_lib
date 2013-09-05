package com.sam.aidlservice;
import com.sam.aidlservice.Dog;
import com.sam.aidlservice.ITypeServiceListener;
interface ITypeService{
void basicTypes(int anInt, long aLong, char aChar, boolean aBoolean, String aString);
void objectType(in Dog aDog);
Dog getDogWithType(int type);
void request(ITypeServiceListener lintener);
}