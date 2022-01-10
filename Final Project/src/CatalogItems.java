//These classes allow the user to view and add items to their cart. 
//	This is the main shopping experience
import java.io.*;
import java.util.*;

//The following is catalogItems, an object that acts as storage for cart items made by the customer.
public class CatalogItems {
	
	
	
	String[] cart;
	double total = 0.00;
	String supplier = null;
	Catalog supplierCatalog;
	
	//Name: CatalogItems
	//Use: creates the catalog itemList for the user to store items.
	//Input: N/A
	//Output: N/A
	public CatalogItems()
	{
		cart = new String[1000];  //1000 items can be bought from a cart.
		total = 0.00;
		supplier = null;
		//a catalog is made to use.  Since all programs use the same, a String sends to it called Catalog to store.
		supplierCatalog = new Catalog("catalog");
	}
	
	
	public double getTotal()
	{
		return total;
	}
	
	public String[] getCart()
	{
		return cart;
	}
	
	public String getSupplier()
	{
		return supplier;
	}
	//Method name: shop
	//use: Allows the user to add items to their cart and adds it to the total
	//input: supplier string to represent who we are buying from
	//output: N/A stores all info
	 public void shop(String supplier)throws IOException {

		//Scanner object is used to read input.
		Scanner sc = new Scanner(System.in);
		 
		int i = 0;
		int j;
		int choice = -1;
		int amount = 0;
		
		//set the local supplier to the new one
		this.supplier = supplier;
		
		System.out.println("Now, please select an item to purchase by typing 1,2,etc. for it.  Note that 0 will exit the program.");
		 
		//While loop allows input until the user types 0, exiting.
		 while(true) 
		 {
			 //Prompts and stores input for an item listing.
			 System.out.println("Please enter a number listing to purchase.");
			 try
			 {
				 choice = sc.nextInt();
			 }
			 catch(InputMismatchException incorrectInput)
			 {
				 System.out.println("Error!  only enter integer values for choosing items!");
				 
				 sc.nextLine(); //used to flush out any bad input.
				 choice = -1;
				 continue;
			 }
			 
			 //Checks if input is exit, 0.  
			 if(choice == 0)
				 break;
			 else if(choice < 0)
			 {
				 System.out.println("can not have negative listings!");
				 choice = -1;
				 continue;
			 }
			 
			//Checks if the choice is valid.
			if(choice < supplierCatalog.size + 1)
			{
				//Prompts and stores an input for the amount for an item.
				System.out.println("How many " + supplierCatalog.content[choice] + "s would you like?");	//This just lets them choose
				try
				{
					amount = sc.nextInt();
				}
				catch(InputMismatchException incorrectInput)
				{
					System.out.println("Error!  you must only put integers for the amount wanted!");
					
					 sc.nextLine(); //used to flush out any bad input.
					 amount = 0;
					 continue;
				}
				
				for(j = 0; j < amount; j++) {
						cart[i] = supplierCatalog.content[choice];		//Adds to the cart
						total = total + supplierCatalog.costs[choice]; 		//Adds to the total
						i++;
					//Checks if user actually put in any amount or not.
					if(j + 1 == amount)
						System.out.println("Items added");
				}
			}
		}
	}
}
//Creates another class of catalog, used to store information from a file.
class Catalog{
	
	String[] content;
	Double[] costs;
	int size;
	
	
	//Name: Catalog
	//Use: reads the catalog file and defines the items to the Catalog.
	//Input: catalogFileName, for location of the catalog.
	//Output: N/A
	public Catalog(String catalogFileName)
	{
		content = new String[100];
		costs = new Double[100];
		
		Scanner fileReader = null;
		File catalogFile = new File("Catalog/" + catalogFileName + ".txt");
		
		
		try
		{
			int count = 0;
			fileReader = new Scanner(catalogFile);
			while(fileReader.hasNext())
			{
				while(!fileReader.hasNextInt())
					fileReader.next();
				
				count = fileReader.nextInt();	//Stores the index position for an item.
				content[count] = fileReader.next();	//Stores the actual name of the content.
				costs[count] = fileReader.nextDouble();	//Stores the price for an item on the catalog.
			}
			size = count;
			
		}
		catch(IOException e)
		{
			System.out.println("Catalog had an error.");
		}
		finally
		{
			
			fileReader.close();  //we have to close the file.
		}
	}
	
	//Method name: display
	//use: displays the catalog and its items
	//Input: N/A
	//output: diplays the catalog
	public void display() 
	{
		System.out.println("catalog");
		for(int i = 1; i < size + 1; i++)
		{
			System.out.println(i + ". Item:" +  content[i] + " Cost: $" + costs[i]);
		}
	}
}