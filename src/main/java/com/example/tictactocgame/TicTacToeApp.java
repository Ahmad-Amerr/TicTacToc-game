package com.example.tictactocgame;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TicTacToeApp extends Application {
    private TicTacToeGame game;
    private int currentRound = 1;
    private BorderPane gamePane;
    private int scoreFirstPlayer = 0;
    private int scoreSecondPlayer = 0;
    private String playerNameX;
    private String playerNameO;
    private int totalRounds;
    private Button[][] buttons = new Button[3][3];
    private ComboBox<String> gameModes;
    private ComboBox<String> firstPlayerChoice;
    private Label playerTurn;
    private Label scoreLabel;
    private Label roundLabel;
    private TextField firstPlayerName;
    private Label playerFirstX;
    private Label playerSecO;
    private TextField secPlayerName;
    private String currentPlayer = "X";
    private TextField numberOfRounds;

    String currentPlayerName;
    @Override
    public void start(Stage primaryStage) {
        game = new TicTacToeGame();
        gamePane = new BorderPane();

        VBox gameStartInfo = gameStartInfo();
        gamePane.setCenter(gameStartInfo);

        Scene scene = new Scene(gamePane, 700, 700);
        primaryStage.setTitle("X-O GAME");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createBoard() {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(200, 200);
                int rowClicked = i, colClicked = j;
                button.setOnAction(e -> handlePlayerMove(rowClicked, colClicked));
                gridPane.add(button, j, i);
                buttons[i][j] = button;
            }
        }
        return gridPane;
    }
    private VBox gameStartInfo() {
        firstPlayerName = new TextField("Player X");
        secPlayerName = new TextField("Player O");
        numberOfRounds = new TextField("1");
        gameModes = new ComboBox<>();
        gameModes.getItems().addAll("Human vs Human", "Human vs AI (Random)", "Human vs AI (Minimax)");
        gameModes.setValue("Human vs Human");
        Button btnStartGame = new Button("Start Game");
        btnStartGame.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        firstPlayerChoice = new ComboBox<>();
        firstPlayerChoice.getItems().addAll("Player X", "Player O", "Random");
        firstPlayerChoice.setValue("Player X");
        btnStartGame.setOnAction(e -> initializeGame());
        Label titleLabel = new Label("Tic-Tac-Toe Game start");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox gameSettings = new VBox(10, titleLabel,
                new Label("Player X Name:"), firstPlayerName,
                new Label("Player O Name:"), secPlayerName,
                new Label("Number of Rounds:"), numberOfRounds,
                new Label("Game Mode:"), gameModes,
                new Label("Who starts first?"), firstPlayerChoice,
                btnStartGame);
        gameSettings.setAlignment(Pos.CENTER);
        gameSettings.setPadding(new Insets(10));
        return gameSettings;
    }


    private void initializeGame() {
        playerNameX = firstPlayerName.getText();
        playerNameO = secPlayerName.getText();
        currentPlayer=playerNameX;
        try {
            totalRounds = Integer.parseInt(numberOfRounds.getText());
            if(totalRounds < 1){
                totalRounds=1;
            }
        } catch (NumberFormatException e) {
            totalRounds = 1;
        }
        currentRound = 1;
        gamePane.setCenter(createBoard());
        gamePane.setRight(createSidePanel());
        startGame();
    }
    private VBox createSidePanel() {
        playerFirstX=new Label("First Player  :"+playerNameX + "[ X ]"  );
        playerSecO=new Label("Sec Player :"+playerNameO +"[ O ]" );
        playerTurn = new Label("Player "+playerNameX+"/'s turn");
        scoreLabel = new Label("Score - X: 0, O: 0");
        roundLabel = new Label("Round: 1");
        Button startGameButton = new Button("reset Game");
        startGameButton.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        startGameButton.setOnAction(e -> resetGame());
        VBox sidePanel = new VBox(10, playerFirstX,playerSecO,playerTurn, scoreLabel, roundLabel);
        sidePanel.setAlignment(Pos.CENTER);
        sidePanel.setPrefWidth(200);
        sidePanel.getChildren().addAll(startGameButton);
        return sidePanel;
    }
    private void startGame() {
        game.initializeBoard();
        resetGameBoardUI();
        currentRound = 1;
        updateRoundDisplay();

        String startingPlayer = firstPlayerChoice.getValue();
        char firstToStart = 'X';
        if (startingPlayer.equals("Player O")) {
            firstToStart = 'O';
        } else if (startingPlayer.equals("Random")) {
            firstToStart = Math.random() < 0.5 ? 'X' : 'O';
        }
        game.setCurrentPlayer(firstToStart);
        updateStatus();
    }
    private String getGameModeChoice() {
        return gameModes.getValue();
    }
    private void handlePlayerMove(int row, int col) {
        if (!game.makeMove(row, col)) {
            return;
        }
        updateBoard(row, col);
        if (checkGameState()) {
            return;
        }
        game.changePlayer();
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        String currentGameMode=getGameModeChoice();
        if (game.getCurrentPlayer() == 'O' && "Human vs AI (Random)".equals(currentGameMode) || "Human vs AI (Minimax)".equals(currentGameMode)) {
            performAIMove();
        }
        pause.play();
    }
    private void updateStatus() {
         currentPlayerName = game.getCurrentPlayer() == 'X' ?  playerNameX: playerNameO;
        System.out.println("turn :"+currentPlayerName);
        playerTurn.setText(currentPlayerName + "'s turn");
    }
    private void updateBoard(int row, int col) {
        currentPlayerName = game.getCurrentPlayer() == 'X' ?  playerNameX: playerNameO;
        buttons[row][col].setText(String.valueOf(game.getCurrentPlayer()));
        currentPlayerName = game.getCurrentPlayer() == 'X' ?  playerNameO:playerNameX ;
        playerTurn.setText("Player " +currentPlayerName+ "'s turn");
    }

    private boolean checkGameState() {
        if (game.checkForWin()) {
            game.setGameEnded(true);
            updateScores();
            endGame("Player " + game.getCurrentPlayer() + " wins!");
            return true;
        } else if (game.isBoardFull()) {
            game.setGameEnded(true);
            endGame("Game is a draw!");
            return true;
        }
        currentPlayerName=game.getCurrentPlayer()=='X'?"O":"X";
        playerTurn.setText("Player " +currentPlayerName + "'s turn");
        return false;
    }

    private void performAIMove() {
        String gameMode = gameModes.getValue();
        int[] aiMove;
            if ("Human vs AI (Random)".equals(gameMode)) {
                RandomPlayer aiRandomMove = new RandomPlayer();
                aiMove = aiRandomMove.random_option(game.getBoard());
            } else if ("Human vs AI (Minimax)".equals(gameMode)) {
                game.updateButtonsWithScores(game.getBoard(), game.getCurrentPlayer(),buttons);
                MinimaxAIPlayer aiAdvancedPlayer = new MinimaxAIPlayer();
                aiMove = aiAdvancedPlayer.findMoveThatItTheBest(game.getBoard(), game.getCurrentPlayer());
            } else {
                return;
            }
            if (aiMove != null) {
                game.makeMove(aiMove[0], aiMove[1]);
                updateBoard(aiMove[0], aiMove[1]);
                checkGameState();
            }
            game.changePlayer();

    }

    private void updateScores() {
        if (game.getCurrentPlayer() == 'X') {
            scoreFirstPlayer++;
        } else {
            scoreSecondPlayer++;
        }
        scoreLabel.setText("Score - X: " + scoreFirstPlayer + ", O: " + scoreSecondPlayer);
    }

    private void endGame(String message) {
        playerTurn.setText(message);
        disableBoard();

        if (currentRound < totalRounds) {
            currentRound++;
            Button nextRoundButton = new Button("Next Round");
            nextRoundButton.setOnAction(e -> {
                resetGameForNextRound();
            });
            gamePane.setBottom(nextRoundButton);
        }
    }
    private void resetGameForNextRound() {
        game.resetBoard();
        resetGameBoardUI();
        updateRoundDisplay();


        String startingPlayer = firstPlayerChoice.getValue();
        char firstToStart = startingPlayer.equals("Player X") ? 'X' : 'O';
        if (startingPlayer.equals("Random")) {
            firstToStart = Math.random() < 0.5 ? 'X' : 'O';
        }
        game.setCurrentPlayer(firstToStart);
        updateStatus();

        gamePane.setBottom(null);
    }
    private void disableBoard() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
    }
    private void resetGameBoardUI() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setDisable(false);
            }
        }
    }
    private void updateRoundDisplay() {
        roundLabel.setText("Round: " + currentRound + "/" + totalRounds);
    }
    private void resetGame() {
        game.resetBoard();
        resetGameBoardUI();
        updateRoundDisplay();
        if (firstPlayerChoice.getValue().equals("Random")) {
            game.setCurrentPlayer(Math.random() < 0.5 ? 'X' : 'O');
        } else {
            game.setCurrentPlayer(firstPlayerChoice.getValue().equals("Player X") ? 'X' : 'O');
        }
        updateStatus();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
