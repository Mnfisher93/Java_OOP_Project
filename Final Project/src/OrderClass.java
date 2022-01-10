//The orderClass is used to store information about an order object.  
//	The orderclass will either read and store information from an order file 
//	(located in the Order directory) and store it in an object, or it will create a new Order text 
//	file from a call by the MakeOrderRequest and store that information about the object.
import java.util.*;
import java.io.*;

public class OrderClass {
	
	//The OrderClass will store variables read from an order.
	String userid;
	String cart[];
	double total;
	int authorize;
	String ostatus;
	int orderNumber;
	String supplier;
	
	//constuctor name: OrderClass
	//use: Used to create a file that stores the items the user wants along with the total
	//input: userid, supplier, cart, total, authorize, and ostatus 
	public OrderClass(String userid, String supplier, String[] cart, double total, int authorize, String ostatus) 
	{
		//Initialize all the variables that will be used
		this.userid = userid;
		this.cart = cart;
		this.total = total;
		this.authorize = authorize;
		this.ostatus = ostatus;
		this.supplier = supplier;
		
		//Creating file directory, if needed.
		File orderDirectory = new File("Order");
		
		if(!orderDirectory.exists())
		{
			orderDirectory.mkdir(); //Make the directory, if it doesn't exist
			orderNumber = 1;
		}
		else
		{
			orderNumber = orderDirectory.listFiles().length + 1; //updates file position to be the number of orders + 1
		}
		
		File orderFile = new File(orderDirectory.getName() + "/" + userid + "-" + supplier  + "-" + orderNumber + ".txt");
		
		PrintWriter orderFileWriter = null; //defines the filewriter.
		
		try
		{
			orderFileWriter = new PrintWriter(orderFile); //create an object to be able to write info for the order.
			
			
			orderFileWriter.println(userid); //Write the userid onto the file
			
			//Loops through the cart, and writes an item in the order for each.
			for(String cartItems: cart)
			{	if(cartItems == null)
					break;
				orderFileWriter.println(cartItems);
			}
			
			
			
			orderFileWriter.println(total);		//write the total on the file
			orderFileWriter.println(authorize);	//write the payment on the file
			orderFileWriter.println(ostatus);	//write the ostatus on the file
			
		}
		catch(IOException e)
		{
			//In case that the file fails
			System.out.println("file can't be made.");
		}
		finally
		{
			orderFileWriter.close();//Always make sure to close the file
		}
	}
	
	//Contructor name: OrderClass
	//use: This will be used to make an order object to store order info into.
	//input: The string of the filename for the order.
	public OrderClass(String fileName) throws FileNotFoundException
	{
		
		File orderDirectory = new File("Order");
		if(!orderDirectory.exists())
			orderDirectory.mkdir();
		
		
		//If the input has a .txt, remove to properly make an order.
		if(fileName.contains(".txt"))
		{
			fileName = fileName.substring(0, fileName.indexOf(".txt"));
			
		}
		
		//Split the parts of the filename to get the ordernumber and supplier to store.
		String fileNameContent[] = fileName.split("-");
		if(fileNameContent.length != 3)
		{
			throw new FileNotFoundException();
		}
		
		
		supplier = fileNameContent[1];
		try
		{
			orderNumber = Integer.parseInt(fileNameContent[2]);
		}
		catch(InputMismatchException | NumberFormatException incorrectFile)
		{
			throw new FileNotFoundException();
		}
		
		File orderFile = new File("Order/" + fileName + ".txt"); //Makes a file referencing the object.
		
		Scanner orderReader = new Scanner(orderFile);	//makes a scanner to read the order reader.
		userid = orderReader.nextLine();	//Reads from the order class, stores userid.
		
		cart = new String[1000];	//cart is defined along with size to help reading.
		int size = 0;
		
		while(!orderReader.hasNextDouble()) 	//Increment the size of the cart until a double is encountered, being the total price of order.
		{
			cart[size++] = orderReader.nextLine(); //Stores a singular item in the cart.
		}
		
		total = orderReader.nextDouble();		//assign the double to total
		authorize = orderReader.nextInt();	//assign the int to authorize
		//We use nextLine to properly be able to read the order status.
		orderReader.nextLine();
		ostatus = orderReader.nextLine();	//after reading the next line assign status
		
		//we always have to close the file.
		orderReader.close();
			
	}
	
	public String getID()
	{
		return userid;
	}
	
