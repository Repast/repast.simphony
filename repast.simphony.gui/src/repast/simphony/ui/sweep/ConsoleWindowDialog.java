package repast.simphony.ui.sweep;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import repast.simphony.parameter.MutableParameters;

public class ConsoleWindowDialog extends JDialog {

	private JPanel buttonBar;
	private JButton cancelButton;
	private JButton okButton;

	private Process process;
	private JTextArea textArea;

	private MutableParameters params;

	PrintStream err = null;
	PrintStream out = null;

	PipedInputStream pis = null;
	InputStream is = null;
	InputStreamReader isr = null;
	BufferedReader br = null;

	public ConsoleWindowDialog(Frame owner) {
		super(owner);
		initComponents("Console Log");
		setLocationRelativeTo(getOwner());
	}

	public ConsoleWindowDialog(Dialog owner, String title) {
		super(owner);
		initComponents(title);
		setLocationRelativeTo(getOwner());
		Point parent = getOwner().getLocation();
		setLocation((int) parent.getX()+50, (int) parent.getY()+50);

	}

	private void restore() {
		try {
			if (br != null)
				br.close();
			if (isr != null)
				isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (err != null)
			System.setErr(err);
		if (out != null)
			System.setOut(out);
	}

	private void addListeners() {
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ConsoleWindowDialog.this.restore();
				ConsoleWindowDialog.this.dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ConsoleWindowDialog.this.restore();
				ConsoleWindowDialog.this.dispose();
			}
		});
	}

	private void initComponents(String title) {
		setTitle(title);
		setModal(false);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		textArea = getInputPanel();
		textArea.setEditable(false);

		contentPane.add(new JScrollPane(textArea),BorderLayout.CENTER);

		contentPane.add(getButtonBar(),BorderLayout.SOUTH);
		addListeners();

	}

	public void init(MutableParameters params) {

		this.params = params;

		Container contentPane = getContentPane();
		textArea = getInputPanel();

		contentPane.add(new JScrollPane(textArea),BorderLayout.CENTER);

		contentPane.add(getButtonBar(),BorderLayout.SOUTH);
		addListeners();

	}

	private JPanel getButtonBar() {
		if (buttonBar == null) {
			buttonBar = new JPanel();
			okButton = new JButton();
			cancelButton = new JButton();
			buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
			buttonBar.setLayout(new GridBagLayout());
			((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{80, 80, 80};
			((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0};

			//---- okButton ----
			okButton.setText("Done");
			buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));

			//		        //---- cancelButton ----
			//		        cancelButton.setText("Cancel");
			//		        buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
			//		                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			//		                new Insets(0, 0, 0, 0), 0, 0));

		}
		return buttonBar;
	}
	public void setLogPrintStream() {
		err = System.err;
		System.setErr(System.out);
		out = System.out;

		pis = new PipedInputStream();
		try {
			System.setOut(new PrintStream(new PipedOutputStream(pis)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		is = pis;
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);
	}

	public void logPrintStream() {


		String line;

		try {
			textArea.append("*********** Processing Started ***********");
			textArea.append("\n\n\n");
			textArea.setCaretPosition(textArea.getText().length());
			while ((line = br.readLine()) != null) {
				textArea.append(line);
				textArea.append("\n");
				textArea.setCaretPosition(textArea.getText().length());
			}
			textArea.append("\n\n\n*********** Processing Completed ***********");
			textArea.append("\n");
			textArea.setCaretPosition(textArea.getText().length());
		} catch (IOException e) {
			// TODO Auto-generated catch block

			this.restore();

//			System.out.println("################# IOException ConsoleWindowDialog logPrintStream #############################");
//
//						e.printStackTrace();

		}
	}

	public void log(Process p) {
		process = p;
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		try {
			textArea.append("*********** Processing Started in Separate Process***********");
			textArea.append("\n\n\n");
			textArea.setCaretPosition(textArea.getText().length());
			while ((line = br.readLine()) != null) {
				textArea.append(line);
				textArea.append("\n");
				textArea.setCaretPosition(textArea.getText().length());
			}
			textArea.append("\n\n\n*********** Processing Completed ***********");
			textArea.append("\n");
			textArea.setCaretPosition(textArea.getText().length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("################# IOException ConsoleWindowDialog log #############################");
			e.printStackTrace();
		}
	}

	private JTextArea getInputPanel() {
		return new JTextArea(20, 100);

	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}


}
