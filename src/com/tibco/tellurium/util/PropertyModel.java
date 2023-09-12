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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * PropertyModel provides model interface for Propeties object
 * 
 * @author Chris Steel - Sun Java Center
 * @version $Revision: 1.2 $
 * @date $Date: 2001/06/23 06:17:34 $
 */

public class PropertyModel extends AbstractTableModel {
	static final long serialVersionUID = 1L;
	private final Vector<String> columnNames;
	private Properties properties = new Properties();
	private TreeMap map = new TreeMap(properties);

	public PropertyModel() {
		columnNames = new Vector<String>();
		columnNames.addElement("Name");
		columnNames.addElement("Value");
	}

	public void load(InputStream inputStream) {
		try {
			properties.load(inputStream);
			map = new TreeMap(properties);
		}
		catch (Exception e) {
			System.out.println("Exception loading from input stream: " + e);
		}
		fireTableDataChanged();
	}

	public void save(OutputStream outputStream) {
		try {
			properties.clear();
			properties.putAll(map);
			properties.store(outputStream, null);
		}
		catch (Exception e) {
			System.out.println("Exception storing to output stream: " + e);
		}
		fireTableDataChanged();
	}

	public void loadSystemProperties() {
		map.clear();
		map.putAll(System.getProperties());
		fireTableDataChanged();
	}

	public void setSystemProperties() {
		System.getProperties().clear();
		System.getProperties().putAll(map);
		fireTableDataChanged();
	}

	public void importSystemProperties() {
		map.putAll(System.getProperties());
		fireTableDataChanged();
	}

	public void addSystemProperties() {
		System.getProperties().putAll(map);
		fireTableDataChanged();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		super.clone();
		PropertyModel copyObj = new PropertyModel();
		copyObj.properties = (Properties) properties.clone();
		copyObj.map = (TreeMap) map.clone();
		return (copyObj);
	}

	public int getColumnCount() {
		return (columnNames.size());
	}

	public int getRowCount() {
		return (map.size());
	}

	@Override
	public String getColumnName(int col) {
		return columnNames.elementAt(col);
	}

	public Object getValueAt(int row, int col) {
		// System.out.println("getValueAt called for row: " + row);
		if (col == 0)
			return map.keySet().toArray()[row];
		else
			return (map.values().toArray()[row]);
	}

	public void changeRowAt(String name, String value, int row) {
		// System.out.println("changeRowAt called for row: " + row);
		if (map.size() > row) {
			map.put(name, value);
			fireTableDataChanged();
		}
		else {
			addRow(name, value);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		// System.out.println("setValueAt called for row: " + row);
		if (column == 0) {
			String val = (String) getValueAt(row, 1);
			map.put(value, val);
		}
		else {
			String key = (String) getValueAt(row, 0);
			map.put(key, value);
		}
		fireTableRowsUpdated(row, row);
	}

	public void addRow(String name, String value) {
		// System.out.println("addRow for name: " + name);
		map.put(name, value);
		fireTableDataChanged();
	}

	public boolean isRow(String key) {
		if (map.containsKey(key)) {
			return (true);
		}
		return (false);
	}

	public void removeRow(String key) {
		// System.out.println("remove row: " + key);
		map.remove(key);
		fireTableDataChanged();
	}

	public void removeRowAt(int index) {
		// System.out.println("removeRowAt: " + index);
		String key = (String) getValueAt(index, 0);
		map.remove(key);
		fireTableDataChanged();
	}

	public void removeAllRows() {
		// System.out.println("removeAllRows called");
		map.clear();
		fireTableDataChanged();
	}

	public Object getRow(int row) {
		// System.out.println("getRow called for: " + row);
		return (map.keySet().toArray()[row]);
	}
}
