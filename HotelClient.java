import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.*;

public class HotelClient{
    public static List<String> guests = new ArrayList<String>();
    public static void main(String [] args){
        try{
            RoomManager h = (RoomManager)
            Naming.lookup("rmi://localhost:1099/Hotel");

        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("list")){
                System.out.println(h.list());
            }
            else if(args[0].equalsIgnoreCase("book") && args.length == 3){
                System.out.println(h.book(args[1],args[2]));
            }
            else{
           		System.out.println("");
				System.out.println("Use one of these Hotel Client Available commands:");
				System.out.println("java HotelClient list (ro check availability)");
				System.out.println("java HotelClient book <room type> <guest name> (to book a room)");
        }
            
           
        }else{
           		System.out.println("");
				System.out.println("Use one of these Hotel Client Available commands:");
				System.out.println("java HotelClient list (ro check availability)");
				System.out.println("java HotelClient book <room type> <guest name> (to book a room)");
        }



        }catch(Exception e){
            System.out.println("Exception received :");
            System.out.println(e);
        }
    }}
