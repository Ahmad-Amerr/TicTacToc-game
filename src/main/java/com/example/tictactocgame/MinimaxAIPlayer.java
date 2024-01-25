package com.example.tictactocgame;

public class MinimaxAIPlayer {


    public int[] findMoveThatItTheBest(char[][] board, char currentPlayer) {
        int bestResault = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};
        for (int i = 0; i < 3; i++) { //row
            for (int j = 0; j < 3; j++) { //col
                if (board[i][j] == '-') {
                    board[i][j] = currentPlayer;
                    int score = minimax(board, false, currentPlayer);
                    board[i][j] = '-';
                    if (score > bestResault) {
                        bestResault = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    int minimax(char[][] board, boolean Maximiz, char player) {
        char otherPlayer = (player == 'X') ? 'O' : 'X';
        int score = checkEachRowAndColEvaluate(board, player);

        if (score == 10 || score == -10) {
            return score;
        }

        if (checkBoardFull(board)) {
            return 0;
        }

        if (Maximiz) {
            int bestResault = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == '-') {
                        board[row][col] = player;
                        bestResault = Math.max(bestResault, minimax(board, false, player));
                        board[row][col] = '-';
                    }
                }
            }
            return bestResault;
        } else {
            int bestScoreMin = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == '-') {
                        board[row][col] = otherPlayer;
                        bestScoreMin = Math.min(bestScoreMin, minimax(board, true, player));
                        board[row][col] = '-';
                    }
                }
            }
            return bestScoreMin;
        }
    }


    private boolean checkBoardFull(char[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    private int checkEachRowAndColEvaluate(char[][] board, char player) {
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == player) {
                    return 10;
                } else if (board[row][0] != '-' && board[row][0] != player) {
                    return -10;
                }
            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == player) {
                    return 10;
                } else if (board[0][col] != '-' && board[0][col] != player) {
                    return -10;
                }
            }
        }

        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] || board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[1][1] == player) {
                return 10;
            } else if (board[1][1] != '-' && board[1][1] != player) {
                return -10;
            }
        }

        return 0;
    }
}