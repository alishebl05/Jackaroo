package view;

import engine.Game;
import engine.GameManager;
import engine.board.Board;
import engine.board.Cell;
import exception.*;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Colour;
import card.Card;
import card.standard.*;
import card.wild.*;
import model.player.Marble;
import model.player.Player;

import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static engine.board.Cell.isTrapActivated;
import static engine.board.Cell.trapActivated;

public class GUI extends Application {
    Game game;

    AnchorPane pane;

    Button player1card1, player1card2, player1card3, player1card4,
            player2card1, player2card2, player2card3, player2card4,
            player3card1, player3card2, player3card3, player3card4,
            player4card1, player4card2, player4card3, player4card4;

    Button player1marble1, player1marble2, player1marble3, player1marble4,
            player2marble1, player2marble2, player2marble3, player2marble4,
            player3marble1, player3marble2, player3marble3, player3marble4,
            player4marble1, player4marble2, player4marble3, player4marble4;

    Button playButton, deSelectButton, discardButton,
           refreshButton, helpButton, exitButton;

    Rectangle firepit,
            player1HomeZone, player2HomeZone, player3HomeZone, player4HomeZone;

    Circle table;

    ImageView background, tableImg, firepitImg, refreshButtonImg,
            playerIcon, cpu1Icon, cpu2Icon, cpu3Icon,
            player1card1img, player1card2img, player1card3img, player1card4img,
            player2card1img, player2card2img, player2card3img, player2card4img,
            player3card1img, player3card2img, player3card3img, player3card4img,
            player4card1img, player4card2img, player4card3img, player4card4img;

    Label playerLabel, cpu1Label, cpu2Label, cpu3Label,
            playerColor, cpu1Color, cpu2Color, cpu3Color,
            currentPlayer, nextPlayer;

    MediaPlayer currentMediaPlayer;

    ArrayList<String> trackFiles = new ArrayList<>();


    private static final double CENTER_X = 540;
    private static final double CENTER_Y = 320;
    private static final double TRACK_RADIUS = 190;
    private static final double SAFE_ZONE_STEP = 13;
    private static final int NUM_SAFE_CELLS = 4;

