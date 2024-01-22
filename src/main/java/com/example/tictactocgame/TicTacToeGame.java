package com.example.tictactocgame;

import javafx.scene.control.Button;

public class TicTacToeGame {
    private char[][] board = new char[3][3];
    private char currentPlayer = 'X';
    private boolean gameOver = false;

    public TicTacToeGame() {
        initializeBoard();
    }
    public void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }
    public char[][] getBoard() {
        return board;
    }
    public boolean makeMove(int row, int col) {
        if (gameOver || board[row][col] != '-') {
            return false;
        }
        board[row][col] = currentPlayer;
        return true;
    }

    public boolean checkForWin() {
        return (checkRowsForWin() || checkColumnsForWin() || checkDiagonalsForWin());
    }
    private boolean checkRowsForWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '-' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }
        return false;
    }
    private boolean checkColumnsForWin() {
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != '-' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalsForWin() {
        return (board[0][0] != '-' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (board[0][2] != '-' && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        gameOver = true;
        return true;
    }
    void updateButtonsWithScores(char[][] board, char player, Button[][] buttons) {
        MinimaxAIPlayer aiPlayer = new MinimaxAIPlayer();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '-') {
                    board[row][col] = player;
                    int score = aiPlayer.minimax(board, 0, false, player);
                    board[row][col] = '-';
                    buttons[row][col].setText("Score: " + score);
                }
            }
        }
    }
    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
        currentPlayer = 'X';
        gameOver = false;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void changePlayer() {
        System.out.println(currentPlayer);
        currentPlayer  = (currentPlayer == 'X') ? 'O' : 'X';
    }
    public void setCurrentPlayer(char player) {
        if (player == 'X' || player == 'O') {
            this.currentPlayer = player;
        }
    }
    public void setGameEnded(boolean b) {
    }
}
