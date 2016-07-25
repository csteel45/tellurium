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

package com.FortMoon.tellurium.view.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Tracks Memory allocated & used, displayed in graph form.
 */
public class MemoryMonitor extends JPanel {

	private static final long serialVersionUID = -3899879807505310822L;
	static JCheckBox dateStampCB = new JCheckBox("Output Date Stamp");
	public final static transient Surface surface = new Surface();
	JPanel controlPanel;
	boolean doControls = false;
	JTextField textField;

	public MemoryMonitor() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder(new EtchedBorder(), "Memory Monitor"));
		add(surface);
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(135, 80));
		Font font = new Font("serif", Font.PLAIN, 10);
		JLabel label = new JLabel("Sample Rate");
		label.setFont(font);
		label.setForeground(Color.black);
		controlPanel.add(label);
		textField = new JTextField("1000");
		textField.setPreferredSize(new Dimension(45, 20));
		controlPanel.add(textField);
		controlPanel.add(label = new JLabel("ms"));
		label.setFont(font);
		label.setForeground(Color.black);
		controlPanel.add(dateStampCB);
		dateStampCB.setFont(font);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				removeAll();
				if ((doControls == false)) {
					try {
						surface.sleepAmount = Long.parseLong(textField
								.getText().trim());
					}
					catch (Exception ex) {
					}
					surface.start();
					add(surface);
				}
				else {
					surface.stop();
					add(controlPanel);
					doControls = false;
				}
				validate();
				repaint();
			}
		});
		surface.start();
		this.setVisible(true);
	}

	public static class Surface extends JPanel implements Runnable {

		/**
		 * TODO Add description.
		 */
		static final long serialVersionUID = -7208851321497431137L;
		public transient Thread thread;
		public long sleepAmount = 200;
		private int w, h;
		private transient BufferedImage bimg;
		private transient Graphics2D big;
		private final Font font = new Font("Times New Roman", Font.PLAIN, 11);
		private final Runtime r = Runtime.getRuntime();
		private int columnInc;
		private int pts[];
		private int ptNum;
		private int ascent, descent;
		private float freeMemory, totalMemory;
		private final Rectangle graphOutlineRect = new Rectangle();
		private final Rectangle2D mfRect = new Rectangle2D.Float();
		private final Rectangle2D muRect = new Rectangle2D.Float();
		private final Line2D graphLine = new Line2D.Float();
		private final Color graphColor = new Color(46, 139, 87);
		private final Color mfColor = new Color(0, 100, 0);
		private String usedStr;
		private static final String allocStr = "K allocated";
		private static final String usedKStr = "K used";
		int graphX;
		int graphY;
		int graphW;
		int graphH;
		float ssH;
		float remainingHeight;
		float blockHeight;
		float blockWidth = 20.0f;
		float remainingWidth;
		int MemUsage;

		public Surface() {
			setBackground(Color.black);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (thread == null)
						start();
					else
						stop();
				}
			});
		}

// public Dimension getMinimumSize() {
// return getPreferredSize();
// }

// public Dimension getMaximumSize() {
// return getPreferredSize();
// }

