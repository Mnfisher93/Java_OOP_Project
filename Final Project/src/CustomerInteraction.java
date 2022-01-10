//This will be the main class that will store methods to call and will be the main source of input.
//Contains the main method
import java.io.*;
import java.util.*;

public class CustomerInteraction {

	//the variables for the customerinteraction interface will be defined
	static Scanner r = new Scanner(System.in);
	static CustomerAccount customerAccount = null;
	static CatalogItems catalogCart = null;
	
	//Method name: createAccount
	//use:  used for the customer to make a new account.
	//input: N/A
	//output: A customer file will be named with the appropriate variables.
	public static void createAccount() throws IOException
	{
		String id, password, name, address, phoneNumber, creditCard;
		
		//boolean will be used to verify if the account was properly made or not.
		boolean accountSuccess = false;
		
		//the do-while loop will go through and check if the accountSuccess is set to zero.  
		//If not, we will continue asking input until it is valid.
		do
		{
			//The do-whiles will go through every value needed for a customer object:
			//id, password, name, address, phonenumber, and creditcard.
			//If the user inputs empty values, or id and password are two words, it will ask again for the respective parts.
			
			do
			{
			
				System.out.println("Please enter an id:\t");
				id = r.nextLine();	//Assign ID
				
				if(id.equals("") || id.contains(" ")) //checks to see if item is valid.  If not, display error to user.  
					System.out.println("Error!  please enter a valid id!");
				
			}
			while(id.equals("") || id.contains(" "));
			
			do
			{
		
				System.out.println("Please enter a password:\t");
				password = r.nextLine(); //Assign password
				
				if(password.equals("") || password.contains(" "))//checks to see if item is valid.  If not, display error to user.  
					System.out.println("Error!  please enter a valid password!");
				
			}
			while(password.equals("") || password.contains(" "));
		
			do
			{

				System.out.println("Please enter a name");
				name = r.nextLine();//Assign name
				
				if(name.equals(""))//checks to see if item is valid.  If not, display error to user.  
					System.out.println("Error!  please enter a valid name!");
				
			}
			while(name.equals(""));
		
			do
			{
				
				System.out.println("Please enter an address");
				address = r.nextLine();//Assign address
				
				if(address.equals(""))//checks to see if item is valid.  If not, display error to user.  
					System.out.println("Error!  please enter a valid address!");
				
			}
			while(address.equals(""));
			
			do
			{
				
				System.out.println("Please enter a phone number");
				phoneNumber = r.nextLine();//Assign phone number
				
				if(phoneNumber.equals(""))//checks to see if item is valid.  If not, display error to user.  
					System.out.println("Error!  please enter a valid phone number!");
				
			}
			while(phoneNumber.equals(""));
			
			do
			{
				System.out.println("Please enter a credit card number");
				creditCard = r.nextLine();//Assign creditCard
				
				if(creditCard.equals(""))//checks to see if item is valid.  If not, display error to user.  
					System.out.println("Error!  please enter a valid credit card number!");
			}
			while(creditCard.equals(""));
		
			try
			{
				//Try to create an account with all the entered information
				accountSuccess = CustomerAccount.createNewAccountCustomer(id, password, name, address, phoneNumber, creditCard);
			}
			catch(IOException fileError)
			{
				System.out.println("error!  File not able to open!"); //Printed whenever the object can't successfully be made.
			}
			
			if(!accountSuccess)
			{
				System.out.println("Error!  Account already exists!"); //printed if the file can be made, but it already exists.
			}
		}
		while(!accountSuccess);
		
		System.out.println("Account created");//result was a success
	}
	
	//Method name: login
	//use: used to log in
	//input: N/A
	//output: defines a customeraccount for the login
	public static void login()
	{
		
		if(customerAccount != null)
		{
			System.out.println("Error - account already exists."); //results if someone is already logged in.  Exit login.
			return;
		}
		
		
		String id, password;
		
		
		//The id and password will be defined and set for a login.
		System.out.println("Please enter an id:\t");
		id = r.nextLine();
		System.out.println("Please enter a password:\t");
		password = r.nextLine();
		
		
		try
		{
			customerAccount = CustomerAccount.logInCustomer(id, password); //send info to log in
			
			if(customerAccount == null)
			{
				System.out.println("Invalid login credentials."); // given if id or password are invalid.  a customeraccount will be still null.
			}
			else
			{
				System.out.println("account logged in"); //account successfully logged in.
			}
			
		}
		catch(FileNotFoundException invalidFileName) //If the file cant be found, this exception catches.
		{
			System.out.println("Login not found - id given didnt exist!");
		}
	
	}
	
