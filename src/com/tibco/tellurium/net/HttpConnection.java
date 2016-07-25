/**
 * @(#)HttpConnection.java	$Revision: 1.0 $ $Date: Sep 5, 2007 3:31:57 PM $
 *
 * Copyright � 2007 FortMoon Consulting, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * FortMoon Consulting, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FortMoon Consulting.
 *
 * FortMoon MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
 * OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. FortMoon SHALL NOT BE LIABLE FOR ANY
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 * Copyright Version 1.0
 */
package com.FortMoon.tellurium.net;

import java.io.File;

import com.FortMoon.tellurium.model.Request;

/**
 * TODO Add docs.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Sep 5, 2007 3:31:57 PM
 *
 */
public class HttpConnection implements Connection {

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#getResults()
	 */
	public ResponseMessage getResults() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#getResults(boolean)
	 */
	public ResponseMessage getResults(boolean b) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#send(com.FortMoon.tellurium.net.RequestMessage)
	 */
	public void send(Request request) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#send(java.lang.String, java.lang.String)
	 */
	public void send(String request, String queryTarget) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#sendFile(java.io.File)
	 */
	public void sendFile(File file) throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#sendRequestFile(java.lang.String)
	 */
	public int sendRequestFile(String dirName) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.FortMoon.tellurium.net.Connection#sendRequestFiles(java.lang.String)
	 */
	public void sendRequestFiles(String dirName) throws Exception {
		// TODO Auto-generated method stub

	}

}
