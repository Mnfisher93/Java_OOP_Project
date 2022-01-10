
//This class modifies the inventory and can create inventory, remove inventory
// help reserve inventory or remove a reserved inventory section

import java.util.*;
import java.io.*;
public class Inventory 
{
	String[] inventoryItems;
	String inventoryFilePath;
	String[] reservedItems;
	
	//Name: Inventory
	//Use: sets items/reserved items to null.  Gives inventory path, called by user.
	//Input: path
	//Output: N/A
	public Inventory(String path)
	{
		inventoryItems = null;
		inventoryFilePath = path;
		reservedItems = null;
	}
	
	//Name: setCatalogItems
	//Use: sets up inventory and reads from a file.
	//Input: N/A
	//Output: N/A
	public boolean setCatalogItems()
	{
		//if the items have already been declared, then this program doesnt need to run.
		if(inventoryItems != null)
			return true;
		
		File inventoryReader = new File("Inventory\\" + inventoryFilePath + ".txt");
		
		//sets up file reader.  If failure, notifies and returns false.
		Scanner fileReader;
		try
		{
			 fileReader = new Scanner(inventoryReader);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("The file was NOT found");
			return false;
		}
		
		//Fileinput takes in values from file for specifically inventory files and not reserved items.
		String fileInput;
		while(fileReader.hasNext())
		{
			
			fileInput = fileReader.nextLine();
			
			//This checks for the reserved section on the file and stores properly.  
			//If so, we will exit this loop and start on reserved.
			if(fileInput.contains("R:"))
			{
				reservedItems = new String[1];
				reservedItems[0] = fileInput;
				break;
			}
			
			//We update old inventoryItems here.  Will make an array one larger than last to properly store.
			String[] tempItems = inventoryItems;
			
			if(inventoryItems != null)
				inventoryItems = new String[tempItems.length + 1];
			else
			{
				inventoryItems = new String[1];
			}
			
			int count;
			for(count = 0; count < inventoryItems.length - 1; count++)
				inventoryItems[count] = tempItems[count];
			
			inventoryItems[count] = fileInput; // new inventory item stored at end of array.
			
		}
		
		//Start of reserved iterations.  Inherently same as inventoryItem setup.
		while(fileReader.hasNext())
		{
			fileInput = fileReader.nextLine();
			
			String[] tempItems = reservedItems;
			
			if(reservedItems != null)
				reservedItems = new String[tempItems.length + 1];
			else
			{
				reservedItems = new String[1];
			}
			
			int count;
			for(count = 0; count < reservedItems.length - 1; count++)
				reservedItems[count] = tempItems[count];
			
			reservedItems[count] = fileInput;
		}
		
		//We always have to close file
		fileReader.close();
		
		//Returns true to denote the inventory object has been properly set up.
		return true;
	}
	
	//Name: changeInventoryFile
	//Use: Rewrites the inventory file after any changes to variable storage.
	//Precondition:  that there has been some change to one of the instance variables.
	//Input: N/A
	//Output: File is recreated with inventory and reserves
	public void changeInventoryFile()
	{
		File inventoryReader = new File("Inventory\\" + inventoryFilePath + ".txt");
		PrintWriter newFile;
		
		//creates an object to write to the file.
		try
		{
			newFile = new PrintWriter(inventoryReader);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Inventory file not found!");
			return;
		}
		
		//This writes regular file entries.
		for(int i = 0; i < inventoryItems.length; i++)
		{
			
			newFile.println(inventoryItems[i]);
			
		}
		
		//This writes any reserved items tinto the file.
		if(reservedItems != null)
		{
			for(int i = 0; i < reservedItems.length; i++)
			{
				newFile.println(reservedItems[i]);
			}
		}
		
		//we always have to close the file.
		newFile.close();
		
	}
	
