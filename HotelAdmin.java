import java.rmi.Naming;
import java.util.*;

public class HotelAdmin {
    public static void main(String[] args) {
        try {
            // Connect to RMI server (same as HotelClient)
            RoomManager h = (RoomManager) Naming.lookup("rmi://localhost:1099/Hotel");

            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("stats")) {
                    System.out.println(h.getStats());
                    
                } else if (args[0].equalsIgnoreCase("reset") && args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                    System.out.println(h.resetSystem());
                    
                } else if (args[0].equalsIgnoreCase("add") && args.length == 4) {
                    // Add rooms: add <type> <count> <price>
                    String type = args[1];
                    int count = Integer.parseInt(args[2]);
                    int price = Integer.parseInt(args[3]);
                    System.out.println(h.addRooms(type, count, price));
                    
                }
                else if(args[0].equalsIgnoreCase("list")){
                System.out.println(h.list());}
                else if(args[0].equals("guests")){
                    System.out.println(h.guests());

            }
            
            else if(args[0].equals("revenue")){
            System.out.println(h.revenue());
            } else {
                 {
        System.out.println("Use one of these Hotel Admin Available commands:");
        System.out.println("");
        System.out.println("java HotelAdmin stats");
        System.out.println("java HotelAdmin reset confirm");
        System.out.println("java HotelAdmin add <room-type> <count> <price>");
        System.out.println("java HotelAdmin guests");
        System.out.println("java HotelAdmin revenue");
        System.out.println("java HotelAdmin list");

    }
            }
                

            } else {
                 {
        System.out.println("Use one of these Hotel Admin Available commands:");
        System.out.println("");
        System.out.println("java HotelAdmin stats");
        System.out.println("java HotelAdmin reset confirm");
        System.out.println("java HotelAdmin add <room-type> <count> <price>");
        System.out.println("java HotelAdmin guests");
        System.out.println("java HotelAdmin revenue");
        System.out.println("java HotelAdmin list");

    }
            }

        } catch (Exception e) {
            System.out.println("Exception received:");
            System.out.println(e);
        }
    }
    
 
}