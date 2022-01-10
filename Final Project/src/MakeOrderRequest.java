
//This class is only used as a class to manage the algorithms of making an Order file by the 
//	customer.
import java.util.*;
import java.io.*;

public class MakeOrderRequest {
			
		//Method name: ValidRequest
		//use: Validates the card then passes to the OrderClass
		//input: customer, cart, total and supplier
		//output: returns null or new delivery base
		public static String ValidRequest(String customerID, String[] cart, double total, String supplier) throws IOException
		{
			CustomerAccount customer;
			try
			{
				customer = CustomerAccount.getCustomerAccountWithID(customerID);
			}
			catch(IOException e)
			{
				System.out.println("An error occured while making an order.");
				return null;
			}
			//Call the BankInterface file to authorize the payment
			int bankAuthorize = BankInterface.authorizePayment(customer.getCreditCard(), total);
			
			//values -1 and -2 are invalid.  1 is sent if valid.
			if (bankAuthorize < 0) 
			{
				while(bankAuthorize < 0)//Keep trying to get a valid card number if invalid.
				{
					//prints issues for bankauthorization.
					if(bankAuthorize == -1)
						System.out.println("Error!  Card doesn't exist!");
					if(bankAuthorize == -2)
						System.out.println("Error!  Transaction amount too high!");
						
					System.out.println("Invalid card! Enter a new card, or type nothing to exit");
					
					Scanner r = new Scanner(System.in);
					
					String newCreditCard = r.nextLine();
					if(newCreditCard.equals(""))
					{
						r.close();
						return null;
					}
					
					bankAuthorize = BankInterface.authorizePayment(newCreditCard, total); //retry authorization with new card.
					
					//Valid card, will be set to 1.
					if(bankAuthorize > 0)
					{
						System.out.println("valid card.  Editing account for new card.");
						
						//customerAccount's card is changed to be valid.
						CustomerAccount.changeCustomerCreditCard(customer, newCreditCard);
					}
					
				}
			}
				
	
			//Once valid then pass it as a parameter to OrderClass	
			OrderClass orderA = new OrderClass(customer.getID(),supplier, cart, total, bankAuthorize ,"Ordered");
			
			return "A new order delivery base been created with the name:\t" + orderA.getOrderName(); //returns the file order name.
			
			
				
				
				
			}
		
			
			
			
	}
		