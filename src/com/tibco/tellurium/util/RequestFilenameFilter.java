/*
 * @(#)RequestFilenameFilter.java
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
package com.FortMoon.tellurium.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class implements a FilenameFilter and is used to filter out all non-XML
 * files in a given directory. Only files with the extension '.xml' are
 * returned.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 * 
 * @see java.io.FilenameFilter
 */
public class RequestFilenameFilter implements FilenameFilter {
	private java.lang.String ext = ".xml";

	public RequestFilenameFilter(java.lang.String ext) {
		this.ext = ext;
	}

	/**
	 * Returns true if the filename ends in .xml
	 */
	public boolean accept(File file, java.lang.String filename) {
// System.err.println("Filename = " + filename);
		if (filename.endsWith(this.ext)) {
			return true;
		}
// System.err.println("Failed Filename = " + filename);
		return false;
	}
}
