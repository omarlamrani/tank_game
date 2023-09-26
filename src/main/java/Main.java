import engine.*;
import engine.components.ImageRenderComponent;
import engine.components.controllers.TankAIController;
import engine.components.controllers.TankKeyboardController;
import engine.components.controllers.TurretMouseController;
import engine.handler.ClientHandler;
import engine.handler.Handler;
import engine.network.stream.Server;
import engine.wrappers.ShadowWrapper;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import objects.RenderComponents.TurretRenderComponent;
import objects.grid.CheckWallType;
import objects.grid.Landmine;
import objects.health.HealthComponent;
import objects.minimap.Minimap;
import objects.tank.Tank;
import objects.tank.TankMovementComponent;
import objects.turret.CannonTurret;
import objects.turret.LaserTurret;

import java.io.File;


public class Main extends Application {

    static String mode = "single";
    private GameLoop gLoop;
    private RenderLoop rLoop;
    public Handler handler;
    private Scene scene;
    private GraphicsContext gc;
    private Server server;
    public boolean muteStatus = false;
    public String turretType = "turret";
    private StackPane layout = new StackPane();
    String mapChoiceSelected = "AI Maze";



    public static void main(String[] args) {
        if (args.length > 0) mode = args[0];
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TANK DESTROYER: " + mode);


        //These number are weird to keep the 1280 x 720 canvas fully in frame, someone who knows more about JFX layout can have a look when doing UI.
        primaryStage.setWidth(1300);
        primaryStage.setHeight(760);

        Canvas canvas = new Canvas(1280, 720);
        gc = canvas.getGraphicsContext2D();

//        Backing Track
        Sound backingtrack = new Sound("src/main/resources/sounds/backingtrack.mp3");
        backingtrack.play();
        backingtrack.setVolume(0.5);

        /* Used this Pane for simple Start Menu might have to choose another one
        for a more complex menu */
        //StackPane layout = new StackPane();
        layout.getStyleClass().addAll("menu");
        layout.getStylesheets().add("file:src/main/resources/stylesheet.css");

        //Header for Game
        Label heading = new Label("TANK DESTROYER");
        heading.getStyleClass().addAll("heading");

        //Clicking Sound
        String path = "src/main/resources/sounds/clickingsound.mp3"; //sound location
        SoundFX click = new SoundFX(path);

        //Tank Image being added and Effect for colour changes
        Image tankImg = new Image("fulltankimage.png");
        ImageView tankImgView = new ImageView(tankImg);
        ShadowWrapper effect = new ShadowWrapper(10.F, Color.BLACK);

//        initializers
        TankMovementComponent.SPEED = 3;
        HealthComponent.maxLives = 1 ;


        //START GAME BUTTON
        Button startGame = new Button();
        startGame.setText("START GAME");
//        Button gameMode = new Button();
//        gameMode.setText("GAME MODE");

        //Options Button
        Button options = new Button();
        //options.setText("OPTIONS");
        Image imageCog = new Image("file:src/main/resources/icons/cog64.png");
        options.setGraphic(new ImageView(imageCog));

        //How to play Button
        Button howToPlay = new Button();
        howToPlay.setText("HOW TO PLAY?");
        //howToPlay.setPrefSize(10, 10);

// Label of Lives Left
//        Label livesChoice = new Label (String.valueOf(HealthComponent.lives));
//        livesChoice.getStyleClass().addAll("normalLabel");


        //Exit Game Button
        Button exitBtn = new Button();
        exitBtn.setText("EXIT GAME");

        TextField ipBox = new TextField();
        ipBox.setPromptText("Enter server IP.");
        ipBox.setPrefColumnCount(1);

        //Mute Button
        Button mute = new Button();
        mute.setPrefSize(40, 40);
        Image imageMute = new Image("file:src/main/resources/icons/not_mute80.png");
        Image imageMuteSmall = new Image("file:src/main/resources/icons/not_mute32.png");
        mute.setGraphic(new ImageView(imageMute));

        //Unmute Button
        Button unmute = new Button();
        unmute.setPrefSize(40, 40);
        Image imageUnmute = new Image("file:src/main/resources/icons/mute80.png");
        Image imageUnmuteSmall = new Image("file:src/main/resources/icons/mute32.png");
        unmute.setGraphic(new ImageView(imageUnmute));

        //Restart Button
        Button returnBtn = new Button();
        returnBtn.setPrefSize(70, 32);
        Image imageClose = new Image("file:src/main/resources/icons/close.png");
        returnBtn.setGraphic(new ImageView(imageClose));

        //COLOUR SLIDER
        Label hueLabel = new Label("0f");
        Slider colSlider = new Slider(-1f, 1f, 0f);
        colSlider.setBlockIncrement(0.1f);
        colSlider.setMaxWidth(750);

        // Listens to change in values for Slider
        colSlider.valueProperty().addListener(new ChangeListener<>() {
            public void changed(ObservableValue<? extends Number> observable, Number prevValue, Number newValue) {
                hueLabel.setText("" + newValue);
                float f = Float.parseFloat(hueLabel.getText());
                Float finalHueValue = f;
                //System.out.println("*** F equals ***" + f);
                effect.setInput(new ColorAdjust(f, 0, 0, 0)); //new effect overlay for the new Hue Value Selected in the slider
                tankImgView.setEffect(effect); //adds the slider colour to that of the tank
            }
        });

        //aligning and adding Objects to layout
        layout.getChildren().addAll(canvas, startGame, heading, colSlider,
                tankImgView, options, exitBtn, mute, howToPlay);

        StackPane.setAlignment(mute, Pos.TOP_RIGHT);
        StackPane.setAlignment(options, Pos.TOP_LEFT);
        StackPane.setAlignment(exitBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(exitBtn, new Insets(250));
        StackPane.setAlignment(startGame, Pos.CENTER);
        StackPane.setAlignment(heading, Pos.TOP_CENTER);
        StackPane.setAlignment(colSlider, Pos.BOTTOM_CENTER);
        StackPane.setMargin(colSlider, new Insets(30));  //margin for slider off the bottom of screen
        StackPane.setAlignment(tankImgView, Pos.BOTTOM_CENTER);
        StackPane.setMargin(tankImgView, new Insets(140));  //margin for tank image
        StackPane.setAlignment(howToPlay, Pos.CENTER_LEFT);
        StackPane.setMargin(howToPlay, new Insets(190));
        //StackPane.setMargin(colSlider, new Insets(30));

        scene = new Scene(layout, 1280, 720);
        //handler = new Handler(gc, scene, TEMP_BLOCK_MAP);

        //RETURN TO MENU
        Button returnMenu = new Button("RETURN TO MENU");
        returnMenu.setOnAction(e1 -> {
            click.play();
            Canvas tempCanvas = new Canvas(1280, 720);
            gc = tempCanvas.getGraphicsContext2D();
            layout.getChildren().clear();

            if (muteStatus) {
                layout.getChildren().addAll(tempCanvas, startGame, heading, colSlider,
                        tankImgView, options, exitBtn, unmute, howToPlay);
            } else {
                layout.getChildren().addAll(tempCanvas, startGame, heading, colSlider,
                        tankImgView, options, exitBtn, mute, howToPlay);
            }

            System.out.println(colSlider.getValue());

            ShadowWrapper n = new ShadowWrapper(10F, Color.BLACK);
            n.setInput(new ColorAdjust(colSlider.getValue(), 0, 0, 0));
            tankImgView.setEffect(n);

        });

        returnBtn.setOnAction(e -> {
            try {
                rLoop.stop();
                gLoop.stop();
                stop();

                if (server != null) server.stopServer();
                System.out.println("Returning");
                if (handler instanceof ClientHandler) ((ClientHandler) handler).stop();
                layout.getChildren().clear();
                Canvas tempCanvas = new Canvas(1280, 720);
                gc = tempCanvas.getGraphicsContext2D();

                if (muteStatus) {
                    layout.getChildren().addAll(tempCanvas, startGame, heading, colSlider,
                            tankImgView, options, exitBtn, unmute, howToPlay);
                } else {
                    layout.getChildren().addAll(tempCanvas, startGame, heading, colSlider,
                            tankImgView, options, exitBtn, mute, howToPlay);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        //START GAME BUTTON
        startGame.setOnAction(e -> {


            click.play(); //plays sound

            mute.setGraphic(new ImageView(imageMuteSmall));
            unmute.setGraphic(new ImageView(imageUnmuteSmall));

            //gets the value of the slider for when the button is pressed
            String mystr = hueLabel.getText();
            Float finalHueValue = Float.parseFloat(mystr);

            //removes menu items to allow game to begin
            layout.getChildren().removeAll(startGame, heading, colSlider, tankImgView,
                    options, exitBtn, howToPlay);

            Button singlePlayer = new Button();
            singlePlayer.setText("SINGLE PLAYER");

            Button hostServer = new Button();
            hostServer.setText("HOST SERVER");

            Button joinServer = new Button();
            joinServer.setText("JOIN SERVER");

            Button connect = new Button();
            connect.setText("Connect");


            singlePlayer.setOnAction(a -> {
                click.play();
                layout.getChildren().removeAll(singlePlayer, hostServer, joinServer, returnMenu);
                layout.getChildren().add(returnBtn);
                startSingle(primaryStage, finalHueValue);
            });

            hostServer.setOnAction(a -> {
                click.play();
                layout.getChildren().removeAll(singlePlayer, hostServer, joinServer, returnMenu);
                layout.getChildren().add(returnBtn);

                startSingle(primaryStage, finalHueValue);
                startServer();
            });

            joinServer.setOnAction(a -> {
                click.play();
                layout.getChildren().removeAll(singlePlayer, hostServer, joinServer);
                layout.getChildren().addAll(connect, ipBox);
            });

            connect.setOnAction(a -> {
                click.play();
                layout.getChildren().removeAll(singlePlayer, hostServer, joinServer, returnMenu, connect, ipBox);
                layout.getChildren().add(returnBtn);

                System.out.println("check");
                startClient(primaryStage, finalHueValue, ipBox.getText());
                System.out.println("check2");
            });

            layout.getChildren().addAll(returnMenu, singlePlayer, hostServer, joinServer);

            StackPane.setAlignment(returnBtn, Pos.TOP_LEFT);
            StackPane.setAlignment(returnMenu, Pos.TOP_LEFT);
            StackPane.setAlignment(singlePlayer, Pos.CENTER);
            StackPane.setAlignment(ipBox, Pos.TOP_CENTER);
            StackPane.setMargin(singlePlayer, new Insets(0,0,200,0));
            StackPane.setMargin(ipBox, new Insets(290, 525 , 0, 525));
            StackPane.setAlignment(hostServer, Pos.CENTER);
            StackPane.setAlignment(joinServer, Pos.CENTER);
            StackPane.setMargin(joinServer, new Insets(200,0,0,0));
//          StackPane.setAlignment(aiLivesChoice, Pos.BOTTOM_RIGHT);
//            //StackPane.setAlignment(livesChoice, Pos.BOTTOM_RIGHT);

            /*
            if (mode.equals("client")) {
                startClient(primaryStage, finalHueValue);
            } else {
                startSingle(primaryStage, finalHueValue);
                if (mode.equals("server")) {
                    startServer();
                }
            }*/
            //startGameState(primaryStage, finalHueValue);
        });


        //EXIT BUTTON
        exitBtn.setOnAction(e -> {
            System.exit(0);
        });

        //OPTIONS SCREEN
        options.setOnAction(e -> {
            click.play();

            layout.getChildren().removeAll(canvas, startGame, heading, colSlider,
                    tankImgView, options, exitBtn, mute, returnBtn, howToPlay);

//            slider for speed
            Label speedLabel = new Label("TANK SPEED");
            speedLabel.getStyleClass().addAll("normalLabel");
            Label speedLabel2 = new Label("3");

            Slider speedSlider = new Slider(1, 5, 3);
            speedSlider.setMaxWidth(750);

            speedSlider.valueProperty().addListener(new ChangeListener<>() {
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    speedLabel2.setText("" + t1);
                    float f = Float.parseFloat(speedLabel2.getText());
                    TankMovementComponent.SPEED = f;
                }
            });

            //DropDown Tank Turret Choice#
            Label selectTurret = new Label("SELECT TURRET");
            selectTurret.getStyleClass().addAll("normalLabel");

            ChoiceBox<String> tankChoice = new ChoiceBox<String>();
            tankChoice.getStyleClass().addAll("button");
            tankChoice.getItems().addAll("Classic", "Dual Wielded", "Laser Turret", "Cannon");
            tankChoice.setValue("Classic");
            TurretRenderComponent.turret = new Image("file:src/main/resources/turrets/turret.png");

            tankChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String prevValue, String newValue) {
                    if (tankChoice.getValue() == "Classic") {
                        TurretRenderComponent.turret = new Image("file:src/main/resources/turrets/turret.png");
                    } else if (tankChoice.getValue() == "Dual Wielded") {
                        TurretRenderComponent.turret = new Image("file:src/main/resources/turrets/dualwieldedturret.png");
                    } else if (tankChoice.getValue() == "Laser Turret") {
                        TurretRenderComponent.turret = new Image("file:src/main/resources/turrets/blasterturret.png");
                        turretType = "laser";
                    } else if (tankChoice.getValue() == "Cannon") {
                        TurretRenderComponent.turret = new Image("file:src/main/resources/turrets/cannonturret.png");
                        turretType = "cannon";
                    }

                }
            });

            //DropDown Select Map Choice#
            Label selectMap = new Label("SELECT MAP");
            selectMap.getStyleClass().addAll("normalLabel");

            ChoiceBox<String> mapChoice = new ChoiceBox<String>();
            mapChoice.getStyleClass().addAll("button");
            mapChoice.getItems().addAll("AI Maze", "Open", "Simple Maze");
            mapChoice.setValue("AI Maze");
            mapChoiceSelected=("AI Maze");
            mapChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observable, String prevValue, String newValue) {
                    mapChoiceSelected= newValue;
                }
            });

            //DropDown Lives to select how many lives AI will have.
            Label setTankLives = new Label("SET TANK LIVES");
            setTankLives.getStyleClass().addAll("normalLabel");

            ChoiceBox<Integer> numOfLives = new ChoiceBox<Integer>();
            numOfLives.getStyleClass().addAll("button");
            numOfLives.getItems().add(1);
            numOfLives.getItems().add(2);
            numOfLives.getItems().add(3);
            numOfLives.setValue(1);

            numOfLives.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
                public void changed(ObservableValue<? extends Integer> observable, Integer prevValue, Integer newValue) {
                    HealthComponent.maxLives = numOfLives.getValue();
//                   livesChoice.setText(String.valueOf(Tank.aiLives));
                }
            });