    @Override
    public void start(Stage primaryStage) throws Exception {
        pane = new AnchorPane();
        Scene scene = new Scene(pane, 1080, 720);
        URL backgroundUrl = getClass().getResource("/resources/background.jpg");

        background = new ImageView();
        if (backgroundUrl != null) {
            background = new ImageView(backgroundUrl.toString());
        }
        background.setFitWidth(1080);
        background.setFitHeight(720);

        trackFiles.add("/music/track1.mp3");
        trackFiles.add("/music/track2.mp3");
        trackFiles.add("/music/track3.mp3");
        trackFiles.add("/music/track4.mp3");
        trackFiles.add("/music/track5.mp3");
        trackFiles.add("/music/track6.mp3");
        trackFiles.add("/music/track7.mp3");
        trackFiles.add("/music/track8.mp3");
        trackFiles.add("/music/track9.mp3");
        trackFiles.add("/music/track10.mp3");
        trackFiles.add("/music/track11.mp3");
        trackFiles.add("/music/track12.mp3");

        table = new Circle(540, 320, 205);
        table.setStyle("-fx-fill: #AA3300;");

        URL tableUrl = getClass().getResource("/resources/wood.jpg");

        tableImg = new ImageView();

        if (tableUrl != null) {
            tableImg = new ImageView(tableUrl.toString());
            tableImg.setFitWidth(410);
            tableImg.setFitHeight(410);

            Circle clip = new Circle(205, 205, 205);  // centerX and centerY relative to ImageView
            tableImg.setClip(clip);

            tableImg.setLayoutX(540 - 205);
            tableImg.setLayoutY(320 - 205);
        }

        String playerName = showNameInputDialog();
        game = new Game(playerName);

        Color[] playerColors = getPlayerColors();

        // makes track
        createTrack();

        Platform.runLater(() -> updatePlayerHand());

        currentPlayer = new Label();
        currentPlayer.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        currentPlayer.setTextFill(Color.WHITE);
        currentPlayer.setLayoutX(10);
        currentPlayer.setLayoutY(10);

        nextPlayer = new Label();
        nextPlayer.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        nextPlayer.setTextFill(Color.WHITE);
        nextPlayer.setLayoutX(10);
        nextPlayer.setLayoutY(40);

        updateTurnIndicators();

        // firepit

        firepit = new Rectangle();
        firepit.setWidth(80);
        firepit.setHeight(115);
        firepit.setStyle("-fx-background-color: #000000");
        firepit.setLayoutX(500);
        firepit.setLayoutY(262);

        firepitImg = new ImageView();
        firepitImg.setFitWidth(80);
        firepitImg.setFitHeight(115);
        setPosition(firepitImg, 500, 262);

        // human player cards

        player1card1 = new Button();
        player1card2 = new Button();
        player1card3 = new Button();
        player1card4 = new Button();

        player1card1.setPrefSize(120, 180);
        player1card2.setPrefSize(120, 180);
        player1card3.setPrefSize(120, 180);
        player1card4.setPrefSize(120, 180);

        setPosition(player1card1, 285, 530);
        setPosition(player1card2, 415, 530);
        setPosition(player1card3, 545, 530);
        setPosition(player1card4, 675, 530);

        player1card1img = new ImageView();
        player1card2img = new ImageView();
        player1card3img = new ImageView();
        player1card4img = new ImageView();

        player1card1img.setMouseTransparent(true);
        player1card2img.setMouseTransparent(true);
        player1card3img.setMouseTransparent(true);
        player1card4img.setMouseTransparent(true);

        player1card1img.setFitHeight(180);
        player1card1img.setFitWidth(120);
        player1card2img.setFitHeight(180);
        player1card2img.setFitWidth(120);
        player1card3img.setFitHeight(180);
        player1card3img.setFitWidth(120);
        player1card4img.setFitHeight(180);
        player1card4img.setFitWidth(120);

        setPosition(player1card1img, 285, 530);
        setPosition(player1card2img, 415, 530);
        setPosition(player1card3img, 545, 530);
        setPosition(player1card4img, 675, 530);

        //player cards functionality

        player1card1.setOnAction(e -> {
            if (game.getPlayers().get(0).getHand().get(0) != null) {
                try {
                    game.selectCard(game.getPlayers().get(0).getHand().get(0));

                    setPosition(player1card1, 285, 510);
                    setPosition(player1card2, 415, 530);
                    setPosition(player1card3, 545, 530);
                    setPosition(player1card4, 675, 530);

                    setPosition(player1card1img, 285, 510);
                    setPosition(player1card2img, 415, 530);
                    setPosition(player1card3img, 545, 530);
                    setPosition(player1card4img, 675, 530);

                    if (game.getPlayers().get(0).getHand().get(0) instanceof Seven)
                        showSplitDistanceDialog();

                } catch (InvalidCardException ex) {
                    showErrorDialog("Error", ex.getMessage());
                }
            } else
                showErrorDialog("Error", "No card to select here!");
        });

        player1card2.setOnAction(e -> {
            if (game.getPlayers().get(0).getHand().get(1) != null) {
                try {
                    game.selectCard(game.getPlayers().get(0).getHand().get(1));

                    setPosition(player1card1, 285, 530);
                    setPosition(player1card2, 415, 510);
                    setPosition(player1card3, 545, 530);
                    setPosition(player1card4, 675, 530);

                    setPosition(player1card1img, 285, 530);
                    setPosition(player1card2img, 415, 510);
                    setPosition(player1card3img, 545, 530);
                    setPosition(player1card4img, 675, 530);

                    if (game.getPlayers().get(0).getHand().get(1) instanceof Seven)
                        showSplitDistanceDialog();

                } catch (InvalidCardException ex) {
                    showErrorDialog("Error", ex.getMessage());
                }
            } else
                showErrorDialog("Error", "No card to select here!");
        });

        player1card3.setOnAction(e -> {
            if (game.getPlayers().get(0).getHand().get(2) != null) {
                try {
                    game.selectCard(game.getPlayers().get(0).getHand().get(2));

                    setPosition(player1card1, 285, 530);
                    setPosition(player1card2, 415, 530);
                    setPosition(player1card3, 545, 510);
                    setPosition(player1card4, 675, 530);

                    setPosition(player1card1img, 285, 530);
                    setPosition(player1card2img, 415, 530);
                    setPosition(player1card3img, 545, 510);
                    setPosition(player1card4img, 675, 530);

                    if (game.getPlayers().get(0).getHand().get(2) instanceof Seven)
                        showSplitDistanceDialog();

                } catch (InvalidCardException ex) {
                    showErrorDialog("Error", ex.getMessage());
                }
            } else
                showErrorDialog("Error", "No card to select here!");
        });

        player1card4.setOnAction(e -> {
            if (game.getPlayers().get(0).getHand().get(3) != null) {
                try {
                    game.selectCard(game.getPlayers().get(0).getHand().get(3));

                    setPosition(player1card1, 285, 530);
                    setPosition(player1card2, 415, 530);
                    setPosition(player1card3, 545, 530);
                    setPosition(player1card4, 675, 510);

                    setPosition(player1card1img, 285, 530);
                    setPosition(player1card2img, 415, 530);
                    setPosition(player1card3img, 545, 530);
                    setPosition(player1card4img, 675, 510);

                    if (game.getPlayers().get(0).getHand().get(3) instanceof Seven)
                        showSplitDistanceDialog();

                } catch (InvalidCardException ex) {
                    showErrorDialog("Error", ex.getMessage());
                }
            } else
                showErrorDialog("Error", "No card to select here!");
        });

        // cpu cards
        URL cardbackred = getClass().getResource("/resources/cardbackred.png");

        player2card1 = new Button();
        player2card1.setPrefSize(80, 115);
        player2card1.setLayoutX(983);
        player2card1.setLayoutY(375);
        player2card1.setRotate(270);
        player2card1.setMouseTransparent(true);

        player2card1img = new ImageView();
        if (cardbackred != null) {
            player2card1img = new ImageView(cardbackred.toString());
        }
        player2card1img.setFitWidth(80);
        player2card1img.setFitHeight(115);
        player2card1img.setLayoutX(983);
        player2card1img.setLayoutY(375);
        player2card1img.setRotate(270);

        player2card2 = new Button();
        player2card2.setPrefSize(80, 115);
        player2card2.setLayoutX(983);
        player2card2.setLayoutY(325);
        player2card2.setRotate(270);
        player2card2.setMouseTransparent(true);

        player2card2img = new ImageView();
        if (cardbackred != null) {
            player2card2img = new ImageView(cardbackred.toString());
        }
        player2card2img.setFitWidth(80);
        player2card2img.setFitHeight(115);
        player2card2img.setLayoutX(983);
        player2card2img.setLayoutY(325);
        player2card2img.setRotate(270);

        player2card3 = new Button();
        player2card3.setPrefSize(80, 115);
        player2card3.setLayoutX(983);
        player2card3.setLayoutY(275);
        player2card3.setRotate(270);
        player2card3.setMouseTransparent(true);

        player2card3img = new ImageView();
        if (cardbackred != null) {
            player2card3img = new ImageView(cardbackred.toString());
        }
        player2card3img.setFitWidth(80);
        player2card3img.setFitHeight(115);
        player2card3img.setLayoutX(983);
        player2card3img.setLayoutY(275);
        player2card3img.setRotate(270);

        player2card4 = new Button();
        player2card4.setPrefSize(80, 115);
        player2card4.setLayoutX(983);
        player2card4.setLayoutY(225);
        player2card4.setRotate(270);
        player2card4.setMouseTransparent(true);

        player2card4img = new ImageView();
        if (cardbackred != null) {
            player2card4img = new ImageView(cardbackred.toString());
        }
        player2card4img.setFitWidth(80);
        player2card4img.setFitHeight(115);
        player2card4img.setLayoutX(983);
        player2card4img.setLayoutY(225);
        player2card4img.setRotate(270);

        player3card1 = new Button();
        player3card1.setPrefSize(80, 115);
        player3card1.setLayoutX(425);
        player3card1.setLayoutY(0);
        player3card1.setMouseTransparent(true);

        player3card1img = new ImageView();
        if (cardbackred != null) {
            player3card1img = new ImageView(cardbackred.toString());
        }
        player3card1img.setFitWidth(80);
        player3card1img.setFitHeight(115);
        player3card1img.setLayoutX(425);
        player3card1img.setLayoutY(0);

        player3card2 = new Button();
        player3card2.setPrefSize(80, 115);
        player3card2.setLayoutX(475);
        player3card2.setLayoutY(0);
        player3card2.setMouseTransparent(true);

        player3card2img = new ImageView();
        if (cardbackred != null) {
            player3card2img = new ImageView(cardbackred.toString());
        }
        player3card2img.setFitWidth(80);
        player3card2img.setFitHeight(115);
        player3card2img.setLayoutX(475);
        player3card2img.setLayoutY(0);

        player3card3 = new Button();
        player3card3.setPrefSize(80, 115);
        player3card3.setLayoutX(525);
        player3card3.setLayoutY(0);
        player3card3.setMouseTransparent(true);

        player3card3img = new ImageView();
        if (cardbackred != null) {
            player3card3img = new ImageView(cardbackred.toString());
        }
        player3card3img.setFitWidth(80);
        player3card3img.setFitHeight(115);
        player3card3img.setLayoutX(525);
        player3card3img.setLayoutY(0);

        player3card4 = new Button();
        player3card4.setPrefSize(80, 115);
        player3card4.setLayoutX(575);
        player3card4.setLayoutY(0);
        player3card4.setMouseTransparent(true);

        player3card4img = new ImageView();
        if (cardbackred != null) {
            player3card4img = new ImageView(cardbackred.toString());
        }
        player3card4img.setFitWidth(80);
        player3card4img.setFitHeight(115);
        player3card4img.setLayoutX(575);
        player3card4img.setLayoutY(0);

        player4card1 = new Button();
        player4card1.setPrefSize(80, 115);
        player4card1.setLayoutX(0);
        player4card1.setLayoutY(225);
        player4card1.setRotate(90);
        player4card1.setMouseTransparent(true);

        player4card1img = new ImageView();
        if (cardbackred != null) {
            player4card1img = new ImageView(cardbackred.toString());
        }
        player4card1img.setFitWidth(80);
        player4card1img.setFitHeight(115);
        player4card1img.setLayoutX(0);
        player4card1img.setLayoutY(225);
        player4card1img.setRotate(90);

        player4card2 = new Button();
        player4card2.setPrefSize(80, 115);
        player4card2.setLayoutX(0);
        player4card2.setLayoutY(275);
        player4card2.setRotate(90);
        player4card2.setMouseTransparent(true);

        player4card2img = new ImageView();
        if (cardbackred != null) {
            player4card2img = new ImageView(cardbackred.toString());
        }
        player4card2img.setFitWidth(80);
        player4card2img.setFitHeight(115);
        player4card2img.setLayoutX(0);
        player4card2img.setLayoutY(275);
        player4card2img.setRotate(90);

        player4card3 = new Button();
        player4card3.setPrefSize(80, 115);
        player4card3.setLayoutX(0);
        player4card3.setLayoutY(325);
        player4card3.setRotate(90);
        player4card3.setMouseTransparent(true);

        player4card3img = new ImageView();
        if (cardbackred != null) {
            player4card3img = new ImageView(cardbackred.toString());
        }
        player4card3img.setFitWidth(80);
        player4card3img.setFitHeight(115);
        player4card3img.setLayoutX(0);
        player4card3img.setLayoutY(325);
        player4card3img.setRotate(90);

        player4card4 = new Button();
        player4card4.setPrefSize(80, 115);
        player4card4.setLayoutX(0);
        player4card4.setLayoutY(375);
        player4card4.setRotate(90);
        player4card4.setMouseTransparent(true);

        player4card4img = new ImageView();
        if (cardbackred != null) {
            player4card4img = new ImageView(cardbackred.toString());
        }
        player4card4img.setFitWidth(80);
        player4card4img.setFitHeight(115);
        player4card4img.setLayoutX(0);
        player4card4img.setLayoutY(375);
        player4card4img.setRotate(90);

        player1card1.setId("player1card1");
        player1card2.setId("player1card2");
        player1card3.setId("player1card3");
        player1card4.setId("player1card4");
        player2card1.setId("player2card1");
        player2card2.setId("player2card2");
        player2card3.setId("player2card3");
        player2card4.setId("player2card4");
        player3card1.setId("player3card1");
        player3card2.setId("player3card2");
        player3card3.setId("player3card3");
        player3card4.setId("player3card4");
        player4card1.setId("player4card1");
        player4card2.setId("player4card2");
        player4card3.setId("player4card3");
        player4card4.setId("player4card4");

        player1card1img.setId("player1card1img");
        player1card2img.setId("player1card2img");
        player1card3img.setId("player1card3img");
        player1card4img.setId("player1card4img");
        player2card1img.setId("player2card1img");
        player2card2img.setId("player2card2img");
        player2card3img.setId("player2card3img");
        player2card4img.setId("player2card4img");
        player3card1img.setId("player3card1img");
        player3card2img.setId("player3card2img");
        player3card3img.setId("player3card3img");
        player3card4img.setId("player3card4img");
        player4card1img.setId("player4card1img");
        player4card2img.setId("player4card2img");
        player4card3img.setId("player4card3img");
        player4card4img.setId("player4card4img");

        // home zones
        player1HomeZone = new Rectangle(75, 75, Color.GRAY);
        player1HomeZone.setLayoutX(860);
        player1HomeZone.setLayoutY(415);

        player2HomeZone = new Rectangle(75, 75, Color.GRAY);
        player2HomeZone.setLayoutX(860);
        player2HomeZone.setLayoutY(175);

        player3HomeZone = new Rectangle(75, 75, Color.GRAY);
        player3HomeZone.setLayoutX(155);
        player3HomeZone.setLayoutY(175);

        player4HomeZone = new Rectangle(75, 75, Color.GRAY);
        player4HomeZone.setLayoutX(155);
        player4HomeZone.setLayoutY(415);

        // marbles
        player1marble1 = new Button();
        player1marble1.setShape(new Circle(5));
        player1marble1.setMinSize(10, 10);
        player1marble1.setMaxSize(10, 10);

        player1marble2 = new Button();
        player1marble2.setShape(new Circle(5));
        player1marble2.setMinSize(10, 10);
        player1marble2.setMaxSize(10, 10);

        player1marble3 = new Button();
        player1marble3.setShape(new Circle(5));
        player1marble3.setMinSize(10, 10);
        player1marble3.setMaxSize(10, 10);

        player1marble4 = new Button();
        player1marble4.setShape(new Circle(5));
        player1marble4.setMinSize(10, 10);
        player1marble4.setMaxSize(10, 10);

        marbleColors(player1marble1, player1marble2, player1marble3, player1marble4, 0, playerColors);

        player2marble1 = new Button();
        player2marble1.setShape(new Circle(5));
        player2marble1.setMinSize(10, 10);
        player2marble1.setMaxSize(10, 10);

        player2marble2 = new Button();
        player2marble2.setShape(new Circle(5));
        player2marble2.setMinSize(10, 10);
        player2marble2.setMaxSize(10, 10);

        player2marble3 = new Button();
        player2marble3.setShape(new Circle(5));
        player2marble3.setMinSize(10, 10);
        player2marble3.setMaxSize(10, 10);

        player2marble4 = new Button();
        player2marble4.setShape(new Circle(5));
        player2marble4.setMinSize(10, 10);
        player2marble4.setMaxSize(10, 10);

        marbleColors(player2marble1, player2marble2, player2marble3, player2marble4, 1, playerColors);

        player3marble1 = new Button();
        player3marble1.setShape(new Circle(5));
        player3marble1.setMinSize(10, 10);
        player3marble1.setMaxSize(10, 10);

        player3marble2 = new Button();
        player3marble2.setShape(new Circle(5));
        player3marble2.setMinSize(10, 10);
        player3marble2.setMaxSize(10, 10);

        player3marble3 = new Button();
        player3marble3.setShape(new Circle(5));
        player3marble3.setMinSize(10, 10);
        player3marble3.setMaxSize(10, 10);

        player3marble4 = new Button();
        player3marble4.setShape(new Circle(5));
        player3marble4.setMinSize(10, 10);
        player3marble4.setMaxSize(10, 10);

        marbleColors(player3marble1, player3marble2, player3marble3, player3marble4, 2, playerColors);

        player4marble1 = new Button();
        player4marble1.setShape(new Circle(5));
        player4marble1.setMinSize(10, 10);
        player4marble1.setMaxSize(10, 10);

        player4marble2 = new Button();
        player4marble2.setShape(new Circle(5));
        player4marble2.setMinSize(10, 10);
        player4marble2.setMaxSize(10, 10);

        player4marble3 = new Button();
        player4marble3.setShape(new Circle(5));
        player4marble3.setMinSize(10, 10);
        player4marble3.setMaxSize(10, 10);

        player4marble4 = new Button();
        player4marble4.setShape(new Circle(5));
        player4marble4.setMinSize(10, 10);
        player4marble4.setMaxSize(10, 10);

        marbleColors(player4marble1, player4marble2, player4marble3, player4marble4, 3, playerColors);

        Platform.runLater(() -> updateMarblePositions());

        // marbles functionality

        player1marble1.setId("player1marble1");
        player1marble1.setOnAction(e -> handleCellPress(player1marble1));
        player1marble2.setId("player1marble2");
        player1marble2.setOnAction(e -> handleCellPress(player1marble2));
        player1marble3.setId("player1marble3");
        player1marble3.setOnAction(e -> handleCellPress(player1marble3));
        player1marble4.setId("player1marble4");
        player1marble4.setOnAction(e -> handleCellPress(player1marble4));

        player2marble1.setId("player2marble1");
        player2marble1.setOnAction(e -> handleCellPress(player2marble1));
        player2marble2.setId("player2marble2");
        player2marble2.setOnAction(e -> handleCellPress(player2marble2));
        player2marble3.setId("player2marble3");
        player2marble3.setOnAction(e -> handleCellPress(player2marble3));
        player2marble4.setId("player2marble4");
        player2marble4.setOnAction(e -> handleCellPress(player2marble4));

        player3marble1.setId("player3marble1");
        player3marble1.setOnAction(e -> handleCellPress(player3marble1));
        player3marble2.setId("player3marble2");
        player3marble2.setOnAction(e -> handleCellPress(player3marble2));
        player3marble3.setId("player3marble3");
        player3marble3.setOnAction(e -> handleCellPress(player3marble3));
        player3marble4.setId("player3marble4");
        player3marble4.setOnAction(e -> handleCellPress(player3marble4));

        player4marble1.setId("player4marble1");
        player4marble1.setOnAction(e -> handleCellPress(player4marble1));
        player4marble2.setId("player4marble2");
        player4marble2.setOnAction(e -> handleCellPress(player4marble2));
        player4marble3.setId("player4marble3");
        player4marble3.setOnAction(e -> handleCellPress(player4marble3));
        player4marble4.setId("player4marble4");
        player4marble4.setOnAction(e -> handleCellPress(player4marble4));

        // labels

        playerLabel = new Label(playerName);
        playerColor = new Label(game.getPlayers().get(0).getColour().toString());

        playerLabel.setLayoutX(865);
        playerLabel.setLayoutY(617);
        playerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        playerLabel.setAlignment(Pos.CENTER);

        playerColor.setLayoutX(865);
        playerColor.setLayoutY(637);
        playerColor.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        playerColor.setAlignment(Pos.CENTER);

        cpu1Label = new Label("CPU 1");
        cpu1Color = new Label(game.getPlayers().get(1).getColour().toString());

        cpu1Label.setLayoutX(960);
        cpu1Label.setLayoutY(186);
        cpu1Label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        cpu1Label.setAlignment(Pos.CENTER);

        cpu1Color.setLayoutX(960);
        cpu1Color.setLayoutY(206);
        cpu1Color.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        cpu1Color.setAlignment(Pos.CENTER);

        cpu2Label = new Label("CPU 2");
        cpu2Color = new Label(game.getPlayers().get(2).getColour().toString());

        cpu2Label.setLayoutX(309);
        cpu2Label.setLayoutY(103);
        cpu2Label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        cpu2Label.setAlignment(Pos.CENTER);

        cpu2Color.setLayoutX(309);
        cpu2Color.setLayoutY(123);
        cpu2Color.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        cpu2Color.setAlignment(Pos.CENTER);

        cpu3Label = new Label("CPU 3");
        cpu3Color = new Label(game.getPlayers().get(3).getColour().toString());

        cpu3Label.setLayoutX(27);
        cpu3Label.setLayoutY(592);
        cpu3Label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        cpu3Label.setAlignment(Pos.CENTER);

        cpu3Color.setLayoutX(27);
        cpu3Color.setLayoutY(612);
        cpu3Color.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Arial';");
        cpu3Color.setAlignment(Pos.CENTER);

        // icons

        URL playerIconUrl = getClass().getResource("/resources/playericon.png");

        playerIcon = new ImageView();

        if (playerIconUrl != null) {
            playerIcon = new ImageView(playerIconUrl.toString());
        }

        playerIcon.setFitWidth(90);
        playerIcon.setFitHeight(90);
        playerIcon.setLayoutX(848);
        playerIcon.setLayoutY(530);

        URL cpuIconUrl = getClass().getResource("/resources/cpuicon.png");

        cpu1Icon = new ImageView();
        cpu2Icon = new ImageView();
        cpu3Icon = new ImageView();

        if (cpuIconUrl != null) {
            cpu1Icon = new ImageView(cpuIconUrl.toString());
            cpu2Icon = new ImageView(cpuIconUrl.toString());
            cpu3Icon = new ImageView(cpuIconUrl.toString());
        }

        cpu1Icon.setFitWidth(90);
        cpu1Icon.setFitHeight(90);
        cpu1Icon.setLayoutX(950);
        cpu1Icon.setLayoutY(99);

        cpu2Icon.setFitWidth(90);
        cpu2Icon.setFitHeight(90);
        cpu2Icon.setLayoutX(292);
        cpu2Icon.setLayoutY(16);

        cpu3Icon.setFitWidth(90);
        cpu3Icon.setFitHeight(90);
        cpu3Icon.setLayoutX(14);
        cpu3Icon.setLayoutY(505);

        // buttons

        playButton = new Button("Play");
        playButton.setPrefSize(100, 50);
        playButton.setLayoutX(145);
        playButton.setLayoutY(520);
        playButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: lime; -fx-text-fill: black; -fx-font-size: 25px;");

        deSelectButton = new Button("Deselect");
        deSelectButton.setPrefSize(100, 50);
        deSelectButton.setLayoutX(145);
        deSelectButton.setLayoutY(590);
        deSelectButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: yellow; -fx-text-fill: black; -fx-font-size: 18px;");

        discardButton = new Button("Discard");
        discardButton.setPrefSize(100, 50);
        discardButton.setLayoutX(145);
        discardButton.setLayoutY(660);
        discardButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: red; -fx-text-fill: black; -fx-font-size: 18px;");

        helpButton = new Button("?");
        helpButton.setMinSize(60, 60);
        helpButton.setMaxSize(60, 60);
        helpButton.setLayoutX(940);
        helpButton.setLayoutY(10);
        helpButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-size: 32px;");

        exitButton = new Button("X");
        exitButton.setMinSize(60, 60);
        exitButton.setMaxSize(60, 60);
        exitButton.setLayoutX(1010);
        exitButton.setLayoutY(10);
        exitButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: red; -fx-text-fill: white; -fx-font-size: 30px;");



        refreshButton = new Button();
        refreshButton.setMinSize(60, 60);
        refreshButton.setMaxSize(60, 60);
        refreshButton.setLayoutX(870);
        refreshButton.setLayoutY(10);
        refreshButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: limegreen; -fx-text-fill: white; -fx-font-size: 12px;");

        URL refreshButtonImgUrl = getClass().getResource("/resources/refresh.png");

        refreshButtonImg = new ImageView();

        if (refreshButtonImgUrl != null) {
            refreshButtonImg = new ImageView(refreshButtonImgUrl.toString());
            refreshButtonImg.setFitWidth(60);
            refreshButtonImg.setFitHeight(60);
            refreshButtonImg.setLayoutX(870);
            refreshButtonImg.setLayoutY(10);
        }
        else{
            refreshButton.setText("Refresh");
        }

        playButton.setOnAction(e -> {
            if (game.getActivePlayerColour() == game.getPlayers().get(0).getColour()) {
                if (game.canPlayTurn()) {
                    if (checkIfMustDiscard(game.getPlayers().get(0)) && game.getPlayers().get(0).getSelectedCard() != null) {
                        showErrorDialog("No Playable Cards", "This card will be discarded.");

                        game.endPlayerTurn();
                        game.deselectAll();
                        marbleColors(player1marble1, player1marble2, player1marble3, player1marble4, 0, playerColors);

                        resetCardPositions();
                        update();
                        playCPUTurns();
                    } else
                        try {
                            game.playPlayerTurn();
                            game.endPlayerTurn();
                            game.deselectAll();
                            marbleColors(player1marble1, player1marble2, player1marble3, player1marble4, 0, playerColors);

                            resetCardPositions();

                            update();
                            playCPUTurns();
                        } catch (GameException ex) {
                            showErrorDialog("Error", ex.getMessage());
                        }
                }
            } else
                showErrorDialog("Error", "Not your turn!");
        });

        deSelectButton.setOnAction(e -> {
            game.deselectAll();
            updateMarblePositions();

            resetCardPositions();
        });

        discardButton.setOnAction(e -> {
            if (game.getPlayers().get(0).getSelectedCard() != null && game.getPlayers().get(0).getSelectedMarbles().isEmpty()) {
                game.endPlayerTurn();
                game.deselectAll();
                marbleColors(player1marble1, player1marble2, player1marble3, player1marble4, 0, playerColors);
                updateMarblePositions();

                resetCardPositions();

                update();
                playCPUTurns();
            } else if (game.getPlayers().get(0).getSelectedCard() == null)
                showErrorDialog("Error", "Must select a card to discard.");
            else if (!game.getPlayers().get(0).getSelectedMarbles().isEmpty())
                showErrorDialog("Error", "Please deselect all marbles first.");
        });

        refreshButton.setOnAction(e -> {
            if (game.getActivePlayerColour() == game.getPlayers().get(0).getColour()) {
                if(!game.getPlayers().get(0).getHand().isEmpty())
                    game.getPlayers().get(0).getHand().remove(0);
                game.endPlayerTurn();
                update();
                playCPUTurns();
            }
        });

        helpButton.setOnAction(e -> {
            showShortcutsDialog();
        });

        exitButton.setOnAction(e -> {
            primaryStage.close();
        });

        // keybinds

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DIGIT1:
                    if (player1card1 != null && !player1card1.isMouseTransparent()) player1card1.fire();
                    break;
                case DIGIT2:
                    if (player1card2 != null && !player1card2.isMouseTransparent()) player1card2.fire();
                    break;
                case DIGIT3:
                    if (player1card3 != null && !player1card3.isMouseTransparent()) player1card3.fire();
                    break;
                case DIGIT4:
                    if (player1card4 != null && !player1card4.isMouseTransparent()) player1card4.fire();
                    break;
                case ENTER:
                    if (playButton != null) playButton.fire();
                    break;
                case ESCAPE:
                    if (deSelectButton != null) deSelectButton.fire();
                    break;
                case BACK_SPACE:
                    if (discardButton != null) discardButton.fire();
                    break;
                case DIGIT9:
                    forceField();
                    break;
            }
        });

        pane.getChildren().addAll(background, table, tableImg, firepit, firepitImg,
                player1card1, player1card2, player1card3, player1card4,
                player1card1img, player1card2img, player1card3img, player1card4img,
                player2card1, player2card2, player2card3, player2card4,
                player2card1img, player2card2img, player2card3img, player2card4img,
                player3card1, player3card2, player3card3, player3card4,
                player3card1img, player3card2img, player3card3img, player3card4img,
                player4card1, player4card2, player4card3, player4card4,
                player4card1img, player4card2img, player4card3img, player4card4img,
                player1HomeZone, player2HomeZone, player3HomeZone, player4HomeZone,
                player1marble1, player1marble2, player1marble3, player1marble4,
                player2marble1, player2marble2, player2marble3, player2marble4,
                player3marble1, player3marble2, player3marble3, player3marble4,
                player4marble1, player4marble2, player4marble3, player4marble4,
                playerLabel, cpu1Label, cpu2Label, cpu3Label,
                playerColor, cpu1Color, cpu2Color, cpu3Color,
                playerIcon, cpu1Icon, cpu2Icon, cpu3Icon,
                playButton, deSelectButton, discardButton,
                refreshButton, helpButton, exitButton, refreshButtonImg,
                currentPlayer, nextPlayer);

        tableImg.toBack();
        table.toBack();
        background.toBack();

        playShuffledMusic();

        primaryStage.setTitle("Jackaroo");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setPosition(Object o, double x, double y) {
        if (o instanceof Button) {
            Button b = (Button) o;
            b.setLayoutX(x);
            b.setLayoutY(y);
        }
        if (o instanceof ImageView) {
            ImageView img = (ImageView) o;
            img.setLayoutX(x);
            img.setLayoutY(y);
        }
    }

    private String showNameInputDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Enter Your Name");
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: deepskyblue; -fx-border-width: 3;");

        Label header = new Label("Welcome to Jackaroo");
        header.setTextFill(Color.WHITE);
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField input = new TextField("Player");
        input.setStyle("-fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 5;");
        input.setMaxWidth(200);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");

        final String[] nameResult = { "Player" };
        okButton.setOnAction(e -> {
            String name = input.getText().trim();
            if (!name.isEmpty()) {
                nameResult[0] = name;
            }
            dialog.close();
        });

        root.getChildren().addAll(header, input, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.setResizable(false);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                okButton.fire();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                dialog.close();
            }
        });

        dialog.showAndWait();
        return nameResult[0];
    }

    private void createTrack() {
        generateCircularTrack();
        addSafeZones();
    }

    private void generateCircularTrack() {
        for (int i = 0; i < 100; i++) {
            double angle = (Math.PI / 2) - (2 * Math.PI * ((i + 2) % 100) / 100.0);

            double x = CENTER_X + TRACK_RADIUS * Math.cos(angle);
            double y = CENTER_Y + TRACK_RADIUS * Math.sin(angle);

            Button cellButton = new Button();
            cellButton.setShape(new Circle(6));
            cellButton.setMinSize(12, 12);
            cellButton.setMaxSize(12, 12);
            cellButton.setId("Cell" + i);
            if (i % 25 == 0)
                cellButton.setStyle("-fx-background-color: gray; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
            else
                cellButton.setStyle("-fx-background-color: white; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
            cellButton.setLayoutX(x - 6);
            cellButton.setLayoutY(y - 6);

            pane.getChildren().add(cellButton);
            cellButton.setOnAction(e -> handleCellPress((Button) e.getSource()));
        }
    }

    private void addSafeZones() {
        int[] baseIndices = {0, 25, 50, 75};

        for (int p = 0; p < baseIndices.length; p++) {
            int index = baseIndices[p];
            double angle = (Math.PI / 2) - (2 * Math.PI * index / 100.0);

            for (int s = 1; s <= NUM_SAFE_CELLS; s++) {
                double r = TRACK_RADIUS - (s * SAFE_ZONE_STEP);
                double x = CENTER_X + r * Math.cos(angle);
                double y = CENTER_Y + r * Math.sin(angle);

                Button safeCell = new Button();
                safeCell.setShape(new Circle(6));
                safeCell.setMinSize(12, 12);
                safeCell.setMaxSize(12, 12);
                safeCell.setId("player" + (p + 1) + "SafeZone" + s);
                safeCell.setLayoutX(x - 6);
                safeCell.setLayoutY(y - 6);
                safeCell.setStyle("-fx-background-color: gray; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");


                Text label = new Text(x - 4, y + 4, String.valueOf(s));
                label.setFont(Font.font(9));
                label.setFill(Color.BLACK);

                pane.getChildren().addAll(safeCell, label);
                safeCell.setOnAction(e -> handleCellPress((Button) e.getSource()));
            }
        }
    }

    private void handleCellPress(Button button) {
        String id = button.getId();
        System.out.println("Pressed: " + id);

        if (id == null) return;

        try {
            if (id.startsWith("Cell")) {
                // Track cell
                int index = Integer.parseInt(id.substring(4));
                Cell cell = game.getBoard().getTrack().get(index);
                if (cell.getMarble() != null) {
                    game.selectMarble(cell.getMarble());
                    updateMarbleColorSelect(button);
                }

            } else if (id.contains("SafeZone")) {
                // Safe zone cell
                String[] parts = id.replace("player", "").split("SafeZone");
                int playerIndex = Integer.parseInt(parts[0]) - 1;
                int safeIndex = Integer.parseInt(parts[1]) - 1;

                Colour[] playerColors = new Colour[4];

                for (int i = 0; i < 4; i++) {
                    playerColors[i] = game.getPlayers().get(i).getColour();
                }

                Cell cell = game.getBoard().getSafeZonePublic(playerColors[playerIndex]).get(safeIndex);
                if (cell.getMarble() != null) {
                    game.selectMarble(cell.getMarble());
                    updateMarbleColorSelect(button);
                }

            } else if (id.contains("marble")) {
                // Home zone marble
                int playerIndex = Integer.parseInt(id.substring(6, 7)) - 1; // player1marble2 -> 0-based
                int marbleIndex = Integer.parseInt(id.substring(13)) - 1;

                game.selectMarble(game.getPlayers().get(playerIndex).getMarbles().get(marbleIndex));
                updateMarbleColorSelect(button);
            }

        } catch (Exception ex) {
            showErrorDialog("Error", ex.getMessage());
        }
    }


    private Color[] getPlayerColors() {
        Color[] playerColours = new Color[4];

        for (int i = 0; i < 4; i++) {
            if (game.getPlayers().get(i).getColour() == Colour.RED)
                playerColours[i] = Color.RED;
            else if (game.getPlayers().get(i).getColour() == Colour.BLUE)
                playerColours[i] = Color.BLUE;
            else if (game.getPlayers().get(i).getColour() == Colour.GREEN)
                playerColours[i] = Color.GREEN;
            else
                playerColours[i] = Color.YELLOW;
        }

        return playerColours;
    }

    private void marbleColors(Button marble1, Button marble2, Button marble3, Button marble4, int playerNumber, Color[] playerColors) {
        if (playerColors[playerNumber] == Color.RED) {
            marble1.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble2.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble3.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble4.setStyle("-fx-background-color: red; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        } else if (playerColors[playerNumber] == Color.BLUE) {
            marble1.setStyle("-fx-background-color: blue; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble2.setStyle("-fx-background-color: blue; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble3.setStyle("-fx-background-color: blue; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble4.setStyle("-fx-background-color: blue; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        } else if (playerColors[playerNumber] == Color.GREEN) {
            marble1.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble2.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble3.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble4.setStyle("-fx-background-color: green; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        } else {
            marble1.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble2.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble3.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
            marble4.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        }
    }

    private void updateMarbleColorSelect(Button marble) {
        String color = "";
        String style = marble.getStyle();
        if (style.contains("background-color")) {
            color = style.split("background-color:")[1].split(";")[0].trim();
        }


        if (color.equals("red")) {
            marble.setStyle("-fx-background-color: red; -fx-border-color: gold; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        } else if (color.equals("blue")) {
            marble.setStyle("-fx-background-color: blue; -fx-border-color: gold; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        } else if (color.equals("green")) {
            marble.setStyle("-fx-background-color: green; -fx-border-color: gold; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        } else {
            marble.setStyle("-fx-background-color: yellow; -fx-border-color: gold; -fx-border-width: 1.5; -fx-border-radius: 50; -fx-background-radius: 50;");
        }
    }

    private void updateTurnIndicators() {
        currentPlayer.setText("Current Player Turn: " + game.getActivePlayerColour().toString());
        if (currentPlayer.getText().equals("Current Player Turn: RED"))
            currentPlayer.setTextFill(Color.RED);
        else if (currentPlayer.getText().equals("Current Player Turn: BLUE"))
            currentPlayer.setTextFill(Color.BLUE);
        else if (currentPlayer.getText().equals("Current Player Turn: GREEN"))
            currentPlayer.setTextFill(Color.GREEN);
        else if (currentPlayer.getText().equals("Current Player Turn: YELLOW"))
            currentPlayer.setTextFill(Color.YELLOW);

        nextPlayer.setText("Next Player Turn: " + game.getNextPlayerColour().toString());
        if (nextPlayer.getText().equals("Next Player Turn: RED"))
            nextPlayer.setTextFill(Color.RED);
        else if (nextPlayer.getText().equals("Next Player Turn: BLUE"))
            nextPlayer.setTextFill(Color.BLUE);
        else if (nextPlayer.getText().equals("Next Player Turn: GREEN"))
            nextPlayer.setTextFill(Color.GREEN);
        else if (nextPlayer.getText().equals("Next Player Turn: YELLOW"))
            nextPlayer.setTextFill(Color.YELLOW);

        if (game.getPlayers().get(0).getColour() == game.getActivePlayerColour()) {
            URL soundUrl = getClass().getResource("/resources/dingSound.mp3");
            if (soundUrl != null) {
                AudioClip sound = new AudioClip(soundUrl.toExternalForm());
                sound.play();
            }
        }
    }

    private void updateMarblePositions() {
        // track
        for (int i = 0; i < game.getBoard().getTrack().size(); i++) {
            Cell cell = game.getBoard().getTrack().get(i);
            Button cellButton = (Button) pane.lookup("#Cell" + i);

            if (cellButton != null) {
                if (cell.getMarble() != null) {
                    cellButton.setStyle("-fx-background-color:" + returnColor(cell.getMarble().getColour()) + "; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
                } else if (i % 25 == 0)
                    cellButton.setStyle("-fx-background-color: grey; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
                else
                    cellButton.setStyle("-fx-background-color: white; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
            }
        }

        // safe zones
        Colour[] playerColors = new Colour[4];
        for (int i = 0; i < 4; i++) {
            playerColors[i] = game.getPlayers().get(i).getColour();
        }

        for (int p = 0; p < playerColors.length; p++) {
            ArrayList<Cell> safeZone = game.getBoard().getSafeZonePublic(playerColors[p]);

            for (int s = 0; s < safeZone.size(); s++) {
                Cell cell = safeZone.get(s);
                Button safeCellButton = (Button) pane.lookup("#player" + (p + 1) + "SafeZone" + (s + 1));

                if (safeCellButton != null) {
                    if (cell.getMarble() != null) {
                        safeCellButton.setStyle("-fx-background-color:" + returnColor(cell.getMarble().getColour()) + "; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
                    } else {
                        safeCellButton.setStyle("-fx-background-color: gray; -fx-background-radius: 50; -fx-border-color: black; -fx-border-radius: 50;");
                    }
                }
            }
        }

        // home zone
        double[][][] homePositions = {
                {{877, 432}, {908, 432}, {877, 463}, {908, 463}},  // player 1
                {{877, 192}, {908, 192}, {877, 223}, {908, 223}},  // player 2
                {{172, 192}, {203, 192}, {172, 223}, {203, 223}},  // player 3
                {{172, 432}, {203, 432}, {172, 463}, {203, 463}}   // player 4
        };

        for (int i = 0; i < game.getPlayers().size(); i++) {
            for (int j = 0; j < 4; j++) {
                String id = "#player" + (i + 1) + "marble" + (j + 1);
                Button marble = (Button) pane.lookup(id);

                if (marble != null) {
                    if (j < game.getPlayers().get(i).getMarbles().size()) {
                        // This marble is still in the home zone
                        double x = homePositions[i][j][0];
                        double y = homePositions[i][j][1];
                        marble.setLayoutX(x);
                        marble.setLayoutY(y);
                        marble.setVisible(true);
                        marble.setMouseTransparent(false);
                        marble.toFront();
                    } else {
                        // This marble has been fielded, hide it
                        marble.setVisible(false);
                        marble.setMouseTransparent(true);
                    }
                }
            }

            for (int j = 0; j < game.getPlayers().get(i).getMarbles().size(); j++) {
                String id = "#player" + (i + 1) + "marble" + (j + 1);
                Button marble = (Button) pane.lookup(id);

                if (marble != null) {
                    double x = homePositions[i][j][0];
                    double y = homePositions[i][j][1];

                    marble.setLayoutX(x);
                    marble.setLayoutY(y);

                    marble.setVisible(true);
                    marble.setMouseTransparent(false);

                    marble.toFront();
                }
            }
        }
    }

    public String returnColor(Colour color) {
        if (color == Colour.RED)
            return "red";
        else if (color == Colour.BLUE)
            return "blue";
        else if (color == Colour.GREEN)
            return "green";
        else
            return "yellow";
    }


    private void playCPUTurns() {
        SequentialTransition cpuTurns = new SequentialTransition();

        for (int i = 1; i <= 3; i++) {
            final int cpuIndex = i;

            PauseTransition delay = new PauseTransition(Duration.seconds(3.0));
            delay.setOnFinished(e -> {
                Player cpu = game.getPlayers().get(cpuIndex);
                if (game.canPlayTurn()) {
                    try {
                        cpu.play();
                    } catch (GameException ex) {
                    } finally {
                        game.endPlayerTurn();
                        System.out.println("CPU " + cpuIndex + " ended turn.");
                        System.out.println("Current player: " + game.getCurrentPlayer().getColour().toString());
                        System.out.println("Player hand size: " + game.getPlayers().get(0).getHand().size());
                    }
                } else {
                    game.endPlayerTurn();
                    System.out.println("CPU " + cpuIndex + " ended turn.");
                    System.out.println("Current player: " + game.getCurrentPlayer().getColour().toString());
                    System.out.println("Player hand size: " + game.getPlayers().get(0).getHand().size());
                }
                update();
            });

            cpuTurns.getChildren().add(delay);
        }

        cpuTurns.setOnFinished(e -> {
            update();
        });
        cpuTurns.play();
    }

    public void updatePlayerHand() {
        List<Card> hand = game.getPlayers().get(0).getHand();

        clearCardImages();

        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            ImageView cardImageView = getCardImageView(i);

            if (card != null) {
                if (cardImageView != null) {
                    Image cardImage = loadCardImage(card);
                    cardImageView.setImage(cardImage);

                    try {
                        Tooltip tooltip = new Tooltip(card.getName() + "\n" + card.getDescription());
                        Tooltip.install(getCardButton(i), tooltip);
                    } catch (NullPointerException ignored) {
                    }
                }
            }
        }


        if (game.getPlayers().get(0).getHand().size() == 4) {
            for (int j = 0; j < 4; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(true);
                card.setMouseTransparent(false);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(true);
            }
        } else if (game.getPlayers().get(0).getHand().size() == 3) {
            for (int j = 0; j < 3; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(true);
                card.setMouseTransparent(false);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(true);
            }
            for (int j = 3; j < 4; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(false);
                card.setMouseTransparent(true);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(false);
            }
        } else if (game.getPlayers().get(0).getHand().size() == 2) {
            for (int j = 0; j < 2; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(true);
                card.setMouseTransparent(false);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(true);
            }
            for (int j = 2; j < 4; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(false);
                card.setMouseTransparent(true);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(false);
            }
        } else if (game.getPlayers().get(0).getHand().size() == 1) {
            for (int j = 0; j < 1; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(true);
                card.setMouseTransparent(false);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(true);
            }
            for (int j = 1; j < 4; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(false);
                card.setMouseTransparent(true);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(false);
            }
        } else if (game.getPlayers().get(0).getHand().isEmpty()) {
            for (int j = 0; j < 4; j++) {
                String id = "#player1" + "card" + (j + 1);
                Button card = (Button) pane.lookup(id);

                card.setVisible(false);
                card.setMouseTransparent(true);

                ImageView cardImg = (ImageView) pane.lookup(id + "img");
                cardImg.setVisible(false);
            }
        }
    }

    private Button getCardButton(int index) {
        switch (index) {
            case 0:
                return player1card1;
            case 1:
                return player1card2;
            case 2:
                return player1card3;
            case 3:
                return player1card4;
            default:
                return null;
        }
    }

    private Image loadCardImage(Card card) {
        String fileName = getCardImagePath(card);
        InputStream stream = getClass().getResourceAsStream("cards/" + fileName);

        if (stream != null) {
            return new Image(stream);
        } else {
            return createPlaceholderImage(card.getName(), getSuitName(card));
        }
    }

    private Image createPlaceholderImage(String cardName, String suitName) {
        int width = 100;
        int height = 150;
        WritableImage placeholder = new WritableImage(width, height);
        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, height);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 14));
        gc.fillText(cardName, 10, 70);
        gc.fillText(suitName, 10, 90);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        canvas.snapshot(params, placeholder);

        return placeholder;
    }

    private String getSuitName(Card card) {
        if (card instanceof Standard) {
            Standard standardCard = (Standard) card;
            return standardCard.getSuit().toString().toLowerCase();
        }
        return "";
    }

    private String getCardImagePath(Card card) {
        if (card instanceof Standard) {
            Standard standardCard = (Standard) card;
            String rank = getRankName(standardCard.getRank());
            String suit = standardCard.getSuit().toString().toLowerCase();
            return rank + "_of_" + suit + "s.png";
        } else if (card instanceof Burner) {
            return "burner.png";
        } else if (card instanceof Saver) {
            return "saver.png";
        }
        return "card_back.png";
    }


    private String getRankName(int rank) {
        switch (rank) {
            case 1:
                return "ace";
            case 11:
                return "jack";
            case 12:
                return "queen";
            case 13:
                return "king";
            default:
                return String.valueOf(rank);
        }
    }

    private ImageView getCardImageView(int index) {
        switch (index) {
            case 0:
                return player1card1img;
            case 1:
                return player1card2img;
            case 2:
                return player1card3img;
            case 3:
                return player1card4img;
            default:
                return null;
        }
    }

    private void clearCardImages() {
        player1card1img.setImage(null);
        player1card2img.setImage(null);
        player1card3img.setImage(null);
        player1card4img.setImage(null);
    }

    private void updateFirepitDisplay() {
        if (!game.getFirePit().isEmpty()) {
            Card topCard = game.getFirePit().get(game.getFirePit().size() - 1);
            Image firepitImage = loadCardImage(topCard);
            if (firepitImage != null) {
                firepitImg.setImage(firepitImage);
            }

            URL soundUrl = getClass().getResource("/resources/cardSound.mp3");
            if (soundUrl != null) {
                AudioClip sound = new AudioClip(soundUrl.toExternalForm());
                sound.play();
            }
        }
    }

    private boolean checkIfMustDiscard(Player player) {
        Board board = game.getBoard();
        ArrayList<Card> hand = player.getHand();
        ArrayList<Marble> marbles = board.getActionableMarbles();

        ArrayList<ArrayList<Marble>> subsets = generateMarbleSubsets(marbles);

        for (Card card : hand)
            for (ArrayList<Marble> subset : subsets)
                if (card.validateMarbleSize(subset) && card.validateMarbleColours(subset))
                    return false;

        return true;
    }

    private ArrayList<ArrayList<Marble>> generateMarbleSubsets(ArrayList<Marble> marbles) {
        ArrayList<ArrayList<Marble>> subsets = new ArrayList<>();

        subsets.add(new ArrayList<>());

        for (Marble m : marbles) {
            ArrayList<Marble> single = new ArrayList<>();
            single.add(m);
            subsets.add(single);
        }

        for (int i = 0; i < marbles.size(); i++) {
            for (int j = i + 1; j < marbles.size(); j++) {
                ArrayList<Marble> pair = new ArrayList<>();
                pair.add(marbles.get(i));
                pair.add(marbles.get(j));
                subsets.add(pair);
            }
        }

        return subsets;
    }

    private void showSplitDistanceDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Split Distance");
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: #2196f3; -fx-border-width: 3;");

        Label headerLabel = new Label("Enter split distance (1–6)");
        headerLabel.setTextFill(Color.WHITE);
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField inputField = new TextField();
        inputField.setPromptText("Distance for first marble");
        inputField.setText("1");
        inputField.setMaxWidth(200);
        inputField.setStyle("-fx-background-radius: 10; -fx-padding: 5;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #b00020; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");

        buttonBox.getChildren().addAll(okButton, cancelButton);
        root.getChildren().addAll(headerLabel, inputField, buttonBox);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.setResizable(false);

        // ENTER = OK, ESC = Cancel
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                okButton.fire();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                resetCardPositions();
                dialog.close();
            }
        });

        okButton.setOnAction(e -> {
            String input = inputField.getText().trim();
            try {
                int splitDistance = Integer.parseInt(input);
                game.editSplitDistance(splitDistance);
                dialog.close();
            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid Input", "Please enter a number between 1 and 6");
            } catch (SplitOutOfRangeException ex) {
                showErrorDialog("Invalid Input", "Please enter a number between 1 and 6");
            }
        });

        cancelButton.setOnAction(e -> {
            game.deselectAll();
            updateMarblePositions();
            resetCardPositions();
            dialog.close();
        });

        dialog.showAndWait();
    }

    private void resetCardPositions() {
        setPosition(player1card1, 285, 530);
        setPosition(player1card2, 415, 530);
        setPosition(player1card3, 545, 530);
        setPosition(player1card4, 675, 530);

        setPosition(player1card1img, 285, 530);
        setPosition(player1card2img, 415, 530);
        setPosition(player1card3img, 545, 530);
        setPosition(player1card4img, 675, 530);
    }

    private void updateCPUHand() {
        for (int i = 1; i < 4; i++) {
            if (game.getPlayers().get(i).getHand().size() == 4) {
                for (int j = 0; j < 4; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(true);
                    card.setMouseTransparent(false);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(true);
                }
            } else if (game.getPlayers().get(i).getHand().size() == 3) {
                for (int j = 0; j < 3; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(true);
                    card.setMouseTransparent(false);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(true);
                }
                for (int j = 3; j < 4; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(false);
                    card.setMouseTransparent(true);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(false);
                }
            } else if (game.getPlayers().get(i).getHand().size() == 2) {
                for (int j = 0; j < 2; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(true);
                    card.setMouseTransparent(false);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(true);
                }
                for (int j = 2; j < 4; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(false);
                    card.setMouseTransparent(true);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(false);
                }
            } else if (game.getPlayers().get(i).getHand().size() == 1) {
                for (int j = 0; j < 1; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(true);
                    card.setMouseTransparent(false);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(true);
                }
                for (int j = 1; j < 4; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(false);
                    card.setMouseTransparent(true);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(false);
                }
            } else if (game.getPlayers().get(i).getHand().isEmpty()) {
                for (int j = 0; j < 4; j++) {
                    String id = "#player" + (i + 1) + "card" + (j + 1);
                    Button card = (Button) pane.lookup(id);

                    card.setVisible(false);
                    card.setMouseTransparent(true);

                    ImageView cardImg = (ImageView) pane.lookup(id + "img");
                    cardImg.setVisible(false);
                }
            }
        }
    }

    private void updateTrapActivation() {
        if (isTrapActivated()) {
            showTrapActivatedPopup(trapActivated.toString() + " has activated a trap!");
        }
    }

    private void showTrapActivatedPopup(String message) {

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        if (trapActivated == Colour.RED)
            root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: red; -fx-border-width: 3;");
        else if (trapActivated == Colour.BLUE)
            root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: blue; -fx-border-width: 3;");
        else if (trapActivated == Colour.GREEN)
            root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: green; -fx-border-width: 3;");
        else if (trapActivated == Colour.YELLOW)
            root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: yellow; -fx-border-width: 3;");

        Label msgLabel = new Label(message);
        msgLabel.setTextFill(Color.WHITE);
        msgLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        msgLabel.setWrapText(true);
        msgLabel.setAlignment(Pos.CENTER);
        msgLabel.setTextAlignment(TextAlignment.CENTER);

        Button okButton = new Button("Continue");
        okButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");
        okButton.setOnAction(e -> popupStage.close());

        root.getChildren().addAll(msgLabel, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.setResizable(false);

        URL soundUrl = getClass().getResource("/resources/trapSound.mp3");
        if (soundUrl != null) {
            AudioClip sound = new AudioClip(soundUrl.toExternalForm());
            sound.play();
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                popupStage.close();
            }
        });

        trapActivated = null;
        popupStage.showAndWait();
    }

    private void showErrorDialog(String title, String message) {

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle(title);
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: red; -fx-border-width: 3;");

        Label msgLabel = new Label(message);
        msgLabel.setTextFill(Color.WHITE);
        msgLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        msgLabel.setWrapText(true);
        msgLabel.setAlignment(Pos.CENTER);
        msgLabel.setTextAlignment(TextAlignment.CENTER);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #b00020; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");
        okButton.setOnAction(e -> dialog.close());

        root.getChildren().addAll(msgLabel, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.setResizable(false);

        URL soundUrl = getClass().getResource("/resources/errorSound.mp3");
        if (soundUrl != null) {
            AudioClip sound = new AudioClip(soundUrl.toExternalForm());
            sound.play();
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                dialog.close();
            }
        });

        dialog.showAndWait();
    }

    private void showShortcutsDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Keyboard Shortcuts");
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: deepskyblue; -fx-border-width: 3;");

        Label title = new Label("Keyboard Shortcuts");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label play = new Label("Play: Enter");
        Label deselect = new Label("Deselect: Esc");
        Label discard = new Label("Discard: Backspace");

        Label refresh = new Label("If the game freezes, press the green refresh button.");
        refresh.setStyle("-fx-font-style: italic; -fx-font-size: 13px; -fx-text-fill: lightgray;");
        refresh.setWrapText(true);
        refresh.setTextAlignment(TextAlignment.CENTER);
        refresh.setAlignment(Pos.CENTER);

        Label tip = new Label("Tip: Hover over your cards to find out what they do.");
        tip.setStyle("-fx-font-style: italic; -fx-font-size: 13px; -fx-text-fill: lightgray;");
        tip.setWrapText(true);
        tip.setTextAlignment(TextAlignment.CENTER);
        tip.setAlignment(Pos.CENTER);

        for (Label label : new Label[]{play, deselect, discard}) {
            label.setTextFill(Color.WHITE);
            label.setStyle("-fx-font-size: 16px;");
        }

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-color: #1e90ff; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");
        okButton.setOnAction(e -> dialog.close());

        root.getChildren().addAll(title, play, deselect, discard, refresh, tip, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.setResizable(false);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                dialog.close();
            }
        });

        dialog.showAndWait();
    }


    private void update() {
        Platform.runLater(() -> {
            updatePlayerHand();
            updateMarblePositions();
            updateTurnIndicators();
            updateFirepitDisplay();
            updateCPUHand();
            updateTrapActivation();
        });

        if (game.getPlayers().get(0).getColour() == game.getActivePlayerColour() && !game.canPlayTurn()) {
            game.endPlayerTurn();
            playCPUTurns();
        }

        Colour winner = game.checkWin();
        if (winner != null) {
            showWinner(winner);
        }
    }

    private void showWinner(Colour winner) {
        currentMediaPlayer.stop();
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Game Over");
        dialog.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: rgba(30, 30, 30, 0.95); -fx-background-radius: 20; -fx-border-radius: 20; -fx-border-color: gold; -fx-border-width: 3;");

        Label msgLabel = new Label(winner.toString() + " player has won the game!");
        msgLabel.setTextFill(Color.WHITE);
        msgLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        msgLabel.setWrapText(true);
        msgLabel.setAlignment(Pos.CENTER);
        msgLabel.setTextAlignment(TextAlignment.CENTER);

        Button okButton = new Button("Exit");
        okButton.setStyle("-fx-background-color: #228B22; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");
        okButton.setOnAction(e -> {
            dialog.close();
            Platform.exit();
        });

        root.getChildren().addAll(msgLabel, okButton);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.setResizable(false);

        URL soundUrl = getClass().getResource("/resources/victory.mp3");
        if (soundUrl != null) {
            AudioClip sound = new AudioClip(soundUrl.toExternalForm());
            sound.play();
        }

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                dialog.close();
                Platform.exit();
            }
        });

        dialog.showAndWait();
    }

    private void playShuffledMusic() {
        Collections.shuffle(trackFiles); // shuffle playlist

        playNextTrack(new LinkedList<>(trackFiles)); // pass as queue
    }

    private void playNextTrack(Queue<String> trackQueue) {
        if (trackQueue.isEmpty()) {
            playShuffledMusic(); // restart with reshuffle
            return;
        }

        String nextPath = trackQueue.poll();
        URL soundURL = getClass().getResource(nextPath);
        if (soundURL == null) {
            playNextTrack(trackQueue); // skip missing file
            return;
        }

        Media media = new Media(soundURL.toExternalForm());
        currentMediaPlayer = new MediaPlayer(media);
        currentMediaPlayer.setOnEndOfMedia(() -> playNextTrack(trackQueue));
        currentMediaPlayer.play();
    }

    private void forceField() {
        if (game.getActivePlayerColour() == game.getPlayers().get(0).getColour()) {
            Ace ace = new Ace("Ace", "Fields a marble from the Home Zone or Moves one of your own marbles 1 step forward.",Suit.SPADE,game.getBoard(),(GameManager)game);
            update();

            game.getPlayers().get(0).getHand().remove(0);
            game.getPlayers().get(0).getHand().add(0,ace);

            try {
                game.selectCard(game.getPlayers().get(0).getHand().get(0));
                game.playPlayerTurn();
                game.endPlayerTurn();
                game.deselectAll();
                marbleColors(player1marble1, player1marble2, player1marble3, player1marble4, 0, getPlayerColors());

                resetCardPositions();

                update();
                playCPUTurns();
            } catch (GameException ex) {
                showErrorDialog("Error", ex.getMessage());
            }
        }

    }
}
