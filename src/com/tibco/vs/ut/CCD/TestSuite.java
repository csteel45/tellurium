package com.tibco.vs.ut.CCD;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Assert;
import org.junit.Test;

import com.tibco.tellurium.net.JMSConnection;
import com.tibco.tellurium.net.RequestMessage;
import com.tibco.tellurium.net.ResponseMessage;
import com.tibco.vs.ut.ESBException;
import com.tibco.vs.ut.PersonNameTextType;
import com.tibco.vs.ut.PersonNameType;
import com.tibco.vs.ut.PersonSearchResult;
import com.tibco.vs.ut.PersonType;

@SuppressWarnings("unused")
public class TestSuite extends JMSConnection {
	public final static String QUERY_TARGET = "CCD";

	/**
	 * Test harness for the Verification Service.
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	@Test
	public void TC1() throws Exception {
		RequestMessage request = new RequestMessage();

		request.setVisaFoilNumber("48467040");
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC1-"
				+ getNextSequenceNumber());

		send(request);

		ResponseMessage response = getResults();
		Assert.assertNotNull(response);
		List<PersonSearchResult> list = response.getPersonSearchResult();
		Assert.assertTrue(list.size() > 0);
		PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
		Assert.assertNotNull(pResult.getPerson());
		Assert.assertTrue(pResult.getVisaVerification().getPersonDigitalImage()
				.size() == 0);
	}

	@Test
	public void TC2() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC2-"
				+ getNextSequenceNumber());

		request.setVisaFoilNumber("48-46-7040");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		ESBException e = response.getEsbException();
		Assert.assertTrue((e != null) && (e.getErrorMessage().length() > 1));

	}

	@Test
	public void TC3() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC3-"
				+ getNextSequenceNumber());

		request.setVisaFoilNumber("98467040");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		List<PersonSearchResult> list = response.getPersonSearchResult();
		Assert.assertTrue(list.size() > 0);
		PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
		Assert.assertNull(pResult.getPerson());

	}

	@Test
	public void TC4() throws Exception {
		RequestMessage request = new RequestMessage();
		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC4-"
				+ getNextSequenceNumber());

		request.setVisaFoilNumber("48467040");

		send(request.toString() + "</garbage XML>", QUERY_TARGET);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		ESBException e = response.getEsbException();
		Assert.assertTrue((e != null) && (e.getErrorMessage().length() > 1));
	}

	@Test
	public void TC5() throws Exception {
		RequestMessage request = new RequestMessage();

		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC5-"
				+ getNextSequenceNumber());

		// Set valid FOIL record that has no Photo
		request.setVisaFoilNumber("48467040");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		ESBException e = response.getEsbException();
		// FIXME: Add a FOIL number that breaks when available.
// Assert.assertNotNull(e);
	}

	@Test
	public void TC6() throws Exception {
		RequestMessage request = new RequestMessage();

		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC6-"
				+ getNextSequenceNumber());

		request.setVisaFoilNumber("48467040");
		request.setFirstName("SABAH K");
		request.setLastName("MOHAMMED");
// request.setBirthDate("1976-01-02");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		List<PersonSearchResult> list = response.getPersonSearchResult();
		Assert.assertTrue(list.size() > 0);
		PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
		PersonType person = pResult.getPerson();
		Assert.assertNotNull(person);
		List<PersonNameType> nameList = person.getPersonName();
		Assert.assertNotNull(nameList);
		Assert.assertTrue(!nameList.isEmpty());
		PersonNameType name = (PersonNameType) nameList.toArray()[0];
		PersonNameTextType nameText = (PersonNameTextType) (name
				.getPersonSurName().toArray()[0]);
		Assert.assertTrue(nameText.getValue().equals("MOHAMMED"));
	}

	@Test
	public void TC7() throws Exception {
		RequestMessage request = new RequestMessage();

		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC7-"
				+ getNextSequenceNumber());

		request.setVisaFoilNumber("48467040");
		request.setFirstName("SABAH K");
		request.setLastName("AMOHAMMEDA");
		request.setBirthDate("1976-01-31");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		List<PersonSearchResult> list = response.getPersonSearchResult();
		Assert.assertTrue(list.size() > 0);
		PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
		PersonType person = pResult.getPerson();
		Assert.assertNotNull(person);
	}

	@Test
	public void TC8() throws Exception {
		RequestMessage request = new RequestMessage();

		request.setQueryTarget(QUERY_TARGET);
		request.setTransactionID(this.getClass().getName() + ".TC8-"
				+ getNextSequenceNumber());

// request.setVisaFoilNumber("48467040");
		request.setVisaFoilNumber("09876543");

		send(request);

		ResponseMessage response = getResults();

		Assert.assertNotNull(response);
		List<PersonSearchResult> list = response.getPersonSearchResult();
		Assert.assertTrue(list.size() > 0);
		PersonSearchResult pResult = (PersonSearchResult) list.toArray()[0];
		Assert.assertNull(pResult.getPerson());
	}

	public static junit.framework.Test suite() {
		return new JUnit4TestAdapter(TestSuite.class);
	}
}
