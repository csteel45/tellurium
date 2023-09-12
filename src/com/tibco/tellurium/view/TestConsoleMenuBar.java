/**
 * @(#)TestConsoleMenuBar.java $Revision: 1.0 $ $Date: Aug 27, 2007 9:44:11 AM $
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This class constructs the JMenu bar for the TestConsole application. It
 * controls the menu settings and issues the appropriate call-backs.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 27, 2007 9:44:11 AM
 *
 * @see tibco.tellurium.TestConsole
 * @see javax.swing.JMenuBar
 */
public class TestConsoleMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private transient ActionListener listener;

	/**
	 * Private constructor to prevent construction without appropriate event
	 * handlers.
	 *
	 */
	@SuppressWarnings("unused")
	private TestConsoleMenuBar() {

	}

	/**
	 * Public constructor for creating a TestCosnoleMenuBar.
	 *
	 * @param jFrame
	 *            The JFrame instance of the owning application.
	 * @param aListener
	 *            An action listener for handling menu events.
	 */
	public TestConsoleMenuBar(JFrame jFrame, ActionListener aListener) {
		super();
		this.frame = jFrame;
		this.listener = aListener;

		JMenu menu;
		JMenuItem menuItem;

		menu = new JMenu("File");
		this.add(menu);

		menuItem = menu.add(new JMenuItem("Reset"));
		menuItem.addActionListener(listener);

		menuItem = menu.add(new JMenuItem("Exit"));
		menuItem.addActionListener(listener);

		/* TestConsole related stuff. */
		menu = new JMenu("Options");
		this.add(menu);

		menuItem = menu.add(new LookAndFeelMenu(frame, "Look and Feel"));
		// menuItem.addActionListener(new ThemeAction());

		menu.addSeparator();
		menuItem = menu.add(new JMenuItem("Properties"));
		menuItem.addActionListener(listener);

		/* TestConsole related stuff. */
		menu = new JMenu("Help");
		try {
			menuItem = menu.add(new JMenuItem("Documentation"));
			menuItem.addActionListener(listener);
		}
		catch (Exception mue) {
			System.out.println("Exception: " + mue);
		}
		menu.addSeparator();
		menuItem = menu.add(new JMenuItem("About Test Console"));

		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AboutDialog dialog = new AboutDialog(frame,
						"Tellurium Test Console", "Christopher Steel", "1.0",
						"2007-08-13", "Copyright � 2007 FortMoon Consulting",
						"Glasgow", "images/tellurium-icon.jpg");
				dialog.setVisible(true);
			}
		});

		this.add(Box.createHorizontalGlue());
		this.add(menu);

		this.setVisible(true);
	}

}
