import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    boolean isBooked;

    Room(int roomNumber, String category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    double price;

    Booking(String customerName, int roomNumber, String category, double price) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Customer: " + customerName + ", Room No: " + roomNumber +
               ", Category: " + category + ", Price: ₹" + price;
    }
}

public class HotelReservationSystem {
    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static String fileName = "bookings.txt";

    public static void main(String[] args) {
        loadRooms();
        loadBookings();

        int choice;
        do {
            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: viewAvailableRooms(); break;
                case 2: bookRoom(); break;
                case 3: cancelBooking(); break;
                case 4: viewAllBookings(); break;
                case 5: saveBookings(); System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    static void loadRooms() {
        // Create rooms for each category
        for (int i = 1; i <= 5; i++) rooms.add(new Room(i, "Standard"));
        for (int i = 6; i <= 8; i++) rooms.add(new Room(i, "Deluxe"));
        for (int i = 9; i <= 10; i++) rooms.add(new Room(i, "Suite"));
    }

    static void loadBookings() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Booking booking = new Booking(data[0], Integer.parseInt(data[1]), data[2], Double.parseDouble(data[3]));
                bookings.add(booking);
                for (Room r : rooms) {
                    if (r.roomNumber == booking.roomNumber) {
                        r.isBooked = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // No previous bookings
        }
    }

    static void saveBookings() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            for (Booking b : bookings) {
                pw.println(b.customerName + "," + b.roomNumber + "," + b.category + "," + b.price);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    static void viewAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room r : rooms) {
            if (!r.isBooked) {
                System.out.println("Room No: " + r.roomNumber + " (" + r.category + ")");
            }
        }
    }

    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        viewAvailableRooms();
        System.out.print("Enter room number to book: ");
        int roomNum = sc.nextInt();
        sc.nextLine();

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.roomNumber == roomNum && !r.isBooked) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available!");
            return;
        }

        double price = 0;
        switch (selectedRoom.category) {
            case "Standard": price = 2000; break;
            case "Deluxe": price = 3500; break;
            case "Suite": price = 5000; break;
        }

        System.out.println("Room Price: ₹" + price);
        System.out.print("Proceed to payment? (yes/no): ");
        String pay = sc.nextLine();
        if (pay.equalsIgnoreCase("yes")) {
            System.out.println("Payment Successful!");
            selectedRoom.isBooked = true;
            Booking booking = new Booking(name, selectedRoom.roomNumber, selectedRoom.category, price);
            bookings.add(booking);
            saveBookings();
        } else {
            System.out.println("Booking cancelled.");
        }
    }

    static void cancelBooking() {
        System.out.print("Enter your name to cancel booking: ");
        String name = sc.nextLine();
        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.customerName.equalsIgnoreCase(name)) {
                toRemove = b;
                break;
            }
        }
        if (toRemove != null) {
            bookings.remove(toRemove);
            for (Room r : rooms) {
                if (r.roomNumber == toRemove.roomNumber) {
                    r.isBooked = false;
                    break;
                }
            }
            saveBookings();
            System.out.println("Booking cancelled successfully!");
        } else {
            System.out.println("No booking found for this name.");
        }
    }

    static void viewAllBookings() {
        System.out.println("\nAll Bookings:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
        } else {
            for (Booking b : bookings) {
                System.out.println(b);
            }
        }
    }
}
