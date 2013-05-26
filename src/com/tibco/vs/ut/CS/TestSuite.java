package com.FortMoon.vs.ut.CS;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.FortMoon.tellurium.net.JMSConnection;
import com.FortMoon.tellurium.net.RequestMessage;
import com.FortMoon.tellurium.net.ResponseMessage;


public class TestSuite extends JMSConnection {
	private static final String ANUM = "A028252851";
	private static final String STUDENT_ID = "44033";

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	@Test
	public void TC1() throws Exception {

		RequestMessage request = new RequestMessage();
		request.setSevisID(STUDENT_ID);
		request.setAlienNumber(ANUM);

		request.setQueryTarget("SEVIS,CLAIMS4");

		send(request);
		System.err.println("*** Make sure to view the audit log! ***");

		ResponseMessage[] response = new ResponseMessage[2];
		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNull(response[i].getEsbException());
		}
	}

	@Test
	public void TC2() throws Exception {
		RequestMessage request = new RequestMessage();

		request.setSevisID(STUDENT_ID);
		request.setAlienNumber(ANUM);

		request.setQueryTarget("SEVIS,CLAIMS4");

		System.err.println("*** Make sure SEVIS is unavailable! ***");

		send(request);

		ResponseMessage[] response = new ResponseMessage[2];

		for (int i = 0; i < 2; i++) {
			response[i] = getResults();
		}

		for (int i = 0; i < 2; i++) {
			Assert.assertNotNull(response[i]);
			Assert.assertNotNull(response[i]);
			if (response[i].getQueryTarget().equals("CLAIMS4")) {
				Assert.assertNull(response[i].getEsbException());
			}
			else
				Assert.assertNotNull(response[i].getEsbException());
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
