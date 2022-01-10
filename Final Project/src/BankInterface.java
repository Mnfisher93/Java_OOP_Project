//The BankInterface is used for communication for overall payment authorization and commitment from
//	the bank.

import java.util.Random;

public class BankInterface 
{

	
	//Method name: authorizePayment
	//use: Will return -1 if the overall card is not valid.  Will return -2 if the amount is higher 
	// than the max value.
	//input: creditCardNumber and total for charge
	//output: returns authorize
	public static int authorizePayment(String creditCardNumber, double total)
	{
		
		int bankResponse = Bank.authorizePayment(creditCardNumber, total);
		

		if(bankResponse == 1)
		{
			//Here, the authorization for the bank purchase is made.
			Random rand = new Random();
			int authorize = rand.nextInt(1000) + 1;
			return authorize;
			
		}
		else
		{
			return bankResponse;
		}
	}
	
	

	//Method name: commitCharge
	//use: Charges the credit card
	//input: creditCardNumber and total
	//output: returns boolean of the banks response
	public static boolean commitCharge(String creditCardNumber, double total)
	{
		boolean bankResponse = Bank.commitPayment(creditCardNumber, total);
		
		if(!bankResponse)
		{
			System.out.println("The commit for the credit card was invalid.");
		}
		
		return bankResponse;
		
	}
	
}