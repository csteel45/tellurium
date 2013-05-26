/*
 * @(#)TestConsole.java
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

package com.FortMoon.tellurium;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import com.FortMoon.tellurium.util.PropertyEditor;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import com.birosoft.liquid.LiquidLookAndFeel;
import com.FortMoon.tellurium.model.Dispatcher;
import com.FortMoon.tellurium.model.RequestStats;
import com.FortMoon.tellurium.model.RunInfo;
import com.FortMoon.tellurium.net.JMSConnection;
import com.FortMoon.tellurium.util.SplashScreen;
import com.FortMoon.tellurium.view.HelpDialog;
import com.FortMoon.tellurium.view.LogArea;
import com.FortMoon.tellurium.view.ResultsPanel;
import com.FortMoon.tellurium.view.TestConsoleMenuBar;
import com.FortMoon.tellurium.view.TestPanel;
import com.FortMoon.tellurium.view.monitor.MemoryMonitor;
import com.FortMoon.tellurium.view.monitor.MeterPanel;

/**
 * This class is the UI for running performance tests. It servers as both the
 * view and controller classes. It is fairly modular with most of the panels
 * broken out. It is an evolutionary artifact that could benefit from extensive
 * refactoring. It is Swing-based and has been updated with some Java 5
 * technology.
 *
 * @author Christopher Steel - Principal Architect, FortMoon
 * @version 4.0
 * @see com.FortMoon.tellurium.model.Dispatcher
 * @see com.FortMoon.tellurium.view.ResultsPanel
 */
public class TestConsole extends JFrame implements ActionListener, Observer {

	private static final long serialVersionUID = -2522594006163125832L;

	private JScrollPane topScrollPane;
	private JScrollPane logScrollPane;
	private JSplitPane mainPane;
	private LogArea logArea;
	private ResultsPanel resultPanel;
	private TestPanel testPanel;
	private JButton startButton;
	private JButton clearButton;
	private final JFrame frame;
	private JTabbedPane tabPanel;
	private PropertyEditor editor;

	private int numRequests;
	private final Dispatcher dispatcher = new Dispatcher();

	public TestConsole() {
		super();
		frame = this;
// dispatcher.start();
		dispatcher.addObserver(this);
	}

	public void init() {

		try {
			LiquidLookAndFeel liquidLNF = new LiquidLookAndFeel();
			UIManager.setLookAndFeel(liquidLNF);
		}
		catch (Exception e) {
			System.err.println("Error loading L&F: " + e);
		}

		// Create log component and put it in a scroller
		logArea = LogArea.getInstance();

		System.setOut(new PrintStream(logArea));
		System.setErr(new PrintStream(logArea));

		// Initialize worker vars
		numRequests = 0;

		// Create the base panel
		JPanel basePanel = new JPanel();
		basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
		basePanel.setPreferredSize(new Dimension(500, 500));
		// Create the tabbed pane
		tabPanel = new JTabbedPane();

		tabPanel.setPreferredSize(new Dimension(500, 180));
		tabPanel.setMinimumSize(new Dimension(500, 180));
		// tabPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

		// Setup Test panel
		testPanel = new TestPanel();
		tabPanel.addTab("Load Test Configuration", null, testPanel,
				"Load test based on manual input");

		JPanel memoryPanel = new MemoryMonitor();
		MeterPanel meterPanel = new MeterPanel();
		dispatcher.registerMonitorListener(meterPanel.getMeter());

		JPanel monitorPanel = new JPanel();
		monitorPanel.setLayout(new BorderLayout());
		monitorPanel.add(meterPanel, BorderLayout.WEST);
		monitorPanel.add(memoryPanel, BorderLayout.CENTER);

		tabPanel.addTab("Memory Monitor", null, monitorPanel,
				"Monitor Test Console Memeory Usage");

		tabPanel.setSelectedIndex(0);

		// Create action button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setPreferredSize(new Dimension(480, 50));
		buttonPanel.setMinimumSize(new Dimension(480, 50));
		buttonPanel.setMaximumSize(new Dimension(480, 50));

		startButton = new JButton("Start Requests");
		startButton.setEnabled(false);
		buttonPanel.add(startButton);
		buttonPanel.add(Box.createHorizontalStrut(40));

		clearButton = new JButton("Clear Results");
		clearButton.setEnabled(true);
		buttonPanel.add(clearButton);

		// Add Callbacks for Mouse Events
		SymMouse aSymMouse = new SymMouse();
		startButton.addMouseListener(aSymMouse);
		clearButton.addMouseListener(aSymMouse);
		testPanel.runPerfSuite.addMouseListener(aSymMouse);

		basePanel.add(Box.createVerticalStrut(10));
		basePanel.add(tabPanel);
		basePanel.add(Box.createVerticalStrut(10));
		basePanel.add(buttonPanel);

		resultPanel = new ResultsPanel();
		basePanel.add(resultPanel);

		/* Put the master panel and the results panel in a scroller. */
		topScrollPane = new JScrollPane();
		topScrollPane.getViewport().add(basePanel);
		topScrollPane.setPreferredSize(new Dimension(300, 615));

		/* Put the log area in a scroller. */
		logScrollPane = new JScrollPane();
		logScrollPane.getViewport().add(logArea.getComponent());

		/* Show them */

		mainPane = new JSplitPane();
		mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false);
		mainPane.setTopComponent(topScrollPane);
		mainPane.setBottomComponent(logScrollPane);

