package com.FortMoon.vs.ut.SEVIS;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.FortMoon.tellurium.net.JMSConnection;
import com.FortMoon.tellurium.net.RequestMessage;
import com.FortMoon.tellurium.net.ResponseMessage;
import com.FortMoon.vs.ut.ESBException;
import com.FortMoon.vs.ut.PersonSearchResult;

public class TestSuite extends JMSConnection {

	public static final String QUERY_TARGET = "SEVIS";
	public static final String VALID_SEVIS_ID = "N0000062529";
	public static final String INVALID_SEVIS_ID = "N0200062529";
	public static final String MULTIRESULT_SEVIS_ID = "N0000039450";
	public static final String MALFORMATTED_SEVIS_ID = "44-0-33";

	/**
	 * <BR>
	 * Request type: Valid request test case. <BR>
	 * Description: Send valid SevisID with single matching record. <BR>
	 * Input Data: SevisID=N0000062529<BR>
	 * Expected Results: VerificationResponse returned from ESB with single
	 * matching record.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void TC1a() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC1a-"
				+ getNextSequenceNumber());

		request.setSevisID(VALID_SEVIS_ID);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getNumPersonSearchResults() == 1);
		PersonSearchResult result = (PersonSearchResult) response
				.getPersonSearchResult().toArray()[0];
		Assert.assertNotNull(result);

		Assert.assertNotNull(result.getAlienStudentVerification()
				.getImmigrationStatusCode());
		Assert.assertNotNull(result.getAlienStudentVerification().getSevisID());
		Assert.assertNull(result.getAlienStudentVerification()
				.getPortOfEntryCode());
		Assert.assertNotNull(result.getAlienStudentVerification()
				.getVisaNumberID());
	}

	/**
	 * <BR>
	 * Request type: Valid request test case. <BR>
	 * Description: Send valid SevisID with empty PersonSearchResults. <BR>
	 * Input Data: SevisID=N0200062529<BR>
	 * Expected Results: VerificationResponse returned from ESB with an empty
	 * PersonSearchResults.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void TC1b() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC1b-"
				+ getNextSequenceNumber());

		request.setSevisID(INVALID_SEVIS_ID);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	/**
	 * <BR>
	 * Request type: Invalid request test case. <BR>
	 * Description: Invalid XML Request. <BR>
	 * Input Data: Invalid XML <BR>
	 * Expected Results: VerificationResponse returned from ESB with an
	 * ESBException detailing the parsing error.
	 * 
	 * @throws Exception
	 *             If anything gos wrong.
	 */
	@Test
	public void TC1c() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC1c-"
				+ getNextSequenceNumber());
		request.setSourceSystemID("VIS");
		request.setSourceUserID("UNIT TEST HARNESS");

		send(request.toString() + "</GarbageXML>", QUERY_TARGET);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
		ESBException e = response.getEsbException();
		Assert.assertTrue((e != null) && (e.getErrorMessage().length() > 1));
		System.err.println("ESB Exception for "
				+ response.getSourceTransactionID() + ": "
				+ e.getErrorMessage());
	}

	/**
	 * <BR>
	 * Request type: Valid request test case. <BR>
	 * Description: Send valid SevisID with multiple Student records and a
	 * program end date later than two of those records<BR>
	 * Input Data: SevisID=N0000039450<BR>
	 * ProgramEndDate=2004-03-31<br>
	 * Expected Results: VerificationResponse returned from ESB with a single
	 * matching record.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void TC2a() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC3-"
				+ getNextSequenceNumber());

		request.setSevisID(MULTIRESULT_SEVIS_ID);
// request.

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 3);
		PersonSearchResult result = (PersonSearchResult) response
				.getPersonSearchResult().toArray()[0];
		System.out.println("status change date = "
				+ result.getAlienStudentVerification().getStatusChangeDate());
		// Assert.assertTrue(result.getAlienStudentVerification().getStatusChangeDate().equals(XXX));
	}

	/**
	 * <BR>
	 * Request type: Invalid request test case. <BR>
	 * Description: Send request with invalid SevisID. <BR>
	 * Input Data: SevisID=44-0-33 <BR>
	 * Expected Results: VerificationResponse returned from ESB with an
	 * ESBException detailing SevisID error.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void TC3a() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC3a-"
				+ getNextSequenceNumber());

		request.setSevisID(MALFORMATTED_SEVIS_ID);
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
		ESBException e = response.getEsbException();
		Assert.assertTrue((e != null) && (e.getErrorMessage().length() > 1));
		System.err.println("INCLUDE ME!!!!!!!! ESB Exception for "
				+ response.getSourceTransactionID() + ": "
				+ e.getErrorMessage());
	}

	/**
	 * <BR>
	 * Request type: Invalid request test case. <BR>
	 * Description: Send request with empty SevisID. <BR>
	 * Input Data: Empty SevisID <BR>
	 * Expected Results: VerificationResponse returned from ESB with an
	 * ESBException detailing empty SevisID error.
	 * 
	 * @throws Exception
	 *             If anything s wrong.
	 */
	@Test
	public void TC3b() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC3a-"
				+ getNextSequenceNumber());

		request.setSevisID("");
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
		ESBException e = response.getEsbException();
		Assert.assertTrue((e != null) && (e.getErrorMessage().length() > 1));
		System.err.println("INCLUDE ME!!!!!!!! ESB Exception for "
				+ response.getSourceTransactionID() + ": "
				+ e.getErrorMessage());
	}

	/**
	 * Hook method for JUnit test suite.
	 * 
	 * @return Test Suite adapter.
	 */
	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
