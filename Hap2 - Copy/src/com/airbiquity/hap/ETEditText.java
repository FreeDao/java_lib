/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *
 *              @author
 *              
 *
 *****************************************************************************/
package com.airbiquity.hap;

import android.widget.EditText;

/** 
 * @ClassName: ABQEditText 
 * @Description: ABQ EditText
 * @author Leo,Fan
 * @date Mar 27, 2012 10:57:29 AM 
 *  
 */
public class ETEditText {
	private EditText et;
	private String label;
	public EditText getEt() {
		return et;
	}
	public void setEt(EditText et) {
		this.et = et;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public ETEditText() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ETEditText(EditText et, String label) {
		super();
		this.et = et;
		this.label = label;
	}
	
}