		this.setName("TELLURIUM Test Console");
		this.setTitle("TELLURIUM Test Console");

		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"images/FortMoon-logo.gif"));

		WindowListener l = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(l);

		this.setJMenuBar(new TestConsoleMenuBar(this, this));
		this.getContentPane().add(mainPane);

		// Set dimensions and show the frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
// this.setSize(screenSize.width - screenSize.width / 2, screenSize.height
// - screenSize.height / 4);
		this.setSize(800, 768);

// this.setLocation(screenSize.width / 4, 10);
		this.setLocation((screenSize.width - 800) / 2,
				(screenSize.height - 768) / 2);

		this.setVisible(true);
		System.out.println("Test Console started");
		SplashScreen splash = new SplashScreen(this, "images/splash.jpg");
// FIXME
		new Thread(new Runnable() {
			public void run() {
				try {
					JMSConnection.setUp();
					startButton.setEnabled(true);
				}
				catch (Exception e) {
					System.err.println("Exception setting up harness: " + e);
					e.printStackTrace(System.err);
				}
			}
		}).start();

	}

	public void clearResults() {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// clear hashtable
		dispatcher.clear();
		resultPanel.clearResults();
		logArea.clear();
		resultPanel.setRequestNumber(0, false);
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Callback method for handling Dispatcher events.
	 *
	 */
	public void update(Observable o, Object arg) {
// System.out.println("TestConsole update called with: " + arg);
		if (arg instanceof RequestStats) {
			resultPanel.updateStats((RequestStats) arg);
		}
		if (arg instanceof java.lang.String) {
			if (arg.equals("COMPLETE")) {
				startButton.setEnabled(true);
				clearButton.setEnabled(true);
			}
		}
		if (arg instanceof RunInfo) {
			resultPanel.clearResults();
			resultPanel
					.setRequestNumber(((RunInfo) arg).getNumRequests(), true);
		}
	}

	/**
	 * Call-back method for handling menu bar action events.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(("Exit"))) {
			System.exit(0);
		}
		if (e.getActionCommand().equals(("Reset"))) {
			clearResults();
			System.out.println("Reset called.");
			startButton.setEnabled(true);
			clearButton.setEnabled(true);
			try {
				JMSConnection.shutdown();
				JMSConnection.setUp();
			}
			catch (Exception ex) {
				System.err.println("Exception reinitializing TestHarness: "
						+ ex);
				ex.printStackTrace(System.err);
			}
		}
		if (e.getActionCommand().equals(("Documentation"))) {
			new HelpDialog("docs/api/help-doc.html");
		}
		if (e.getActionCommand().equals(("Properties"))) {
			if(editor == null) {
				editor = new PropertyEditor();
			}
			editor.setVisible(true);
			editor.setFocusableWindowState(true);
			editor.setAlwaysOnTop(true);
		}
	}

	/**
	 * Main method for launching the TestConsole
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestConsole t = new TestConsole();
			t.init();
		}
		catch (Exception e) {
			System.err.println("Exception in init: " + e);
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Helper class for handling GUI mouse events.
	 *
	 */
	class SymMouse extends java.awt.event.MouseAdapter {
		@Override
		public void mouseClicked(java.awt.event.MouseEvent event) {

			Object object = event.getSource();

			if (object == startButton) {
				if (startButton.isEnabled()) {
					clearResults();
					numRequests = testPanel.getNumRequests();
					dispatcher.setDelay(testPanel.getDelay());
					// Needed for progress bar
					resultPanel.setRequestNumber(numRequests, true);
					if (testPanel.runPerfSuite()) {
						dispatcher.runSuite();
					}
					else {
// System.out.println("Simultaneous = "+ testPanel.isSimultaneous());
						dispatcher.startRequests(numRequests, testPanel
								.getRequestDir(), "", testPanel
								.isSimultaneous());
					}

					startButton.setEnabled(false);
					clearButton.setEnabled(false);
				}
			}

			if (object == clearButton) {
				startButton.setEnabled(false);
				clearButton.setEnabled(false);
				clearResults();
				startButton.setEnabled(true);
				clearButton.setEnabled(true);
			}

// if (object == testPanel.runPerfSuite) {
// if (testPanel.runPerfSuite.isSelected()) {
// testPanel.requestDir.setEnabled(false);
// testPanel.numRequests.setEnabled(false);
// }
// else {
// testPanel.requestDir.setEnabled(true);
// testPanel.numRequests.setEnabled(true);
// }
// }
// System.err.println("Event source = " + object);
		} // end-mouseClicked
	} // end-SymMouse

}
