/*******************************************************************************
 * Copyright � 2007 FortMoon Consulting, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions, the following disclaimer, and the original author.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * This software is the confidential and proprietary information of FortMoon
 * Consulting, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with FortMoon.
 * 
 * FORTMOON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. FORTMOON SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 
 * Copyright Version 1.0
 ******************************************************************************/

package com.FortMoon.tellurium.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * LogWriter writes log messages taken from the log queue to file.
 * 
 * @author Chris Steel
 * @version $Revision: 1.3 $
 * @date $Date: 2001/06/25 04:33:31 $
 */

class LogWriter implements Runnable {
	private static boolean stop = false;
	private byte[] msgBuf = null;
	private final int logInterval = 3000; // 5 second default
	private final int initialCapacity = 100;
	public static PrintWriter logStream;
	private String logFileName = null;
	private BufferedOutputStream outStream;
	private static boolean initialized = false;
	private static Thread me = null;
	private Vector msgQ = null;
	private final PrintStream StdOut = System.out;
	private final PrintStream StdErr = System.err;

	LogWriter() {
		super();
		msgQ = new Vector(initialCapacity);
// System.out.println("LogWriter constructor called.");
	} // Constructor

	void init() throws Exception {
		if (initialized) {
			return;
		}
		logFileName = ConfigInfo.getStringProperty(
				"Blackwave.Logger.LogFileName", "logger.log");
		ClassLoader cl = this.getClass().getClassLoader();
		if (cl == null) {
			cl = ClassLoader.getSystemClassLoader();
		}
		java.net.URL logPathURL = cl.getResource("logs");
		if (logPathURL != null) {
			String logPath = logPathURL.getPath();
			logFileName = logPath + File.separator + logFileName;
		}
		else {
			logFileName = "blackwave.log";
		}
		System.err.println("Log file = " + logFileName);
		outStream = new BufferedOutputStream(new FileOutputStream(logFileName));
		// System.out.println("Log file name: " + logFileName);
		initialized = true;
		// System.out.println("Logger Initialized.");
	} // init

	public synchronized void uninit() {
		if (!initialized) {
			return;
		}
		this.Stop();
		try {
			processMsgQ();
			if (outStream != null) {
				outStream.flush();
				outStream.close();
			}
		}
		catch (IOException e) {
		}
		initialized = false;
	} // uninit

	public void logStdOut(boolean ans) {
		if (ans) {
			System.setOut(new PrintStream(outStream));
		}
		else {
			System.setOut(this.StdOut);
		}
	}

	public void logStdErr(boolean ans) {
		if (ans) {
			System.setErr(new PrintStream(outStream));
		}
		else {
			System.setErr(this.StdErr);
		}
	}

	public void flush() {
		processMsgQ();
	}

	public static void Stop() {
		// System.out.println("LogWriter: Stop called.");
		stop = true;
		me.interrupt();
	} // stop

	public void write(byte[] msg) {
		msgQ.addElement(msg);
		synchronized (msgQ) {
			msgQ.notifyAll(); // May need to use notifyAll in future. Notify
								// is faster for now.
		}
	} // println

	public void run() {
		me = Thread.currentThread();
		me.setName("LogWriter");
		me.setPriority(Thread.MIN_PRIORITY);
		// System.out.println("LogWriter: started.");
		while (!stop) {
			if (msgQ.isEmpty()) { // Wait on notification of additions to the
									// queue
				try {
					synchronized (msgQ) {
						msgQ.wait(logInterval);
					}
				}
				catch (InterruptedException e) {
					// System.out.println("Logger: woken up on an addElement");
				}
			}
			processMsgQ();
			try {
				Thread.sleep(logInterval);
			}
			catch (InterruptedException e) {
				// System.out.println("Logger: sleep interrupted");
			}
		}
		processMsgQ();
		// System.out.println("LogWriter: stopped.");

		System.gc();
	} // run

	private void processMsgQ() {
		try {
			// System.out.println("LogWriter: msgQ elements before: " +
			// msgQ.size());
			while (!msgQ.isEmpty()) {
				msgBuf = (byte[]) msgQ.remove(0); // Other entries will be
													// shifted left
				// System.out.println("LogWriter: writing message: " + new
				// String(msgBuf));
				outStream.write(msgBuf, 0, msgBuf.length);
			}
			outStream.flush();
		}
		catch (Exception e) {
			System.err.println("LogWriter.processMsgQ() Exception: " + e);
			e.printStackTrace();
		}
		// System.out.println("LogWriter: msgQ elements after: " + msgQ.size());
	}

} // logWriter
