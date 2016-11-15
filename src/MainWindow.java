import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3394864265265863223L;
	
	// GUI variables and elements
	private JPanel main_panel;
	private JPanel button_panel;
	private JScrollPane scrollpane;
	private JTable table;
	private GridBagConstraints gbc;

	// Data storage variables, the first two are a part of the
	// JScrollPane object.
	public Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	public Vector<Object> columns = new Vector<Object>();
	public Vector<Bill> bills = new Vector<Bill>();

	/**
	 *  The Constructor
	 */
	public MainWindow() {
		super("Bill Manager");

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				saveToFile();
				dispose();
				System.exit(0);
			}
		});
		
		if (!readFromFile())
			saveToFile();
		fillTheTable();
		drawWindow();
		refreshLoop();
	}

	/**
	 *  Writes the billing data to serialized file
	 */
	private void saveToFile() {
		try {
			FileOutputStream fs = new FileOutputStream("bill_data.ser");
			ObjectOutputStream outs = new ObjectOutputStream(fs);
			outs.writeObject(bills);
			outs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Loads the billing data from serialized file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean readFromFile() {
		try {
			FileInputStream fs = new FileInputStream("bill_data.ser");
			ObjectInputStream ins = new ObjectInputStream(fs);
			bills = (Vector<Bill>) ins.readObject();
			ins.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 *  Runs a thread which updates the GUI every 100ms
	 */
	private void refreshLoop() {
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					fillTheTable();
					scrollpane.setVisible(false);
					scrollpane.setVisible(true);
				}
			}
		};
		t.start();
	}

	/** 
	 * Fills the table in the GUI with billing data
	 */
	private void fillTheTable() {

		data.clear();

		columns.add("Name");
		columns.add("Day");
		columns.add("Last Bill");
		columns.add("Due/Overdue");

		for (Bill b : bills) {
			Vector<Object> row = new Vector<Object>();
			row.addElement(b.getName());
			row.addElement(b.getPaymentDay());
			row.addElement(b.getLastBill());
			int overdueQ = b.isItDue();
			if (overdueQ == 2)
				row.addElement("YES!");
			else if(overdueQ == 1)
				row.addElement("YES");
			else
				row.addElement("No");
			data.addElement(row);
		}
	}

	/**
	 * Draws the GUI
	 */
	private void drawWindow() {

		this.setLayout(new GridBagLayout());
		this.setSize(400, 250);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 2.5;
		gbc.insets = new Insets(10, 10, 0, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		table = new JTable(data, columns);
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(150);
		col = table.getColumnModel().getColumn(1);
		col.setPreferredWidth(50);
		col = table.getColumnModel().getColumn(2);
		col.setPreferredWidth(100);
		col = table.getColumnModel().getColumn(3);
		col.setPreferredWidth(100);
		table.setFillsViewportHeight(true);

		scrollpane = new JScrollPane(table);
		main_panel = new JPanel();
		main_panel.setLayout(new BorderLayout());
		main_panel.add(scrollpane);
		add(main_panel, gbc);

		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.weighty = 0.1;
		gbc.gridy = 1;

		button_panel = new JPanel();

		JButton btnP = new JButton("Pay");
		btnP.addActionListener(this);
		btnP.setActionCommand("pay");
		button_panel.add(btnP);

		JButton btnA = new JButton("Add");
		btnA.addActionListener(this);
		btnA.setActionCommand("add");
		button_panel.add(btnA);

		JButton btnE = new JButton("Edit");
		btnE.addActionListener(this);
		btnE.setActionCommand("edit");
		button_panel.add(btnE);
		
		JButton btnH = new JButton("History");
		btnH.addActionListener(this);
		btnH.setActionCommand("history");
		button_panel.add(btnH);

		JButton btnD = new JButton("Delete");
		btnD.addActionListener(this);
		btnD.setActionCommand("delete");
		button_panel.add(btnD);
		add(button_panel, gbc);
		
		

		this.setVisible(true);
	}

	/**
	 * ActionListner Interface method handles the button actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String inp = e.getActionCommand();
		if (inp == "pay") {
			if(table.getSelectedRow() > -1) {
				if (!bills.elementAt(table.getSelectedRow()).getBillWebsite().isEmpty()) {
					try {
						Desktop.getDesktop().browse(new URI(bills.elementAt(table.getSelectedRow()).getBillWebsite()));
					} catch (IOException errormsg) {
						errormsg.printStackTrace();
					} catch (URISyntaxException errormsg) {
						errormsg.printStackTrace();
					}
				}
				String input = JOptionPane.showInputDialog("Enter amount paid.");
				if (input != null) {
					try {
						bills.elementAt(table.getSelectedRow()).billPaid();
						bills.elementAt(table.getSelectedRow()).setLastBill(Double.parseDouble(input) + "");
						bills.elementAt(table.getSelectedRow()).updateBillHistory(input);
					} catch (NumberFormatException errormsg) {
						JOptionPane.showMessageDialog(null, "Incorrect input");
					}
				}
			}
		} else if (inp == "add") {
			BillEditor be = new BillEditor(bills);
		} else if (inp == "edit") {
			if(table.getSelectedRow() > -1) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					BillEditor be = new BillEditor(bills, index);
				}
			}
		} else if (inp == "history") {
			if(table.getSelectedRow() > -1) {
				BillHistory bh = new BillHistory(bills.elementAt(table.getSelectedRow()));
			}
		} else if (inp == "delete") {
			bills.remove(table.getSelectedRow());

		}
	}
}
