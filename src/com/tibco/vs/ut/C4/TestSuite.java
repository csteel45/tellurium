package com.FortMoon.vs.ut.C4;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.FortMoon.tellurium.net.JMSConnection;
import com.FortMoon.tellurium.net.RequestMessage;
import com.FortMoon.tellurium.net.ResponseMessage;
import com.FortMoon.vs.ut.PersonSearchResult;

public class TestSuite extends JMSConnection {
	private static final String QUERY_TARGET = "CLAIMS4";
	private static final String CLAIMS_USER_FN = "NGUYEN";
	private static final String CLAIMS_USER_LN = "LAMG";
	private static final String CLAIMS_USER_DOB = "1966-09-15";
	private static final String CLAIMS_USER_ANUM = "A028252851";
	private static final String CLAIMS_USER_NATNUM = "00004001";
	private static final String CLAIMS_USER_SSN = "625385132";
	private static final String CLAIMS_USER_DUP_DOB = "1949-01-01";
	private static final String CLAIMS_USER_DUP_FN = "FIRST";
	private static final String CLAIMS_USER_DUP_LN = "AME";

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	@Test
	public void TC1() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC1-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() > 0);
		PersonSearchResult result = (PersonSearchResult) response
				.getPersonSearchResult().toArray()[0];
		Assert.assertNotNull(result.getAlienNumber());
		Assert.assertNotNull(result.getAlienBirthCountryName());
		Assert.assertNotNull(result.getPerson().getPersonNationalityText());
		Assert.assertNotNull(result.getPerson().getPersonSex());
	}

	@Test
	public void TC2() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC2-"
				+ getNextSequenceNumber());

		request.setSSN("000-20-1912");
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertFalse(response.getPersonSearchResult().iterator()
				.hasNext());
	}

	@Test
	public void TC3() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC3-"
				+ getNextSequenceNumber());

		// SSN with two different Alien numbers
		request.setSSN("888888888");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() > 1);
	}

	@Test
	public void TC4() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC4-"
				+ getNextSequenceNumber());

		// Improper length
		request.setSSN(CLAIMS_USER_SSN + "1");
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
		Assert.assertNull(response.getEsbException());
	}

	@Test
	public void TC5() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC5-"
				+ getNextSequenceNumber());

		request.setAlienNumber(CLAIMS_USER_ANUM);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC6() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC6-"
				+ getNextSequenceNumber());

		// Improper Alien Num format (No A)
		request.setAlienNumber("028252851");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	public void TC7() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC7-"
				+ getNextSequenceNumber());

		Assert.assertTrue("Get an Alien number with multiple records", false);
		request.setAlienNumber("A000202133");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 4);
	}

	@Test
	public void TC8() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC8-"
				+ getNextSequenceNumber());

		request.setAlienNumber(CLAIMS_USER_ANUM + "1");
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void TC9() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC9-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setSSN(CLAIMS_USER_SSN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC10() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC10-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC11() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC11-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_DUP_FN);
		request.setLastName(CLAIMS_USER_DUP_LN);
		request.setBirthDate(CLAIMS_USER_DUP_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	public void TC12() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC12-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_DUP_FN);
		request.setLastName(CLAIMS_USER_DUP_LN);
		request.setBirthDate(CLAIMS_USER_DUP_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 2);
	}

	public void TC13() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC13-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		// FIXME: MUST RUN MANUALLY
		// request.setBirthDate("1990/01/01");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
		System.err
				.println("CHECK THIS MESSAGE: contained and ESB Exception "
						+ "with a parsing error with details regarding the date format of"
						+ " the request: "
						+ response.getEsbException().getErrorMessage());
	}

	@Test
	public void TC14() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC14-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_FN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC15() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC15-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC16() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC16-"
				+ getNextSequenceNumber());

		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC17() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC17-"
				+ getNextSequenceNumber());

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	@Test
	public void TC18() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC18-"
				+ getNextSequenceNumber());

		request.setCitizenshipNumber(CLAIMS_USER_NATNUM);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 2);
	}

	@Test
	public void TC19() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC19-"
				+ getNextSequenceNumber());

		// No matching Citizenship number
		request.setCitizenshipNumber("77747157");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	@Test
	public void TC20() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC20-"
				+ getNextSequenceNumber());

		// Improper length CItenship number
		request.setCitizenshipNumber(CLAIMS_USER_NATNUM + "1");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	@Test
	public void TC21() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC21-"
				+ getNextSequenceNumber());

		// Improper format Citinzenship number
		request.setCitizenshipNumber("00-00-4001");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	@Test
	public void TC22() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC21-"
				+ getNextSequenceNumber());

		request.setFirstName("MARK");
		request.setLastName("LONDON");
		request.setBirthDate("1942-12-02");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
