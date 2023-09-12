/**
 * @(#)RunPanel.java $Revision: 1.0 $ $Date: Aug 24, 2007 8:01:07 PM $
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
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * This panel contains the widgets and sub-panels to configure a Run. A Run
 * consists of one or more Requests. The requests are configured in the embedded
 * RequestPanel.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 8:01:07 PM
 *
 * @see com.tibco.tellurium.RequestPanel
 */
@SuppressWarnings("unused")
public class RunPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JTextField delay;
	public JTextField numRequests;
	private final RequestPanel requestPanel;
	private boolean simultaneous;
	private final JRadioButton sequentialRB;
	private final JRadioButton simultaneousRB;

	public RunPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		requestPanel = new RequestPanel();
		add(requestPanel);

		JPanel delayPanel = new JPanel();
		delayPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		delay = new JTextField("");
		delay.setFont(new Font("Monospaced", Font.PLAIN, 12));
		delay.setPreferredSize(new Dimension(80, 20));
		delay.setMinimumSize(new Dimension(80, 20));
		delay.setMaximumSize(new Dimension(220, 20));
		delayPanel.add(new JLabel("Sequential Client Delay (ms)"));
		delayPanel.add(delay);
		add(delayPanel);

		JPanel numReqPanel = new JPanel();
		numReqPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		numRequests = new JTextField("1");
		numRequests.setPreferredSize(new Dimension(60, 20));
		numRequests.setMinimumSize(new Dimension(60, 20));
		numRequests.setMaximumSize(new Dimension(60, 20));
		numReqPanel.add(new JLabel("Number of Requests"));
		numReqPanel.add(numRequests);

		add(numReqPanel);

		// Create radio buttons for processing type
		ButtonGroup bg = new ButtonGroup();
		sequentialRB = new JRadioButton("Sequential");
		sequentialRB.setToolTipText("Use for issuing requests sequentially.");
		simultaneousRB = new JRadioButton("Simultaneous", true);
		simultaneousRB
				.setToolTipText("Use for issuing requests simultaneously.");

		// Add into button group
		bg.add(simultaneousRB);
		bg.add(sequentialRB);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		// Add onto panel
		buttonPanel.add(sequentialRB);
		buttonPanel.add(Box.createHorizontalStrut(80));
		buttonPanel.add(simultaneousRB);
		add(buttonPanel);

		this.setVisible(true);
	}

	/**
	 * Returns the number of requests as an integer.
	 *
	 * @return The number of requests to run.
	 */
	public int getNumRequests() {
		if (numRequests.getText().equals(""))
			return 0;
		return Integer.parseInt(numRequests.getText());
	}

	/**
	 * Returns the number of milliseconds to delay before starting the next
	 * requests in a Sequential run.
	 *
	 * @return The number of milliseconds o delay.
	 */
	public int getDelay() {
		if (delay.getText().equals(""))
			return 0;
		return Integer.parseInt(delay.getText());
	}

	/**
	 * @return The XML Client Directory
	 */
	public String getRequestDir() {
		// TODO Auto-generated method stub
		return this.requestPanel.requestDir.getText();
	}

	public boolean isSimultaneous() {
		return (simultaneousRB.isSelected());
	}

}
