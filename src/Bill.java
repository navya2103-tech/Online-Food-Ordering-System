package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Bill {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Order ID: ");
        int orderId = sc.nextInt();

        String sql =
                "SELECT o.order_id, o.order_date, " +
                "m.item_name, oi.quantity, oi.price, " +
                "(oi.quantity * oi.price) AS item_total " +
                "FROM orders o " +
                "JOIN order_items oi ON o.order_id = oi.order_id " +
                "JOIN menu m ON oi.menu_id = m.menu_id " +
                "WHERE o.order_id = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            System.out.println();
            System.out.println("========== FOOD ORDER BILL ==========");

            boolean found = false;
            double total = 0;

            while (rs.next()) {

                found = true;

                int id = rs.getInt("order_id");
                String date = rs.getString("order_date");
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double itemTotal = rs.getDouble("item_total");

                total += itemTotal;

                System.out.println("Order ID    : " + id);
                System.out.println("Order Date  : " + date);
                System.out.println("Item        : " + itemName);
                System.out.println("Quantity    : " + quantity);
                System.out.println("Price       : ₹" + price);
                System.out.println("Item Total  : ₹" + itemTotal);
                System.out.println("-------------------------------------");
            }

            if (found) {
                System.out.println("TOTAL AMOUNT: ₹" + total);
                System.out.println("=====================================");
                System.out.println("Thank you for your order!");
            } else {
                System.out.println("Order not found!");
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
