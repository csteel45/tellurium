package com.FortMoon.vs.ut.PIERS;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.FortMoon.tellurium.net.JMSConnection;
import com.FortMoon.tellurium.net.RequestMessage;
import com.FortMoon.tellurium.net.ResponseMessage;
import com.FortMoon.vs.ut.ESBException;
import com.FortMoon.vs.ut.PersonSearchResult;

public class TestSuite extends JMSConnection {
	public final static String QUERY_TARGET = "PIERS";

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

		request.setFirstName("Gregory");
		request.setLastName("Thomas");
		request.setBirthDate("1967-07-31");
		request.setPassportNumber("026473811");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getNumPersonSearchResults() == 1);
		PersonSearchResult result = (PersonSearchResult) response
				.getPersonSearchResult().toArray()[0];
		Assert.assertNotNull(result);
	}

	public void TC2() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC2-"
				+ getNextSequenceNumber());

		request.setFirstName("AYAD");
		request.setLastName("FERMADZ");
		request.setBirthDate("1942-09-21");
		request.setPassportNumber("99");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	public void TC3() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC3-"
				+ getNextSequenceNumber());

		request.setFirstName("AYAD");
		request.setLastName("FERMADZ");
		request.setBirthDate("1942-09-21");
		request.setPassportNumber("123-45-6789");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 0);
	}

	public void TC4() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC4-"
				+ getNextSequenceNumber());

		request.setFirstName("AYAD");
		request.setLastName("FERMADZ");
		request.setBirthDate("1942-09-21");
		request.setPassportNumber("123456789");

		send(request.toString() + "</garbage XML>", QUERY_TARGET);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response.getEsbException());
		ESBException e = response.getEsbException();
		Assert.assertTrue((e != null) && (e.getErrorMessage().length() > 1));
		System.err.println("ESB Exception for "
				+ response.getSourceTransactionID() + ": "
				+ e.getErrorMessage());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
