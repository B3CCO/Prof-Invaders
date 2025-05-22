package it.edu.iisgubbio.profinvaders;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ProfInvaders extends Application {
    Pane sfondo = new Pane();
    ImageView eddyNico = new ImageView();
    List<ImageView> colpi = new ArrayList<>();
    List<ImageView> invasori = new ArrayList<>();
    List<ImageView> immaginiInvasori = new ArrayList<>();
    GridPane principale = new GridPane();

    int sX = 275;
    int velocitaColpi = 6;

    Label ondate = new Label();
    Label obiettivo = new Label("Raggiungi le 5 ondate vinte per dominare il corridoio!");
    Label comandi = new Label(" Vai a Destra= -> \n Vai a Sinistra= <- \n Colpo= Space Bar");

    int ondateVinte = 0;
    boolean ondataInCorso = true;

    Timeline muoviInvasoriDx;
    Timeline muoviColpi;

    boolean muoviVersoDestra = true;
    boolean partitaInCorso = true;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Accordion istruzioni = new Accordion();
        TitledPane pannelloIstruzioni = new TitledPane("comandi", comandi);
        principale.requestFocus();
        istruzioni.getPanes().add(pannelloIstruzioni);

        principale.add(istruzioni, 2, 0, 1, 3);
        principale.add(obiettivo, 0, 0);
        principale.add(ondate, 0, 1);
        principale.add(sfondo, 0, 2, 2, 1);
        sfondo.setPrefSize(600, 600);

        // Giocatore
        Image imgGiocatore = new Image("file:eddyNico.png");
        eddyNico.setImage(imgGiocatore);
        eddyNico.setFitWidth(84);
        eddyNico.setFitHeight(102);
        sfondo.getChildren().add(eddyNico);
        eddyNico.setY(490);
        eddyNico.setX(sX);

        Scene scena = new Scene(principale);
        scena.getStylesheets().add("it/edu/iisgubbio/profinvaders/profinvaders.css");
        sfondo.getStyleClass().add("sfondo");
        pannelloIstruzioni.getStyleClass().add("pannelloIstruzioni");

        primaryStage.setScene(scena);
        primaryStage.setTitle("Prof Invaders");
        primaryStage.show();

        resetInvasori();

        muoviInvasoriDx = new Timeline(new KeyFrame(Duration.millis(100), e -> MuoviInvasori()));
        muoviInvasoriDx.setCycleCount(Timeline.INDEFINITE);
        muoviInvasoriDx.play();

        muoviColpi = new Timeline(new KeyFrame(Duration.millis(20), e -> MuoviColpi()));
        muoviColpi.setCycleCount(Timeline.INDEFINITE);
        muoviColpi.play();

        Timeline focus = new Timeline(new KeyFrame(Duration.millis(800), e -> resetFocus()));
        focus.setCycleCount(Timeline.INDEFINITE);
        focus.play();

        scena.setOnKeyPressed(e -> TastoPremuto(e));
    }

    private void resetFocus() {
        principale.requestFocus();
    }

    private void TastoPremuto(KeyEvent tasto) {
        if (tasto.getCode() == KeyCode.RIGHT) {
            sX = Math.min(sX + 10, 520);
        }
        if (tasto.getCode() == KeyCode.LEFT) {
            sX = Math.max(sX - 10, 0);
        }
        if (tasto.getCode() == KeyCode.SPACE) {
            Colpo();
            
        }
        eddyNico.setX(sX);
    }

    boolean cColpo = true;

    private void Colpo() {

        if (cColpo) {
            Image img = new Image("file:colpo.png");
            ImageView colpo = new ImageView(img);
            colpo.setFitWidth(50);
            colpo.setFitHeight(40);
            colpo.setX(eddyNico.getX() + 17);
            colpo.setY(eddyNico.getY());

            colpi.add(colpo);
            sfondo.getChildren().add(colpo);

            cColpo = false;
            Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0.45), e -> cColpo = true));
            timer.setCycleCount(1);
            timer.play();
        }
    }

    private void MuoviColpi() {
        List<ImageView> fuori = new ArrayList<>();

        for (int c = colpi.size() - 1; c >= 0; c--) {
            ImageView colpo = colpi.get(c);
            colpo.setY(colpo.getY() - velocitaColpi);
            

            if (colpo.getY() < 0) {
                fuori.add(colpo);
            }

            Bounds b = colpo.getBoundsInParent();

            for (int i = invasori.size() - 1; i >= 0; i--) {
                ImageView invasore = invasori.get(i);
                Bounds b1 = invasore.getBoundsInParent();

                if (b.intersects(b1)) {
                    
                    invasori.remove(i);
                    sfondo.getChildren().remove(invasore);
                    colpi.remove(colpo);
                    sfondo.getChildren().remove(colpo);
                    break;
                }
            }
        }

        colpi.removeAll(fuori);
        sfondo.getChildren().removeAll(fuori);
    }

    private void MuoviInvasori() {
        if (ondateVinte == 0) {
            ondate.setText("Esito ondata: -----    Ondate vinte: -");
        }

        if (!partitaInCorso) return;

        if (invasori.isEmpty()) {
            if (ondataInCorso) {
                ondateVinte++;
                ondataInCorso = false;
                ondate.setText("Esito ondata: vinta!!  Ondate vinte: " + ondateVinte);

                if (ondateVinte >= 5) {
                    partitaInCorso = false;
                    muoviInvasoriDx.stop();
                    muoviColpi.stop();

                    Platform.runLater(() -> {
                        Alert vittoria = new Alert(Alert.AlertType.INFORMATION);
                        vittoria.setHeaderText("Hai vinto!");
                        vittoria.setContentText("Hai superato 5 ondate!");
                        vittoria.showAndWait();

                        ondateVinte = 0;
                        ondataInCorso = true;
                        ondate.setText("");
                        resetInvasori();
                        partitaInCorso = true;
                        muoviInvasoriDx.play();
                        muoviColpi.play();
                    });

                    for (ImageView colpo : colpi) {
                        sfondo.getChildren().remove(colpo);
                    }
                    colpi.clear();
                } else {
                    resetInvasori();
                }
            }
            return;
        }

        for (ImageView invasore : invasori) {
        	int spostamento;
        	if (muoviVersoDestra) {
        	    spostamento = 18;
        	} else {
        	    spostamento = -18;
        	}
            invasore.setX(invasore.getX() + spostamento);
        }

        double minimo = Double.MAX_VALUE;
        double massimo = Double.MIN_VALUE;

        for (ImageView inv : invasori) {
            minimo = Math.min(minimo, inv.getX());
            massimo = Math.max(massimo, inv.getX());
        }

        if (massimo >= 515) {
            muoviVersoDestra = false;
            for (ImageView inv : invasori) {
                inv.setY(inv.getY() + 30);
            }
        }

        if (minimo <= 10) {
            muoviVersoDestra = true;
            for (ImageView inv : invasori) {
                inv.setY(inv.getY() + 30);
            }
        }

        if (invasoriRaggiungonoGiocatore()) {
            partitaInCorso = false;
            muoviInvasoriDx.stop();
            muoviColpi.stop();
            ondate.setText("Esito ondata: persa :(");

            Platform.runLater(() -> {
                Alert sconfitta = new Alert(Alert.AlertType.INFORMATION);
                sconfitta.setHeaderText("Hai perso!");
                sconfitta.setContentText("Gli invasori ti hanno colpito!");
                sconfitta.showAndWait();

                for (ImageView colpo : colpi) {
                    sfondo.getChildren().remove(colpo);
                }
                colpi.clear();

                resetInvasori();
                partitaInCorso = true;
                muoviInvasoriDx.play();
                muoviColpi.play();
            });
        }
    }

    private boolean invasoriRaggiungonoGiocatore() {
        double yGiocatoreBottom = eddyNico.getY() + eddyNico.getFitHeight();
        for (ImageView invasore : invasori) {
            double invasoreBottom = invasore.getY() + invasore.getFitHeight();
            if (invasoreBottom >= yGiocatoreBottom - 5) {
                return true;
            }
        }
        return false;
    }

    private void resetInvasori() {
        for (ImageView invasore : invasori) {
            sfondo.getChildren().remove(invasore);
        }

        invasori.clear();
        immaginiInvasori.clear();

        for (int cInvasore = 0; cInvasore < 10; cInvasore++) {
            int riga = cInvasore / 5;
            int colonna = cInvasore % 5;

            String nomeFile = "invasore" + cInvasore + ".png";
            Image immagineInvasore = new Image("file:" + nomeFile);
            ImageView invasore = new ImageView(immagineInvasore);

            invasore.setFitWidth(72);
            invasore.setFitHeight(84);
            invasore.setX(85 + colonna * 86);
            invasore.setY(20 + riga * 80);

            invasori.add(invasore);
            immaginiInvasori.add(invasore);
            sfondo.getChildren().add(invasore);
        }

        ondataInCorso = true;
    }

    public static void main(String[] args) {
        launch(args);
    }
}