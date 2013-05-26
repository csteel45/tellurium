/*
 * @(#)RequestStats.java
 * 
 * Copyright � 2007 FortMoon Consulting, Inc. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of FortMoon
 * Software, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with FortMoon Consulting.
 * 
 * FortMoon MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. FortMoon SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 
 * Copyright Version 1.0
 */
package com.FortMoon.tellurium.model;

/**
 * TODO Add docs.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 */
public class RequestStats {
	private long workStart = 0L;
	private long workEnd = 0L;
	private long connectStart = 0L;
	private long connectEnd = 0L;
	private Integer reqNumber = 0;
	private String id;
	private String state = Client.PENDING;
	private boolean complete = false;
	private String retVal;

	public RequestStats() {
		workStart = 0l;
		workEnd = 0l;
		connectStart = 0l;
		connectEnd = 0l;
		retVal = null;
	}

	public void setState(String state) {
		this.state = state;
		if (this.state.equals(Client.COMPLETE)
				|| this.state.equals(Client.FAILURE)) {
			this.complete = true;
		}
	}

	@Override
	public String toString() {
		return "Id = " + id + " State = " + state + " complete = " + complete
				+ " reqNumber = " + reqNumber + " retVal = " + retVal;
	}

	public String getState() {
		return (state);
	}

	public boolean isComplete() {
		return this.complete;
	}

	public void setComplete(boolean complete) {
		this.complete = true;
	}

	public float getTotalWorkTime() {
		if ((workStart > 0) && (workEnd == 0))
			return (-1.0f);
		return ((workEnd - workStart) / 1000.0f);
	}

	public float getTotalConnectTime() {
		if ((connectStart > 0) && (connectEnd == 0))
			return (-1.0f);
		return ((connectEnd - connectStart) / 1000.0f);
	}

	public String getID() {
		return (id);
	}

	public void setRetVal(String r) {
		retVal = r;
	}

	public String getRetVal() {
		return (retVal);
	}

	public void setID(String id) {
		this.id = id;
	}

	public Integer getRequestNumber() {
		return (reqNumber);
	}

	public void setRequestNumber(Integer reqNumber) {
		this.reqNumber = reqNumber;
	}

	public void markWorkStart() {
		workStart = System.currentTimeMillis();
		workEnd = 0l;
	}

	public void markWorkEnd() {
		workEnd = System.currentTimeMillis();
	}

	public void markConnectStart() {
		connectStart = System.currentTimeMillis();
		connectEnd = 0l;
	}

	public void markConnectEnd() {
		connectEnd = System.currentTimeMillis();
	}

}
