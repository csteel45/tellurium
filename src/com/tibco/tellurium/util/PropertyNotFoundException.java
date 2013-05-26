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
package com.FortMoon.tellurium.util;

/*
 * PropertyNotFoundException is thrown in the case that a property value is not
 * found for a given key.
 *
 * @author Chris Steel - Sun Java Center
 *
 * @version $Revision: 1.1.1.1 $ @date $Date: 2001/06/02 06:11:20 $
 * @since JDK1.1
 */
public class PropertyNotFoundException extends java.lang.RuntimeException {

	static final long serialVersionUID = 1L;

	/**
	 * Constructor for PropertyNotFoundException
	 */
	public PropertyNotFoundException() {
		super();
	}

	/**
	 * Overloaded constructor for PropertyNotFoundException Sets the exception
	 * error message
	 */
	public PropertyNotFoundException(String reason) {
		super(reason);
	}
}
