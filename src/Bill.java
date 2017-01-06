import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

public class Bill implements Serializable{

	private static final long serialVersionUID = 4731372722822688256L;
	
	// Bill detail variables
	private String name;
	private String last_bill = "0.0";
	private int day;
	private int last_bill_month;
	private int last_bill_year;
	private String bill_website = "";
	
	// A set of past payments composed of (date, amount) pairs
	private Vector<StringPair> bill_history;
	
	/**
	 * The constructor
	 */
	public Bill(String name, String last_bill, int day) {
		this.name = name;
		this.last_bill = last_bill;
		if(day < 30)
			this.day = day;
		else
			this.day = 30;
		if(Calendar.getInstance().get(Calendar.MONTH) == 0) {
			this.last_bill_month = 11;
			this.last_bill_year = Calendar.getInstance().get(Calendar.YEAR) - 1;
		} else {
			this.last_bill_month = Calendar.getInstance().get(Calendar.MONTH) - 1;
			this.last_bill_year = Calendar.getInstance().get(Calendar.YEAR);
		}
		
		bill_history = new Vector<StringPair>();
	}
	
	/**
	 * Getter/setter methods
	 * @return
	 */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastBill() {
		return last_bill;
	}
	
	public void setLastBill(String last_bill) {
		this.last_bill = last_bill;
	}
	
	public int getPaymentDay() {
		return day;
	}
	
	public void setPaymentDay(int day) {
		this.day = day;
	}
	
	public String getBillWebsite() {
		return bill_website;
	}
	
	public void setBillWebsite(String bill_website) {
		this.bill_website = bill_website;
	}
	
	public void billPaid() {
		last_bill_month = Calendar.getInstance().get(Calendar.MONTH);
		last_bill_year = Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public Vector<StringPair> getBillHistory() {
		return bill_history;
	}
	
	/**
	 * Updates the billing history with a new payment
	 * @param amount
	 */
	public void updateBillHistory(String amount) {
		StringPair strpair = new StringPair((Calendar.getInstance().get(Calendar.MONTH)+1)+"/"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
				+"/"+Calendar.getInstance().get(Calendar.YEAR), amount);
		bill_history.addElement(strpair);
	}
	
	/**
	 * Checks if the bill is due/overdue
	 * @return
	 */
	public int isItDue() {
		int current_date_value = (Calendar.getInstance().get(Calendar.YEAR) - last_bill_year)*12;
		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) >= day && (current_date_value + Calendar.getInstance().get(Calendar.MONTH)) > last_bill_month) {
			if((current_date_value + Calendar.getInstance().get(Calendar.MONTH)) > last_bill_month+1)
				return 2;
			else
				return 1;
		}
		return 0;
	}
	
}
