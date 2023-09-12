package com.tibco.vs.ut.runner;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.tibco.tellurium.net.JMSConnection;


public class TestSuite extends JMSConnection {

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	@Test
	public void runFiles() throws Exception {
		try {
			sendRequestFiles("xml/requests");
		}
		catch (Exception e) {
			System.err.println("Exception rending request files: " + e);
			e.printStackTrace(System.err);
			throw e;
		}
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
