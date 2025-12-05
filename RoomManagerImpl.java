import java.rmi.*;
import java.util.*;


public class RoomManagerImpl extends java.rmi.server.UnicastRemoteObject implements RoomManager{
    //Number of available rooms of each type
    public static int room1 = 5;
    public static int room2 = 6;
    public static int room3 = 2;
    public static int room4 = 2;
    public static int room5 = 6;

    //Price of each room
	public static int price_1 = 55000;
	public static int price_2 = 75000;
	public static int price_3 = 80000;

    public static List<String> guests = new ArrayList<String>();

    // public static Hashtable<String, String> guest_list = new Hashtable<String, String>();

    //Stores guests and their room types
    public static Map<String, List<String>> map = new HashMap<String, List<String>>();

    //Stores the booked guests per room type
    public static List<String> room_1 = new ArrayList<String>();
    public static List<String> room_2 = new ArrayList<String>();
	public static List<String> room_3 = new ArrayList<String>();
    public static List<String> room_4 = new ArrayList<String>();
    public static List<String> room_5 = new ArrayList<String>();

	public RoomManagerImpl()
		throws RemoteException { 
			super();
		}
		//Prints the list of rooms
		  public String list()
            throws RemoteException{
           	  String room1 = RoomManagerImpl.room1 + " Single rooms are available for "+price_1+" DT per night";
           	  String room2 = RoomManagerImpl.room2 + " Double rooms are available for "+price_2+" DT per night";
           	  String room3 = RoomManagerImpl.room3 + " Triple rooms are available for "+price_3+" DT per night";

        	return room1 + "\n" + room2 + "\n" + room3 + "\n" ;
            }

 
		public String book(String type, String guest_name)
			throws RemoteException{

		
				if(type.equalsIgnoreCase("Single")){
					// boolean check = true;
					if(RoomManagerImpl.room1 > 0){
	
					
						//Book a room is the map is empty
						if (RoomManagerImpl.map.size() == 0){
                            RoomManagerImpl.room_1.add(guest_name);
							          RoomManagerImpl.room1 = RoomManagerImpl.room1 - 1;
            						RoomManagerImpl.map.put("Single",RoomManagerImpl.room_1);
            						return "Single room Booked ";	
						}else{ 
						//Loop through the map incase it's not empty
						for (Map.Entry<String, List<String>> entry : RoomManagerImpl.map.entrySet()) {
            			
            			String key = entry.getKey();
            			//Store the list of guests in a list called values
            			List<String> values = entry.getValue();
            			if(key.equalsIgnoreCase("Single")){
            				for(String temp: values){
            					//Check if the user has already booked the room before
            					if(temp.equalsIgnoreCase(guest_name)){
									// check = false;
            						return "Already booked";
            					}else{
                                    RoomManagerImpl.room_1.add(guest_name);
            						//Book the room if they haven't
            						RoomManagerImpl.room1 = RoomManagerImpl.room1 - 1;
            						//Store the guest details in the map
            						RoomManagerImpl.map.put("Single",RoomManagerImpl.room_1);
            						return "You have booked a single room";
            						
            					}
            			  }
            			}else{
                                    RoomManagerImpl.room_1.add(guest_name);
            						//Book the room if they haven't
            						RoomManagerImpl.room1 = RoomManagerImpl.room1 - 1;
            						//Store the guest details in the map
            						RoomManagerImpl.map.put("Single",RoomManagerImpl.room_1);
            						return "You have booked a signle room";
                        }
            		   }
        			}
						return "Room1 is available";
				}else{
					return "Single Rooms are fully booked";
				}
					}
		
				else if(type.equalsIgnoreCase("Double")){
					// boolean check = true;
					if(RoomManagerImpl.room2 > 0){
	
					
						//Book a room is the map is empty
						if (RoomManagerImpl.map.size() == 0){
                            RoomManagerImpl.room_2.add(guest_name);
							          RoomManagerImpl.room2 = RoomManagerImpl.room2 - 1;
            						RoomManagerImpl.map.put("Double",RoomManagerImpl.room_2);
            						return "Double room Booked";	
						}else{ 
						//Loop through the map incase it's not empty
						for (Map.Entry<String, List<String>> entry : RoomManagerImpl.map.entrySet()) {
            			
            			String key = entry.getKey();
            			//Store the list of guests in a list called values
            			List<String> values = entry.getValue();
            			if(key.equalsIgnoreCase("Double")){
            				for(String temp: values){
            					//Check if the user has already booked the room before
            					if(temp.equalsIgnoreCase(guest_name)){
									// check = false;
            						return "Already booked";
            					}else{
                                    RoomManagerImpl.room_2.add(guest_name);
            						//Book the room if they haven't
            						RoomManagerImpl.room2 = RoomManagerImpl.room2 - 1;
            						//Store the guest details in the map
            						RoomManagerImpl.map.put("Double",RoomManagerImpl.room_2);
            						return "You have booked a double room";
            						
            					}
            			  }
            			}else{
                                    RoomManagerImpl.room_2.add(guest_name);
            						//Book the room if they haven't
            						RoomManagerImpl.room2 = RoomManagerImpl.room2 - 1;
            						//Store the guest details in the map
            						RoomManagerImpl.map.put("Double",RoomManagerImpl.room_2);
            						return "You have booked a Double room";
                        }
            		   }
        			}
						return "Room2 is available";
				}else{
					return "Double Rooms are fully booked";
				}
					}else if(type.equalsIgnoreCase("Triple")){
					if(RoomManagerImpl.room3 > 0){
	
						//Book a room is the map is empty
						if (RoomManagerImpl.map.size() == 0){
                            RoomManagerImpl.room_3.add(guest_name);
						    RoomManagerImpl.room3 = RoomManagerImpl.room3 - 1;
            				RoomManagerImpl.map.put("Triple",RoomManagerImpl.room_3);
            				return "Triple room Booked";	
						}else{ 
						//Loop through the map incase it's not empty
						for (Map.Entry<String, List<String>> entry : RoomManagerImpl.map.entrySet()) {
            			
            			String key = entry.getKey();
            			//Store the list of guests in a list called values
            			List<String> values = entry.getValue();
            			if(key.equalsIgnoreCase("Triple")){
            				for(String temp: values){
            					//Check if the user has already booked the room before
            					if(temp.equalsIgnoreCase(guest_name)){
            						return "Already booked";
									
            					}else{
                                    RoomManagerImpl.room_3.add(guest_name);
            						//Book the room if they haven't
            						RoomManagerImpl.room3 = RoomManagerImpl.room3 - 1;
            						//Store the guest details in the map
            						RoomManagerImpl.map.put("Triple",RoomManagerImpl.room_3);
            						return "You have booked a triple room";
            						
            					}
            			  }
            			}else{
                                RoomManagerImpl.room_3.add(guest_name);
         						//Book the room if they haven't
            					RoomManagerImpl.room3 = RoomManagerImpl.room3 - 1;
            					//Store the guest details in the map
            					RoomManagerImpl.map.put("Triple",RoomManagerImpl.room_3);
            					return "You have booked a triple";
                        }
            		   }
        			}
						return "Room3 is available";
				}else{
                    return "Triple Rooms are fully booked";
                }
					}
					else{
					//Prints if the room entered from the client does not exist
					return "Room number does not exist";
				}
		
			}
		

