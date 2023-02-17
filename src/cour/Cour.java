	package cour;
	import java.util.Scanner;
import java.io.IOException;
import java.security.DrbgParameters.Capability;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;




	
	public class Cour {

	
		    static final int ROWS = 6;
		    static final int COLS = 7;
		    AI ai = new AI();
		    
		    private static void addNewPlayer(Connection connection, String playerName) {
		        try (Statement statement = connection.createStatement()) {
		            String query = "INSERT INTO joueurs (nom, victoires) VALUES ('" + playerName + "', 0)";
		            statement.executeUpdate(query);
		        } catch (SQLException e) {
		            System.out.println("Erreur lors de l'ajout du joueur à la base de données : " + e.getMessage());
		        }
		    }


		    public static void main(String[] args) throws IOException, ClassNotFoundException {
	
		        Scanner scanner = new Scanner(System.in);

		        char[][] grid = new char[ROWS][COLS];
		        boolean playAgain = true;
		        String player1Name;
		        String player2Name;
		        
		        System.out.println("Voulez-vous jouer contre l'IA ? (O/N) (en cour de build N par deffaut)");
		        String response = scanner.nextLine();
		        
		        if (response.equalsIgnoreCase("o")) {
		            player1Name = "Joueur 1";
		            System.out.print("Entrez votre pseudo : ");
		            player2Name = scanner.nextLine();
		        } else {
		            System.out.print("Entrez le nom du joueur 1 : ");
		            player1Name = scanner.nextLine();
		            System.out.print("Entrez le nom du joueur 2 : ");
		            player2Name = scanner.nextLine();
		        }
		        
		        
	
		     // informations d'identification pour la connexion MySQL
		        String url = "jdbc:mysql://localhost:3306/java";
		        String utilisateur = "root";
		        String motDePasse = "";
	
		        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse)) {
		            System.out.println("Connecté à la base de données MySQL.");

	
		            while (playAgain) {
		                initializeGrid(grid);
		                boolean player1Turn = true;
		                boolean gameOver = false;
	
		                while (!gameOver) {
		                    displayGrid(grid);
	//les int des pion
		                    int col;
		                    do {
		                        System.out.print((player1Turn ? player1Name : player2Name) + ", choisissez une colonne (1-" + COLS + ") : ");
		                        while (!scanner.hasNextInt()) {
		                            System.out.println("Veuillez entrer un nombre entier compris entre 1 et " + COLS + ".");
		                            System.out.print((player1Turn ? player1Name : player2Name) + ", choisissez une colonne (1-" + COLS + ") : ");
		                            scanner.next();
		                        }
		                        col = scanner.nextInt() - 1;
		                    } while (col < 0 || col >= COLS || grid[0][col] != ' ');

		                    int row = getFirstAvailableRow(grid, col);
		                    grid[row][col] = player1Turn ? 'X' : 'O';

		                    if (isWinner(grid, row, col)) {
		                        displayGrid(grid);
		                        String winnerName = player1Turn ? player1Name : player2Name;
		                        System.out.println(winnerName + " a gagné !");
		                        gameOver = true;


	
		                        // Mettre à jour le nombre de victoires du joueur gagnant dans la base de données
		                        try (Statement statement = connexion.createStatement()) {
		                            // Rechercher le joueur dans la base de données
		                            String query = "SELECT * FROM joueurs WHERE nom='" + winnerName + "'";
		                            ResultSet resultSet = statement.executeQuery(query);
	
		                            if (resultSet.next()) {
		                                // Si le joueur existe déjà dans la base de données, mettre à jour son nombre de victoires
		                                int wins = resultSet.getInt("victoires") + 1;
		                                query = "UPDATE joueurs SET victoires=" + wins + " WHERE nom='" + winnerName + "'";
		                                statement.executeUpdate(query);
		                            } else {
		                                // Sinon, insérer un nouveau joueur dans la base de données avec une victoire
		                                query = "INSERT INTO joueurs (nom, victoires) VALUES ('" + winnerName + "', 1)";
		                                statement.executeUpdate(query);
		                            }
		                        } catch (SQLException e) {
		                            System.out.println("Erreur lors de la mise à jour de la base de données : " + e.getMessage());
		                        }
		                    } else if (isGridFull(grid)) {
		                        displayGrid(grid);
		                        System.out.println("Match nul !");
		                        gameOver = true;
		                    }
	
		                    player1Turn = !player1Turn;
		                }
		            
		        
	
	
		             // Demande de redémarrage ou de sortie
		                System.out.print("Voulez-vous rejouer ? (O/N) : ");
		                char choice = scanner.next().charAt(0);
		                playAgain = (choice == 'O' || choice == 'o');
		                if (choice == 'n' || choice == 'N') {
		                    if (gameOver) {
		                        Scor score = new Scor();
		                        score.afficherScores();
		                    }
		                }
		            }
	
		        } catch (SQLException e) {
		            System.err.format("SQL Échec de la connexion: %s\n", e.getMessage());
		        }
		    }
	
	 
	
	    
	
	    private static boolean isGridFull(char[][] grid) {
			// TODO Stub de méthode généré automatiquement
			return false;
		}
	    
