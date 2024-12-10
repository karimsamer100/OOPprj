package javaproject;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static List<Customer> customers = new ArrayList<>();
    public static List<Admin> admins = new ArrayList<>();
    public static List<Category> categories = new ArrayList<>();
    public static List<Product> products = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();

    // Initialize dummy data
    public static void initializeDummyData() {
        categories.add(new Category("Electronics", "Devices and gadgets"));
        categories.add(new Category("Books", "Fiction and non-fiction"));

        products.add(new Product("Laptop", 1200.00, "High-performance laptop", 10, categories.get(0)));
        products.add(new Product("Smartphone", 700.00, "Latest model smartphone", 20, categories.get(0)));
        products.add(new Product("Novel", 15.00, "Best-selling novel", 50, categories.get(1)));
        products.add(new Product("Science Book", 30.00, "Educational science book", 25, categories.get(1)));

        List<String> johnInterests = new ArrayList<>();
        johnInterests.add("Electronics");
        johnInterests.add("Books");
        customers.add(new Customer("john_doe", "password123", "1990-01-01", 500.00, "123 Main St", Gender.MALE, johnInterests));

        List<String> janeInterests = new ArrayList<>();
        janeInterests.add("Books");
        customers.add(new Customer("jane_smith", "password456", "1992-05-10", 300.00, "456 Oak St", Gender.FEMALE, janeInterests));

        admins.add(new Admin("admin1", "adminpass", "1985-06-15", "Manager", 40));

        orders.add(new Order("Order placed by john_doe using Credit Card for a total of $1200.00"));
        orders.add(new Order("Order placed by jane_smith using PayPal for a total of $45.00"));
    }
}