		public List<String> guests()
			throws RemoteException{
						List<String> names = new ArrayList<String>();
						for (Map.Entry<String, List<String>> entry : map.entrySet()) {
    
            			List<String> values = entry.getValue();
            			for(String temp: values){
                        if(!names.contains(temp)){
            				names.add(temp);
                        }
            			  }
            	
			}
			return names;
		}

		public String revenue()
			throws RemoteException{
            //The money made from each room
			int revenue1 = (5 - RoomManagerImpl.room1) * RoomManagerImpl.price_1;
			int revenue2 = (6 - RoomManagerImpl.room2) * RoomManagerImpl.price_2;
			int revenue3 = (2 - RoomManagerImpl.room3) * RoomManagerImpl.price_3;
	
            
			  String room1 = (revenue1 == 0)? "Room 1 has brought no revenue" : "Room 1 has brought revenue of "+revenue1+" with "+room_1.size()+" guests";
           	  String room2 = (revenue2 == 0)? "Room 2 has brought no revenue" : "Room 2 has brought revenue of "+revenue2+" with "+room_2.size()+" guests";
           	  String room3 = (revenue3 == 0)? "Room 3 has brought no revenue" : "Room 3 has brought revenue of "+revenue3+" with "+room_3.size()+" guests";

        	return room1 + "\n" + room2 + "\n" + room3 + "\n";
			}
		
	
}
