import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

class OthelloTreeNode {
    char[][] board;
    List<OthelloTreeNode> children;
    int[] move;

    public OthelloTreeNode(char[][] board, int[] move) {
        this.board = board;
        this.children = new ArrayList<>();
        this.move = move;
    }

    public int evaluateBoard() {
        int playerXCount = countChips(board, 'X');
        int playerOCount = countChips(board, 'O');
        return playerXCount - playerOCount;
    }

    private int countChips(char[][] board, char player) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    public void generateChildren(char currentPlayer) {
        char player = (currentPlayer == 'X') ? 'X' : 'O';
        List<int[]> validMoves = getValidMoves(player);
        for (int[] move : validMoves) {
            char[][] newBoard = deepCopy(board);
            simulateMove(newBoard, move[0], move[1], player);
            OthelloTreeNode childNode = new OthelloTreeNode(newBoard, move);
            children.add(childNode);
        }
    }

    public List<int[]> getValidMoves(char player) {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isValidMove(i, j, player)) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }
        return validMoves;
    }

    public boolean isValidMove(int row, int col, char player) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length || board[row][col] != '-') {
            return false;
        }
        char opponent = (player == 'X') ? 'O' : 'X';
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == opponent) {
                r += dir[0];
                c += dir[1];
                while (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == opponent) {
                    r += dir[0];
                    c += dir[1];
                }
                if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    public void simulateMove(char[][] board, int row, int col, char player) {
        board[row][col] = player;
        char opponent = (player == 'X') ? 'O' : 'X';
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == opponent) {
                List<int[]> flip = new ArrayList<>();
                flip.add(new int[] { r, c });
                r += dir[0];
                c += dir[1];
                while (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == opponent) {
                    flip.add(new int[] { r, c });
                    r += dir[0];
                    c += dir[1];
                }
                if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == player) {
                    for (int[] move : flip) {
                        board[move[0]][move[1]] = player;
                    }
                }
            }
        }
    }

    public char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }
        char[][] result = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                result[i][j] = original[i][j];
            }
        }
        return result;
    }
}

class OthelloGame {
    public static final int BOARD_SIZE = 8;
    static char EMPTY = '-';
    static char PLAYER_X = 'X';
    static char PLAYER_O = 'O';

    public char[][] board;

    public OthelloGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    public void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }

        int mid = BOARD_SIZE / 2;
        board[mid - 1][mid - 1] = PLAYER_X;
        board[mid][mid] = PLAYER_X;
        board[mid - 1][mid] = PLAYER_O;
        board[mid][mid - 1] = PLAYER_O;
    }

    public boolean isGameOver() {
        return getValidMoves(PLAYER_X).isEmpty() && getValidMoves(PLAYER_O).isEmpty() || isBoardFull();
    }

    public boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<int[]> getValidMoves(char player) {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isValidMove(i, j, player)) {
                    validMoves.add(new int[] { i, j });
                }
            }
        }
        return validMoves;
    }

    public boolean isValidMove(int row, int col, char player) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != EMPTY) {
            return false;
        }
        char opponent = (player == PLAYER_X) ? PLAYER_O : PLAYER_X;
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == opponent) {
                r += dir[0];
                c += dir[1];
                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == opponent) {
                    r += dir[0];
                    c += dir[1];
                }
                if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void simulateMove(char[][] board, int row, int col, char player) {
        board[row][col] = player;
        char opponent = (player == PLAYER_X) ? PLAYER_O : PLAYER_X;
        int[][] directions = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == opponent) {
                List<int[]> flip = new ArrayList<>();
                flip.add(new int[] { r, c });
                r += dir[0];
                c += dir[1];
                while (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == opponent) {
                    flip.add(new int[] { r, c });
                    r += dir[0];
                    c += dir[1];
                }
                if (r >= 0 && r < BOARD_SIZE && c >= 0 && c < BOARD_SIZE && board[r][c] == player) {
                    for (int[] move : flip) {
                        board[move[0]][move[1]] = player;
                    }
                }
            }
        }
    }
}

