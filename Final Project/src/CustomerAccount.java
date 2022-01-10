	//This class will be able to create or change customer account files, which store information 
//	about a customer for the shopping system.  In addition, customerAccount can read a 
// 	customerAccount file and will be able to store it in specific instance variables.  	

import java.util.*;
import java.io.*;
public class CustomerAccount 
{

	
	//All instance variables for the customerAccount, in String for both customer and shipper.
	private String id;
	private String password;
	
	//unique intance variables for the customer account.
	private String name;
	private String address;
	private String phoneNumber;
	private String creditCard;
	

	//Name: CustomerAccount
	//inputs: id, password
	//use: assign the id and password for a supplier customeraccount.
	public CustomerAccount(String id, String password)
	{
		this.id = id;
		this.password = password;
	}
	
	
	
	//Name: CustomerAccount
	//inputs: id, password, name, address, phone number, credit card, customerType
	//use: assign the values for id, password, name, address, phone, and creditcard for a customer customeraccount
	public CustomerAccount(String id, String password, String name, String address, String phoneNumber, String creditCard)
	{
		this.id = id;
		this.password = password;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.creditCard = creditCard;
	}
	
	
	//Name: getID
	//Use: returns the ID	
	public String getID()
	{
		return id;
	}
	
	//Name: getPassword
	//Use: returns the password
	public String getPassword()
	{
		return password;
	}
	
	//Name: getName
	//Use: returns the name
	public String getName()
	{
		return name;
	}
	
	
	//Name: getAddress
	//Use: returns the address
	public String getAddress()
	{
		return address;
	}
	
	
	//Name: getPhoneNumber
	//Use: returns the phone number
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	//Name: getCreditCard
	//Use: returns the creditcard info
	public String getCreditCard()
	{
		return creditCard;
	}
	
	
	//Method name: createNewAccountCustomer
	//use: True-false for both create indicates success/failure
	//input: id, password, name, address, phonenumber, creditcard
	//output: returns boolean for whether account made or not.
	public static boolean createNewAccountCustomer(String id, String password, String name, String address, String phoneNumber, String creditCard) throws IOException
	{
		File idFile = new File("Customer\\");
		
		//If directory not found, will make one.
		if(!idFile.exists())
		{
			idFile.mkdir();
		}
		
		idFile = new File("Customer\\" + id + ".txt");
		
		//if the account was already made, we return false.
		if(idFile.exists())
		{
			return false;
		}
		
		
		PrintWriter fileWriter = new PrintWriter(idFile);
		fileWriter.println("Password: " + password);		//Write the password
		fileWriter.println("Name: " + name);				//Write the name
		fileWriter.println("Address: " + address);			//Write the address
		fileWriter.println("Phone Number: " + phoneNumber);	//Write the phonenumber
		fileWriter.println("Credit Card: " + creditCard);	//Write the credit card
		
		fileWriter.close();		//Always close
		return true;
		
	}

	//Method name: changeCustomerCreditCard
	//use: update the creditcard on a file
	//input: customer, creditcard number
	//output: returns boolean for whether account changed or not.
	public static boolean changeCustomerCreditCard(CustomerAccount customer, String creditCardNumber)
	{
		File idFile = new File("Customer\\" + customer.getID() + ".txt");
		try
		{
			PrintWriter fileWriter = new PrintWriter(idFile);

			fileWriter.println("Password:" + customer.password);			//Write password
			fileWriter.println("Name:" + customer.name);					//Write name
			fileWriter.println("Address:" + customer.address);				//Write address
			fileWriter.println("Phone Number:" + customer.phoneNumber);	//Write phone
			fileWriter.println("Credit Card: " + creditCardNumber);			//write new credit card
			
			fileWriter.close();
			
			return true;
		}
		catch(IOException e)
		{
			return false;//Return false if fails to run from an error.
		}

		
	}
	
