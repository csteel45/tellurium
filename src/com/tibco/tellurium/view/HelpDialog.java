/*
 * @(#)HelpDialog.java $Revision: 1.1 $ $Date: 2000/11/30 21:07:53 $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;

/**
 * This class implements a frame that displays HTML text describing how to use
 * the application.
 * 
 * Note that the hyperlinks do not work because the Swing's HTML editor doesn't
 * seem to handle URL fragments yet.
 * 
 * @author Rosanna Lee. Modified by Christopher Steel, Principal Architect -
 *         FortMoon.
 * @see javax.swing.event.HyperlinkListener
 */
public class HelpDialog extends JFrame implements HyperlinkListener {
	private static final long serialVersionUID = 1L;
	private JEditorPane html;
	private final String imageFileName = "images/magnify.gif";

	public HelpDialog(String helpFilePath) {
		super("Help");

		setBackground(Color.white);
		setIconImage(Toolkit.getDefaultToolkit().getImage(imageFileName));

		// Workaround for Swing/Net? bug.
		// Must add this because URLConnection doesn't return
		// correct type for resource URL
		JEditorPane.registerEditorKitForContentType("content/unknown",
				"javax.swing.text.html.HTMLEditorKit");

		if (helpFilePath == null) {
			System.err.println("Help document not found");
			this.dispose();
			return;
		}
		File file = new File(".");

		try {
			String path = file.getCanonicalPath();
// System.out.println("URL names: " + path + "/" + helpFilePath);
			URL helpFile = new URL("file:///" + path + "/" + helpFilePath);
// System.out.println("URL: " + helpFile);
			html = new JEditorPane(helpFile);
			html.setEditable(false);
			html.addHyperlinkListener(this);
		}
		catch (IOException e) {
			System.err.println("Cannot open help document: " + e);
			e.printStackTrace();
			this.dispose();
			return;
		}

		// Put editor inside scroller
		JScrollPane scroller = new JScrollPane();
		scroller.setPreferredSize(new Dimension(1000, 750));
		JViewport vp = scroller.getViewport();
		vp.add(html);

		// Add scroller to frame
		getContentPane().add(scroller, BorderLayout.CENTER);

		// Ensures that frame will be destroyed
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		// make visible
		this.setVisible(true);
		pack();
	}

	/**
	 * Notification of a change relative to a hyperlink. %%% From HtmlPane.java
	 * in SwingSet example
	 */
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
// System.out.println("Hyper link event " + e);
			linkActivated(e.getURL());
		}
	}

	/**
	 * Follows the reference in an link. The given url is the requested
	 * reference. By default this calls <a href="#setPage">setPage</a>, and if
	 * an exception is thrown the original previous document is restored and a
	 * beep sounded. If an attempt was made to follow a link, but it represented
	 * a malformed url, this method will be called with a null argument.
	 * 
	 * @param u
	 *            the URL to follow
	 * 
	 * %%% from HtmlPanel.java in SwingSet example
	 */
	protected void linkActivated(URL u) {
// System.out.println("link activated: " + u);
		Cursor c = html.getCursor();
		Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
		html.setCursor(waitCursor);
		SwingUtilities.invokeLater(new PageLoader(u, c));
	}

	/**
	 * temporary class that loads synchronously (although later than the request
	 * so that a cursor change can be done).
	 * 
	 * %%% from HtmlPanel.java in SwingSet example
	 */
	class PageLoader implements Runnable {
		private URL url;
		private final Cursor cursor;

		PageLoader(URL u, Cursor c) {
			url = u;
			cursor = c;
		}

		public void run() {
			if (url == null) {
				// restore the original cursor
				html.setCursor(cursor);

				// PENDING(prinz) remove this hack when
				// automatic validation is activated.
				Container parent = html.getParent();
				parent.repaint();
			}
			else {
				Document doc = html.getDocument();
				try {
					html.setPage(url);
				}
				catch (IOException ioe) {
					html.setDocument(doc);
					getToolkit().beep();
				}
				finally {
					// schedule the cursor to revert after
					// the paint has happended.
					url = null;
					SwingUtilities.invokeLater(this);
				}
			}
		}
	}

}
