/*
 * @(#)Dispatcher.java
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
package com.FortMoon.tellurium.model;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.FortMoon.tellurium.view.monitor.MonitorFeedListener;

/**
 * This class is responsible for dispatching Run requests, monitoring status and
 * updating listeners. It is the main engine of the Test Console.
 * 
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 */
public class Dispatcher extends AsyncObservable implements ClientManager {
	private static final long serialVersionUID = 1L;
	private int numRequests = 0;
	private int numReturned = 0;
	private int numSent = 0;
	private int numFailed = 0;
	private int totalRequests = 0;
	private float totalTime = 0.0f;
	private String requestDir;
	private boolean simultaneous = false;
	private final transient ThreadPoolExecutor executor;
	private long delay = 0L;
	private transient MonitorFeedListener monitorFeedListener;
	private boolean runComplete = true;
// FIXME: Make this configurable in the UI
	private static final String STATS_FILE = "stats.txt";

	public Dispatcher() {
		super();
		executor = new ThreadPoolExecutor(152, 152, 999999, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(500));
		executor.prestartAllCoreThreads();
	}

	public Dispatcher(int numRequests, int totalRequests, String requestDir,
			String svc) {
		this();
		this.numRequests = numRequests;
		this.totalRequests = totalRequests;
		this.requestDir = requestDir;
	}

	public void runSuite() {
		writeHeader();
		loadTest();
	}

	public void write(String msg) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(STATS_FILE), true);
			fos.write(msg.getBytes());
			fos.write("\n".getBytes());
		}
		catch (Exception e) {
			System.err.println("Exception writing header: " + e);
			e.printStackTrace(System.err);
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
				}
				catch (Exception e) {
					// Never happens.
				}
			}
		}
	}

	public void writeHeader() {
		write("Performance Test, " + new Date().toString());
	}

	public synchronized void loadTest() {

		executor.execute(new LoadTest(this));
	}

	public void writeResults() {
		float avgTime = (totalTime / numRequests);
		System.out.println("Average time for run = " + avgTime);
		if (this.simultaneous) {
			write("Simultaneous," + LoadTest.getLoadTestDir() + ","
					+ this.numRequests + "," + numFailed + "," + avgTime);
		}
		else {
			write("Sequential," + LoadTest.getLoadTestDir() + ","
					+ this.numRequests + "," + avgTime);
		}
	}

	public void startRequests(int numRequests, String requestDir,
			String queryTargets, boolean simultaneous) {

		this.simultaneous = simultaneous;
		this.numRequests = numRequests;
		this.requestDir = requestDir;
		if (simultaneous) {
			startRequests(numRequests, requestDir);
		}
		else {
			startRequest(0, requestDir);
		}
	}

	void startRequests(int numRequests, String requestDir) {
		this.numRequests = numRequests;
		this.numSent = 0;
		this.numReturned = 0;
		this.numFailed = 0;
		this.totalRequests = 0;
		this.simultaneous = true;
		runComplete = false;
		RunInfo runInfo = new RunInfo(numRequests);
		if (monitorFeedListener != null) {
			monitorFeedListener.clear();
		}

		notifyObservers(runInfo);
		System.gc();
		try {
			for (int i = 0; i < numRequests; i++) {
				Client client = new Client(Integer
						.valueOf(i + totalRequests));
				client.register(this);
				client.setRequestDir(requestDir);
				executor.execute(client);
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	private void startRequest(int id, String requestDir) {

		Client worker = new Client(Integer.valueOf(id + totalRequests));
		worker.register(this);
		// worker.setSvcName(svcName);
		worker.setRequestDir(requestDir);
		// worker.start();
		executor.execute(worker);
		// addWorker(worker.getSvcName(), worker);
	}

	public void startDelayedRequest(String reqDir) {
		executor.execute(new Runnable() {
			public void run() {

				try {
					Thread.sleep(delay);
				}
				catch (Exception e) {
					System.err
							.println("Interrupted exception delaying. results may be scewed. "
									+ e);
				}
				startRequest(++numSent, requestDir);
			} // run
		});
	}

	public void notify(RequestStats stats) {
		synchronized (this) {
			notifyObservers(stats);
		}
	}

	public synchronized void update(RequestStats stats) {
// System.err.println("Manager update stats called: " + stats);
		notifyObservers(stats);

		if (stats.isComplete()) {
			totalTime += stats.getTotalWorkTime();
			numReturned++;
			if (stats.getState() == Client.FAILURE) {
				numFailed++;
			}
			if (numReturned == numRequests) {
				writeResults();
				numRequests = numReturned = numFailed = numSent = 0;
				totalTime = 0.0f;
				notifyObservers("COMPLETE");
				runComplete = true;
				System.out.println("Run complete.");
			}
			else {
				if (!simultaneous) {
					if (delay == 0L) {
						startRequest(numReturned, requestDir);
					}
				}
			}
			if (monitorFeedListener != null) {
				monitorFeedListener.removeActivity();
			}
		}
		else {
			if ((monitorFeedListener != null)
					&& (stats.getState() == Client.SENT)) {
				monitorFeedListener.addActivity();
			}
			if (!simultaneous && (delay > 0L)
					&& stats.getState().equals(Client.SENT)) {
				if (numSent < numRequests - 1) {
					startDelayedRequest(requestDir);
				}
			}
		}

	}

	public void clear() {
		this.numRequests = 0;
		this.numReturned = 0;
		this.numSent = 0;
		this.totalRequests = 0;
		this.numFailed = 0;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.FortMoon.tellurium.model.ClientManager#registerMonitorListener(com.FortMoon.tellurium.view.monitor.MonitorFeedListener)
	 */
	public void registerMonitorListener(MonitorFeedListener listener) {
		this.monitorFeedListener = listener;
	}

	public String getRequestDir() {
		return this.requestDir;
	}

	public boolean isRunComplete() {
		return runComplete;
	}

}
