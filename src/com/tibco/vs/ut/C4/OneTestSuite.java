package com.FortMoon.vs.ut.C4;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.FortMoon.tellurium.net.JMSConnection;
import com.FortMoon.tellurium.net.RequestMessage;
import com.FortMoon.tellurium.net.ResponseMessage;


public class OneTestSuite extends JMSConnection {
	private static final String QUERY_TARGET = "CLAIMS4";
	private static final String CLAIMS_USER_FN = "NGUYEN";
	private static final String CLAIMS_USER_LN = "LAMG";
	private static final String CLAIMS_USER_DOB = "1966-09-15";
	private static final String CLAIMS_USER_ANUM = "A028252851";
	private static final String CLAIMS_USER_NATNUM = "00004001";
	private static final String CLAIMS_USER_SSN = "625385132";
	private static final String CLAIMS_USER_DUP_DOB = "1978-10-10";
	private static final String CLAIMS_USER_DUP_FN = "Alex";
	private static final String CLAIMS_USER_DUP_LN = "Bell";

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	@Test
	public void TC5() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC5-"
				+ getNextSequenceNumber());

// request.setAlienNumber(CLAIMS_USER_ANUM + "1");
// request.setFirstName("MARK");
// request.setLastName("LONDON");
// request.setBirthDate("1942-12-02");
		request.setCitizenshipNumber(CLAIMS_USER_NATNUM);

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		Assert.assertNull(response.getEsbException());
		Assert.assertNotNull(response.getPersonSearchResult());
		Assert.assertTrue(response.getPersonSearchResult().size() == 1);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(OneTestSuite.class);
	}
}
