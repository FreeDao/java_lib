package com.caigang.process.model;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class BasicProgramUtil implements Serializable{
    /*
     * ����Ӧ�ó���ļ�Ҫ��Ϣ����
     */
    private Drawable icon;                            // ����ͼ��
    private String programName;                        // ��������
    private String processName;
    private String cpuMemString;
    
    public BasicProgramUtil() {
        icon = null;
        programName = "";    
        processName = "";    
        cpuMemString = "";
    }

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCpuMemString() {
		return cpuMemString;
	}

	public void setCpuMemString(String cpuMemString) {
		this.cpuMemString = cpuMemString;
	}
    
    
    
}