//           slider for sound
            Label volumeLabel = new Label("GAME VOLUME");
            volumeLabel.getStyleClass().addAll("normalLabel");
            Label volumeLabel2 = new Label("0.5");

            Slider volumeSlider = new Slider(0, 1, backingtrack.getVolume());
            volumeSlider.setBlockIncrement(1.0f);
            volumeSlider.setMaxWidth(750);
            Double soundVol = 0.5;
            volumeSlider.valueProperty().addListener(new ChangeListener<>() {
                public void changed(ObservableValue<? extends Number> observable, Number prevValue, Number newValue) {
                    backingtrack.setVolume(volumeSlider.getValue());
                    volumeLabel2.setText("" + newValue);
                }
            });

            layout.getChildren().addAll(returnMenu, speedLabel, speedSlider, volumeLabel, volumeSlider, tankChoice, numOfLives, setTankLives, selectTurret, selectMap, mapChoice);

            StackPane.setAlignment(returnMenu, Pos.TOP_RIGHT);

            StackPane.setAlignment(selectMap, Pos.TOP_LEFT);
            StackPane.setMargin(selectMap, new Insets(100, 0 , 0, 100));
            StackPane.setAlignment(mapChoice, Pos.TOP_RIGHT);
            StackPane.setMargin(mapChoice, new Insets(100,300 , 0, 0));

            StackPane.setAlignment(setTankLives, Pos.TOP_LEFT);
            StackPane.setMargin(setTankLives, new Insets(190, 0 , 0, 100));
            StackPane.setAlignment(numOfLives, Pos.TOP_RIGHT);
            StackPane.setMargin(numOfLives, new Insets(190,300 , 0, 0));

            StackPane.setAlignment(selectTurret, Pos.TOP_LEFT);
            StackPane.setMargin(selectTurret, new Insets(280,0 , 0, 100));
            StackPane.setAlignment(tankChoice, Pos.TOP_RIGHT);
            StackPane.setMargin(tankChoice, new Insets(280, 300 , 0 ,0 ));

            StackPane.setAlignment(speedLabel, Pos.TOP_LEFT);
            StackPane.setMargin(speedLabel, new Insets(400, 0 , 0 ,100 ));
            StackPane.setAlignment(speedSlider, Pos.TOP_RIGHT);
            StackPane.setMargin(speedSlider, new Insets(375, 100 , 0 ,0 ));

            StackPane.setAlignment(volumeLabel, Pos.TOP_LEFT);
            StackPane.setMargin(volumeLabel, new Insets(550, 0 , 0 ,100 ));
            StackPane.setAlignment(volumeSlider, Pos.TOP_RIGHT);
            StackPane.setMargin(volumeSlider, new Insets(550,100, 0 , 0));
        });

        //HOW TO PLAY SCREEN
        howToPlay.setOnAction(e -> {
            click.play();

            //Removed as we can't seem to remove it when going back to main menu.
            //Can added again when fix found.

            //Image ground = ImageSheet.getSubImage(new File("src/main/resources/terrain.png"), 0, 384, 128, 128);
            //BackgroundImage background = new BackgroundImage(ground,
            //        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
            //        BackgroundSize.DEFAULT);
            //layout.setBackground(new Background(background));

            layout.getChildren().removeAll(canvas, startGame, heading, colSlider,
                    tankImgView, options, exitBtn, mute, returnBtn, howToPlay);
            layout.getChildren().clear();


            Label howToPlayHeading = new Label("How To Play?");
            howToPlayHeading.getStyleClass().addAll("heading");

            Label howToMoveLabel = new Label("Move your tank around using the WASD keys\n" +
                    "The W and S keys will move you forwards and backwards\n" +
                    "The A and D keys will rotate your tank");

            Label howToRotateLabel = new Label("Rotate the turret of your tank with your mouse \n" +
                    "Aim at other tanks and prepare to fire");

            Label howToFireLabel = new Label("Once locked on to the enemy... FIRE!\n" +
                    "(using the left mouse button)");

            howToFireLabel.getStyleClass().addAll("normalText");
            howToMoveLabel.getStyleClass().addAll("normalText");
            howToRotateLabel.getStyleClass().addAll("normalText");
            howToPlayHeading.getStyleClass().addAll("normalLabel");

            Image howToMoveImg = new Image("file:src/main/resources/howTos/howToMove.png");
            ImageView howToMoveImgView = new ImageView(howToMoveImg);

            Image howToRotateImg = new Image("file:src/main/resources/howTos/howToRotate.png");
            ImageView howToRotateImgView = new ImageView(howToRotateImg);

            Image enemyTankImg = new Image("file:src/main/resources/fullTankImage.png");
            ImageView enemyTankImgView = new ImageView(enemyTankImg);
            ShadowWrapper hue = new ShadowWrapper(10.F, Color.BLACK);
            effect.setInput(new ColorAdjust(150, 150, 150, 150));
            enemyTankImgView.setEffect(hue);
            ImageView enemyTankImgView2 = new ImageView(enemyTankImg);

            Image howToFireImg = new Image("file:src/main/resources/howTos/howToFire.png");
            ImageView howToFireImgView = new ImageView(howToFireImg);

            layout.getChildren().addAll(returnMenu, howToPlayHeading, howToMoveImgView, howToMoveLabel, howToRotateImgView, howToRotateLabel, enemyTankImgView, howToFireLabel, howToFireImgView, enemyTankImgView2);

            StackPane.setAlignment(returnMenu, Pos.TOP_RIGHT);
            StackPane.setAlignment(howToPlayHeading, Pos.TOP_CENTER);
            StackPane.setAlignment(howToMoveImgView, Pos.TOP_RIGHT);
            StackPane.setMargin(howToMoveImgView, new Insets(130, 250, 0, 0));
            StackPane.setAlignment(howToMoveLabel, Pos.TOP_LEFT);
            StackPane.setMargin(howToMoveLabel, new Insets(180));
            StackPane.setAlignment(howToRotateImgView, Pos.CENTER_LEFT);
            StackPane.setMargin(howToRotateImgView, new Insets(0, 0, 0, 180));
            StackPane.setAlignment(howToRotateLabel, Pos.CENTER_RIGHT);
            StackPane.setMargin(howToRotateLabel, new Insets(100, 180, 0, 0));
            StackPane.setAlignment(enemyTankImgView, Pos.CENTER_LEFT);
            StackPane.setMargin(enemyTankImgView, new Insets(150, 0, 0, 400));
            StackPane.setAlignment(howToFireLabel, Pos.BOTTOM_LEFT);
            StackPane.setMargin(howToFireLabel, new Insets(0, 0, 90, 180));
            StackPane.setAlignment(howToFireImgView, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(howToFireImgView, new Insets(0, 380, 30, 0));
            StackPane.setAlignment(enemyTankImgView2, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(enemyTankImgView2, new Insets(0, 200, 30, 0));
        });

        //mute buttons
        final double[] previousVolume = new double[1];
        mute.setOnAction(e -> {
            click.play();

            muteStatus = true;
            previousVolume[0] = backingtrack.getVolume();
            backingtrack.setVolume(0);
            layout.getChildren().removeAll(mute);
            StackPane.setAlignment(unmute, Pos.TOP_RIGHT);
            layout.getChildren().addAll(unmute);
        });

        unmute.setOnAction(e -> {
            click.play();

            muteStatus = false;
            backingtrack.setVolume(previousVolume[0]);
            layout.getChildren();
            layout.getChildren().removeAll(unmute);
            StackPane.setAlignment(mute, Pos.TOP_RIGHT);
            layout.getChildren().addAll(mute);
        });



        //begins stage and shows window
        primaryStage.setScene(scene);
        //
        primaryStage.show();
        primaryStage.setMinHeight(760);
        primaryStage.setMinWidth(1300);

    }

    public void startSingle(Stage primaryStage, Float hueValue) {
        Maze maze = new Maze(mapChoiceSelected, 40, 22);
        final int[][] BLOCK_MAP = maze.getFinalMaze();

        handler = new Handler(gc, scene, BLOCK_MAP);
        gLoop = new GameLoop(handler);

        rLoop = new RenderLoop(handler, primaryStage);
        rLoop.start();

        CheckWallType.SpawnWalls(BLOCK_MAP, handler);

        Tank playertest = new Tank(handler, handler.getGridWorld().getSpawn(), hueValue);   //test Player object
        playertest.addComponent(new TankKeyboardController(playertest.moveComp, handler.getKeyboardTracker()));
        if (turretType == "laser") {
            playertest.turret.remove();
            playertest.turret = new LaserTurret(handler, playertest);
        }
        if (turretType == "cannon") {
            playertest.turret.remove();
            playertest.turret = new CannonTurret(handler, playertest);
        }
        playertest.turret.addComponent(new TurretMouseController(playertest.turret, handler.getMouseTracker()));

        Tank ai = new Tank(handler, handler.getGridWorld().getSpawn(), 0.5F);   //test Player object
        ai.addComponent(new TankAIController(ai));

        //adding a test landmine (should be part of randomly generated map eventually
        Landmine testMine = new Landmine(handler, 5, 5, handler.getGridWorld());

        new Minimap(handler);

        handler.setTackingID(playertest.getComponent(ImageRenderComponent.class).UID);
    }

    public void startServer() {
        server = new Server(handler);
    }

    public void startClient(Stage primaryStage, Float hueValue, String IP) {
        if (IP.isBlank()) IP = "127.0.0.1";
        handler = new ClientHandler(gc, scene, hueValue, IP);
        gLoop = new GameLoop(handler);

        rLoop = new RenderLoop(handler, primaryStage);
        rLoop.start();

    }


    @Override
    public void stop() throws Exception {
        System.out.println("Stopping");
        if (gLoop != null) gLoop.stop();
        if (server != null) server.stopServer();
        if (handler instanceof ClientHandler) ((ClientHandler) handler).stop();
        super.stop();
    }



}

