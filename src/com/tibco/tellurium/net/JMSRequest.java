/*
 * @(#)RequestMessage.java
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
package com.tibco.tellurium.net;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * This class wraps the JAXB-generated VerificationRequest class. It converts
 * The generated String any other class parameter methods to usable ones. It
 * contains a toString method that outputs the XML form of the request.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 *
 * @see com.tibco.vs.ut.VerificationRequest
 */
public class JMSRequest extends RequestMessage {

	/**
	 * Construct a new RequestMessage Object. Sets the SourceSystemID and the
	 * SourceUserId. Also, creates a new, empty PersonSearchCriteria.
	 */
	public JMSRequest() {
		super();
	}

	/**
	 * Returns the String representation of this VerificationRequest object.
	 *
	 * @return The String representation of this object.
	 */
	@Override
	public String toString() {
		java.io.OutputStream stream = new java.io.ByteArrayOutputStream();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_CONTEXT);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller
					.setProperty("jaxb.schemaLocation",
							"http://uscis.gov/uscis/xsd/services/verification/2.0/verification");
			marshaller.marshal(this, stream);
		}
		catch (Exception e) {
			// If you return the error message it might not be caught up front.
			return "";
		}
		return stream.toString();
	}

}
