/*
 * @(#)JMSConnection.java
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
package com.tibco.tellurium.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;

import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;

import org.junit.Assert;
import org.junit.BeforeClass;

import com.tibco.tellurium.model.Request;
import com.tibco.tellurium.naming.TibcoCFLookup;

/**
 * This class servers as the base class for the Unit Test Suites. It provides
 * utility methods for sending and receiving requests, as well as reading and
 * writing XML files. It performs the connection to the ESB.
 *
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 *
 * @see tibco.tellurium.naming.FortMoonCFLookup
 */
public class JMSConnection implements Connection {

	static MessageProducer producer;
	static TopicConnection connection = null;
	static TopicSession session = null;
	static QueueConnection queueConn = null;
	static Topic topic = null;
	static TemporaryQueue tempQueue = null;
	static QueueSession queueSession = null;
	static QueueReceiver receiver = null;
	protected final static Random random;
	static long sequenceNumber;
	static final java.lang.String TempQueueFactoryName = "SSLQueueConnectionFactoryMGMT";
	protected static final java.lang.String COMMAND_DEST_NAME = "US.DHS.USCIS.ESB.VerificationService.Verify.v1.0";
	protected static final int TIMEOUT = 10000;
	private static Object mutex = new Object();
	private static final java.lang.String REQUEST_DIR = "xml/requests";
	private static final java.lang.String RESPONSE_DIR = "xml/responses";
	private static final java.lang.String XML_EXT = ".xml";
	private static ResponseMessage mockResponse = null;
	private static boolean setJMSType = true;

	private static boolean MOCK_RUN = true;

	static {
		random = new Random(System.currentTimeMillis());
	}

