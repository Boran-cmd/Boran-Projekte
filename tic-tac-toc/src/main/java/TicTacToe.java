import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private char aiPlayer = 'O';
    private boolean vsAI = false;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                panel.add(buttons[i][j]);
            }
        }

        add(panel, BorderLayout.CENTER);

        JButton vsAIButton = new JButton("Gegen KI spielen");
        vsAIButton.addActionListener(e -> {
            vsAI = true;
            resetBoard();
        });

        JButton vsFriendButton = new JButton("Gegen Freund spielen");
        vsFriendButton.addActionListener(e -> {
            vsAI = false;
            resetBoard();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(vsAIButton);
        buttonPanel.add(vsFriendButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
            }
        }
        currentPlayer = 'X';
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (buttons[row][col].getText().equals(" ")) {
                buttons[row][col].setText(String.valueOf(currentPlayer));
                buttons[row][col].setEnabled(false);

                if (checkWinner()) {
                    JOptionPane.showMessageDialog(null, "Spieler " + currentPlayer + " hat gewonnen!");
                    resetBoard();
                    return;
                }

                if (isBoardFull()) {
                    JOptionPane.showMessageDialog(null, "Das Spiel endet unentschieden!");
                    resetBoard();
                    return;
                }

                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Wechselt den Spieler

                if (vsAI && currentPlayer == aiPlayer) {
                    aiMove();
                }
            }
        }
    }

    private void aiMove() {
        int[] bestMove = findBestMove();
        buttons[bestMove[0]][bestMove[1]].setText(String.valueOf(aiPlayer));
        buttons[bestMove[0]][bestMove[1]].setEnabled(false);

        if (checkWinner()) {
            JOptionPane.showMessageDialog(null, "Spieler " + aiPlayer + " hat gewonnen!");
            resetBoard();
            return;
        }

        if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "Das Spiel endet unentschieden!");
            resetBoard();
        }

        currentPlayer = 'X'; 
    }

    private int[] findBestMove() {
        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = new int[2];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals(" ")) {
                    buttons[i][j].setText(String.valueOf(aiPlayer));
                    int moveValue = minimax(0, false);
                    buttons[i][j].setText(" ");

                    if (moveValue > bestValue) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }

    private int minimax(int depth, boolean isMax) {
        if (checkWinner()) {
            return isMax ? -1 : 1;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals(" ")) {
                        buttons[i][j].setText(String.valueOf(aiPlayer));
                        bestValue = Math.max(bestValue, minimax(depth + 1, false));
                        buttons[i][j].setText(" ");
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().equals(" ")) {
                        buttons[i][j].setText(String.valueOf(currentPlayer));
                        bestValue = Math.min(bestValue, minimax(depth + 1, true));
                        buttons[i][j].setText(" ");
                    }
                }
            }
            return bestValue;
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if ((buttons[i][0].getText().equals(String.valueOf(currentPlayer)) && buttons[i][1].getText().equals(String.valueOf(currentPlayer)) && buttons[i][2].getText().equals(String.valueOf(currentPlayer))) ||
                    (buttons[0][i].getText().equals(String.valueOf(currentPlayer)) && buttons[1][i].getText().equals(String.valueOf(currentPlayer)) && buttons[2][i].getText().equals(String.valueOf(currentPlayer)))) {
                return true;
            }
        }
        return (buttons[0][0].getText().equals(String.valueOf(currentPlayer)) && buttons[1][1].getText().equals(String.valueOf(currentPlayer)) && buttons[2][2].getText().equals(String.valueOf(currentPlayer))) ||
                (buttons[0][2].getText().equals(String.valueOf(currentPlayer)) && buttons[1][1].getText().equals(String.valueOf(currentPlayer)) && buttons[2][0].getText().equals(String.valueOf(currentPlayer)));
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals(" ")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
