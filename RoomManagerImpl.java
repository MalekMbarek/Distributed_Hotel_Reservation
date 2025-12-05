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

        
		public String book(String type, String guest_name) throws RemoteException {
	    String roomType = type.toLowerCase();
    
   		 if (roomType.equals("single")) {
    	    if (RoomManagerImpl.room1 <= 0) {
            return "Single Rooms are fully booked";
        }
        
        // Check if guest already booked ANY room (using case-insensitive)
        for (String guest : RoomManagerImpl.guests) {
            if (guest.equalsIgnoreCase(guest_name)) {
                return "Already booked (guest exists in system)";
            }
        }
        
        // Book the room
        RoomManagerImpl.room1--;
        RoomManagerImpl.room_1.add(guest_name);
        RoomManagerImpl.guests.add(guest_name);
        RoomManagerImpl.map.put("single", RoomManagerImpl.room_1);
        return "Single room booked for " + guest_name;
    }
    else if (roomType.equals("double")) {
        if (RoomManagerImpl.room2 <= 0) {
            return "Double Rooms are fully booked";
        }
        
        for (String guest : RoomManagerImpl.guests) {
            if (guest.equalsIgnoreCase(guest_name)) {
                return "Already booked (guest exists in system)";
            }
        }
        
        RoomManagerImpl.room2--;
        RoomManagerImpl.room_2.add(guest_name);
        RoomManagerImpl.guests.add(guest_name);
        RoomManagerImpl.map.put("double", RoomManagerImpl.room_2);
        return "Double room booked for " + guest_name;
    }
    else if (roomType.equals("triple")) {
        if (RoomManagerImpl.room3 <= 0) {
            return "Triple Rooms are fully booked";
        }
        
        for (String guest : RoomManagerImpl.guests) {
            if (guest.equalsIgnoreCase(guest_name)) {
                return "Already booked (guest exists in system)";
            }
        }
        
        RoomManagerImpl.room3--;
        RoomManagerImpl.room_3.add(guest_name);
        RoomManagerImpl.guests.add(guest_name);
        RoomManagerImpl.map.put("triple", RoomManagerImpl.room_3);
        return "Triple room booked for " + guest_name;
    }
    else {
        return "Room type does not exist. Use: Single, Double, or Triple";
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