	public double getTotal()
	{
		return total;
	}
	
	//Method name: showOrders
	//use: to show the orders
	//input: id
	//output: names of the orders in the directory for a specific customer will be printed.
	public static boolean showOrders(String userID)
	{
		File orderDirectory = new File("Order");
		if(!orderDirectory.exists())
			orderDirectory.mkdir();
		
		File[] ordersList = orderDirectory.listFiles();	//gets an array of all the files.
		if(ordersList.length == 0)
			return false;
		
		//loops through all files in the directory.  
		for(File order: ordersList)
		{
			//splits the name segments for an order.
			String fileName = order.getName();
			String[] fileNameSegments = fileName.split("-");
			//if the customerId of the order matches the sent one for the class, it will show.
			if(fileNameSegments[0].equals(userID))
			{
				System.out.println(order.getName());
			}
		}
		return true;
	}
	//Method name: getOrdersForSupplier
	//use:  to show the orders
	//input: id of supplier.
	//output: returns the order objects of a supplier.
	public static LinkedList<OrderClass> getOrdersForSupplier(String id) throws FileNotFoundException
	{
		File orderDirectory = new File("Order");
		if(!orderDirectory.exists())
			orderDirectory.mkdir();
		
		
		File[] ordersList = orderDirectory.listFiles();//gets an array of all the files.
		
		//LinkedList is stored for all the order objects.
		LinkedList<OrderClass> orders = new LinkedList<OrderClass>();
		
		//loops through all files in the directory.  
		for(File order: ordersList)
		{
			String fileName = order.getName();
			
			String[] fileNameSegments = fileName.split("-");
			//if the supplerID of the order matches the sent one for the class, it will show.
			if(fileNameSegments[1].equals(id))
			{
				//A new order is made and will be put in the linked list.
				OrderClass fileOrder = new OrderClass(fileName);
				orders.addLast(fileOrder);
			}
		}
		
		//returns linked list.
		return orders;
	}
	
	//Method name: printOrder
	//use: display the order of the user
	//input: N/A
	//output: order, items, total, authorization number, and status
	public void printOrder()
	{
		System.out.println("Order for account:\t" + userid);
		System.out.println("Items bought:\t");
		
		String[] invoiceItems = new String[10];
		int[] invoiceAmount = new int[10];
		int invoiceSize = 0;
		for(String cartItem: cart)
		{
			if(cartItem == null)
			{
				break;
			}
			
			for(int i = 0; i < 10; i++)
			{
				if(invoiceItems[i] == null || invoiceItems[i].equals(cartItem))
				{
					if(invoiceItems[i] == null)
					{
						invoiceItems[i] = cartItem;
					}
					invoiceAmount[i]++;
					break;
				}
			}
		}
		
		for(int i = 0; i < 10; i++)
		{
			if(invoiceItems[i] == null)
			{	
				invoiceSize = i;
				break;
			}
		}
		
		for(int i = 0; i < invoiceSize; i++)
		{
			System.out.println("Item: " + invoiceItems[i] + "  Amount: " + invoiceAmount[i]);
		}
		System.out.println("Total:\t $" + total);
		System.out.println("Authorization number:\t" + authorize);
		System.out.println("Order status:\t" + ostatus);
	}
	
	//Method name: change status
	//use: change the status of an order
	//input: status
	//output: Creates an updated order
	public boolean changeStatus(String status)
	{
		ostatus = status; //This will be for when we change the status of the order. 
		
		File orderFile =  new File("Order/" + userid + "-" + supplier  + "-" + orderNumber + ".txt");
		
		PrintWriter orderFileWriter = null;
		
		try
		{
			orderFileWriter = new PrintWriter(orderFile);
			
			orderFileWriter.println(userid);
			
			for(String cartItems: cart)
			{	if(cartItems == null)
					break;
				orderFileWriter.println(cartItems);
			}
			
			
			
			orderFileWriter.println(total);
			orderFileWriter.println(authorize);
			orderFileWriter.println(ostatus);
			
		}
		catch(IOException e)
		{
			System.out.println("file can't be made.");
			return false;
		}
		finally
		{
			orderFileWriter.close();
		}
		return true;
		
	}
	
	//Method name: getOrderName
	//use: retrieves the order
	//input: N/A
	//output: returns the userid, supplier, and orderNumber	
	public String getOrderName()
	{
		return userid + "-" + supplier + "-" + orderNumber;
	}
	
	
	
	
}