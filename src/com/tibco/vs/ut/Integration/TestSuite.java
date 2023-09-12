package com.tibco.vs.ut.Integration;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.tibco.tellurium.net.JMSConnection;
import com.tibco.tellurium.net.RequestMessage;
import com.tibco.tellurium.net.ResponseMessage;
import com.tibco.vs.ut.PersonSearchResult;

@SuppressWarnings("unused")
public class TestSuite extends JMSConnection {
	private static final String CLAIMS_USER_FN = "NGUYEN";
	private static final String CLAIMS_USER_LN = "LAMG";
	private static final String CLAIMS_USER_DOB = "1966-09-15";
	private static final String CLAIMS_USER_ANUM = "A028252851";
	private static final String CLAIMS_USER_NATNUM = "00004001";
	private static final String CLAIMS_USER_SSN = "625385132";
	private static final String CLAIMS_USER_DUP_DOB = "1949-01-01";
	private static final String CLAIMS_USER_DUP_FN = "FIRST";
	private static final String CLAIMS_USER_DUP_LN = "AME";
	private static final String SEVIS_ID = "N0000062534";

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	@Test
	public void TC1() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC1-"
				+ getNextSequenceNumber());

		request.setSevisID(SEVIS_ID);
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage[] response = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
			Assert.assertTrue(response[i].getPersonSearchResult().size() == 1);
		}
	}

	@Test
	public void TC2() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC2-"
				+ getNextSequenceNumber());

		// request.setSevisID("N0000062534");
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage[] response = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull(response[i]);
			if (response[i].getQueryTarget().equals("CLAIMS4")) {
				Assert
						.assertTrue(response[i].getPersonSearchResult().size() == 1);
			}
			else {
				Assert.assertNotNull(response[i].getEsbException());
			}
		}
	}

	@Test
	public void TC3() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("CLAIMS4,SEVIS");
		request.setTransactionID(this.getClass().getName() + ".TC3-"
				+ getNextSequenceNumber());

		request.setSevisID("99999");
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request);

		ResponseMessage[] response = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
			if (response[i].getQueryTarget().equals("CLAIMS4")) {
				Assert
						.assertTrue(response[i].getPersonSearchResult().size() == 1);
			}
		}
	}

	@Test
	public void TC4() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4");
		request.setTransactionID(this.getClass().getName() + ".TC4-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);
		request.setSevisID(SEVIS_ID);
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);

		send(request.toString() + "</Garbage XML>", "SEVIS,CLAIMS4");

		ResponseMessage[] response = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNotNull(response[i].getEsbException());
		}
	}

	@Test
	public void TC5() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4,CISCOR");
		request.setTransactionID(this.getClass().getName() + ".TC5-"
				+ getNextSequenceNumber());

		request.setSSN(CLAIMS_USER_SSN);
		request.setSevisID(SEVIS_ID);
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);
		request.setSSN(CLAIMS_USER_SSN);

		send(request);

		ResponseMessage[] response = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull("Assertion error with response: " + i,
					response[i]);
			Assert.assertNull(response[i].getEsbException());
		}
	}

	@Test
	public void TC6() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4,CCD");
		request.setTransactionID(this.getClass().getName() + ".TC6-"
				+ getNextSequenceNumber());

		request.setSevisID(SEVIS_ID);
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);
		request.setSSN(CLAIMS_USER_SSN);
		request.setVisaFoilNumber("48467040");

		send(request);

		ResponseMessage[] response = new ResponseMessage[3];
		for (int i = 0; i < 3; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
			Assert.assertTrue(response[i].getPersonSearchResult().size() == 1);
		}
	}

	@Test
	public void TC7() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4,CCD,PIERS");
		request.setTransactionID(this.getClass().getName() + ".TC7-"
				+ getNextSequenceNumber());

		request.setSevisID(SEVIS_ID);
		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);
		request.setSSN(CLAIMS_USER_SSN);
		request.setVisaFoilNumber("48467040");

		send(request);

		ResponseMessage[] response = new ResponseMessage[3];
		for (int i = 0; i < 3; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
			Assert.assertTrue(response[i].getPersonSearchResult().size() == 1);
		}
		// Timeout on PIERS
