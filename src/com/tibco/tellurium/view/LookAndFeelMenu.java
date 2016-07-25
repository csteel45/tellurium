/*
 * @(#)LookAndFeelMenu.java $Revision: 1.1 $ $Date: 2000/11/08 21:01:33 $
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
package com.FortMoon.tellurium.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Provides a menu item for setting the Look and Feel.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 */
public class LookAndFeelMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	JFrame frame;

	public LookAndFeelMenu(JFrame parentFrame, String title) {
		super(title);
		this.frame = parentFrame;
		JMenuItem aquaItem = new JMenuItem("Liquid");
		aquaItem.setMnemonic('L');
		aquaItem.setName("Liquid");
		JMenuItem windowsItem = new JMenuItem("Windows");
		windowsItem.setMnemonic('W');
		windowsItem.setName("Windows");
		JMenuItem motifItem = new JMenuItem("Motif");
		motifItem.setName("Motif");
		motifItem.setMnemonic('O');
		JMenuItem metalItem = new JMenuItem("Metal");
		metalItem.setName("Metal");
		metalItem.setMnemonic('M');
		JMenuItem systemItem = new JMenuItem("System");
		metalItem.setName("System");
		metalItem.setMnemonic('S');
		this.setMnemonic('L');
		this.add(aquaItem);
		this.add(windowsItem);
		this.add(motifItem);
		this.add(metalItem);
		this.add(systemItem);

		aquaItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel", frame);
			}
		});
		windowsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookAndFeel(
						"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
						frame);
			}
		});
		motifItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookAndFeel(
						"com.sun.java.swing.plaf.motif.MotifLookAndFeel", frame);
			}
		});
		metalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel", frame);
			}
		});
		systemItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setLookAndFeel(UIManager.getSystemLookAndFeelClassName(), frame);
			}
		});
	}

	private static void setLookAndFeel(String lookAndFeelClass, Component c) {
		try {
			UIManager.setLookAndFeel(lookAndFeelClass);
			SwingUtilities.updateComponentTreeUI(c);
		}
		catch (Exception e) {
			System.out
					.println("LookAndFeelMenu.setLookAndFeel exception: " + e);
			e.printStackTrace();
		}
	}
}
