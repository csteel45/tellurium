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
package com.FortMoon.tellurium.net;

import java.io.FileInputStream;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.junit.Assert;

import com.FortMoon.tellurium.model.Request;
import com.FortMoon.vs.ut.DateType;
import com.FortMoon.vs.ut.IdentificationType;
import com.FortMoon.vs.ut.PersonNameTextType;
import com.FortMoon.vs.ut.PersonNameType;
import com.FortMoon.vs.ut.PersonSearchCriteria;
import com.FortMoon.vs.ut.PersonType;
import com.FortMoon.vs.ut.VerificationRequest;

/**
 * This class wraps the JAXB-generated VerificationRequest class. It converts
 * The generated String any other class parameter methods to usable ones. It
 * contains a toString method that outputs the XML form of the request.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 *
 * @see com.FortMoon.vs.ut.VerificationRequest
 */
public class RequestMessage extends VerificationRequest implements Request {
	public static final String SOURCE_USER_ID = "User";
	public static final String SOURCE_SYSTEM_ID = "TestConsole";
	public final static String JAXB_CONTEXT = "com.FortMoon.vs.ut";
	private PersonType person;
	private final PersonSearchCriteria criteria;
	private PersonNameType personName;

	/**
	 * Construct a new RequestMessage Object. Sets the SourceSystemID and the
	 * SourceUserId. Also, creates a new, empty PersonSearchCriteria.
	 */
	public RequestMessage() {
		super();
		this.criteria = new PersonSearchCriteria();
		this.setSourceSystemID(SOURCE_SYSTEM_ID);
		this.setSourceUserID(SOURCE_USER_ID);
	}

