/*
 * @(#)ClientManager.java
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
package com.tibco.tellurium.model;

import java.io.Serializable;

import com.tibco.tellurium.view.monitor.MonitorFeedListener;


/**
 * TODO Add docs.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 */
public interface ClientManager extends Serializable {
	public static final int SEQUENTIAL = 0;
	public static final int SIMULTANEOUS = 1;

	public void notify(RequestStats stats);

	public void update(RequestStats stats);

// public void addWorker(String id, Thread t);
	public void startRequests(int numRequests, String requestDir,
			String queryTargets, boolean simultaneous);

	public void clear();

	public void registerMonitorListener(MonitorFeedListener listener);
}
