/**
 * @(#)LoadTest.java $Revision: 1.0 $ $Date: Aug 28, 2007 9:34:08 AM $
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

import com.tibco.tellurium.net.JMSConnection;

/**
 * TODO Add docs.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 28, 2007 9:34:08 AM
 * 
 */
public class LoadTest implements Runnable {
	private static final int SLEEP_TIME = 2000;
	private static String REQUEST_DIR_BASE = "xml/requests/loadtest/";
	private static int[] runSet = new int[] { 1, 10, 25, 50, 75, 100, 125, 150 };
	// private static String[] loadSet = new String[] {
	// "C4","SEVIS","CCD","C4-SEVIS","C4-CCD","C4-SEVIS-CCD","SEVIS-CCD","C4-NullJMSType","SEVIS-NullJMSType","CCD-NullJMSType"};
	private static String[] loadSet = new String[] { "C4", "CCD" };
	private Dispatcher dispatcher;
	private static String loadTestDir;

	public LoadTest(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * The run method for executing the Load Test.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String loadTestSubType;
		try {
			for (String element : loadSet) {
				loadTestSubType = element;
				loadTestDir = REQUEST_DIR_BASE + loadTestSubType;
				System.out.println("Client dir = " + loadTestDir);
				if (loadTestSubType.contains("JMSType")) {
					JMSConnection.setJMSType(false);
				}
				else {
					JMSConnection.setJMSType(true);
				}
				for (int element2 : runSet) {
					System.out.println("Starting run: " + element2);
					dispatcher.startRequests(element2, loadTestDir);
					while (!dispatcher.isRunComplete()) {
						try {
							Thread.sleep(SLEEP_TIME);
						}
						catch (InterruptedException ie) {
							System.out.println("INTERRUPTED EXCEPTION!!!!!");
							// Do something?
						}
					}
				}
			}
		}
		catch (Exception e) {
			System.err.println("Exception setting up harness: " + e);
			e.printStackTrace(System.err);
		}
		System.out.println("Load test complete.");
	}

	public static String getLoadTestDir() {
		return loadTestDir;
	}

}
