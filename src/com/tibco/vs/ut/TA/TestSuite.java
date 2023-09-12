package com.tibco.vs.ut.TA;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.tibco.tellurium.net.JMSConnection;
import com.tibco.tellurium.net.RequestMessage;
import com.tibco.tellurium.net.ResponseMessage;
import com.tibco.vs.ut.PersonSearchResult;

public class TestSuite extends JMSConnection {
	private static final String CLAIMS_USER_SSN = "625385132";

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public void TC1() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC1-"
				+ getNextSequenceNumber());

		System.err
				.println("***Make sure the CLAIMS 4 Database is unavailable!");
		request.setCitizenshipNumber("00004001");

		send(request.toString(), "CLAIMS4");

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
		Assert.assertTrue(response.getEsbException().getErrorMessage()
				.contains("JDBC"));
	}

	@Test
	public void TC1a() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC1a-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);
		send(request.toString(), "");
		ResponseMessage response = getResults();
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getPersonSearchResult());

	}

	@Test
	public void TC1b() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("");
		request.setTransactionID(this.getClass().getName() + ".TC1b-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);

		send(request.toString(), "");

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
	}

	@Test
	public void TC2() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC2-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);

		send(request);

		ResponseMessage response = getResults();
		Assert.assertNotNull(response);
		response.assertPersonResultNum(1);
	}

	@Test
	public void TC3() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CLAIMS4,SEVIS");
		request.setTransactionID(this.getClass().getName() + ".TC3-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);

		send(request);

		ResponseMessage response[] = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			if ((response[i] != null)
					&& response[i].getQueryTarget().equals("CLAIMS4")) {
				Assert.assertNotNull(response[i]);
				Assert.assertNull(response[i].getEsbException());
				List<PersonSearchResult> list = response[i]
						.getPersonSearchResult();
				Assert.assertTrue(list.size() > 0);
				PersonSearchResult pResult = (PersonSearchResult) list
						.toArray()[0];
				Assert.assertNotNull(pResult.getPerson());
			}
			if ((response[i] != null)
					&& response[i].getQueryTarget().equals("SEVIS")) {
				Assert.assertNotNull(response[i]);
				Assert.assertNotNull(response[i].getEsbException());
			}
		}

	}

	@Test
	public void TC4() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CISCOR");
		request.setTransactionID(this.getClass().getName() + ".TC4-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
	}

	@Test
	public void TC5() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CISCOR,CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC5-"
				+ getNextSequenceNumber());

		request.setSSN("999999999");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		List<PersonSearchResult> list = response.getPersonSearchResult();
		Assert.assertTrue(list.size() > 0);
		PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
		Assert.assertNotNull(pResult.getPerson());

	}

	public void TC6() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS");
		request.setTransactionID(this.getClass().getName() + ".TC6-"
				+ getNextSequenceNumber());

		System.err.println("MAKE SURE SEVIS IS UNAVAILABLE");

		request.setSevisID("44033");
		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getEsbException());
		Assert
				.assertTrue(response.getEsbException().getErrorMessage()
						.length() > 1);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
