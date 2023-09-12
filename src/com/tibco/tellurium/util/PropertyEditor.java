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
package com.tibco.tellurium.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSeparator;

import com.tibco.tellurium.view.AboutDialog;
import com.tibco.tellurium.view.LookAndFeelMenu;

/**
 * GUI for editing and saving properties files.
 *
 * @author Chris Steel - Sun Java Center
 * @version $Revision: 1.3 $
 * @date $Date: 2002/03/16 04:59:32 $
 * @since 1.4
 */
public class PropertyEditor extends javax.swing.JFrame implements
		javax.swing.event.ListSelectionListener {
	static final long serialVersionUID = 1L;
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenuItem newMenuItem;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenuItem importMenuItem;
	private javax.swing.JMenuItem saveMenuItem;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JSeparator exitSeparator;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu optionsMenu;
	private javax.swing.JMenuItem editSystemPropertiesMenuItem;
	private javax.swing.JMenuItem importSystemPropertiesMenuItem;
	private javax.swing.JMenuItem setSystemPropertiesMenuItem;
	private javax.swing.JMenuItem addSystemPropertiesMenuItem;
	private LookAndFeelMenu lookAndFeelMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JButton setButton;
	private javax.swing.JButton removeButton;
	private javax.swing.JPanel propPanel;
	private javax.swing.JPanel propertyPanel;
	private javax.swing.JPanel namePanel;
	private java.awt.Label nameLabel;
	private javax.swing.JTextField nameTextField;
	private javax.swing.JPanel valuePanel;
	private java.awt.Label valueLabel;
	private javax.swing.JTextField valueTextField;
	private javax.swing.JScrollPane scrollPane;
	private javax.swing.JTable propTable;
	private PropertyModel propertyModel;
	private static JFrame frame = null;
	private String fileName = null;
	private final String DEFAULT_FILENAME = "config/tellurium.properties";
	private final Dimension frameSize;
	// End of variables declaration//GEN-END:variables
// private static ClassLoader cl;
// private static Initiator initiator;

	static Preferences prefs = Preferences.systemRoot().node(
			"/org/tellurium");

	/** Creates new form PropertyEditor */
	public PropertyEditor() {
		frame = this;
		try {
			SplashScreen splash = new SplashScreen(frame,
					"images/console-logo.gif");
			propertyModel = new PropertyModel();
			fileName = prefs.get("FileName", null);
			if (fileName != null)
				propertyModel.load(new FileInputStream(fileName));
			else {
				propertyModel.load(new FileInputStream(DEFAULT_FILENAME));
			}
		}
		catch (Exception e) {
			System.out.println("Exception" + e.toString());
		}
		initComponents();
// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
// setBackground(BORDERCOLOR);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/magnify.gif"));
		pack();
		frameSize = this.getSize();
		int w = (int) frameSize.getWidth();
		int h = (int) frameSize.getHeight();
		int x = (screenSize.width - w) / 2;
		int y = (screenSize.height - h) / 2;
		setBounds(x, y, w, h);
		frame.setLocation(
				(int) (screenSize.getWidth() - frameSize.getWidth()) / 2,
				(int) (screenSize.getHeight() - frameSize.getHeight()) / 2);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the FormEditor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		newMenuItem = new javax.swing.JMenuItem();
		openMenuItem = new javax.swing.JMenuItem();
		importMenuItem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitSeparator = new javax.swing.JSeparator();
		exitMenuItem = new javax.swing.JMenuItem();
		optionsMenu = new javax.swing.JMenu();
		editSystemPropertiesMenuItem = new javax.swing.JMenuItem();
		importSystemPropertiesMenuItem = new javax.swing.JMenuItem();
		setSystemPropertiesMenuItem = new javax.swing.JMenuItem();
		addSystemPropertiesMenuItem = new javax.swing.JMenuItem();
//		lookAndFeelMenu = new LookAndFeelMenu(this, "Look and Feel");
		helpMenu = new javax.swing.JMenu();
		aboutMenuItem = new javax.swing.JMenuItem();
		mainPanel = new javax.swing.JPanel();
		buttonPanel = new javax.swing.JPanel();
		setButton = new javax.swing.JButton();
		removeButton = new javax.swing.JButton();
		propPanel = new javax.swing.JPanel();
		propertyPanel = new javax.swing.JPanel();
		namePanel = new javax.swing.JPanel();
		nameLabel = new java.awt.Label();
		nameTextField = new javax.swing.JTextField();
		valuePanel = new javax.swing.JPanel();
		valueLabel = new java.awt.Label();
		valueTextField = new javax.swing.JTextField();
		scrollPane = new javax.swing.JScrollPane();
		propTable = new javax.swing.JTable();

		fileMenu.setText("File");
		fileMenu.setMnemonic('F');

		newMenuItem.setText("New");
		newMenuItem.setMnemonic('N');
		newMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				propertyModel.removeAllRows();
				fileName = null;
			}
		});
		fileMenu.add(newMenuItem);

		openMenuItem.setText("Open");
		openMenuItem.setMnemonic('O');
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openMenuItem);

		importMenuItem.setText("Import");
		importMenuItem.setMnemonic('I');
		importMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				importMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(importMenuItem);

		saveMenuItem.setText("Save");
		saveMenuItem.setMnemonic('S');
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveMenuItem);

		saveAsMenuItem.setText("Save As");
		saveAsMenuItem.setMnemonic('A');
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveAsMenuItem);

		fileMenu.add(exitSeparator);

		exitMenuItem.setText("Exit");
		exitMenuItem.setMnemonic('x');
		fileMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					prefs.sync();
				}
				catch (Exception e) {
					System.out.println("Exception saving prefs: " + e);
				}
				dispose();
			}
		});

		menuBar.add(fileMenu);

		optionsMenu.setText("Options");
		optionsMenu.setMnemonic('O');

		editSystemPropertiesMenuItem.setText("Edit System Properties");
		editSystemPropertiesMenuItem.setMnemonic('E');
		editSystemPropertiesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						editSystemPropertiesMenuItemActionPerformed(evt);
					}
				});
		optionsMenu.add(editSystemPropertiesMenuItem);

		importSystemPropertiesMenuItem.setText("Import System Properties");
		importSystemPropertiesMenuItem.setMnemonic('I');
		importSystemPropertiesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						importSystemPropertiesMenuItemActionPerformed(evt);
					}
				});
		optionsMenu.add(importSystemPropertiesMenuItem);

		setSystemPropertiesMenuItem.setText("Set System Properties");
		setSystemPropertiesMenuItem.setMnemonic('S');
		setSystemPropertiesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						setSystemPropertiesMenuItemActionPerformed(evt);
					}
				});
		optionsMenu.add(setSystemPropertiesMenuItem);

		addSystemPropertiesMenuItem.setText("Add To System Properties");
		addSystemPropertiesMenuItem.setMnemonic('A');
		addSystemPropertiesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						addSystemPropertiesMenuItemActionPerformed(evt);
					}
				});
		optionsMenu.add(addSystemPropertiesMenuItem);
		optionsMenu.add(new JSeparator());
