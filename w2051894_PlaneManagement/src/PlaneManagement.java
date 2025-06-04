import java.util.Scanner;
import java.util.InputMismatchException;
public class PlaneManagement {
    private static final int ROWS = 4; // Number of rows
    private static final int[] SEATS_PER_ROW = {14, 12, 12, 14}; // Number of seats per row
    private static final int AVAILABLE = 0;
    private static final int SOLD = 1;
    private static final int[][] seats = new int[ROWS][]; // 2D array to track seat availability (0 for available, 1 for sold)
    private static final Ticket[] tickets = new Ticket[ROWS * 14 + 2 * 12]; // Array to store all tickets sold during the session
    private static int ticketIndex = 0; // Index to track the next available slot in the tickets array

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Plane Management application!");

        // Initialize all seats as available
        initializeSeats();

        // Main menu loop
        while (true) {
            displayMenu();
            System.out.print("Please select an option: ");
            try {
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        buySeat(scanner);
                        break;
                    case 2:
                        cancelSeat(scanner);
                        break;
                    case 3:
                        findFirstAvailableSeat();
                        break;
                    case 4:
                        showSeatingPlan();
                        break;
                    case 5:
                        printTicketsInfo();
                        break;
                    case 6:
                        searchTicket(scanner);
                        break;
                    case 0:
                        System.out.println("Exiting program. Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please select a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer option.");
                scanner.nextLine(); // Clear the input buffer
            }
        }
    }
    // Method to display menu
    private static void displayMenu() {
        for (int i = 0; i < 50; i++) {
            System.out.print("*");
        }
        System.out.println("\n*                  Menu Options                  *");
        for (int i = 0; i < 50; i++) {
            System.out.print("*");
        }
        System.out.println("\n\t1. Buy a seat");
        System.out.println("\t2. Cancel a seat");
        System.out.println("\t3. Find first available seat");
        System.out.println("\t4. Show seating plan");
        System.out.println("\t5. Print tickets information and total sales");
        System.out.println("\t6. Search ticket");
        System.out.println("\t0. Quit");
        for (int i = 0; i < 50; i++) {
            System.out.print("*");
        }
        System.out.println();
    }
    // Method to initialize all seats as available
    private static void initializeSeats() {
        for (int i = 0; i < ROWS; i++) {
            seats[i] = new int[SEATS_PER_ROW[i]];
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                seats[i][j] = AVAILABLE;
            }
        }
    }

    // Method to buy a seat
    private static void buySeat(Scanner scanner) {
        // Prompt user for row letter and seat number
        System.out.print("Enter row letter (A, B, C, or D): ");
        char rowLetter = scanner.next().toUpperCase().charAt(0); // Convert input to uppercase and get first character
        System.out.print("Enter seat number: ");
        try {
            int seatNumber = scanner.nextInt();

            // Validate row and seat number
            int rowIndex = getRowIndex(rowLetter);
            if (rowIndex == -1 || seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]) {
                System.out.println("Invalid row letter or seat number.");
                return;
            }

            // Check if seat is available (0 for available, 1 for sold)
            if (seats[rowIndex][seatNumber - 1] == SOLD) {
                System.out.println("Sorry, this seat is already sold. Please choose another seat.");
                return;
            }

            // Prompt user for person information
            System.out.print("Enter person's name: ");
            String name = scanner.next();
            System.out.print("Enter person's surname: ");
            String surname = scanner.next();
            System.out.print("Enter person's email: ");
            String email = scanner.next();

            // Create a new Person object
            Person person = new Person(name, surname, email);

            // Calculate ticket price based on seat number
            double price;
            if (seatNumber <= 5) {
                price = 200;
            } else if (seatNumber <= 9) {
                price = 150;
            } else {
                price = 180;
            }

            // Create a new Ticket object
            Ticket ticket = new Ticket(rowIndex, seatNumber, price, person);

            // Mark seat as sold (1)
            seats[rowIndex][seatNumber - 1] = SOLD;
            System.out.println("Seat " + rowLetter + seatNumber + " has been successfully sold.");

            // Add the ticket to the tickets array
            tickets[ticketIndex++] = ticket;

            // Print the person information
            System.out.println("Person Information:");
            System.out.println("Name: " + name);
            System.out.println("Surname: " + surname);
            System.out.println("Email: " + email);
            ticket.save();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Seat number must be a integer.");
            scanner.nextLine(); // Clear the input buffer
        }
    }

    // Method to cancel a seat
    private static void cancelSeat(Scanner scanner) {
        // Prompt user for row letter and seat number
        System.out.print("Enter row letter (A, B, C, or D): ");
        char rowLetter = scanner.next().toUpperCase().charAt(0); // Convert input to uppercase and get first character
        System.out.print("Enter seat number: ");
        try {
            int seatNumber = scanner.nextInt();

            // Validate row and seat number
            int rowIndex = getRowIndex(rowLetter);
            if (rowIndex == -1 || seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]) {
                System.out.println("Invalid row letter or seat number.");
                return;
            }

            // Check if seat was previously bought
            if (seats[rowIndex][seatNumber - 1] == AVAILABLE) {
                System.out.println("This seat has not been bought yet. There is nothing to cancel.");
                return;
            }

            // Search for the ticket in the tickets array
            for (int i = 0; i < ticketIndex; i++) {
                Ticket ticket = tickets[i];
                if (ticket.getRow() == rowIndex && ticket.getSeat() == seatNumber) {
                    // Remove ticket from tickets array
                    removeTicketAtIndex(i);
                    // Mark seat as available (0)
                    seats[rowIndex][seatNumber - 1] = AVAILABLE;
                    System.out.println(" The Ticket canceled Successfully!");
                    return;
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Seat number must be a number.");
            scanner.nextLine(); // Clear the input buffer
        }
    }

    // Method to remove a ticket at a specific index from the tickets array
    private static void removeTicketAtIndex(int index) {
        for (int i = index; i < ticketIndex - 1; i++) {
            tickets[i] = tickets[i + 1];
        }
        tickets[--ticketIndex] = null; // Remove the last booking and decrement the ticket index
    }

    // Method to show tickets information and total sales
    private static void printTicketsInfo() {
        double totalSales = 0;

        System.out.println("Tickets Information:");
        for (int i = 0; i < ticketIndex; i++) {
            Ticket ticket = tickets[i];
            System.out.println("Ticket for seat " + (char) ('A' + ticket.getRow()) + ticket.getSeat() +
                    " sold for £" + ticket.getPrice() + " to " + ticket.getPerson().getName() + " " + ticket.getPerson().getSurname());
            totalSales += ticket.getPrice();
        }
        System.out.println("Total sales: £" + totalSales);
    }


    // Method to search for a ticket
    private static void searchTicket(Scanner scanner) {
        // Prompt user for row letter and seat number
        System.out.print("Enter row letter (A, B, C, or D): ");
        char rowLetter = scanner.next().toUpperCase().charAt(0); // Convert input to uppercase and get first character
        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Validate row and seat number
        int rowIndex = getRowIndex(rowLetter);
        if (rowIndex == -1 || seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]) {
            System.out.println("Invalid row letter or seat number.");
            return;
        }

        // Check if seat has been sold
        if (seats[rowIndex][seatNumber - 1] == SOLD) {
            // Search for the ticket in the tickets array
            for (int i = 0; i < ticketIndex; i++) {
                Ticket ticket = tickets[i];
                if (ticket.getRow() == rowIndex && ticket.getSeat() == seatNumber) {
                    // Display buyer's information
                    System.out.println("Buyer Name: " + ticket.getPerson().getName()+ " " +ticket.getPerson().getSurname());
                    return;
                }
            }
        }

        // If seat is available (can't find it in tickets array)
        System.out.println("This seat is available.");
    }


    //  get the index of the row
    private static int getRowIndex(char rowLetter) {
        return switch (rowLetter) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            default -> -1; // Invalid row letter
        };
    }
    // Method to find first available seat
    private static void findFirstAvailableSeat() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                if (seats[i][j] == AVAILABLE) {
                    char rowLetter = getRowLetter(i);
                    int seatNumber = j + 1;
                    System.out.println("The first available seat is: " + rowLetter + seatNumber);
                    return; // Exit method once the first available seat is found
                }
            }
        }
        System.out.println("Sorry, no available seats.");
    }

    //  Method to get the row letter based on its index
    private static char getRowLetter(int rowIndex) {
        return switch (rowIndex) {
            case 0 -> 'A';
            case 1 -> 'B';
            case 2 -> 'C';
            case 3 -> 'D';
            default -> '?'; // Invalid row index
        };
    }



    // Method to show seating plan
    private static void showSeatingPlan() {
        System.out.println("Seating Plan:");
        String rowName;
        for (int i = 0; i < ROWS; i++) {
            if (i == 0){
                rowName="A";
            } else if (i == 1) {
                rowName="B";
            }else if (i == 2) {
                rowName="C";
            }else {
                rowName="D";
            }

            System.out.print(rowName+" | ");
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                char seatStatus = (seats[i][j] == AVAILABLE) ? 'O' : 'X';
                System.out.print(seatStatus + " ");
            }
            System.out.println(); // Move to the next row after printing all seats in the current row
        }
    }
}
