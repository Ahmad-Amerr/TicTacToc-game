package com.example.tictactocgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPlayer {
    public int[] random_option(char[][] board) {
        List<int[]> emptyMoves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == '-') {
                    emptyMoves.add(new int[]{i, j});
                }
            }
        }
        if (emptyMoves.isEmpty()) {
            return null;
        }
        int[] move = emptyMoves.get(new Random().nextInt(emptyMoves.size()));
        return move;
    }
}