//		optionsMenu.add(lookAndFeelMenu);

		menuBar.add(optionsMenu);

		// helpMenu.setIcon(new javax.swing.ImageIcon("null"));
		helpMenu.setText("Help");
		helpMenu.setMnemonic('H');

		aboutMenuItem.setText("About");
		aboutMenuItem.setMnemonic('A');
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String version = "$Revision: 1.3 $";
				version = version.substring(11, version.lastIndexOf("$") - 1);
				String buildDate = "$Date: 2002/03/16 04:59:32 $";
				buildDate = buildDate.substring(7,
						buildDate.lastIndexOf("$") - 1);
				AboutDialog ad = new AboutDialog(frame, "Property Editor",
						"Christopher Steel - Principal Architect", version,
						buildDate, "Copyright(c) 2007 FortMoon Consulting",
						"Glasgow", "images/console-logo.gif");
				ad.setLocationRelativeTo(frame);
				ad.setVisible(true);
			}
		});

		helpMenu.add(aboutMenuItem);
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(helpMenu);

		setName("mainFrame");
		setTitle("Property Editor");
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		mainPanel.setLayout(new java.awt.BorderLayout());
		mainPanel.setBorder(new javax.swing.border.BevelBorder(0));
		mainPanel.setName("Property Editor");

		setButton.setText("Set");
		setButton.setActionCommand("setButton");
		setButton.setFont(new java.awt.Font("Dialog", 0, 14));
		setButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(setButton);

		removeButton.setActionCommand("removeButton");
		removeButton.setFont(new java.awt.Font("Dialog", 0, 14));
		removeButton.setText("Remove");
		removeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(removeButton);

		mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

		propPanel.setLayout(new java.awt.BorderLayout());
		propPanel.setBorder(new javax.swing.border.TitledBorder(
				new javax.swing.border.EtchedBorder(), "Properties", 4, 2,
				new java.awt.Font("Dialog", 0, 14), java.awt.Color.black));

		propertyPanel.setLayout(new java.awt.BorderLayout());

		namePanel.setLayout(new java.awt.FlowLayout(0, 5, 5));

		nameLabel.setFont(new java.awt.Font("Dialog", 0, 14));
		nameLabel.setName("nameLabel");
		nameLabel.setForeground(java.awt.Color.black);
		nameLabel.setText("Name:");

		namePanel.add(nameLabel);

		nameTextField.setPreferredSize(new java.awt.Dimension(300, 25));
		nameTextField.setToolTipText("Enter name.");
		nameTextField.setBorder(new javax.swing.border.BevelBorder(1,
				java.awt.Color.white, java.awt.Color.lightGray,
				java.awt.Color.darkGray, java.awt.Color.gray));
		nameTextField.setMinimumSize(new java.awt.Dimension(300, 25));

		namePanel.add(nameTextField);

		propertyPanel.add(namePanel, java.awt.BorderLayout.NORTH);

		valuePanel.setLayout(new java.awt.FlowLayout(0, 5, 5));

		valueLabel.setFont(new java.awt.Font("Dialog", 0, 14));
		valueLabel.setName("valueLabel");
		valueLabel.setForeground(java.awt.Color.black);
		valueLabel.setText("Value:");

		valuePanel.add(valueLabel);

		valueTextField.setPreferredSize(new java.awt.Dimension(300, 25));
		valueTextField.setToolTipText("Enter value");
		valueTextField.setBorder(new javax.swing.border.BevelBorder(1,
				java.awt.Color.white, java.awt.Color.lightGray,
				java.awt.Color.darkGray, java.awt.Color.gray));
		valueTextField.setMinimumSize(new java.awt.Dimension(300, 25));

		valuePanel.add(valueTextField);

		propertyPanel.add(valuePanel, java.awt.BorderLayout.SOUTH);

		propPanel.add(propertyPanel, java.awt.BorderLayout.SOUTH);

		scrollPane.setBorder(new javax.swing.border.BevelBorder(1));

		propTable.setSelectionForeground(java.awt.Color.white);
		propTable.setModel(propertyModel);
		propTable.setSelectionBackground(new java.awt.Color(0, 0, 153));
		propTable.setShowHorizontalLines(false);
		propTable.setShowVerticalLines(true);
