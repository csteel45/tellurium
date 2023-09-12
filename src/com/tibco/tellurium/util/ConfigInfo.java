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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.PropertyResourceBundle;

/**
 * Utility class for loading configuration information from and URL or a file.
 * Auxiliary methods for loading and storing to a file. The class also contains
 * static member functions for retrieving and converting property values to
 * primitives.
 * <p>
 * Example usage for a new properties file:<BR>
 * <code>
 * ConfigInfo ci = new ConfigInfo("test.properties");<BR>
 * int val = ci.put("test.val","5");<BR>
 * ci.store();<BR>
 * </code>
 * <P>
 * Example usage for a pre-existing properties file:<BR>
 * <code>
 * ConfigInfo ci = ConfigInfo.load("test.properties");<BR>
 * int val = ci.getIntProperty("test.val");<BR><BR>
 * </code> Class contains a main for test purposes.
 *
 * @author Chris Steel
 * @version $Revision: 1.5 $
 * @date $Date: 2001/07/14 13:57:19 $
 * @since JDK1.1
 */
public class ConfigInfo extends Properties {

	static final long serialVersionUID = 1L;
	private static final String PROPERTY_FILE_HEADER = "ConfigInfo properties"; //$NON-NLS-1$
	private static final String WEBSTART_HOME_KEY = "jnlpx.home"; //$NON-NLS-1$
	private static String CONFIG_FILENAME = "config/tellurium.properties"; //$NON-NLS-1$
	private static ConfigInfo instance = null;
	private static PropertyResourceBundle messages;

	static {
		try {
			messages = new PropertyResourceBundle(new FileInputStream(
					"config/MessagesBundle.properties"));
		}
		catch (Exception e) {
			System.exit(-1);
		}

		instance = new ConfigInfo();
		load(CONFIG_FILENAME);
		System.getProperties().putAll(instance);
	}

	private ConfigInfo() {
		super();
	} // constructor

	public static ConfigInfo load(String config) {
		// String CONFIG_FILENAMEName;
		InputStream is;
		URL configURL = null;
		try {
			// Load Http URLs or Files (absolute or from classpath)
			if (new File(config).exists()) {
				FileInputStream fis = new FileInputStream(config);
				instance.load(fis);
				CONFIG_FILENAME = config;
				fis.close();
			}
			else {
				if (System.getProperties().containsKey(WEBSTART_HOME_KEY)) {
					configURL = instance.getClass().getClassLoader()
							.getResource(config);
				}
				else {
					configURL = ClassLoader.getSystemResource(config);
				}
				if (configURL == null) {
					throw new RuntimeException(getMessage("FILE_NOT_FOUND")
							+ ": " + config);
				}
				is = configURL.openStream();
				instance.load(is);
			}
		}
		catch (IOException e) {
			throw new RuntimeException(getMessage("ConfigInfo.2") + ": " + config); //$NON-NLS-1$
		}
		return instance;
	}

	public static void store() throws IOException {
		if (CONFIG_FILENAME == null) {
			throw new IOException(getMessage("NO_FILENAME_SPECIFIED"));
		}
		FileOutputStream fos = new FileOutputStream(CONFIG_FILENAME);
		instance.store(fos, PROPERTY_FILE_HEADER);
		fos.close();
	}

	public static void storeAs(String name) throws IOException {
		CONFIG_FILENAME = name;
		store();
	}

	public static String getStringProperty(String key)
			throws PropertyNotFoundException {
		String value = instance.getProperty(key);
		if (value == null) {
			throw new PropertyNotFoundException(getMessage("ConfigInfo.0")
					+ ": " +  key);
		}
		return (value);
	}

	public static String getStringProperty(String key, String defaultVal) {
		String value;
		try {
			value = getStringProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static boolean getBooleanProperty(String key)
			throws PropertyNotFoundException {

		String val = instance.getProperty(key);
		if (val == null) {
			throw new PropertyNotFoundException(getMessage("NO_VALUE_FOR")
					+ ": " + key);
		}
		return Boolean.valueOf(val.trim());
	}

	public static boolean getBooleanProperty(String key, boolean defaultVal) {
		boolean value;
		try {
			value = getBooleanProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static int getIntProperty(String key)
			throws PropertyNotFoundException {
		String val = instance.getProperty(key);
		if (val == null) {
			throw new PropertyNotFoundException(getMessage("NO_VALUE_FOR")
					+ ": " + key);
		}
		int value = Integer.parseInt(val.trim());
		return (value);
	}

	public static int getIntProperty(String key, int defaultVal) {
		int value;
		try {
			value = getIntProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static short getShortProperty(String key)
			throws PropertyNotFoundException {
		String val = instance.getProperty(key);
		if (val == null) {
			throw new PropertyNotFoundException(getMessage("NO_VALUE_FOR")
					+ ": " + key);
		}
		short value = Short.parseShort(val.trim());
		return (value);
	}

	public static short getShortProperty(String key, short defaultVal) {
		short value;
		try {
			value = getShortProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static long getLongProperty(String key)
			throws PropertyNotFoundException {
		String val = instance.getProperty(key);
		if (val == null) {
			throw new PropertyNotFoundException(getMessage("NO_VALUE_FOR")
					+ ": " + key);
		}
		long value = Long.parseLong(val.trim());
		return (value);
	}

	public static long getLongProperty(String key, long defaultVal) {
		long value;
		try {
			value = getLongProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static float getFloatProperty(String key)
			throws PropertyNotFoundException {
		String val = instance.getProperty(key);
		if (val == null) {
			throw new PropertyNotFoundException(getMessage("NO_VALUE_FOR")
					+ ": " + key);
		}
		float value = Float.parseFloat(val.trim());
		return (value);
	}

	public static float getFloatProperty(String key, float defaultVal) {
		float value;
		try {
			value = getFloatProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static double getDoubleProperty(String key)
			throws PropertyNotFoundException {
		String val = instance.getProperty(key);
		if (val == null) {
			throw new PropertyNotFoundException(getMessage("NO_VALUE_FOR")
					+ ": " + key);
		}
		double value = Double.parseDouble(val.trim());
		return (value);
	}

	public static double getDoubleProperty(String key, double defaultVal) {
		double value;
		try {
			value = getDoubleProperty(key);
		}
		catch (PropertyNotFoundException e) {
			return (defaultVal);
		}
		return (value);
	}

	public static String get(String key) {
		return instance.getProperty(key);
	}

	public static String getMessage(String key) {
		return messages.getString(key);
	}

	public static void set(String key, String value) {
		instance.setProperty(key, value);
	}

	@Override
	public Object clone() {
		Object props = super.clone();
		return props;
	}

} // ConfigInfo