// public Dimension getPreferredSize() {
// return new Dimension(135, 80);
// }

		@Override
		public void paint(Graphics g) {

			if (big == null) {
				return;
			}

			big.setBackground(getBackground());
			big.clearRect(0, 0, w, h);

			freeMemory = r.freeMemory();
			totalMemory = r.totalMemory();

			// .. Draw allocated and used strings ..
			big.setColor(Color.green);
			big.drawString(String.valueOf((int) totalMemory / 1024) + allocStr,
					4.0f, ascent + 0.5f);
			usedStr = String.valueOf(((int) (totalMemory - freeMemory)) / 1024)
					+ usedKStr;
			big.drawString(usedStr, 4, h - descent);

			// Calculate remaining size
			ssH = ascent + descent;
			remainingHeight = (h - (ssH * 2) - 0.5f);
			blockHeight = remainingHeight / 10;
			blockWidth = 20.0f;
			remainingWidth = (w - blockWidth - 10);

			// .. Memory Free ..
			big.setColor(mfColor);
			MemUsage = (int) ((freeMemory / totalMemory) * 10);
			int i = 0;
			for (; i < MemUsage; i++) {
				mfRect.setRect(5, ssH + i * blockHeight, blockWidth,
						blockHeight - 1);
				big.fill(mfRect);
			}

			// .. Memory Used ..
			big.setColor(Color.green);
			for (; i < 10; i++) {
				muRect.setRect(5, ssH + i * blockHeight, blockWidth,
						blockHeight - 1);
				big.fill(muRect);
			}

			// .. Draw History Graph ..
			big.setColor(graphColor);
			graphX = 30;
			graphY = (int) ssH;
			graphW = w - graphX - 5;
			graphH = (int) remainingHeight;
			graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
			big.draw(graphOutlineRect);

			int graphRow = graphH / 10;

			// .. Draw row ..
			for (int j = graphY; j <= graphH + graphY; j += graphRow) {
				graphLine.setLine(graphX, j, graphX + graphW, j);
				big.draw(graphLine);
			}

			// .. Draw animated column movement ..
			int graphColumn = graphW / 15;

			if (columnInc == 0) {
				columnInc = graphColumn;
			}

			for (int j = graphX + columnInc; j < graphW + graphX; j += graphColumn) {
				graphLine.setLine(j, graphY, j, graphY + graphH);
				big.draw(graphLine);
			}

			--columnInc;

			if (pts == null) {
				pts = new int[graphW];
				ptNum = 0;
			}
			else if (pts.length != graphW) {
				int tmp[] = null;
				if (ptNum < graphW) {
					tmp = new int[ptNum];
					System.arraycopy(pts, 0, tmp, 0, tmp.length);
				}
				else {
					tmp = new int[graphW];
					System.arraycopy(pts, pts.length - tmp.length, tmp, 0,
							tmp.length);
					ptNum = tmp.length - 2;
				}
				pts = new int[graphW];
				System.arraycopy(tmp, 0, pts, 0, tmp.length);
			}
			else {
				big.setColor(Color.yellow);
				pts[ptNum] = (int) (graphY + graphH
						* (freeMemory / totalMemory));
				for (int j = graphX + graphW - ptNum, k = 0; k < ptNum; k++, j++) {
					if (k != 0) {
						if (pts[k] != pts[k - 1]) {
							big.drawLine(j - 1, pts[k - 1], j, pts[k]);
						}
						else {
							big.fillRect(j, pts[k], 1, 1);
						}
					}
				}
				if (ptNum + 2 == pts.length) {
					// throw out oldest point
					for (int j = 1; j < ptNum; j++) {
						pts[j - 1] = pts[j];
					}
					--ptNum;
				}
				else {
					ptNum++;
				}
			}
			g.drawImage(bimg, 0, 0, this);
		}

		public void start() {
			thread = new Thread(this);
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setName("MemoryMonitor");
			thread.start();
		}

		public synchronized void stop() {
			thread = null;
			notifyAll();
		}

		public void run() {

			Thread me = Thread.currentThread();

			while ((thread == me) && !isShowing()) {
				try {
					thread.sleep(500);
				}
				catch (InterruptedException e) {
					return;
				}
			}

			while ((thread == me) && isShowing()) {
				Dimension d = getSize();
				if ((d.width != w) || (d.height != h)) {
					w = d.width;
					h = d.height;
					bimg = (BufferedImage) createImage(w, h);
					big = bimg.createGraphics();
					big.setFont(font);
					FontMetrics fm = big.getFontMetrics(font);
					ascent = fm.getAscent();
					descent = fm.getDescent();
				}
				repaint();
				try {
					thread.sleep(sleepAmount);
				}
				catch (InterruptedException e) {
// break;
				}
				if (MemoryMonitor.dateStampCB.isSelected()) {
					System.out.println(new Date().toString() + " " + usedStr);
				}
			}
			thread = null;
		}
	}

	public static void display() {
		final MemoryMonitor demo = new MemoryMonitor();
		WindowListener l = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				demo.surface.start();
			}

			@Override
			public void windowIconified(WindowEvent e) {
				demo.surface.stop();
			}
		};
		JFrame f = new JFrame("Java2D Demo - MemoryMonitor");
		f.addWindowListener(l);
		f.getContentPane().add("Center", demo);
		f.pack();
		f.setSize(new Dimension(200, 200));
		f.setVisible(true);
		demo.surface.start();
	}

	public static void main(String s[]) {
		MemoryMonitor.display();
	}
}
