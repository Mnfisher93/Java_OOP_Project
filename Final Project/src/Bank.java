//This class allows corrections to be made in the bank and authorizes if the charge is able to
// be done or if there is insufficient money in the bank.  (This is essentially an external system).

import java.util.*;
import java.io.*;
public class Bank 
{
	//Stores the location of the bank's credit cards.
	static final String bankPath = "BankCardNumbers\\";
	
	//Method name: commitPayment
	//Use: commits the bank payment
	//Input: creditcard, total
	//Output: bool if the payment was committed
	public static boolean commitPayment( String creditCard, double total)
	{


			File bankAccountsFile = new File(bankPath + creditCard + ".txt");
			Scanner fileReader;
			try
			{
				fileReader = new Scanner(bankAccountsFile);
			}
			catch(FileNotFoundException fileE)
			{
				return false;
			}
			

			double transactionAmount = fileReader.nextDouble();		//Assign amount based on file
			double transactionLimit = fileReader.nextDouble();		//Assign limit based on file
			
			fileReader.close();	//the file always needs to be closed.
			
			//Checks if the transaction exceeds current limit or not.
			if(transactionAmount + total > transactionLimit)
			{
				return false;
			}
			else
			{
				changeBankListing(creditCard,transactionAmount + total, transactionLimit);
				return  true;
			}
		
	}
	
	//Method name: authorizePayment
	//Use: authorizes if the payment can be done (if they have enough money)
	//Input: creditcard, total
	//Output: returns value based on whether the payment can be made or not
	//Three states for output: -1 = creditCard doesnt exist.  -2 = transaction too high. 1 = valid.	
	public static int authorizePayment(String creditCard, double total)
	{
		File bankAccountsFile = new File(bankPath + creditCard + ".txt");
		Scanner fileReader;
		
		//Check for if the file exists (creditcard is real)
		try
		{
			fileReader = new Scanner(bankAccountsFile);
		}
		catch(FileNotFoundException fileE)
		{
			return -1;
		}
		

		double transactionAmount = fileReader.nextDouble();		//Assign amount based on file
		double transactionLimit = fileReader.nextDouble();		//Assign limit based on file
		
		fileReader.close();
		
		//check if amount is not higher than limit.
		if(transactionAmount + total > transactionLimit)
		{
			return -2;
		}
		else
		{
			return 1;
		}
	}
	
	//Method name: changeBankListing
	//Use: This will be called if we know a transaction is valid to change the total/limit.
	//Input: creditCard, total, transactionLimit
	//Output: creates an updated file with the new values of the bank
	private static void changeBankListing(String creditCard, double total, double transactionLimit)
	{
		File bankAccountsFile = new File(bankPath + creditCard + ".txt");
		PrintWriter fileWrite;
		
		//defines fileWrite for credit card.
		try
		{
			fileWrite = new PrintWriter(bankAccountsFile);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("unencountered error");
			return;
		}
		
		//Prints the changed creditCardNumber values.
		fileWrite.print(total + " ");
		fileWrite.print(transactionLimit);
		
		fileWrite.close();	//file always needs to be closed.
		
	}

	

	
}