	/**
	 * Returns a the Query Target attribute from an XML request file.
	 *
	 * @param requestStr
	 *            The name of the XML request file.
	 * @return The Query Target from the marshaled XML request file.
	 * @throws Exception
	 */
	public static String getMarshalledQueryTarget(String requestStr)
			throws Exception {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_CONTEXT);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			FileInputStream stream = new FileInputStream(requestStr);
			VerificationRequest request = (VerificationRequest) unmarshaller
					.unmarshal(stream);
			return request.getQueryTarget().getValue();
		}
		catch (Exception e) {
			System.err.println("Exception marshalling request: " + e);
			e.printStackTrace(System.err);
			Assert.assertNull(e);
		}
		return null;
	}

	/**
	 * Returns the String representation of this VerificationRequest object.
	 *
	 * @return The String representation of this object.
	 */
	@Override
	public String toString() {
		if ((personName != null) && (person == null)) {
			person = new PersonType();
		}
		if (personName != null) {
			person.getPersonName().add(personName);
		}
		if (person != null) {
			criteria.setPerson(person);
		}

		this.setPersonSearchCriteria(criteria);

		java.io.OutputStream stream = new java.io.ByteArrayOutputStream();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_CONTEXT);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			// marshaller.setProperty("JMMarshallerImpl.JAXME_INDENTATION_STRING",
			// "\r\n");
			// marshaller.setProperty("jaxb.noNamespaceSchemaLocation",
			// "http://uscis.gov/uscis/xsd/services/verification/2.0/verification");
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

	/**
	 * @param queryTarget
	 *            the queryTarget to set
	 */
	public void setQueryTarget(String queryTarget) {
		com.FortMoon.vs.ut.String queryTargetS = new com.FortMoon.vs.ut.String();
		queryTargetS.setValue(queryTarget);
		this.setQueryTarget(queryTargetS);
	}

	/**
	 * @param sourceSystemID
	 *            the sourceSystemID to set
	 */
	public void setSourceSystemID(String sourceSystemID) {
		com.FortMoon.vs.ut.String sourceSystemIDS = new com.FortMoon.vs.ut.String();
		sourceSystemIDS.setValue(sourceSystemID);
		this.setSourceSystemID(sourceSystemIDS);
	}

	/**
	 * @param transactionID
	 *            the transactionID to set
	 */
	public void setTransactionID(String transactionID) {
		com.FortMoon.vs.ut.String transactionIDS = new com.FortMoon.vs.ut.String();
		transactionIDS.setValue(transactionID);
		this.setSourceTransactionID(transactionIDS);
	}

	/**
	 * @param sourceUserID
	 *            the sourceUserID to set
	 */
	public void setSourceUserID(String sourceUserID) {
		com.FortMoon.vs.ut.String sourceUserIDS = new com.FortMoon.vs.ut.String();
		sourceUserIDS.setValue(sourceUserID);
		this.setSourceUserID(sourceUserIDS);
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		if (personName == null) {
			personName = new PersonNameType();
		}
		PersonNameTextType givenName = new PersonNameTextType();
		givenName.setValue(firstName);
		personName.getPersonGivenName().add(givenName);
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		if (personName == null) {
			personName = new PersonNameType();
		}
		PersonNameTextType surNameT = new PersonNameTextType();
		surNameT.setValue(lastName);
		personName.getPersonSurName().add(surNameT);
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		if (personName == null) {
			personName = new PersonNameType();
		}
		PersonNameTextType middleNameT = new PersonNameTextType();
		middleNameT.setValue(middleName);
		personName.getPersonMiddleName().add(middleNameT);
	}

	@SuppressWarnings("deprecation")
	public void setBirthDate(String birthDateString) throws Exception {

		StringTokenizer st = new StringTokenizer(birthDateString, "-");
		int year = Integer.parseInt(st.nextToken());
		int month = Integer.parseInt(st.nextToken());
		int day = Integer.parseInt(st.nextToken());

		DatatypeFactory factory = DatatypeFactory.newInstance();
		XMLGregorianCalendar cal = factory.newXMLGregorianCalendarDate(year,
				month, day, 0);

		// This strips off the trailing Z that was causing problems
		// with the JDBC query in CLAIMS4.
		cal.setTimezone(DatatypeConstants.FIELD_UNDEFINED);

		com.FortMoon.vs.ut.Date date = new com.FortMoon.vs.ut.Date();
		date.setValue(cal);

		JAXBElement<com.FortMoon.vs.ut.Date> element = new JAXBElement<com.FortMoon.vs.ut.Date>(
				new QName("http://niem.gov/niem/niem-core/2.0", "Date"),
				com.FortMoon.vs.ut.Date.class, date);

		DateType birthDateDT = new DateType();
		birthDateDT.getDateRepresentation().add(element);
		if (person == null) {
			person = new PersonType();
		}
		person.getPersonBirthDate().add(birthDateDT);
	}

	/**
	 * @param alienNumber
	 *            the alienNumber to set
	 */
	public void setAlienNumber(String alienNumber) {
		IdentificationType identification = new IdentificationType();
		com.FortMoon.vs.ut.String identificationID = new com.FortMoon.vs.ut.String();
		identificationID.setValue(alienNumber);
		identification.getIdentificationID().add(identificationID);

		criteria.setAlienNumber(identification);
	}

	/**
	 * @param ssn
	 *            the ssn to set
	 */
	public void setSSN(String ssn) {
		IdentificationType identification = new IdentificationType();
		com.FortMoon.vs.ut.String identificationID = new com.FortMoon.vs.ut.String();
		identificationID.setValue(ssn);
		identification.getIdentificationID().add(identificationID);

		criteria.setPersonSSNIdentification(identification);
	}

	/**
	 * @param visaNumber
	 *            the visaNumber to set
	 */
	public void setVisaNumber(String visaNumber) {
		com.FortMoon.vs.ut.String visaNumberS = new com.FortMoon.vs.ut.String();
		visaNumberS.setValue(visaNumber);
		criteria.setVisaNumberID(visaNumberS);
	}

	/**
	 * @param citizenshipNumber
	 *            the citizenshipNumber to set
	 */
	public void setCitizenshipNumber(String citizenshipNumber) {
		IdentificationType identification = new IdentificationType();
		com.FortMoon.vs.ut.String identificationID = new com.FortMoon.vs.ut.String();
		identificationID.setValue(citizenshipNumber);
		identification.getIdentificationID().add(identificationID);

		criteria.setCitizenshipCertificateIdentification(identification);
	}

	/**
	 * @param passportNumber
	 *            the passportNumber to set
	 */
	public void setPassportNumber(String passportNumber) {
		IdentificationType identification = new IdentificationType();
		com.FortMoon.vs.ut.String identificationID = new com.FortMoon.vs.ut.String();
		identificationID.setValue(passportNumber);
		identification.getIdentificationID().add(identificationID);

		criteria.setPassportNumberIdentification(identification);
	}

	/**
	 * @param studentID
	 *            the studentID to set
	 */
	public void setSevisID(String studentID) {
		IdentificationType identification = new IdentificationType();
		com.FortMoon.vs.ut.String identificationID = new com.FortMoon.vs.ut.String();
		identificationID.setValue(studentID);
		identification.getIdentificationID().add(identificationID);

		criteria.setSevisID(identification);
	}

	/**
	 * @param visaFoilNumber
	 *            the visaFOILNumber to set
	 */
	public void setVisaFoilNumber(String visaFoilNumber) {
		com.FortMoon.vs.ut.String visaFoilNumberS = new com.FortMoon.vs.ut.String();
		visaFoilNumberS.setValue(visaFoilNumber);

		criteria.setVisaFoilNumber(visaFoilNumberS);
	}

}