	//Name: reserveInventoryItems
	//Use: Will go around and ensure the files are correctly reserved.
	//Also checks to see if we have enough for the given item.
	//Input: order
	//Output: a new section of the reserved listing is generated, and we remove items to go to reserve.
	public boolean reserveInventoryItems(OrderClass order)
	{
		int invoiceSize = 0; //denotes the size for the Order invoice sent.
		boolean encounterError = false; //marker to represent if there has been an error reading.
		
		//Calibrates the object and the files/ reserved if needed.
		if(!setCatalogItems())
		{
			System.out.println("There was an error while processing");
			return false;
		}
		
		//This will encode the orderClass to be able to handle the inventory.
		String[] invoiceItems = new String[10];
		int[] invoiceAmount = new int[10];
		String[] cartItems = order.cart;
		//For loop goes over for the names of all files and stores in unique array.
		for(String cartItem: cartItems)
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
		//The order amounts are placed in the items array.	
		for(int i = 0; i < 10; i++)
		{
			if(invoiceItems[i] == null)
			{	
				invoiceSize = i;
				break;
			}
			invoiceItems[i] = invoiceItems[i] + " " + invoiceAmount[i];
		}
		
		
		//Will iterate until all invoiceItems are checked and made some reserve.
		String[] reserved = reservedItems;
		for(int i = 0; i < invoiceSize; i++)
		{
			String invoiceItemName = invoiceItems[i].substring(0,invoiceItems[i].indexOf(' '));
		
			//Block should get the last number of the string for the inventory.
			String temp = invoiceItems[i].substring(invoiceItems[i].indexOf(' ') + 1);
			
			int invoiceQuantity = Integer.parseInt(temp);
			
			
			
			//For loop will be used to find a matching inventory item to the invoice and properly reserve.
			for(int j = 0; j < inventoryItems.length; j++)
			{
				String inventoryItemName = inventoryItems[j].substring(0, inventoryItems[j].indexOf(' '));
				
				
				String inventoryQuantityString = inventoryItems[j].substring(inventoryItems[j].indexOf(' ') + 1);
				
				
				int inventoryQuantity = Integer.parseInt(inventoryQuantityString);
				
				
				if(invoiceItemName.contentEquals(inventoryItemName))
				{
					//Checks if the quantity is greater than the amount requested to be reserved.  If not, an error is sent
					//and system is notified.
					if(inventoryQuantity < invoiceQuantity)
					{
						System.out.println(inventoryItemName + " has not enough stock!  An inventory order is required!");
						encounterError = true;
					}
					else
					{
						//inventoryItems are properly placed, and we subtract amount
						inventoryItems[j] = inventoryItemName;
						inventoryItems[j] += " " + (inventoryQuantity - invoiceQuantity);
						
						//The inventory should just be form:
							//item .... amount
						
						String[] tempItems = reserved;
						
						//The new reservation in the file is properly stored.
						if(reserved != null)
							reserved = new String[tempItems.length + 1];
						else
						{
							reserved = new String[1];
						}
						
						int count;
						for(count = 0; count < reserved.length - 1; count++)
							reserved[count] = tempItems[count];
						
						reserved[count] = "R:" + order.orderNumber + " " + inventoryItemName + " " + invoiceQuantity;
						
						
					}
				}
				
			}
			//We update the reservedItems intance variable.
			reservedItems = reserved;
		}

		//Checks if the file encountered errors.  If not, we chang ethe file with new contents.
		if(encounterError == false)
		{
			changeInventoryFile();
			System.out.println("File should have been changed");
			return true;
		}
		System.out.println(("File did not work"));
		return false;
	}
	
	
	