class Main extends JFrame {

    private OthelloGame game;
    private char currentPlayer;
    private JPanel boardPanel;

    public Main() {
        game = new OthelloGame();
        currentPlayer = OthelloGame.PLAYER_X;

        boardPanel = new JPanel(new GridLayout(OthelloGame.BOARD_SIZE, OthelloGame.BOARD_SIZE));

        updateBoardGUI();

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = new OthelloGame();
                currentPlayer = OthelloGame.PLAYER_X;
                updateBoardGUI();
            }
        });

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(boardPanel, BorderLayout.CENTER);
        rootPanel.add(newGameButton, BorderLayout.SOUTH);

        add(rootPanel);

        setTitle("Othello Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    
    public void simulateMoveWithAnimation(char[][] board, int row, int col, char player) {
        JButton button = (JButton) boardPanel.getComponent(row * OthelloGame.BOARD_SIZE + col);
        button.setBackground(Color.RED); // Highlight the AI's move with red color
        Timer timer = new Timer(500, new ActionListener() { // Change back to original color after 500 milliseconds
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setBackground(Color.WHITE); // Change back to original color
                OthelloGame.simulateMove(board, row, col, player); // Simulate the move after animation
                updateBoardGUI(); // Update the GUI after move simulation
                ((Timer) e.getSource()).stop(); // Stop the timer
            }
        });
        timer.setRepeats(false); // Only run the timer once
        timer.start(); // Start the timer
    }

    public void updateBoardGUI() {
        boardPanel.removeAll();
        char[][] board = game.board;
        for (int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
            for (int j = 0; j < OthelloGame.BOARD_SIZE; j++) {
                JButton button = new JButton();
                button.setBackground(Color.GREEN);
                boardPanel.add(button);
                if (board[i][j] == OthelloGame.PLAYER_X) {
                    button.setBackground(Color.BLACK);
                } else if (board[i][j] == OthelloGame.PLAYER_O) {
                    button.setBackground(Color.WHITE);
                }
                int row = i, col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleMove(row, col);
                    }
                });
            }
        }

        if (currentPlayer == OthelloGame.PLAYER_O) {
            // AI's turn
            int[] aiMove = getBestMove();
            if (aiMove != null) {
                simulateMoveWithAnimation(game.board, aiMove[0], aiMove[1], currentPlayer); // Animate AI's move with flipping chips
                currentPlayer = OthelloGame.PLAYER_X;
                return; // Exit the method after animating AI's move
            }
        }

        // Check if the game is over after the AI's move animation completes
        if (game.isGameOver()) {
            showWinner();
        }

        revalidate();
        repaint();
    }

    public void handleMove(int row, int col) {
    if (!game.isValidMove(row, col, currentPlayer)) {
        // Display a message indicating that the move is invalid
        JOptionPane.showMessageDialog(this, "Invalid move. Please select a valid move.", "Invalid Move", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the move is invalid
    }

    // Animate user's move
    animateUserMove(row, col);

    // Update the game board GUI after animation
    Timer timer = new Timer(500, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Simulate and update user's move on the board
            OthelloGame.simulateMove(game.board, row, col, currentPlayer);
            updateBoardGUI(); // Update the GUI after the user's move

            if (game.isGameOver()) {
                showWinner();
            } else {
                currentPlayer = (currentPlayer == OthelloGame.PLAYER_X) ? OthelloGame.PLAYER_O : OthelloGame.PLAYER_X;
                if (currentPlayer == OthelloGame.PLAYER_O && !game.isGameOver()) {
                    // AI's turn
                    List<int[]> aiValidMoves = game.getValidMoves(currentPlayer);
                    if (!aiValidMoves.isEmpty()) {
                        int[] aiMove = getBestMove();
                        if (aiMove != null) {
                            simulateMoveWithAnimation(game.board, aiMove[0], aiMove[1], currentPlayer); // Animate AI's move with flipping chips
                            currentPlayer = OthelloGame.PLAYER_X;
                            return; // Exit the method after animating AI's move
                        }
                    } else {
                        // Skip AI's turn and give the turn back to the user
                        JOptionPane.showMessageDialog(Main.this, "AI has no valid moves. Skipping AI's turn.", "No Valid Moves", JOptionPane.INFORMATION_MESSAGE);
                        currentPlayer = OthelloGame.PLAYER_X;
                    }
                }
            }
            ((Timer) e.getSource()).stop(); // Stop the timer
        }
    });
    timer.setRepeats(false); // Only run the timer once
    timer.start(); // Start the timer
}

    
    public void animateUserMove(int row, int col) {
        JButton button = (JButton) boardPanel.getComponent(row * OthelloGame.BOARD_SIZE + col);
        Color originalColor = button.getBackground(); // Get the original color of the button
    
        // Set a temporary color to indicate the user's move
        Color tempColor = new Color(0, 191, 255); // Light blue color
        button.setBackground(tempColor);
    
        // Create a timer to revert the color back to its original state after a short delay
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setBackground(originalColor); // Revert the color back to original
                ((Timer) e.getSource()).stop(); // Stop the timer
            }
        });
        timer.setRepeats(false); // Only run the timer once
        timer.start(); // Start the timer
    }
    
    public void showWinner() {
        int playerXCount = countChips(game.board, OthelloGame.PLAYER_X);
        int playerOCount = countChips(game.board, OthelloGame.PLAYER_O);
        String winner;
        if (playerXCount > playerOCount) {
            winner = "User wins with " + playerXCount + " chips!";
        } else if (playerXCount < playerOCount) {
            winner = "AI wins with " + playerOCount + " chips!";
        } else {
            winner = "It's a tie! Both players have " + playerXCount + " chips.";
        }
        JOptionPane.showMessageDialog(this, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    public int countChips(char[][] board, char player) {
        int count = 0;
        for (int i = 0; i < OthelloGame.BOARD_SIZE; i++) {
            for (int j = 0; j < OthelloGame.BOARD_SIZE; j++) {
                if (board[i][j] == player) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[] getBestMove() {
        AIPlayer aiPlayer = new AIPlayer(OthelloGame.PLAYER_O);
        return aiPlayer.getBestMove(game);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}

class AIPlayer {
    char player;

    public AIPlayer(char player) {
        this.player = player;
    }

    public int[] getBestMove(OthelloGame game) {
        List<int[]> validMoves = game.getValidMoves(player);
        if (validMoves.isEmpty()) {
            return null;
        }

        int bestScore = Integer.MAX_VALUE;
        int[] bestMove = null;

        for (int[] move : validMoves) {
            OthelloGame simGame = new OthelloGame();
            char[][] simBoard = deepCopy(game.board);
            simGame.board = simBoard;
            OthelloGame.simulateMove(simBoard, move[0], move[1], player);
            OthelloTreeNode rootNode = new OthelloTreeNode(simBoard, move);
            rootNode.generateChildren('X');
            int score = minimax(rootNode, false);
            if (score < bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    public int minimax(OthelloTreeNode node, boolean maximizingPlayer) {
        if (node.children.isEmpty()) {
            return node.evaluateBoard();
        }

        node.generateChildren(maximizingPlayer ? player : (player == 'X' ? 'O' : 'X'));

        if (maximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            for (OthelloTreeNode child : node.children) {
                maxScore = Math.max(maxScore, minimax(child, false));
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (OthelloTreeNode child : node.children) {
                minScore = Math.min(minScore, minimax(child, true));
            }
            return minScore;
        }
    }

    public char[][] deepCopy(char[][] original) {
        if (original == null) {
            return null;
        }
        char[][] result = new char[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                result[i][j] = original[i][j];
            }
        }
        return result;
    }
}
