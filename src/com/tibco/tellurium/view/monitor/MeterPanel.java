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

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MeterPanel extends JPanel {
	static final long serialVersionUID = 1L;
	Meter meter;
	MonitorFeed feed;

	public MeterPanel() {
		super();
		init();
	}

	public MeterPanel(MonitorFeed feed) {
		super();
		this.feed = feed;
		init();
	}

	public void init() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder(new EtchedBorder(), "Requests"));
// setBorder(BorderFactory.createRaisedBevelBorder());
		meter = new Meter();
// meter.registerFeed(feed);
		add(meter);
// this.setBackground(Color.black);
// this.setVisible(true);
	}

	public Meter getMeter() {
		return meter;
	}

	public void setActivity(double count) {
		meter.activityNotify(count);
	}

	@Override
	public Dimension getPreferredSize() {
		return (new Dimension(80, Integer.MAX_VALUE));
	}

	public void setFeed(MonitorFeed feed) {
		this.feed = feed;
		meter.registerFeed(this.feed);
	}

	void setIndicateOnLine() {
		meter.setActivityBarColor(Color.green);
		meter.setCounterColor(Color.green);
	}

	void setIndicateOffLine() {
		meter.setActivityBarColor(Color.red);
		meter.setCounterColor(Color.red);
	}

/*
 * public static void main(String[] args) { JFrame frame = new JFrame();
 * MeterPanel mp = new MeterPanel(); frame.getContentPane().add(mp);
 * frame.pack(); frame.setVisible(true); }
 */
}
