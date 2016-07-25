/*
 * @(#)ResponseMessage.java
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
package com.FortMoon.tellurium.net;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import org.junit.Assert;

import com.FortMoon.vs.ut.ESBException;
import com.FortMoon.vs.ut.PersonSearchResult;
import com.FortMoon.vs.ut.VerificationResponse;

/**
 * This class wraps the JAXB-generated VerificationResponset class. It converts
 * The generated String any other class parameter methods to usable ones. It
 * contains a toString method that outputs the XML form of the response.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 * 
 * @see com.FortMoon.vs.ut.VerificationResponse
 */
public class ResponseMessage implements ValidationEventHandler {
	private final static java.lang.String JAXB_CONTEXT = "com.FortMoon.vs.ut";
	private VerificationResponse instance;
	private final java.lang.String responseXML;

	@SuppressWarnings("deprecation")
	public ResponseMessage(java.lang.String response) throws Exception {
		responseXML = response;
		try {
			java.io.InputStream stream = new java.io.StringBufferInputStream(
					response);
			JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_CONTEXT);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			unmarshaller.setEventHandler(this);
			instance = (VerificationResponse) unmarshaller.unmarshal(stream);
		}
		catch (Exception e) {
			System.err.println("Exception creating response: " + e);
			e.printStackTrace(System.err);
			Assert.assertNull(e);
		}

	}

	public void assertPersonResultNum(int num) {
		Assert.assertNull(getEsbException());
		Assert.assertNotNull(getPersonSearchResult());
		Assert.assertTrue(getPersonSearchResult().size() == num);
	}

	public int getNumPersonSearchResults() {
		if (null != this.getPersonSearchResult())
			return this.getPersonSearchResult().size();
		else
			return 0;

	}

	public java.lang.String toXML() {
		return this.responseXML;
	}

	@Override
	public java.lang.String toString() {
		return this.responseXML;
	}

	/**
	 * @return the sourceSystemID
	 */
	public java.lang.String getSourceSystemID() {
		return instance.getSourceSystemID().getValue();
	}

	/**
	 * @return the sourceTransactionID
	 */
	public java.lang.String getSourceTransactionID() {
		return instance.getSourceTransactionID().getValue();
	}

	/**
	 * @return the queryTarget
	 */
	public java.lang.String getQueryTarget() {
		return instance.getQueryTarget().getValue();
	}

	/**
	 * @return the personSearchResult
	 */
	public List<PersonSearchResult> getPersonSearchResult() {
		return instance.getPersonSearchResult();
	}

	/**
	 * @return the esbException
	 */
	public ESBException getEsbException() {
		return instance.getESBException();
	}

	public boolean handleEvent(ValidationEvent event) {
		System.err.println("SAX Error: " + event);
		System.err.println("SAX Linked Error: " + event.getLinkedException());
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instance == null) ? 0 : instance.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ResponseMessage other = (ResponseMessage) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		}
		else if (!instance.equals(other.instance))
			return false;
		return true;
	}

}
