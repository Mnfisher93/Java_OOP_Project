import java.util.*;
import java.io.*;


public class process_delivery_order 
{

	
	//Method name: getDeliveryOrders
	//Use:	retrieve the orders
	//Input: supplier
	//Output: returns if the file was found and its orders
	public static LinkedList<OrderClass> getDeliveryOrders(String supplier)
	{
		LinkedList<OrderClass> orders;
		try
		{
			orders = OrderClass.getOrdersForSupplier(supplier);		//call contructor in orderclass
		}
		catch(FileNotFoundException e )
		{
			return null;
		}
		
		return orders;

	}
	
	//method name: reserveItems
	//use: reserves the order
	//input: order
	//output: reserves the order
	public static boolean reserveItems(OrderClass order)
	{
		Inventory inventoryItems = new Inventory(order.supplier);
		if(inventoryItems.checkInventoryItems(order))
			return false;
		inventoryItems.reserveInventoryItems(order);
		return order.changeStatus("Ready");
	}
}