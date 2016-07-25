/*
 * @(#)ResultsPanel.java
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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumnModel;

import com.FortMoon.tellurium.model.Client;
import com.FortMoon.tellurium.model.RequestStats;

/**
 * TODO Add docs.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 */
public class ResultsPanel extends JPanel {
	private static final long serialVersionUID = -6465325987668742043L;
	private final JTable resultTable;
	private final ResultTableModel dataModel;
	private final JProgressBar progressBar;
	private int requestsComplete = 0;
	private int numRequests = 0;

	public ResultsPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// setLayout(new FlowLayout(FlowLayout.LEFT));

		// Instead of making the table display the data normally with:
		// JTable resultTable = new JTable(dataModel);
		// Add a sorter, use following three lines instead;
		// JTable resultTable = new JTable(sorter);

		dataModel = new ResultTableModel();
		resultTable = new JTable(dataModel);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		resultTable.getTableHeader().setReorderingAllowed(true);
		resultTable.getTableHeader().setResizingAllowed(true);
		resultTable.setColumnSelectionAllowed(true);
		resultTable.setBackground(Color.WHITE);

		// Set up column sizes.
		TableColumnModel cm = resultTable.getColumnModel();
		cm.getColumn(0).setPreferredWidth(50);
		cm.getColumn(1).setPreferredWidth(150);
		cm.getColumn(2).setPreferredWidth(180);
		cm.getColumn(3).setPreferredWidth(250);

		JScrollPane scrollpane = new JScrollPane(resultTable);
		scrollpane.setPreferredSize(new Dimension(450, 175));
		scrollpane.setBackground(Color.WHITE);

		// add(Box.createVerticalStrut(10));
		add(scrollpane);

		// Create progerss bar indicator
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.BLUE);
		progressBar.setBackground(Color.LIGHT_GRAY);
		progressBar.setPreferredSize(new Dimension(450, 20));
		progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(progressBar);
	}

	public void setRequestNumber(int num, boolean clear) {
		numRequests = num;
		dataModel.clear();
		for (int i = 0; i < num; i++) {
// tableData.insertElementAt(null, i);
// fireTableRowsInserted(i, i);
			dataModel.addStats();
		}
		progressBar.setValue(0);
		progressBar.setMinimum(0);
		progressBar.setMaximum(num);
		if (clear) {
			progressBar.setString(null);
		}
		progressBar.setStringPainted(clear);
	}

// FIXME Make sure that removing synchro worked.
	public void updateStats(RequestStats cs) {
// System.out.println("ResultsPanel updateStats called for: " +
// cs.getRequestNumber());
		int rowCount = dataModel.getRowCount();
		boolean found = false;
		for (int i = 0; i < rowCount; i++) {
			Integer value = (Integer) dataModel.getValueAt(i, 0);
// System.out.println("Checking table value = " + value);
			if (value.equals(cs.getRequestNumber())) {
// System.out.println("Found value = " + value);
				dataModel.setValueAt(cs, i);
				found = true;
				break;
			}
		}
		if (!found) {
			System.err.println("ERROR finding stats for = "
					+ cs.getRequestNumber());
		}

		if (cs.isComplete()) {
			requestsComplete++;
			if (progressBar.getValue() < progressBar.getMaximum()) {
				progressBar.setValue(progressBar.getValue() + 1);
			}
		}

		if (requestsComplete == numRequests) {
			if (cs.getState() == Client.FAILURE) {
				progressBar.setForeground(Color.RED);
			}
			else {
				progressBar.setForeground(Color.GREEN);
			}
		}
	}

	public void clearResults() {
		dataModel.clear();
		dataModel.fireTableDataChanged();
		progressBar.setForeground(Color.BLUE);
		this.numRequests = 0;
		this.requestsComplete = 0;
	}

	public void setTotalTime(float time) {
		progressBar.setString("100 % Total elapsed time: " + time);
	}

}
