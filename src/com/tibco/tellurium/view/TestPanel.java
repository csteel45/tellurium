/*
 * @(#)TestPanel.java
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

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This panel contains the widgets and sub-panels to configure a test. A Test
 * consists of Runs and Runs consists of Requests. This hierarchy is contained
 * within this top-most level.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 *
 * @see tibco.tellurium.RunPanel
 */
public class TestPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JCheckBox runPerfSuite;
	private final RunPanel runPanel = new RunPanel();

	public TestPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(runPanel);

		JPanel perfTestPanel = new JPanel();
		runPerfSuite = new JCheckBox();
		runPerfSuite.setSelected(false);
		perfTestPanel.add(runPerfSuite);
		perfTestPanel.add(Box.createHorizontalStrut(1));
		perfTestPanel.add(new JLabel("Run Performance Test Suite"));
		add(perfTestPanel);

		setAlignmentX(LEFT_ALIGNMENT);
		setPreferredSize(new Dimension(350, 110));
		setMaximumSize(new Dimension(350, 120));

		this.setVisible(true);
	}

	public int getNumRequests() {
		return runPanel.getNumRequests();
	}

	public boolean runPerfSuite() {
		return this.runPerfSuite.isSelected();
	}

	public long getDelay() {
		return runPanel.getDelay();
	}

	/**
	 * @return
	 */
	public String getRequestDir() {
		return this.runPanel.getRequestDir();
	}

	/**
	 * @return
	 */
	public boolean isSimultaneous() {
		// TODO Auto-generated method stub
		return runPanel.isSimultaneous();
	}

}