	//Method name: logout
	//use: logs out current account.
	//input: N/A
	//output: logs customerAccount out, if currently logged in.
	public static void logout()
	{
		if(customerAccount == null)
		{
			System.out.println("Error - account already logged out."); //Will be sent if the account was already logged out or never logged in.  
		}
		else
		{
			//Sets the account to null and effectively logs out.  Displays to user.
			customerAccount = null;
			System.out.println("Account logged out");
		}
		
	}

	
	//Method name: selectItems
	//use: allows customer to put items in their cart
	//input: N/A
	//output: items are stored in the cart, a SelectItems object.
	public static void selectItems() throws IOException
	{
		//The following if triggers if a user already bought objects.  It notifies that running will remove their items.
		//If the user types yes, program continues.  Else, over.
		if(catalogCart != null)
		{
			System.out.print("Selecting items will erasse the old cart.  Are you sure?  Type yes if you wish to continue:\t");
			System.out.print("\n Type yes if you wish to continue.  Typing anything else will exit from selecting:\t");
			if(!r.nextLine().equals("yes"))
				return;
		}
		
		//will store the shipper catalog that a user wishes to look from.
		String shipper;
		
		//Will return a list of suppliers that can be purchased.
		System.out.println("Here a list of shippers to buy from:\t");
		if(!CustomerAccount.showSuppliers())
			return;
		
		while(true)
		{
			System.out.println("Please enter a shipper that you want to buy from:\t");
			shipper = r.nextLine(); //will store the shipper string for whatever a person wishes to look for.
			
			if(CustomerAccount.isShipper(shipper))
			{
				break;
			}
			else
			{
				System.out.println("Error!  Not a valid shipper!");
			}
			
		}
		//Creates a catalog to display it.
		Catalog currentCatalog = new Catalog("catalog");
		currentCatalog.display();
		
		//Makes a new shoppingcart object, pulls shop to buy from.
		catalogCart = new CatalogItems();
		catalogCart.shop(shipper);
		
		//If the user browsed, but didnt select anything, we will revert the cart to a null value.
		if(catalogCart.getTotal() == 0.0)
		{
			catalogCart = null;
			System.out.println("No items were selected.  Returning");
		}
		else
		{
			System.out.println("a cart was made for the " + shipper + " catalog.");
		}
	}
	
	
	//Method name: makeOrder
	//use: allows the user to make the order
	//input: N/A
	//output: order will be made and stored in a proper order file.
	public static void makeOrder() throws IOException
	{
		//if-else if validates that a customer is logged and that they have a valid shopping cart.
		if(customerAccount == null)
		{
			System.out.println("You must be logged in to use!");
			return;
		}
		else if(catalogCart == null)
		{
			System.out.println("You must purchase items before entering an order!");
			return;
		}
		
		//make an order using the given information
		String result = MakeOrderRequest.ValidRequest(customerAccount.getID(), catalogCart.getCart(), catalogCart.getTotal(), catalogCart.getSupplier());
		
		//if-else will either say if it failed, or it will give true and generate the file name.
		if(result == null)
		{
			System.out.println("No output generated");
		}
		else
		{
			System.out.println("File created with name:\t" + result);
			
			//We recreate a new customeraccount in case the card file changed.
			customerAccount = new CustomerAccount(customerAccount.getID(), customerAccount.getPassword());
		}
		
		//makes cart null to indicate the selected items are now purchased.
		catalogCart = null;
	}
	
	//Method name: viewOrder
	//use: Allows the customer to view all orders
	//input: N/A
	//output: displays the orders
	public static void viewOrder()
	{
		//verifies that a customeraccount is real
		if(customerAccount == null)
		{
			System.out.println("You must be logged in to use!");
			return;
		}
		
		
		if(!OrderClass.showOrders(customerAccount.getID()))
		{
			System.out.println("Currently, there are no orders.");
			
		}
		System.out.println("Please select one of the files by typing in the name:");
		
		String fileName = r.nextLine();
		try
		{
			OrderClass newOrder = new OrderClass(fileName);
			newOrder.printOrder();//Displays items in order
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error!  The file name given is not valid!"); //given if the file given doesnt exist.
			return;
		}
				

	}
	
	//Method name: main
	//use: displays the main menu and allows the user to choose what to do
	//input: string[] args
	//output: menu options
	public static void main(String[] args) throws IOException
	{
		String userInput;
		
		System.out.println("Welcome customer");
		
		//Display options to the user
		System.out.println("Input options:");
		System.out.println("0 - create account");
		System.out.println("1 - login");
		System.out.println("2 - logout");
		System.out.println("3 - select Items from catalog");
		System.out.println("4 - make an Order");
		System.out.println("5 - view an Order");
		System.out.println("-1 - exit");
		
		//do while goes until -1 enters, which is exit.
		do
		{
			
			System.out.print("Please enter input:\t");
			userInput = r.nextLine();
			
			switch(userInput)
			{
			//Call methods based on chosen case
				case "0":
					createAccount();
					break;
				case "1":
					login();
					break;
				case "2":
					logout();
					break;
				case "3":
					selectItems();
					break;
				case "4":
					makeOrder();
					break;
				case "5":
					viewOrder();
					break;
			}
			
			
		}
		while(!userInput.equals("-1"));
	}

}