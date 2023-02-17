package cour;

public class IA {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private char[][] grid;

    public IA(char[][] grid) {
        this.grid = grid;
    }

    public int getBestMove() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        for (int col = 0; col < COLS; col++) {
            if (grid[0][col] == '-') {
                int row = getFirstAvailableRow(grid, col);
                grid[row][col] = 'O';
                int score = minimax(grid, 4, false);
                grid[row][col] = '-';
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = col;
                }
            }
        }
        return bestMove;
    }

    private int minimax(char[][] grid, int depth, boolean isMaximizingPlayer) {
        if (depth == 0 || isGridFull(grid) || isWinner(grid, ROWS - 1, COLS - 1)) {
            return evaluateGrid(grid);
        }

        if (isMaximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (grid[0][col] == '-') {
                    int row = getFirstAvailableRow(grid, col);
                    grid[row][col] = 'O';
                    int score = minimax(grid, depth - 1, false);
                    grid[row][col] = '-';
                    bestScore = Math.max(bestScore, score);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int col = 0; col < COLS; col++) {
                if (grid[0][col] == '-') {
                    int row = getFirstAvailableRow(grid, col);
                    grid[row][col] = 'X';
                    int score = minimax(grid, depth - 1, true);
                    grid[row][col] = '-';
                    bestScore = Math.min(bestScore, score);
                }
            }
            return bestScore;
        }
    }

    private boolean isGridFull(char[][] grid2) {
		// TODO Auto-generated method stub
		return false;
	}

	private int getFirstAvailableRow(char[][] grid2, int col) {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean isWinner(char[][] grid2, int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}

	private int evaluateGrid(char[][] grid) {
        int score = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col] == 'O') {
                    score += evaluatePosition(grid, row, col);
                } else if (grid[row][col] == 'X') {
                    score -= evaluatePosition(grid, row, col);
                }
            }
        }
        return score;
    }

    private int evaluatePosition(char[][] grid2, int row, int col) {
		// TODO Auto-generated method stub
		return 0;
	}
}

