import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class BillHistory extends JFrame{

	// GUI variables and elements
	private JPanel main_panel;
	private JScrollPane scrollpane;
	private JTable table;
	private GridBagConstraints gbc;
	
	// Data storage variables, the first two are a part of the
	// JScrollPane object.
	private Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	private Vector<Object> columns = new Vector<Object>();
	private Bill bill;
	
	/**
	 * The constructor
	 * @param bill
	 */
	public BillHistory(Bill bill) {
		super("Bill History");
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
			}
		});
		
		this.bill = bill;
		fillTheTable();
		drawWindow();
		refreshLoop();
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

		columns.add("Date");
		columns.add("Amount");
		
		for (StringPair strpair : bill.getBillHistory()) {
			Vector<Object> row = new Vector<Object>();
			row.addElement(strpair.a);
			row.addElement(strpair.b);
			data.addElement(row);
		}
	}
	
	/**
	 * Draws the GUI
	 */
	private void drawWindow() {
		this.setLayout(new GridBagLayout());
		this.setSize(400, 200);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 2.5;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		table = new JTable(data, columns);
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(120);
		col = table.getColumnModel().getColumn(1);
		col.setPreferredWidth(100);
		table.setFillsViewportHeight(true);

		scrollpane = new JScrollPane(table);
		main_panel = new JPanel();
		main_panel.setLayout(new BorderLayout());
		main_panel.add(scrollpane);
		add(main_panel, gbc);

		this.setVisible(true);
	}

}
