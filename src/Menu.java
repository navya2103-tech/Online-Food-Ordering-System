
package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter item name: ");
        String itemName = sc.nextLine();

        System.out.print("Enter price: ");
        double price = sc.nextDouble();

        String sql = "INSERT INTO menu (item_name, price) VALUES (?, ?)";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, itemName);
            ps.setDouble(2, price);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Menu item added successfully!");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}