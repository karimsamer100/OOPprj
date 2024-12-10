package javaproject;

import java.util.List;
import java.util.Scanner;

public class Javaproject {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database.initializeDummyData();

        Cart cart = null;
        Customer loggedInCustomer = null;
        Admin loggedInAdmin = null;

        while (true) {
            try {
                System.out.println("\n1. Login");
                System.out.println("2. Register");
                System.out.println("3. View Products");
                System.out.println("4. Add Product to Cart");
                System.out.println("5. View Cart");
                System.out.println("6. Place Order");
                System.out.println("7. Admin Actions");
                System.out.println("8. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        Object loggedInUser = login(scanner);
                        if (loggedInUser instanceof Customer) {
                            loggedInCustomer = (Customer) loggedInUser;
                            cart = new Cart();
                            loggedInCustomer.displayInfo(); // Polymorphism: Calls Customer's displayInfo()
                        } else if (loggedInUser instanceof Admin) {
                            loggedInAdmin = (Admin) loggedInUser;
                            loggedInCustomer = null;
                            loggedInAdmin.displayInfo(); // Polymorphism: Calls Admin's displayInfo()
                        }
                        break;

                    case 2:
                        register(scanner);
                        break;

                    case 3:
                        System.out.println("\nAvailable Products:");
                        for (Product product : Database.products) {
                            System.out.println(product.getName() + " - $" + product.getPrice());
                        }
                        break;

                    case 4:
                        if (loggedInCustomer == null) {
                            System.out.println("Please log in as a customer first.");
                            break;
                        }
                        addProductToCart(scanner, cart);
                        break;

                    case 5:
                        if (loggedInCustomer == null) {
                            System.out.println("Please log in as a customer first.");
                            break;
                        }
                        cart.viewCart();
                        break;

                    case 6:
                        if (loggedInCustomer == null) {
                            System.out.println("Please log in as a customer first.");
                            break;
                        }
                        placeOrder(scanner, cart, loggedInCustomer);
                        break;

                    case 7:
                        if (loggedInAdmin == null) {
                            System.out.println("Please log in as an admin to access this feature.");
                            break;
                        }
                        adminActions(scanner, loggedInAdmin);
                        break;

                    case 8:
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static Object login(Scanner scanner) {
        try {
            System.out.print("Are you logging in as (1) Customer or (2) Admin? ");
            int userType = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (userType == 1) {
                for (Customer customer : Database.customers) {
                    if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                        System.out.println("Customer login successful.");
                        return customer;
                    }
                }
            } else if (userType == 2) {
                for (Admin admin : Database.admins) {
                    if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                        System.out.println("Admin login successful.");
                        return admin;
                    }
                }
            }

            System.out.println("Invalid credentials.");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear invalid input
        }
        return null;
    }

    private static void register(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Are you registering as (1) Customer or (2) Admin? ");
                int userType = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                System.out.print("Enter date of birth (YYYY-MM-DD): ");
                String dateOfBirth = scanner.nextLine();

                if (userType == 1) {
                    System.out.print("Enter store credit: ");
                    double storeCredit = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter gender (MALE/FEMALE): ");
                    Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());

                    System.out.print("Enter your interests (comma-separated): ");
                    List<String> interests = List.of(scanner.nextLine().split("\\s*,\\s*"));

                    Customer newCustomer = new Customer(username, password, dateOfBirth, storeCredit, address, gender, interests);
                    Database.customers.add(newCustomer);
                    System.out.println("Customer registration successful.");
                } else if (userType == 2) {
                    System.out.print("Enter role: ");
                    String role = scanner.nextLine();
                    System.out.print("Enter working hours: ");
                    int workingHours = scanner.nextInt();
                    scanner.nextLine();

                    Admin newAdmin = new Admin(username, password, dateOfBirth, role, workingHours);
                    Database.admins.add(newAdmin);
                    System.out.println("Admin registration successful.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    private static void addProductToCart(Scanner scanner, Cart cart) {
        try {
            System.out.print("Enter the product name to add to cart: ");
            String productName = scanner.nextLine();
            Product selectedProduct = null;

            for (Product product : Database.products) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    selectedProduct = product;
                    break;
                }
            }

            if (selectedProduct != null) {
                cart.addProduct(selectedProduct);
                System.out.println("Product added to cart: " + productName);
            } else {
                System.out.println("Product not found.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void placeOrder(Scanner scanner, Cart cart, Customer customer) {
        try {
            if (cart.getProducts().isEmpty()) {
                System.out.println("Your cart is empty, cannot place order.");
                return;
            }

            // Ask user to choose a payment method
            System.out.println("\nSelect payment method:");
            System.out.println("1. Store Credit");
            System.out.println("2. Credit Card");
            System.out.println("3. PayPal");
            System.out.print("Enter your choice (1/2/3): ");
            int paymentChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String paymentMethod = "";

            switch (paymentChoice) {
                case 1:
                    paymentMethod = "Store Credit";
                    break;
                case 2:
                    paymentMethod = "Credit Card";
                    break;
                case 3:
                    paymentMethod = "PayPal";
                    break;
                default:
                    System.out.println("Invalid choice, defaulting to Credit Card.");
                    paymentMethod = "Credit Card";
                    break;
            }

            double totalCost = cart.calculateTotal();
            if (paymentMethod.equals("Store Credit")) {
                if (customer.getStoreCredit() >= totalCost) {
                    customer.setStoreCredit(customer.getStoreCredit() - totalCost);
                    System.out.println("Payment completed using store credit.");
                } else {
                    System.out.println("Insufficient store credit. Please choose another payment method.");
                    return;
                }
            } else if (paymentMethod.equals("Credit Card") || paymentMethod.equals("PayPal")) {
                System.out.println("Payment completed using " + paymentMethod + ".");
            }

            // Process the order
            String orderDetails = "Order placed by " + customer.getUsername() + " using " + paymentMethod + " for a total of $" + totalCost;
            Order order = new Order(orderDetails);
            Database.orders.add(order);

            // Optionally: Increment store credit (e.g., 5% cashback)
            double cashback = totalCost * 0.05;
            customer.setStoreCredit(customer.getStoreCredit() + cashback);
            System.out.println("You earned $" + cashback + " in store credit!");

            // Clear the cart after placing the order
            cart.clearCart();

            System.out.println("Order placed successfully!");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void adminActions(Scanner scanner, Admin admin) {
        try {
            System.out.println("\nAdmin Actions:");
            System.out.println("1. Show all users");
            System.out.println("2. Show all products");
            System.out.println("3. Show all orders");
            System.out.println("4. Create a new product");
            System.out.println("5. Update an existing product");
            System.out.println("6. Delete a product");
            System.out.println("7. Create a new category"); // Added category creation option
            System.out.print("Enter admin action: ");
            int adminAction = scanner.nextInt();
            scanner.nextLine();

            switch (adminAction) {
                case 1:
                    admin.showUsers(Database.customers);
                    break;
                case 2:
                    admin.showProducts(Database.products);
                    break;
                case 3:
                    admin.showOrders(Database.orders);
                    break;
                case 4:
                    Admin.createProduct(scanner);
                    break;
                case 5:
                    Admin.updateProduct(scanner);
                    break;
                case 6:
                    deleteProduct(scanner);
                    break;
                case 7:
                    Admin.createCategory(scanner);  // Category creation method call
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    private static void deleteProduct(Scanner scanner) {
        try {
            System.out.print("Enter product name to delete: ");
            String productName = scanner.nextLine();
            Product productToDelete = null;

            for (Product product : Database.products) {
                if (product.getName().equalsIgnoreCase(productName)) {
                    productToDelete = product;
                    break;
                }
            }

            if (productToDelete == null) {
                System.out.println("Product not found.");
                return;
            }

            Database.products.remove(productToDelete);
            System.out.println("Product deleted successfully.");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }
}