	//Method name: createNewAccountSupplier
	//use: Writes new file for supplier storing password and id
	//input: id, password
	//output: returns boolean for if supplier made or not.
	public static boolean createNewAccountSupplier(String id, String password) throws IOException
	{
		File idFile = new File("Supplier\\");
		
		//If directory not found, will make one.
		if(!idFile.exists())
		{
			idFile.mkdir();
		}
		
		idFile = new File("Supplier\\" + id + ".txt");
		
		//if the account was already made, we return false.
		if(idFile.exists())
		{
			return false;
		}
		
		PrintWriter fileWriter = new PrintWriter(idFile);	
		
		Inventory.createNewInventory(id);
		
		
		fileWriter.println("Password: " + password);
		fileWriter.close();
		return true;
		
	}
	
	
	//Method name: logInShipper
	//use: Logs in the shipper 
	//input: id, userInputPassword
	//output: returns a shipper Customer object, making logged in.
	public static CustomerAccount logInShipper(String id, String userInputPassword) throws FileNotFoundException
	{
		Scanner fileReader;
		
		File idFile = new File("Supplier\\" + id + ".txt");
		
		fileReader = new Scanner(idFile);
		
		CustomerAccount loggedInAccount = null;
		
		//We assume that password will be the first thing.
		if(fileReader.next().contentEquals("Password:"))
		{
			String accountPassword = fileReader.next();
			if(userInputPassword.equals(accountPassword))
			{
				loggedInAccount = new CustomerAccount(id, userInputPassword); //Only logs in if password matches
			}
		
		}
		
		fileReader.close();
	
		return loggedInAccount;
		
		
	}
	
	//Method name: logInCustomer
	//use: reads the file and assigns log in info
	//input: id, userInputPassword
	//output: returns a customer customeraccount object, making it logged in.
	public static CustomerAccount logInCustomer(String id, String userInputPassword) throws FileNotFoundException
	{
		Scanner fileReader;
		File idFile = new File("Customer\\" + id + ".txt");
		
		fileReader = new Scanner(idFile);
		
		CustomerAccount loggedInAccount = null;
		
		if(fileReader.nextLine().contentEquals("Password: " + userInputPassword))
		{
				String name = fileReader.nextLine().substring(6);
				String address = fileReader.nextLine().substring(9);
				String phoneNumber = fileReader.nextLine().substring(14);
				String creditCard = fileReader.nextLine().substring(13);
				
				loggedInAccount = new CustomerAccount( id,  userInputPassword,  name,  address,  phoneNumber,  creditCard);
		}

		fileReader.close();
		return loggedInAccount;
		
		
	}
	
	//Method name: getCreditCardForShipper
	//use: searches the file for a creditcard info
	//input: userID
	//output: returns the credit card information
	public static String getCreditCardForShipper(String userID)
	{
		Scanner fileReader = null;
		File idFile = new File("Customer\\" + userID + ".txt");
		
		String creditCard = null;
		try
		{
			fileReader = new Scanner(idFile);
			while(fileReader.hasNext())
			{
				String fileLine = fileReader.nextLine();
				if(fileLine.contains("Credit Card:"))
					creditCard = fileLine.substring(13);
			}
		}
		catch(IOException e)
		{
			System.out.println("error"); //If fails print error
		}
		finally
		{
			fileReader.close();
		}
		return creditCard;
		
		
	}
	
	public static CustomerAccount getCustomerAccountWithID(String customerID) throws IOException
	{
		Scanner fileReader;
		File idFile = new File("Customer\\" + customerID + ".txt");
		fileReader = new Scanner(idFile);
		
		String password = fileReader.nextLine().substring(9);
		String name = fileReader.nextLine().substring(5);
		String address = fileReader.nextLine().substring(8);
		String phoneNumber = fileReader.nextLine().substring(13);
		String creditCard = fileReader.nextLine().substring(13);
		
		
		
		CustomerAccount loggedInAccount = null;
		loggedInAccount = new CustomerAccount( customerID,  password,  name,  address,  phoneNumber,  creditCard);
		
		fileReader.close();
		return loggedInAccount;
		
	}
	
	
	//Method name: showSuppliers
	//use: shows suppliers whenever looking for a catalog to look for.
	//input: userID
	//output:prints the values of the suppliers to be seen.
	public static boolean showSuppliers()
	{
		File supplierD = new File("Supplier");
		if(!supplierD.exists())
			supplierD.mkdir();
		
		File[] suppliers =supplierD.listFiles();
		if(suppliers.length == 0)
		{
			System.out.println("error!  Suppliers do not exist!");
			return false;
		}
		
		for(File supplier: suppliers)
		{
			String fileName = supplier.getName();
			fileName = fileName.substring(0,fileName.indexOf('.'));
			System.out.println(fileName);
		}
		return true;
	}
	
	public static boolean isShipper(String shipper)
	{
		File[] suppliers = new File("Supplier").listFiles();
		
		for(File supplier: suppliers)
		{
			String fileName = supplier.getName();
			fileName = fileName.substring(0,fileName.indexOf('.'));
			if(fileName.equals(shipper))
				return true;
		
		}
		return false;
	}

}