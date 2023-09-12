/**
 * @(#)Connection.java $Revision: 1.0 $ $Date: Aug 31, 2007 7:06:23 PM $
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
package com.tibco.tellurium.net;

import java.io.File;

import com.tibco.tellurium.model.Request;

/**
 * TODO Add docs.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 31, 2007 7:06:23 PM
 *
 */
public interface Connection {

	public abstract void send(Request request) throws Exception;

	public abstract void sendRequestFiles(java.lang.String dirName)
			throws Exception;

	public abstract int sendRequestFile(java.lang.String dirName)
			throws Exception;

	public abstract void send(java.lang.String request,
			java.lang.String queryTarget) throws Exception;

	public abstract void sendFile(File file) throws Exception;

	public abstract ResponseMessage getResults() throws Exception;

	public abstract ResponseMessage getResults(boolean b) throws Exception;

}