	/**
	 * This method is called before each of the unit test classes. It
	 * establishes the connection to the ESB and initializes the requisite
	 * member variables.
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@BeforeClass
	public static void setUp() throws Exception {
		System.err.println("Setting up test.");
		sequenceNumber = System.currentTimeMillis();

		if (MOCK_RUN) {
			mockResponse = new ResponseMessage(readFile(new File(
					"xml/responses/MockResponse.xml")));

			return;
		}
		try {
			// System.err.println("creating initial queue context");
			// create initial queue context
			TibcoCFLookup lookup = new TibcoCFLookup();
			Properties props = new java.util.Properties();
			props.load(new FileInputStream("jndi.properties"));
			Context qctx = lookup.getInitialContext(props);
			System.err.println("got queue's initial context");
			ConnectionFactory connectionFactory = (ConnectionFactory) qctx
					.lookup(TempQueueFactoryName);
			System.err.println("got factoryName from JNDI: "
					+ TempQueueFactoryName);
			queueConn = (QueueConnection) connectionFactory.createConnection();

			queueSession = queueConn.createQueueSession(false,
					javax.jms.Session.AUTO_ACKNOWLEDGE);

			tempQueue = queueSession.createTemporaryQueue();
			// System.err.println("Created temporay queue: "
			// + tempQ.getQueueName());

			// java.lang.String tempQueueName = tempQ.getQueueName();
			// System.out.println("tempQueueName = " + tempQueueName);

			java.lang.String factoryName = "SSLTopicConnectionFactoryMGMT";
			// Topic JMSConnection Factory
			// System.out.println("creating initial context");
			// create initial context
			Context ctx = lookup.getInitialContext(props);
			// System.out.println("got InitialContext");
			// do lookup to get cf
			TopicConnectionFactory cf = (TopicConnectionFactory) ctx
					.lookup(factoryName);
			// System.out.println("got factoryName '" + factoryName + "' in
			// JNDI");

			connection = cf.createTopicConnection();
			// Start the connection. connection.start();
			// Use the connection to create a session.
			session = connection.createTopicSession(false,
					Session.AUTO_ACKNOWLEDGE);
			// Get the destination.
			topic = (Topic) ctx.lookup(COMMAND_DEST_NAME);
			// Topic topic = (Topic)ctx.lookup(commandDestName);
			// System.out.println("Topic Name: " + topic.getTopicName());

			receiver = queueSession.createReceiver(tempQueue);

			queueConn.start();

		}
		catch (Exception e) {
			System.err.println("Exception setting up test: " + e);
			e.printStackTrace(System.err);
			// throw e;
		}

	}

	public void send(Request request) throws Exception {

		java.lang.String requestString = request.toString();
		// System.err.println("Marshalled string: " + requestString);
		writeRequest((RequestMessage) request);
		send(requestString, ((ResponseMessage) request).getQueryTarget());
	}

	public void sendRequestFiles(java.lang.String dirName) throws Exception {
		File dir = new File(dirName);
		System.err.println("Reading dir: " + dirName);
		if ((dir == null) || !dir.isDirectory()) {
			System.err.println("ERROR with dir: " + dirName);
			throw new Exception("Directory does not exist: " + dirName);
		}
		FilenameFilter filter = new RequestFilenameFilter(XML_EXT);
		java.lang.String[] files = dir.list(filter);
		try {
			for (java.lang.String element : files) {
				// System.err.println("Sending file: " + files[i]);
				sendFile(new File(REQUEST_DIR + element));
			}
			for (@SuppressWarnings("unused") java.lang.String element : files) {
				// System.err.println("Receiving file.");
				this.getResults();
			}
		}
		catch (Exception e) {
			System.err.println("Exception sending file: " + e);
			e.printStackTrace(System.err);
			throw e;
		}

	}

	public int sendRequestFile(java.lang.String dirName) throws Exception {
		File dir = new File(dirName);
		if ((dir == null) || !dir.isDirectory()) {
			System.err.println("ERROR with dir: " + dirName);
			throw new Exception("Directory does not exist: " + dirName);
		}
		FilenameFilter filter = new RequestFilenameFilter(XML_EXT);
		java.lang.String[] files = dir.list(filter);

		Random random = new Random();
		java.lang.String file = null;
		if (files.length == 0) {
			throw new Exception("No files to send!");
		}
		if (files.length == 1) {
			file = files[0];
		}
		else {
			int randomInt = random.nextInt(files.length);
			// System.out.println("Random = " + randomInt);
			file = files[randomInt];
		}
		java.lang.String absoluteFileName = dirName + File.separatorChar + file;
// System.out.println("Sending File: " + absoluteFileName);
		int numServices = parseServices(absoluteFileName);
		sendFile(new File(absoluteFileName));

		return numServices;

	}

	public int parseServices(java.lang.String fileName) throws Exception {
		java.lang.String queryTarget = RequestMessage
				.getMarshalledQueryTarget(fileName);
		StringTokenizer token = new StringTokenizer(queryTarget, ",");
		int num = 0;
		while (token.hasMoreTokens()) {
			java.lang.String target = token.nextToken();
			// System.err.println("TARGET = " + target);
			if (target.contains("CLAIMS4") || target.contains("SEVIS")
					|| target.contains("CCD") || target.contains("PIERS")) {
				num++;
			}
		}
		// This needs to be fixed.
// System.err.println("Returning = " + num);
		return num;
	}

	public void send(java.lang.String request, java.lang.String queryTarget)
			throws Exception {

		if (MOCK_RUN) {
			return;
		}
		producer = session.createProducer(topic);
		TextMessage msg = session.createTextMessage();

		// msg.setStringProperty("QueryTargets", queryTarget);
		if (setJMSType) {
			msg.setJMSType(queryTarget);
		}

		msg.setText(request);
		msg.setJMSReplyTo(tempQueue);
		// System.out.println("SENDING MESSAGE NOW ==>");
		try {
			producer.send(msg);
			// System.out.println("MESSAGE SENT SUCCESSFULLY ==>");
		}
		catch (Exception e) {
			System.err.println("Exception sending message: " + e);
			Assert.assertTrue(false);
		}
	}

	public void sendFile(File file) throws Exception {
		java.lang.String request = readFile(file);
		java.lang.String queryTarget = RequestMessage
				.getMarshalledQueryTarget(file.getAbsolutePath());
		// System.out.println("Sending request: " + queryTarget);
		send(request, queryTarget);
	}

	public static void writeRequest(RequestMessage request) throws Exception {
		java.lang.String fileName = REQUEST_DIR
				+ request.getSourceTransactionID().getValue() + XML_EXT;
		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(request.toString().getBytes());
		fos.flush();
		fos.close();
	}

	public static void writeResponse(ResponseMessage response) throws Exception {
		java.lang.String fileName = RESPONSE_DIR
				+ response.getSourceTransactionID() + XML_EXT;
		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(response.toString().getBytes());
		fos.flush();
		fos.close();
	}

	public static java.lang.String readFile(File file) throws Exception {

		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		fis.read(buf);
		fis.close();
		return new java.lang.String(buf, "UTF-8");
	}

	public ResponseMessage getResults() throws Exception {
		return getResults(true);
	}

	public ResponseMessage getResults(boolean writeFlag) throws Exception {
		javax.jms.Message message = null;

		if (MOCK_RUN) {
			// Thread.sleep(getNextRandomNumber(5) * 100);
			Thread.sleep(500);
			return mockResponse;
		}

		// Receive is not thread safe according to an exception message.
		synchronized (mutex) {
			message = receiver.receive(TIMEOUT);
		}

		TextMessage text = null;
		if (message instanceof TextMessage) {
			text = (TextMessage) message;
			if (writeFlag) {
				System.out.println("RECEIVED MESSAGE ==>: " + text.getText());
			}
		}
		else {
			if (message != null) {
				System.err.println(message.toString());
				throw new Exception("ERROR: Received a non-Text message.");
			}
			else {
				System.err.println("ERROR: Receive timeout.");
				return null;
			}
		}

		ResponseMessage response = new ResponseMessage(text.getText());
		Assert.assertNotNull(response);
		if (writeFlag) {
			writeResponse(response);
		}

		return response;
	}

	public synchronized java.lang.String getNextSequenceNumber() {
		return Long.toString(sequenceNumber++);
	}

	public synchronized int getNextRandomNumber(int limit) {
		return random.nextInt(limit);
	}

	public static void setJMSType(boolean val) {
		setJMSType = val;
	}

	@org.junit.AfterClass
	public static void shutdown() throws Exception {
		System.err.println("\nShutting down...");
		if (tempQueue != null)
			try {
				tempQueue.delete();
			}
			catch (Exception e) {
				// Ignore
			}

		if (queueConn != null)
			queueConn.close();

		// Close the Session
		if (session != null)
			session.close();

		// Close the JMSConnection
		if (connection != null)
			connection.close();
	}

}
