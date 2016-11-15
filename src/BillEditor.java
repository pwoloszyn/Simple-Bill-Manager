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
import javax.swing.JLabel;
import javax.swing.JTextField;

public class BillEditor extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 291553117092367422L;
	
	// Class variables
	private Vector<Bill> bills;
	private int index;
	private boolean adding;
	
	// GUI variables and elements
	private GridBagConstraints gbc;
	private JTextField name_text;
	private JTextField day_text;
	private JTextField amount_text;
	private JTextField webadr_text;

	/**
	 * The primary constructor which handles adding bills
	 * into the billing database
	 * @param bills
	 */
	public BillEditor(Vector<Bill> bills) {
		super("Bill Editor");
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}
		});
		
		this.bills = bills;
		adding = true;
		name_text = new JTextField();
		day_text = new JTextField();
		amount_text = new JTextField("0.0");
		webadr_text = new JTextField();
		drawWindow();
	}
	
	/**
	 * The secondary constructor which handles editing existing
	 * bills in the billing database
	 * @param bills
	 * @param index
	 */
	public BillEditor(Vector<Bill> bills, int index) {
		this.bills = bills;
		this.index = index;
		adding = false;
		name_text = new JTextField(bills.get(index).getName());
		day_text = new JTextField(bills.get(index).getPaymentDay()+"");
		amount_text = new JTextField(bills.get(index).getLastBill());
		webadr_text = new JTextField(bills.get(index).getBillWebsite());
		
		drawWindow();
	}
	
	/**
	 * Draws the GUI
	 */
	private void drawWindow() {
		this.setLayout(new GridBagLayout());
		this.setSize(350, 200);
		this.setResizable(false);
		
		gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		JLabel name_label = new JLabel("Name:");
		add(name_label, gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		add(name_text, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		JLabel day_label = new JLabel("Day:");
		add(day_label, gbc);
		
		gbc.gridx = 1;
		add(day_text, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		JLabel amount_label = new JLabel("Amount:");
		add(amount_label, gbc);
		
		gbc.gridx = 1;
		add(amount_text, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel webadr_label = new JLabel("Web Address:");
		add(webadr_label, gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		add(webadr_text, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 4;
		JButton btnS = new JButton("Set");
		btnS.addActionListener(this);
		btnS.setActionCommand("set");
		add(btnS, gbc);
		
		gbc.gridx = 2;
		JButton btnC = new JButton("Close");
		btnC.addActionListener(this);
		btnC.setActionCommand("close");
		add(btnC, gbc);
		
		this.setVisible(true);
	}

	/**
	 * ActionListner Interface method handles the button actions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "set") {
			if(adding) {
				if(name_text.getText() != null && day_text.getText() != null && amount_text.getText() != null) {
					Bill bill = new Bill(name_text.getText(), amount_text.getText(), Integer.parseInt(day_text.getText()));
					bills.addElement(bill);
				}
			} else {
				if(name_text.getText() != null && day_text.getText() != null && amount_text.getText() != null) {
					bills.get(index).setName(name_text.getText());
					bills.get(index).setPaymentDay(Integer.parseInt(day_text.getText()));
					bills.get(index).setLastBill(amount_text.getText());
					bills.get(index).setBillWebsite(webadr_text.getText());
				}
			}
		} else if(e.getActionCommand() == "close") {
			this.setVisible(false);
			this.dispose();
		}
	}
	
}
