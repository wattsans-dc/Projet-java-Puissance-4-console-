package cour;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Scor {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/java";
    private static final String USER = "root";
    private static final String PASS = "";
    private Connection connexion;

    public Scor() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        this.connexion = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void afficherScores() {
        try {
            Statement statement = connexion.createStatement();
            String query = "SELECT nom, victoires, date, heure FROM joueurs ORDER BY victoires DESC";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("Tableau des scores :");
            System.out.println("+----------------------+---------+------------+----------+");
            System.out.println("| Pseudo               |Victoires| Date       | Heure    |");
            System.out.println("+----------------------+---------+------------+----------+");
            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                int victoires = resultSet.getInt("victoires");
                String date = resultSet.getString("date");
                String heure = resultSet.getString("heure");
                System.out.format("| %-20s | %7d | %-10s | %8s |\n", nom, victoires, date, heure);
            }
            System.out.println("+----------------------+---------+------------+----------+");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des scores : " + e.getMessage());
        }
    }
}
