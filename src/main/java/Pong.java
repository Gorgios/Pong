import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Pong extends Application {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final int PADDLE_HEIGHT = 100;
    private static final int PADDLE_WIDTH = 20;
    private static final double BALL_RADIUS = 20;
    private double playerY = HEIGHT * 0.5;
    private static final double playerX = 0;
    private double aiY = HEIGHT * 0.5;
    private static final double aiX = WIDTH - PADDLE_WIDTH;
    private double ballXPos,ballYPos;
    private int ballXSpeed,ballYSpeed;
    private int playerScore = 0;
    private int aiScore = 0;
    private boolean isStarted = false;
    private int randomizer = 1;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        canvas.setFocusTraversable(true);
        canvas.setOnMouseMoved(e -> playerY = e.getY() - PADDLE_HEIGHT * 0.5);
        canvas.setOnMouseClicked(e -> isStarted = true);
        canvas.setOnKeyPressed(e -> {
            if (!isStarted)
                aiScore = playerScore = 0;
        });
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        timeline.play();
    }
    private void run(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.WHITE);
        gc.fillRect(playerX, playerY, PADDLE_WIDTH, PADDLE_HEIGHT);
        gc.fillRect(aiX, aiY, PADDLE_WIDTH, PADDLE_HEIGHT);
        gc.setFont(new Font(30));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setStroke(Color.WHITE);
        gc.strokeText(playerScore + " : " + aiScore, WIDTH * 0.5, 100);
        if (isStarted) {
            ballXPos += ballXSpeed;
            ballYPos += ballYSpeed;
            if (ballXPos < WIDTH * 0.8)
                aiY = ballYPos - PADDLE_HEIGHT * 0.5;
            else {
                aiY = ballYPos < aiY + PADDLE_HEIGHT * 0.5 ? aiY-1 : aiY+1;
            }
            gc.fillOval(ballXPos, ballYPos, BALL_RADIUS, BALL_RADIUS);
        } else {
            gc.strokeText("CLICK = next round \n " +
                    "ANY KEY = reset", WIDTH * 0.5, HEIGHT * 0.5);
            ballXSpeed = new Random().nextBoolean() ? -1 : 1;
            ballYSpeed = new Random().nextBoolean() ? -1 : 1;
            ballXPos = WIDTH * 0.5;
            ballYPos = HEIGHT * 0.5;
        }
        if (ballYPos >= HEIGHT || ballYPos <= 0)
            ballYSpeed *= -1;
        if ((ballXPos  <= PADDLE_WIDTH && ballYPos >= playerY && ballYPos <= playerY + PADDLE_HEIGHT)
                || (ballXPos >= WIDTH - PADDLE_WIDTH && ballYPos >= aiY && ballYPos <= aiY + PADDLE_HEIGHT)) {
            ballXSpeed += 1 * Math.signum(ballXSpeed);
            ballYSpeed += 1 * Math.signum(ballYSpeed);
            ballXSpeed *= -1;

            // Changing direction depending on the side of paddle

            if (ballXPos > WIDTH * 0.5 && ballYPos + BALL_RADIUS <= aiY + PADDLE_HEIGHT * 0.5)
                ballYSpeed *= -1;
            if (ballXPos < WIDTH * 0.5 && ballYPos + BALL_RADIUS <= playerY + PADDLE_HEIGHT * 0.5)
                ballYSpeed *= -1;
        }
        if (ballXPos <= 0){
            isStarted = false;
            aiScore++;
        }
        if (ballXPos >= WIDTH){
            isStarted = false;
            playerScore++;
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
