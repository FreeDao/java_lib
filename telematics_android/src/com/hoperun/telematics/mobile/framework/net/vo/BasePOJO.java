package com.hoperun.telematics.mobile.framework.net.vo;

/*import java.beans.PropertyDescriptor;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
*/
public class BasePOJO {
    
/*    private static String objectToString(String prefix, Object obj) {
        StringBuffer sb = new StringBuffer();
        if(obj instanceof java.util.List) {
            @SuppressWarnings("unchecked")
            java.util.List<Object> objLst = (java.util.List<Object>) obj;
            sb.append("\n"+prefix+"[\n");
            for(Object subObj : objLst) {
                sb.append(objectToString(prefix+"\t", subObj));
            }
            sb.append("\n"+prefix+"]\n");
        }
        else if(obj instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<Object, Object> objMap = (java.util.Map<Object, Object>) obj;
            sb.append("\n"+prefix+"{\n");
            for(Object key : objMap.keySet()) {
                sb.append(objectToString(prefix, key)).append(":").append(objectToString(prefix, objMap.get(key))).append("\n");
            }
            sb.append("\n"+prefix+"}\n");
        }
        else if(obj instanceof Object[]) {
            Object[] objs = (Object[])obj;
            sb.append("\n"+prefix+"[\n");
            for(Object subObj : objs) {
                sb.append(objectToString(prefix+"\t", subObj)).append(",");
            }
            sb.append("\n"+prefix+"]\n");
        }
        else if(obj != null) {
            sb.append(obj.toString());
        }
        else {
            sb.append("null");
        }
        return sb.toString();
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n{");
        final BeanWrapper wrapper = new BeanWrapperImpl(this);
        for(final PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()){
            try {
                sb.append(descriptor.getName() + " : " + objectToString("\t", descriptor.getReadMethod().invoke(this))).append("\n");
            } catch (Exception e) {
            } 
        }
        sb.append("\n}");
        return sb.toString();
    }*/
}
