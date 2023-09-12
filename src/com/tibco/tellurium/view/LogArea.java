/*
 * @(#)LogArea.java
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
package com.tibco.tellurium.view;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JTextArea;

/**
 * This class provides a TextArea for log output. It extends OutputStream with
 * which to direct log outputs and contains a clear() method for clearing
 * previously written log entries in the LogArea.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 * 
 * @see java.io.OutputStream
 * @see javax.swing.JTextArea
 */
public class LogArea extends OutputStream implements Serializable {
	private static final long serialVersionUID = 1L;
	private static LogArea log = null;
	private JTextArea logArea = null;
	private int logNum = 1;
// private StringBuffer buffer = new StringBuffer();
	private final StringBuffer logOut = new StringBuffer();

	private LogArea() {
		super();
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		logArea.setForeground(Color.BLUE);
		logNum = 1;
	}

	public static synchronized LogArea getInstance() {
		if (log == null)
			log = new LogArea();
		return (log);
	}

	public synchronized void clear() {
		try {
			System.out.println("LogArea Clear called");
			logArea.getDocument().remove(0, logArea.getDocument().getLength());
		}
		catch (Exception e) {
			System.err.println("Error clearing log area [" + e + "]");
		}
	}

	/**
	 * Write a byte of data to the stream. If it is not a newline, then the byte
	 * is appended to the internal buffer. If it is a newline, then the
	 * currently buffered line is sent to the log's output area, prefixed with
	 * the appropriate logging information.
	 */
	@Override
	public void write(int b) {
		if (b == '\n') {
			// synchronize on "this" first to avoid potential deadlock
			synchronized (this) {
				synchronized (logOut) {

					// finally, write the already converted bytes of
					// the log message
					logArea.append(logOut.toString() + "\n");
					logOut.setLength(0);
					logNum++;
				}
			}
		}
		else {
			Character c = Character.valueOf((char) b);
			logOut.append(c.toString());
		}
	}

	/**
	 * Write a subarray of bytes. Pass each through write byte method.
	 */
	@Override
	public void write(byte b[], int off, int len) {
		if (len < 0)
			throw new ArrayIndexOutOfBoundsException(len);
		for (int i = 0; i < len; ++i)
			write(b[off + i]);
	}

	public synchronized JComponent getComponent() {
		if (log == null)
			getInstance();
		return (logArea);
	}

}
