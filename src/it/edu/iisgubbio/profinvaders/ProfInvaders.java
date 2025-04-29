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

public class ProfInvaders extends Application {
    Pane sfondo = new Pane();
    Rectangle eddyNico = new Rectangle(50, 20, Color.GREEN); 
    List<Rectangle> colpi = new ArrayList<>();  
    int sX = 225;
 
    int velocitaColpi = 5;  

    @Override
    public void start(Stage primaryStage) throws Exception {
        sfondo.setPrefSize(500, 500);
        sfondo.getChildren().add(eddyNico);

        eddyNico.setY(470);
        eddyNico.setX(sX);

        Scene scena = new Scene(sfondo);
        primaryStage.setScene(scena);
        primaryStage.setTitle("Prof Invaders");
        primaryStage.show();
        
        
        Timeline muoviColpi = new Timeline(new KeyFrame(Duration.millis(20), e -> MuoviColpi()));
        muoviColpi.setCycleCount(Timeline.INDEFINITE);
        muoviColpi.play();

        scena.setOnKeyPressed(e -> TastoPremuto(e));
    }

    private void TastoPremuto(KeyEvent tasto) {
        if (tasto.getCode() == KeyCode.RIGHT) { 
            sX = sX + 7;
            if (sX > 450) {
                sX = 450;  
            }
        }
        if (tasto.getCode() == KeyCode.LEFT) {
            sX = sX - 7;
            if (sX < 0) {
                sX = 0;  
            }
        }
        if (tasto.getCode() == KeyCode.SPACE) {
  
            Colpo();
        }
        eddyNico.setX(sX);
    }

    private void Colpo() {
        
        Rectangle colpo = new Rectangle(5, 10, Color.RED);
        colpo.setX(eddyNico.getX() + 22); 
        colpo.setY(eddyNico.getY());
        
        colpi.add(colpo);
        sfondo.getChildren().add(colpo);


    }

    private void MuoviColpi() {
       
        List<Rectangle> toRemove = new ArrayList<>();
        for (Rectangle colpo : colpi) {
            colpo.setY(colpo.getY() - velocitaColpi); 

            
            if (colpo.getY() < 0) {
                toRemove.add(colpo);
            }
        }

        
        colpi.removeAll(toRemove);
        sfondo.getChildren().removeAll(toRemove);
    }          

    public static void main(String[] args) {
        launch(args);
    }
}   