//tableau

		private static void initializeGrid(char[][] grid) {
	        for (int i = 0; i < ROWS; i++) {
	            for (int j = 0; j < COLS; j++) {
	                grid[i][j] = ' ';
	            }
	        }
	    }
	
		private static void displayGrid(char[][] grid) {
		    for (int i = 0; i < ROWS; i++) {
		        for (int j = 0; j < COLS; j++) {
		            System.out.print("| " + grid[i][j] + " ");
		        }
		        System.out.println("|");
		        for (int j = 0; j < COLS; j++) {
		            System.out.print("+---");
		        }
		        System.out.println("+");
		    }
		    for (int i = 0; i < COLS; i++) {
		        System.out.print("  " + (i + 1) + " ");
		    }
		    System.out.println();
		}
	
	
	    private static int getFirstAvailableRow(char[][] grid, int col) {
	        for (int row = ROWS - 1; row >= 0; row--) {
	            if (grid[row][col] == ' ') {
	                return row;
	            }
	        }
	        return -1;
	    }
	//les check
	    private static boolean isWinner(char[][] grid, int row, int col) {
	        char player = grid[row][col];
	
	        // vérifier les rangée
	        int count = 0;
	        for (int j = 0; j < COLS; j++) {
	            if (grid[row][j] == player) {
	                count++;
	            } else {
	                count = 0;
	            }
	            if (count >= 4) {
	                return true;
	            }
	        }
	
	        // vérifie les  colone
	        count = 0;
	        for (int i = 0; i < ROWS; i++) {
	            if (grid[i][col] == player) {
	                count++;
	            } else {
	                count = 0;
	            }
	            if (count >= 4) {
	                return true;
	            }
	        }
	
	        // vérifie la  diagonal
	        count = 0;
	        int i = row;
	        int j = col;
	        while (i > 0 && j > 0) {
	            i--;
	            j--;
	        }
	        while (i < ROWS && j < COLS) {
	            if (grid[i][j] == player) {
	                count++;
	            } else {
	                count = 0;
	            }
	            if (count >= 4) {
	                return true;
	            }
	            i++;
	            j++;
	        }
	
	        // vérifie la diagonale inversée
	        count = 0;
	        i = row;
	        j = col;
	        while (i > 0 && j < COLS - 1) {
	            i--;
	            j++;
	        }
	        while (i < ROWS && j >= 0) {
	            if (grid[i][j] == player) {
	                count++;
	            } else {
	                count = 0;
	            }
	            if (count >= 4) {
	                return true;
	            }
	            i++;
	            j--;
	        }
	
	        return false;
	    }
	    
	}
	
