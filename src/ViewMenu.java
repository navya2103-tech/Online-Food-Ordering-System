package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewMenu {

    public static void main(String[] args) {

        String sql = "SELECT * FROM menu";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            System.out.println("----- FOOD MENU -----");

            while (rs.next()) {

                int menuId = rs.getInt("menu_id");
                String itemName = rs.getString("item_name");
                double price = rs.getDouble("price");

                System.out.println(
                    menuId + " | " + itemName + " | ₹" + price
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}