// propTable.setBackground(Color.black);
		propTable.setForeground(Color.blue);
		propTable.getSelectionModel().addListSelectionListener(this);
		scrollPane.setViewportView(propTable);

		propPanel.add(scrollPane, java.awt.BorderLayout.CENTER);
		mainPanel.add(propPanel, java.awt.BorderLayout.CENTER);
		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);
		setJMenuBar(menuBar);
	}// GEN-END:initComponents

	private void setButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setButtonActionPerformed
		// Add your handling code here:
		if (nameTextField.getText().length() > 0) {
			propertyModel.addRow(nameTextField.getText(), valueTextField
					.getText());
		}
		else
			System.out.println("Please specify a name");
		clearFields();
	}// GEN-LAST:event_setButtonActionPerformed

	private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_removeButtonActionPerformed
		// Add your handling code here:
		if (propTable.getSelectedRowCount() > 0) {
			int selectedRows[] = propTable.getSelectedRows();
			String[] keys = new String[selectedRows.length];
			for (int i = 0; i < selectedRows.length; i++) {
				keys[i] = (String) propTable.getValueAt(selectedRows[i], 0);
			}
			for (int i = 0; i < selectedRows.length; i++) {
				propertyModel.removeRow(keys[i]);
			}
		}
		else {
			if (nameTextField.getText() != null) {
				propertyModel.removeRow(nameTextField.getText());
			}
		}
		clearFields();
	}// GEN-LAST:event_removeButtonActionPerformed

	public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
		int row = propTable.getSelectedRow();
		if (row >= 0) {
			nameTextField.setText((String) propTable.getValueAt(row, 0));
			valueTextField.setText((String) propTable.getValueAt(row, 1));
		}
		else
			clearFields();
	}

	private void setFields(int row) {
		nameTextField.setText((String) propTable.getValueAt(row, 0));
		valueTextField.setText((String) propTable.getValueAt(row, 1));
	}

	private void clearFields() {
		nameTextField.setText(null);
		valueTextField.setText(null);
	}

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_systemPropertiesMenuItemActionPerformed
		String configDir = null;
