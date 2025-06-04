import java.io.FileWriter;
import java.io.IOException;

public class Ticket {
    private int row;
    private int seat;
    private double price;
    private Person person;

    // Constructor
    public Ticket(int row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // Method to save ticket information to a text file
    public void save() {
        String filename = String.format("%c%d.txt", (char) ('A' + row), seat);
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Ticket Information:\n");
            writer.write("Row: " + (char) ('A' + row) + "\n");
            writer.write("Seat: " + seat + "\n");
            writer.write("Price: £" + price + "\n");
            writer.write("Person Information:\n");
            writer.write("Name: " + person.getName() + "\n");
            writer.write("Surname: " + person.getSurname() + "\n");
            writer.write("Email: " + person.getEmail() + "\n");
            System.out.println("Ticket information saved to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the ticket information.");
        }
    }

    // Method to print information from Ticket
    public void printTicketInfo() {
        System.out.println("Ticket Information:");
        System.out.println("Row: " + row);
        System.out.println("Seat: " + seat);
        System.out.println("Price: £" + price);
        System.out.println("Person Information:");
        person.printPersonInfo();
    }
}
