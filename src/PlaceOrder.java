package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class PlaceOrder {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();

        System.out.print("Enter menu ID: ");
        int menuId = sc.nextInt();

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        Connection con = null;

        try {
            con = DBConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);

            // Get price from menu
            String priceSql = "SELECT price FROM menu WHERE menu_id = ?";

            PreparedStatement pricePs = con.prepareStatement(priceSql);
            pricePs.setInt(1, menuId);

            ResultSet rs = pricePs.executeQuery();

            if (!rs.next()) {
                System.out.println("Menu item not found!");
                con.rollback();
                return;
            }

            double price = rs.getDouble("price");
            double totalAmount = price * quantity;

            rs.close();
            pricePs.close();

            // Insert order
            String orderSql =
                    "INSERT INTO orders (customer_id, total_amount) VALUES (?, ?)";

            PreparedStatement orderPs = con.prepareStatement(
                    orderSql,
                    Statement.RETURN_GENERATED_KEYS
            );

            orderPs.setInt(1, customerId);
            orderPs.setDouble(2, totalAmount);

            orderPs.executeUpdate();

            // Get generated order ID
            ResultSet generatedKeys = orderPs.getGeneratedKeys();

            int orderId = 0;

            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            generatedKeys.close();
            orderPs.close();

            // Insert order item
            String itemSql =
                    "INSERT INTO order_items (order_id, menu_id, quantity, price) VALUES (?, ?, ?, ?)";

            PreparedStatement itemPs = con.prepareStatement(itemSql);

            itemPs.setInt(1, orderId);
            itemPs.setInt(2, menuId);
            itemPs.setInt(3, quantity);
            itemPs.setDouble(4, price);

            itemPs.executeUpdate();

            itemPs.close();

            // Commit transaction
            con.commit();

            System.out.println("Order placed successfully!");
            System.out.println("Order ID: " + orderId);
            System.out.println("Total Amount: ₹" + totalAmount);

        } catch (Exception e) {

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }

            System.out.println("Order failed. Transaction rolled back.");
            e.printStackTrace();

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            sc.close();
        }
    }
}
