package com.airbiquity.application.manager;

import com.airbiquity.hap.IHapCallback;
/**
 * 
 * @author TGormley
 *
 */
public class RegisteredApplication {

	private String name;
	private String version;
	private String baseActivity;
	private IHapCallback callback;

    /**
     * 
     */
    public RegisteredApplication(String name,
    		String version,
    		String baseActivity,
    		IHapCallback callback) {
        this.name = name;
        this.version = version;
        this.baseActivity = baseActivity;
        this.callback = callback;
    }

    public String getName() { return name; }
    public String getVersion() { return version; }
    public String getBaseActivity() { return baseActivity; }
    public IHapCallback getCallback() { return callback; }

    /**
     *
     */
    public boolean isValid() {
    	return (name != null) && (name.length() > 0) && 
    		(version != null) && (version.length() > 0) && 
    		(baseActivity != null) && (baseActivity.length() > 0) &&
    		(callback != null);
    }

    /**
     * 
     */
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("RegisteredApplication: name=");
    	sb.append(name);
    	sb.append(", version=");
    	sb.append(version);
    	sb.append(", baseActivity=");
    	sb.append(baseActivity);
    	sb.append(", callback=");
    	sb.append(callback.toString());
    	return sb.toString();
    }

}
