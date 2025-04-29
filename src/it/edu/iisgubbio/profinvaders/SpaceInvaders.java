package it.edu.iisgubbio.profinvaders;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvaders extends Application {

    Pane sfondo = new Pane();
    Rectangle player = new Rectangle(30, 10, Color.GREEN); // La navetta del giocatore
    List<Rectangle> bullets = new ArrayList<>(); // Lista dei colpi sparati
    List<Rectangle> invaders = new ArrayList<>(); // Lista degli alieni
    int playerX = 235; // Posizione iniziale della navetta
    int playerSpeed = 5; // Velocità di movimento della navetta
    int bulletSpeed = 5; // Velocità di movimento dei colpi
    int invaderSpeed = 2; // Velocità di movimento degli alieni
    boolean gameRunning = true; // Stato del gioco

    @Override
    public void start(Stage primaryStage) throws Exception {
        sfondo.setPrefSize(500, 500);
        sfondo.getChildren().add(player);
        player.setX(playerX);
        player.setY(450); // Posizione fissa sulla parte bassa della finestra

        createInvaders(); // Creazione degli alieni

        Scene scene = new Scene(sfondo);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Invaders");
        primaryStage.show();

        scene.setOnKeyPressed(this::keyPressed); // Gestione degli input da tastiera

        // Crea una Timeline per muovere gli alieni verso il basso
        Timeline invaderMovement = new Timeline(new KeyFrame(Duration.millis(50), e -> moveInvaders()));
        invaderMovement.setCycleCount(Timeline.INDEFINITE);
        invaderMovement.play();

        // Crea una Timeline per muovere i colpi verso l'alto
        Timeline bulletMovement = new Timeline(new KeyFrame(Duration.millis(20), e -> moveBullets()));
        bulletMovement.setCycleCount(Timeline.INDEFINITE);
        bulletMovement.play();
    }

    private void keyPressed(KeyEvent event) {
        if (!gameRunning) return;

        if (event.getCode() == KeyCode.RIGHT) {
            if (playerX < 470) playerX += playerSpeed;
        }
        if (event.getCode() == KeyCode.LEFT) {
            if (playerX > 0) playerX -= playerSpeed;
        }
        if (event.getCode() == KeyCode.SPACE) {
            shootBullet(); // Sparare un colpo
        }

        player.setX(playerX); // Aggiorna la posizione della navetta
    }

    private void shootBullet() {
        Rectangle bullet = new Rectangle(5, 10, Color.RED); // Colpo rosso
        bullet.setX(player.getX() + 12); // Posizione iniziale del colpo sopra la navetta
        bullet.setY(player.getY());
        bullets.add(bullet);
        sfondo.getChildren().add(bullet);
    }

    private void moveBullets() {
        List<Rectangle> bulletsToRemove = new ArrayList<>();

        for (Rectangle bullet : bullets) {
            bullet.setY(bullet.getY() - bulletSpeed); // Movimento verso l'alto

            // Controllo delle collisioni tra i colpi e gli alieni
            for (Rectangle invader : invaders) {
                if (bullet.getBoundsInParent().intersects(invader.getBoundsInParent())) {
                    sfondo.getChildren().remove(bullet);
                    sfondo.getChildren().remove(invader);
                    bulletsToRemove.add(bullet);
                    invaders.remove(invader);

                    // Se tutti gli alieni sono stati distrutti
                    if (invaders.isEmpty()) {
                        gameRunning = false;
                        System.out.println("Hai vinto!");
                    }
                    break;
                }
            }

            // Rimuovere i colpi che escono dallo schermo
            if (bullet.getY() < 0) {
                bulletsToRemove.add(bullet);
            }
        }

        bullets.removeAll(bulletsToRemove);
        sfondo.getChildren().removeAll(bulletsToRemove);
    }

    private void createInvaders() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                Rectangle invader = new Rectangle(30, 20, Color.BLUE); // Gli alieni
                invader.setX(50 + j * 40); // Posizione iniziale degli alieni sulla griglia
                invader.setY(50 + i * 40);
                invaders.add(invader);
                sfondo.getChildren().add(invader);
            }
        }
    }

    private void moveInvaders() {
        if (!gameRunning) return;

        for (Rectangle invader : invaders) {
            invader.setY(invader.getY() + invaderSpeed); // Movimento verso il basso
        }

        // Controllo se un alieno ha raggiunto la navetta
        for (Rectangle invader : invaders) {
            if (invader.getBoundsInParent().intersects(player.getBoundsInParent())) {
                gameRunning = false;
                System.out.println("Game Over!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}