	//Name: checkInventoryItems
	//Use: checks the inventory items to make sure they are available and can be used
	//Input: order
	//Output: a boolean value is returned, indicating if we have space or not.
	//Note that this behaves almost exactly like the reserve, except we dont reserve and just check instead.
	public boolean checkInventoryItems(OrderClass order)
	{
		int invoiceSize = 0;		//denotes the size for the Order invoice sent.
		boolean encounterError = false; 		//marker to represent if there has been an error reading.
		
		//Calibrates the object and the files/ reserved if needed.
		if(!setCatalogItems())
		{
			System.out.println("There was an error while processing"); //simple error check
			return true;
		}
		
		String[] invoiceItems = new String[10];		//initialize
		int[] invoiceAmount = new int[10];		//initialize
		String[] cartItems = order.cart;		//initialize
		for(String cartItem: cartItems)		//Start a loop to check cart item
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
		for(int i = 0; i < 10; i++)//Start a loop to check the invoice size and get the amounts.
		{
			if(invoiceItems[i] == null)
			{	
				invoiceSize = i;
				break;
			}
			invoiceItems[i] = invoiceItems[i] + " " + invoiceAmount[i];
		}
		//Start a loop to start main check of items and overall reservation
		for(int i = 0; i < invoiceSize; i++)
		{
			
			String invoiceItemName = invoiceItems[i].substring(0,invoiceItems[i].indexOf(' '));
		
			//Block should get the last number of the string for the inventory.
			String temp = invoiceItems[i].substring(invoiceItems[i].indexOf(' ') + 1);
			
			
			int invoiceQuantity = Integer.parseInt(temp);
			
			
			
			//For loop starts checking inventory items in comparison with the ordered listing.
			for(int j = 0; j < inventoryItems.length; j++)
			{
				String inventoryItemName = inventoryItems[j].substring(0, inventoryItems[j].indexOf(' '));
				
				
				String inventoryQuantityString = inventoryItems[j].substring(inventoryItems[j].indexOf(' ') + 1);
				
				
				int inventoryQuantity = Integer.parseInt(inventoryQuantityString);
				
				
				if(invoiceItemName.contentEquals(inventoryItemName))
				{
					if(inventoryQuantity < invoiceQuantity)
					{
						System.out.println("The items: " + inventoryItemName + " has not enough stock!  An inventory order is required!");
						encounterError = true;
					}
				}
			}
		}
		//returns whether there was an error with amount in stock or not.
		return encounterError;
	}
		
	//Name: removeReserveInventoryItems
	//Use:	removes the reserved item, called for confirmShipment
	//Input: reserveOrder for the specific order
	//Output: file is updated and 
	public String[] removeReserveInventoryItems(String reserveOrder)
	{
		//An array for output is created to store.
		String[] output = null;
		
		if(!setCatalogItems())
		{
			System.out.println("There was an error while processing");
			return null;
		}
		
		
		//A new object to write and change the file is given.
		File inventoryReader = new File("Inventory\\" + inventoryFilePath + ".txt");
		PrintWriter newFile;
		try
		{
			newFile = new PrintWriter(inventoryReader);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Inventory file not found!");
			return null;
		}
		
		
		//Prints same inventoryItems into the text file
		for(int i = 0; i < inventoryItems.length; i++)
			newFile.println(inventoryItems[i]);
		
		//For loop goes through the overall reserved items.  If it finds one matching the order, we store in output.
		for(int i = 0; i < reservedItems.length; i++)
		{
			String read = reservedItems[i];
			if(read.contains("R:"+reserveOrder))
			{
				
				String[] tempItems = output;
				
				if(output != null)
					output = new String[tempItems.length + 1];
				else
				{
					output = new String[1];
				}
				
				int count;
				for(count = 0; count < output.length - 1; count++)
					output[count] = tempItems[count];
				
				output[count] = read;
				continue;
			}
			else
			{
				newFile.println(reservedItems[i]);
			}
			
			
		}
		
		
		newFile.close();
		
		return output;
		
	}
	
	//Name: getinventoryItems
	//Use:	returns the items
	//Input: N/A
	//Output: returns the items in an inventory.
	public String[] getinventoryItems()
	{
		return inventoryItems;
	}

	//Name:readInventory
	//Use: reads the inventory
	//Input: N/A
	//Output: returns items after properly setting up
	public String[] readInventory()
	{
		setCatalogItems();
		return inventoryItems;
	}
	
	//Name: createNewInventory
	//Use:	creates the new inventory and sets values
	//Input: Name
	//Output: File will be created containing the inventory
	public static void createNewInventory(String name)
	{
		PrintWriter fileWriter = null;
		
		File fileInventory = new File("Inventory");
		if(!fileInventory.exists())
			fileInventory.mkdir();
		
		
		try
		{
			fileWriter = new PrintWriter("Inventory\\" + name + ".txt");
			
			//fileWriter sets defaults for the inventory.
			fileWriter.println("Apple 60");
			fileWriter.println("HotDog 60");
			fileWriter.println("Banana 60");
			fileWriter.println("CornDog 60");
			fileWriter.println("BigBertha 60");
		}
		catch(IOException e)
		{
			System.out.println("Error generating a new file.");
		}
		finally
		{
			fileWriter.close();		//we have to close the file.
		}
	}
	
	
}