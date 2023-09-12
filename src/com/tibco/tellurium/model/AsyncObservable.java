/*
 * @(#)AsyncObserver.java
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
package com.tibco.tellurium.model;

import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a mechanism to asynchronously notify observers. In many
 * UIs, you do not want to lock a UI update thread notifying all of the
 * observers, especially when those observers may be updating the UI themselves.
 * By extending AsyncObservable, subclasses can continue to use the Observable
 * class, unchanged, the only difference being that the the notifyObservers
 * methods will use an Executor to asynchronously call the base notifyObservers
 * method. Another slight difference is that these methods will also call
 * setChanged(true) as a convenience.
 * 
 * *NOTE: If you require 'double buffering' of the notifications, this class
 * will not work, as it always calls setChanged prior to notify.
 * 
 * @author Christopher Steel, Principal Architect - FortMoon
 * @version Aug 24, 2007 3:02:58 PM
 * 
 */
public class AsyncObservable extends Observable {
	protected Executor executor = new ThreadPoolExecutor(1, 1, 60,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(150));

	/**
	 * Overridden method to asynchronously notify observers.
	 */
	@Override
	public void notifyObservers() {
		Notification notification = new Notification();
		executor.execute(notification);
	}

	/**
	 * Overridden method to asynchronously notify observers passing an argument.
	 * 
	 * @param arg
	 *            The object to pass to the observers.
	 */
	@Override
	public void notifyObservers(Object arg) {
		Notification notification = new Notification(arg);
		executor.execute(notification);
	}

	private void asyncNotifyObservers() {
		super.notifyObservers();
	}

	private void asyncNotifyObservers(Object arg) {
		super.notifyObservers(arg);
	}

	class Notification implements Runnable {
		Object arg = null;

		public Notification() {
		}

		public Notification(Object arg) {
			this.arg = arg;
		}

		public void run() {
			setChanged();
			if (arg == null) {
				asyncNotifyObservers();
			}
			else {
				asyncNotifyObservers(arg);
			}
		}
	}
}