// Assert.assertNotNull(response[3]);
	}

	@Test
	public void TC8() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4,CCD,PIERS");
		request.setTransactionID(this.getClass().getName() + ".TC8-"
				+ getNextSequenceNumber());

		request.setSevisID(SEVIS_ID);
		request.setFirstName("JAMEZ");
		request.setLastName("BAB");
// request.setBirthDate("1962-12-12");
		request.setSSN("888888888");

		send(request);

		ResponseMessage[] response = new ResponseMessage[3];
		for (int i = 0; i < 3; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(response[i]);
			if (response[i].getQueryTarget().equals("CCD")) {
				Assert.assertNotNull(response[i].getEsbException());
			}
			else {
				Assert.assertNull(response[i].getEsbException());
				if (response[i].getQueryTarget().equals("CLAIMS4"))
					Assert.assertTrue(response[i].getPersonSearchResult()
							.size() == 2);
				else
					Assert.assertTrue(response[i].getPersonSearchResult()
							.size() == 1);

			}
		}
		// Timeout on PIERS
// Assert.assertNotNull(response[3]);
	}

	@Test
	public void TC9() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CCD,CLAIMS4,PIERS");
		request.setTransactionID(this.getClass().getName() + ".TC9-"
				+ getNextSequenceNumber());

		request.setSevisID(SEVIS_ID);

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);
		request.setSSN(CLAIMS_USER_SSN);

		request.setVisaFoilNumber("48467040");

		send(request.toString() + "</Garbage XML>", "SEVIS,CCD,CLAIMS4,PIERS");

		ResponseMessage[] response = new ResponseMessage[3];
		for (int i = 0; i < 3; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNotNull(response[i].getEsbException());
		}
		// Timeout on PIERS
// Assert.assertNotNull(response[3]);
	}

	@Test
	public void TC10() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4,CCD,PIERS");
		request.setTransactionID(this.getClass().getName() + ".TC10-"
				+ getNextSequenceNumber());

		request.setSevisID("N1234567890");

		request.setFirstName("MAMM");
		request.setLastName("GREEM");
		request.setBirthDate("1975-11-11");
		request.setSSN("999999999");

		request.setVisaFoilNumber("48467040");

		send(request);

		ResponseMessage[] response = new ResponseMessage[3];
		for (int i = 0; i < 3; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
			if (response[i].getQueryTarget().equals("CLAIMS4")
					|| response[i].getQueryTarget().equals("DOS-CCD")) {
				Assert
						.assertTrue(response[i].getPersonSearchResult().size() == 1);
			}
			else {
				if (response[i].getQueryTarget().equals("SEVIS")) {
					Assert.assertTrue(response[i].getPersonSearchResult()
							.size() == 0);
				}
			}
		}
		// Timeout on PIERS
// Assert.assertNotNull(response[3]);
	}

	@Test
	public void TC11() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget("SEVIS,CLAIMS4,CCD,PIERS");
		request.setTransactionID(this.getClass().getName() + ".TC11-"
				+ getNextSequenceNumber());

		request.setSevisID(SEVIS_ID);

		request.setFirstName(CLAIMS_USER_FN);
		request.setLastName(CLAIMS_USER_LN);
		request.setBirthDate(CLAIMS_USER_DOB);
// request.setSSN(CLAIMS_USER_SSN);

		request.setVisaFoilNumber("48467040");

		send(request);

		ResponseMessage response[] = new ResponseMessage[3];
		for (int i = 0; i < 3; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 3; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
			List<PersonSearchResult> list = response[i].getPersonSearchResult();
			Assert.assertTrue(list.size() > 0);
			PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
			Assert.assertNotNull(pResult.getPerson());
		}
		// Timeout on PIERS
// Assert.assertNotNull(response[3]);
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
