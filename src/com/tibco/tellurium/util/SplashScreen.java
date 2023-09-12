/*******************************************************************************
 * Copyright � 2007 FortMoon Consulting, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions, the following disclaimer, and the original author.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * This software is the confidential and proprietary information of FortMoon
 * Consulting, Inc. ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with FortMoon.
 * 
 * FORTMOON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. FORTMOON SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 
 * Copyright Version 1.0
 ******************************************************************************/
package com.tibco.tellurium.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * Class representing an application splash screen.
 * <p>
 * Typical usage:
 * 
 * <pre>
 * SplashScreen splashScreen = new SplashScreen(&quot;/com/company/splash.jpg&quot;);
 * splashScreen.open(3000);
 * </pre>
 * 
 * @author <a href="mailto:jacob.dreyer@geosoft.no">Jacob Dreyer</a>
 * @author <a href="mailto:csteel@FortMoon.com">Christopher Steel</a>
 */
public class SplashScreen extends JWindow {
	static final long serialVersionUID = 1L;
	private transient Image image_;
	private int x_, y_, width_, height_;
	private MediaTracker mediaTracker;

	/**
	 * Create a new splash screen object of the specified image. The image file
	 * is located and referred to through the deployment, not the local file
	 * system; A typical value might be "/com/company/splash.jpg".
	 * 
	 * @param imageFileName
	 *            Name of image file resource to act as splash screen.
	 */
	public SplashScreen(String imageFileName) {
		this(new JFrame(), imageFileName);
		System.err.println("load image: ");
	}

	public SplashScreen(JFrame jFrame, String img) {
		super(jFrame);
		init(img);
	}

	public SplashScreen(String img, int timeout) {
		this(img);
		open(timeout);
		setVisible(true);
	}

	private void init(String imageFileName) {
		try {
			image_ = new ImageIcon(imageFileName).getImage();
			if (image_ == null) {
				System.err.println("Unable to load image: " + imageFileName);
				return;
			}
			mediaTracker = new MediaTracker(this);
			mediaTracker.addImage(image_, 0);
			mediaTracker.waitForID(0);

			width_ = image_.getWidth(this);
			height_ = image_.getHeight(this);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			x_ = (screenSize.width - width_) / 2;
			y_ = (screenSize.height - height_) / 2;

		}
		catch (Exception ex) {
			System.err.println("Unable to load image: " + ex);
			ex.printStackTrace(System.err);
			image_ = null;
			return;
		}
		open(3000);
	}

	/**
	 * Open the splash screen and keep it open for the specified duration or
	 * until close() is called explicitly.
	 */
	public void open(int nMilliseconds) {
		if (image_ == null) {
			System.err.println("No image to load.");
			return;
		}

		Timer timer = new Timer(Integer.MAX_VALUE, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				((Timer) event.getSource()).stop();
				close();
			};
		});

		timer.setInitialDelay(nMilliseconds);
		timer.start();
		System.err.println("here.");

		setBounds(x_, y_, width_, height_);
		setVisible(true);
		setAlwaysOnTop(true);
	}

	/**
	 * Close the splash screen.
	 */
	public void close() {
		setVisible(false);
		dispose();
	}

	/**
	 * Paint the splash screen window.
	 * 
	 * @param graphics
	 *            The graphics instance.
	 */
	@Override
	public void paint(Graphics graphics) {
		if (image_ == null)
			return;
		graphics.drawImage(image_, 0, 0, width_, height_, this);
	}

	/**
	 * This method is needed to keep Findbugs from complaining that the Image
	 * attribute is not deserializable.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
	}

}
