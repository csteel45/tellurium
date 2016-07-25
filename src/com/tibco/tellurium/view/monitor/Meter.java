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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Meter extends JPanel implements MonitorFeedListener {
	static final long serialVersionUID = 1L;
	private Color activityColor, counterColor;
	private int activityCounter, total, height, filler, margin;
	private final Font activityFont = new Font("Arial", Font.PLAIN, 10);
	private String count = "";
	private String totalCount = "";
	MonitorFeed feed;

	public Meter() {
		super();
		init();
	}

	public Meter(MonitorFeed feed) {
		this();
		registerFeed(feed);
	}

	public void clear() {
		activityCounter = 0;
		total = 0;
		this.drawActivityCount(getGraphics());
	}

	public void init() {
		total = 0;
		activityCounter = 0;
		height = getHeight();
		filler = 4;
		margin = 14;
		activityColor = Color.green;
		counterColor = Color.green;
		setBackground(Color.black);
		setPreferredSize(new Dimension(100, 250));
	}

	public void registerFeed(MonitorFeed feed) {
		this.feed = feed;
		feed.registerMonitorFeedListener(this);
	}

	public void activityNotify(double count) {
		if (count > activityCounter)
			addActivity();
		if (count < activityCounter)
			removeActivity();
	}

	public void memoryNotify(double count) {
	}

	public void terminate() {
		feed.unregisterMonitorFeedListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawActivityCount(g);
	}

	public void setActivityBarColor(Color color) {
		activityColor = color;
		drawActivityCount(getGraphics());
	}

	public Color getActivityBarColor() {
		return (activityColor);
	}

	void setCounterColor(Color color) {
		counterColor = color;
		drawNumActive(getGraphics());
	}

	public synchronized void addActivity() {
		total++;
		activityCounter++;
		drawActivityCount(getGraphics());
	}

	public synchronized void removeActivity() {
		activityCounter--;
		Graphics g = getGraphics();
		if ((g == null) || ((this.isShowing() == false)))
			return;
		g.setColor(Color.black);
		Dimension d = getSize();
		int yvalue = (d.height - 20) - ((activityCounter * filler) + height);
		g.fill3DRect(margin, yvalue, d.width - (margin * 2), height, false);
		drawActivityCount(g);
	}

	void drawActivityCount(Graphics g) {
		if ((g == null) || (this.isShowing() == false))
			return;
		Dimension d = getSize();
		g.setColor(Color.BLACK);
// g.fill3DRect(0, 0, d.width, d.height, true);
		g.fill3DRect(5, 0, d.width - 10, d.height - 2, false);
		drawNumActive(g);
		int center = d.width / 2;
		int leftextent = center - 1;
		int rightextent = center + 1;
		g.setColor(activityColor);
		for (int lineNumber = 1; lineNumber <= activityCounter; lineNumber++) {
			if ((lineNumber > 10) && (lineNumber < 20)) {
				g.setColor(Color.yellow);
			}
			if (lineNumber >= 20) {
				g.setColor(Color.red);
			}
			int yvalue = (d.height - 20) - ((lineNumber * filler) + height);
			g.fill3DRect(margin, yvalue, leftextent - margin, height, true);
			g
					.fill3DRect(rightextent, yvalue, leftextent - margin,
							height, true);
		}
	}

	void drawNumActive(Graphics g) {
		FontMetrics fm;
		fm = getFontMetrics(activityFont);
// clear = count;
		count = String.valueOf(activityCounter);
		totalCount = String.valueOf(total);
		int tw = fm.stringWidth(totalCount);
		Dimension d = getSize();
		g.setFont(activityFont);
// g.setColor(Color.black);

// g.fillRect(margin, d.height - 20, d.width - margin, 20);
		g.setColor(counterColor);

		g.drawString(count, margin, d.height - 5);
		g.drawString(totalCount, d.width - (margin + tw), d.height - 5);

		g.setColor(Color.blue);
// g.drawLine(margin, d.height - 20, d.width - margin, d.height - 20);
// g.drawLine(margin, d.height - 20, d.width - margin, d.height - 20);
// g.drawLine(margin, d.height - 20, d.width - margin, d.height - 20);
// g.drawLine(margin, d.height - 20, d.width - margin, d.height - 20);
		g.draw3DRect(4, 0, d.width - 10, d.height - 2, true);
	}
}
