package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class OrderHistory {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();

        String sql =
                "SELECT o.order_id, o.order_date, " +
                "m.item_name, oi.quantity, oi.price, " +
                "(oi.quantity * oi.price) AS item_total " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN menu m ON oi.menu_id = m.menu_id " +
                "WHERE o.customer_id = ? " +
                "ORDER BY o.order_date DESC";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, customerId);

            ResultSet rs = ps.executeQuery();

            System.out.println("----- ORDER HISTORY -----");

            boolean found = false;

            while (rs.next()) {

                found = true;

                int orderId = rs.getInt("order_id");
                String orderDate = rs.getString("order_date");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double itemTotal = rs.getDouble("item_total");

                System.out.println("Order ID: " + orderId);
                System.out.println("Order Date: " + orderDate);
                System.out.println("Item: " + itemName);
                System.out.println("Quantity: " + quantity);
                System.out.println("Price: ₹" + price);
                System.out.println("Item Total: ₹" + itemTotal);
                System.out.println("------------------------");
            }

            if (!found) {
                System.out.println("No orders found for this customer.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}