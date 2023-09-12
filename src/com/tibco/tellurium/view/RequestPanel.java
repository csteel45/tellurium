/**
 * @(#)RequestPanel.java $Revision: 1.0 $ $Date: Aug 24, 2007 8:01:54 PM $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * TODO Add docs.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 8:01:54 PM
 * 
 */
public class RequestPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public JTextField requestDir;
	public JTextField queryTarget;
	private final RequestPanel me;

	public RequestPanel() {
		me = this;

		setLayout(new FlowLayout(FlowLayout.LEFT));
		requestDir = new JTextField("xml/requests/");
// requestDir.setFont(new Font("Monospaced", Font.PLAIN, 12));
		requestDir.setPreferredSize(new Dimension(420, 20));
		requestDir.setMinimumSize(new Dimension(220, 20));
		requestDir.setMaximumSize(new Dimension(220, 20));
		add(new JLabel("XML Client Dir"));
		add(requestDir);
		JButton fileButton = new JButton("...");
		fileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File("."));
				chooser.showDialog(me, "Select");
				File dir = chooser.getSelectedFile();
				if (dir == null) {
					return;
				}
				try {
// System.out.println("Selected Dir: " + dir.getCanonicalPath());
					me.setRequestDir(dir.getCanonicalPath());
				}
				catch (IOException ioe) {
					System.out.println("IOException getting request dir: "
							+ ioe);
					ioe.printStackTrace();
				}
			}

		});
		add(fileButton);

		this.setVisible(true);
	}

	public String getQueryTarget() {
		String s = null;
		try {
			s = queryTarget.getText();
		}
		catch (NullPointerException n) {
			System.out.println("Query target is empty");
		}
		return (s);
	}

	public String getRequestDir() {
		return requestDir.getText();
	}

	public void setRequestDir(String requestDir) {
		this.requestDir.setText(requestDir);
	}

}
