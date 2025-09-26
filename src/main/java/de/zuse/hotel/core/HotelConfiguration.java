package de.zuse.hotel.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.zuse.hotel.util.ZuseCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for saving how many floors in hotel (floors contain how many rooms)
 */
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class HotelConfiguration
{
    private String hotelName = "Zuse Hotel";
    private ArrayList<Floor> hotelFloors;
    private HashMap<String, Double> roomServices;

    public HotelConfiguration()
    {
        roomServices = new HashMap<>();
        hotelFloors = new ArrayList<>();
    }

    public ArrayList<Floor> getHotelFloors()
    {
        return hotelFloors;
    }

    public void setHotelFloors(ArrayList<Floor> hotelFloors)
    {
        this.hotelFloors = hotelFloors;
    }

    /**
     * Sets the default floors and rooms for the hotel.
     */
    public void setDefaultValues()
    {
        Floor floor = new Floor(1, 10);
        Floor floor2 = new Floor(2, 10);
        hotelFloors.add(floor);
        hotelFloors.add(floor2);

        Room room1 = new Room(floor, 1, 100.0, RoomSpecification.Types.SINGLE);
        Room room2 = new Room(floor, 2, 200.0, RoomSpecification.Types.DOUBLE);
        Room room3 = new Room(floor, 3, 300.0, RoomSpecification.Types.DOUBLE);
        Room room4 = new Room(floor, 4, 500.0, RoomSpecification.Types.FAMILY);
        Room room5 = new Room(floor, 5, 75.0, RoomSpecification.Types.SINGLE);

        Room room6 = new Room(floor2, 1, 200.0, RoomSpecification.Types.DOUBLE);
        Room room7 = new Room(floor2, 2, 300.0, RoomSpecification.Types.FAMILY);
        Room room8 = new Room(floor2, 3, 500.0, RoomSpecification.Types.SINGLE);
        Room room9 = new Room(floor2, 4, 75.0, RoomSpecification.Types.DOUBLE);

        addNewRoom(floor.getFloorNr(), room1);
        addNewRoom(floor.getFloorNr(), room2);
        addNewRoom(floor.getFloorNr(), room3);
        addNewRoom(floor.getFloorNr(), room4);
        addNewRoom(floor.getFloorNr(), room5);

        addNewRoom(floor2.getFloorNr(), room6);
        addNewRoom(floor2.getFloorNr(), room7);
        addNewRoom(floor2.getFloorNr(), room8);
        addNewRoom(floor2.getFloorNr(), room9);

        addNewRoomService("Dinner", 20.0f);
        addNewRoomService("Wifi", 10.0f);
    }

    /**
     * Adds a new floor to the hotel.
     *
     * @param floor The floor to add.
     */
    public void addNewFloor(Floor floor)
    {
        ZuseCore.check(hotelFloors.contains(floor) == false, "Floor is already in Hotel");

        hotelFloors.add(floor);
    }

    /**
     * Adds a new room to a specific floor in the hotel.
     *
     * @param floorNr The number of the floor to add the room to.
     * @param room    The room to add.
     */
    public void addNewRoom(int floorNr, Room room)
    {
        ZuseCore.check(floorNr >= 0, "Floor number must be >= 0");

        //TODO(Basel): optimization
        for (Floor floor : hotelFloors)
        {
            if (floor.getFloorNr() == floorNr)
            {
                floor.addRoom(room);
                return;
            }
        }

        ZuseCore.check(false, "Could not find floor Number");
    }

    /**
     * Removes a room from a specific floor in the hotel.
     *
     * @param floorNr The number of the floor to remove the room from.
     * @param roomNr  The number of the room to remove.
     */
    public void removeRoom(int floorNr, int roomNr)
    {
        //TODO(Basel): optimization
        for (Floor hotelFloor : hotelFloors)
        {
            if (hotelFloor.getFloorNr() == floorNr)
            {
                for (int j = 0; j < hotelFloor.getRooms().size(); j++)
                {
                    if (hotelFloor.getRooms().get(j).getRoomNr() == roomNr)
                        hotelFloor.getRooms().remove(j);
                }
            }
        }
    }

    /**
     * Gets the floor with the given floor number.
     *
     * @param floorNr The number of the floor to get.
     * @return The floor with the given floor number, or null if no such floor exists.
     */
    public Floor getFloorByFloorNr(int floorNr)
    {
        //TODO(Basel): optimization
        for (Floor floor : hotelFloors)
        {
            if (floor.getFloorNr() == floorNr)
                return floor;
        }

        return null;
    }

    /**
     * Gets the room with the given room number on a specific floor.
     *
     * @param floorNr The number of the floor the room is on.
     * @param roomNr  The number of the room to get.
     * @return The room with the given room number on the given floor, or null if no such room exists.
     */
    public Room getRoomByRoomNr(int floorNr, int roomNr)
    {
        //TODO(Basel): optimization
        Floor floor = getFloorByFloorNr(floorNr);

        for (Room room : floor.getRooms())
        {
            if (room.getRoomNr() == roomNr)
                return room;
        }

        return null;
    }

    /**
     * Gets the name of the hotel.
     *
     * @return The name of the hotel.
     */
    public String getHotelName()
    {
        return hotelName;
    }

    /**
     * Sets the name of the hotel.
     *
     * @param hotelName The new name of the hotel.
     */
    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    /**
     * Adds a new room service to the hotel.
     *
     * @param serviceName The name of the new room service.
     * @param price       The price of the new room service.
     */
    public void addNewRoomService(String serviceName, double price)
    {
        serviceName = serviceName.toLowerCase();
        ZuseCore.check(!roomServices.containsKey(serviceName),
                "a service with same name has already been added!");

        serviceName = serviceName.toLowerCase();
        roomServices.put(serviceName, price);
    }

    /**
     * Gets the price of a specific room service.
     *
     * @param serviceName The name of the room service to get the price of.
     * @return The price of the room service with the given name.
     * @throws de.zuse.hotel.util.BreakPointException If no room service with the given name exists.
     */
    public double getRoomServicePrice(String serviceName)
    {
        serviceName = serviceName.toLowerCase();
        Double price = roomServices.get(serviceName);
        ZuseCore.check(price != null, "The Service Name is not valid!");

        return price;
    }

    /**
     * Gets the number of room services available in the hotel.
     *
     * @return The number of room services available in the hotel.
     */
    @JsonIgnore
    public int getRoomServiceNum()
    {
        return roomServices.size();
    }

    /**
     * Checks if a room service with the given name exists in the hotel.
     *
     * @param name The name of the room service to check.
     * @return True if a room service with the given name exists in the hotel, false otherwise.
     */
    public boolean hasServiceName(String name)
    {
        name = name.toLowerCase();
        return roomServices.get(name) != null;
    }

    /**
     * Gets a map of all the room services available in the hotel and their prices.
     *
     * @return A map of all the room services available in the hotel and their prices.
     */
    public Map<String, Double> getRoomServices()
    {
        return roomServices;
    }

}