// String configDir = ClassLoader.getSystemResource("config").getFile();
		JFileChooser chooser = null;
		if (configDir != null)
			chooser = new JFileChooser(new File(configDir));
		else
			chooser = new JFileChooser(new File("."));
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setDialogTitle("Select Properties File");
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory())
					return (true);
				return (file.getName().endsWith(".cfg"));
			}

			@Override
			public String getDescription() {
				return ("Properties Files");
			}
		});
		File configFile = null;
		if ((chooser.showOpenDialog(this)) == JFileChooser.APPROVE_OPTION) {
			configFile = chooser.getSelectedFile();
			try {
				propertyModel.removeAllRows();
				propertyModel.load(new FileInputStream(configFile));
			}
			catch (Exception e) {
				System.out.println("Exception opening: "
						+ configFile.getAbsolutePath());
				return;
			}
			fileName = configFile.getAbsolutePath();
			prefs.put("FileName", fileName);
			System.out.println("Opened: " + fileName);
		}
	}// GEN-LAST:event_systemPropertiesMenuItemActionPerformed

	private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_systemPropertiesMenuItemActionPerformed
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setDialogTitle("Select Properties File");
		chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isDirectory())
					return (true);
				return (file.getName().endsWith(".cfg"));
			}

			@Override
			public String getDescription() {
				return ("Properties Files");
			}
		});
		File configFile = null;
		if ((chooser.showOpenDialog(this)) == JFileChooser.APPROVE_OPTION) {
			configFile = chooser.getSelectedFile();
			try {
				propertyModel.load(new FileInputStream(configFile));
			}
			catch (Exception e) {
				System.out.println("Exception opening: "
						+ configFile.getAbsolutePath());
				return;
			}
			System.out.println("Imported: " + fileName);
		}
	}// GEN-LAST:event_systemPropertiesMenuItemActionPerformed

	private void editSystemPropertiesMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_systemPropertiesMenuItemActionPerformed
		// Add your handling code here:
		propertyModel.loadSystemProperties();
	}// GEN-LAST:event_systemPropertiesMenuItemActionPerformed

	private void importSystemPropertiesMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_systemPropertiesMenuItemActionPerformed
		// Add your handling code here:
		propertyModel.importSystemProperties();
	}// GEN-LAST:event_systemPropertiesMenuItemActionPerformed

	private void setSystemPropertiesMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_systemPropertiesMenuItemActionPerformed
		// Add your handling code here:
		propertyModel.setSystemProperties();
	}// GEN-LAST:event_systemPropertiesMenuItemActionPerformed

	private void addSystemPropertiesMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_systemPropertiesMenuItemActionPerformed
		// Add your handling code here:
		propertyModel.addSystemProperties();
	}// GEN-LAST:event_systemPropertiesMenuItemActionPerformed

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveMenuItemActionPerformed
		// Add your handling code here:
		if (fileName != null) {
			try {
				FileOutputStream fos = new FileOutputStream(fileName);
				propertyModel.save(fos);
				fos.close();
			}
			catch (Exception e) {
				System.out.println("Exception saving: " + fileName);
			}
		}
		else
			saveAsMenuItemActionPerformed(evt);

	}// GEN-LAST:event_saveMenuItemActionPerformed

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveMenuItemActionPerformed
		JFileChooser chooser = new JFileChooser(new File("."));
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle("Save Properties File");
		File configFile = null;
		if ((chooser.showSaveDialog(this)) == JFileChooser.APPROVE_OPTION) {
			configFile = chooser.getSelectedFile();
			try {
				propertyModel.save(new FileOutputStream(configFile));
			}
			catch (Exception e) {
				System.out.println("Exception saving: "
						+ configFile.getAbsolutePath());
				return;
			}
			fileName = configFile.getAbsolutePath();
			prefs.put("FileName", fileName);
			System.out.println("Saved: " + fileName);
		}
	}// GEN-LAST:event_saveAsMenuItemActionPerformed

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		this.dispose();
	}// GEN-LAST:event_exitForm

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			new PropertyEditor().setVisible(true);
		}
		catch (Exception e) {
			System.err.println("Exception in PropertyEditor: " + e);
			e.printStackTrace();
		}
	}

}
