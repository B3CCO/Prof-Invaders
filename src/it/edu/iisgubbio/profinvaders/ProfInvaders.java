package it.edu.iisgubbio.profinvaders;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
	List<ImageView> immaginiInvasori=new ArrayList<>();

	int sX = 225;
	int velocitaColpi = 5;  
	
	Label ondate= new Label();
	Label obiettivo= new Label("Vinci 5 ondate per vincere");
	
	int ondateVinte = 0;
	boolean ondataInCorso = true;

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane principale = new GridPane();
		principale.add(obiettivo, 0,0);
		principale.add(ondate, 0,1);
		principale.add(sfondo, 0,2,2,1);
		
		sfondo.setPrefSize(500, 500);
		
		Image imgGiocatore = new Image("file:eddyNico.png"); 
	    eddyNico.setImage(imgGiocatore);
	    eddyNico.setFitWidth(70);
	    eddyNico.setFitHeight(85);
		
		sfondo.getChildren().add(eddyNico);
		eddyNico.setY(410);
		eddyNico.setX(sX);

		Scene scena = new Scene(principale);
		scena.getStylesheets().add("it/edu/iisgubbio/profinvaders/profinvaders.css");
		sfondo.getStyleClass().add("sfondo");
		primaryStage.setScene(scena);
		primaryStage.setTitle("Prof Invaders");
		primaryStage.show();
		
		
		for(int cInvasore=0;cInvasore<10;cInvasore++) {
			int riga=cInvasore/5;
			int colonna=cInvasore%5;

			String nomeFile = "invasore" + cInvasore + ".png";
			
			Image immagineInvasore = new Image("file:"+nomeFile);
			ImageView invasore=new ImageView(immagineInvasore);
			
			invasore.setFitWidth(60);
			invasore.setFitHeight(70);
			invasore.setX(70+colonna*75);
			invasore.setY(10+riga*65);

			invasori.add(invasore);
			immaginiInvasori.add(invasore);
			sfondo.getChildren().add(invasore);

		}

		Timeline muoviInvasoriDx = new Timeline(new KeyFrame(Duration.millis(100), e -> MuoviInvasori()));
		muoviInvasoriDx.setCycleCount(Timeline.INDEFINITE);
		muoviInvasoriDx.play();
				
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

	boolean cColpo=true;
	private void Colpo() {
		
		if(cColpo) {
			String percorsoImg = "file:colpo.png"; 
	        Image img = new Image(percorsoImg);
	        ImageView colpo = new ImageView(img);
	        
	        colpo.setFitWidth(35);
	        colpo.setFitHeight(40);
			colpo.setX(eddyNico.getX()+13); 
			colpo.setY(eddyNico.getY());

			colpi.add(colpo);
			sfondo.getChildren().add(colpo);
			
			cColpo=false;
			
			Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> cColpo = true)); 
	        timer.setCycleCount(1);
	        timer.play();
		}
		
	}

	private void MuoviColpi() {

		List<ImageView> fuori = new ArrayList<>();
		
		for (int c=colpi.size()-1; c>=0 ;c--) {
			ImageView colpo=colpi.get(c);
			colpo.setY(colpo.getY() - velocitaColpi); 


			if (colpo.getY() < 0) {
				fuori.add(colpo);
			}

			Bounds b = colpo.getBoundsInParent();

			for(int i=invasori.size()-1;i>=0;i--) {
				ImageView invasore=invasori.get(i);
				Bounds b1= invasore.getBoundsInParent();

				if(b.intersects(b1)) {
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



	boolean muoviVersoDestra = true;
	private void MuoviInvasori() {
		
		if (invasori.isEmpty()) {
			if (ondataInCorso) {
				ondateVinte++;
				ondataInCorso = false;
				ondate.setText("Ondata vinta! Totali vinte: " + ondateVinte);

				if (ondateVinte >= 5) {
					Alert vittoria = new Alert(Alert.AlertType.INFORMATION);
					vittoria.setHeaderText("Hai vinto!");
					vittoria.setContentText("Hai superato 5 ondate!");
					vittoria.showAndWait();

					Timeline chiusura = new Timeline(new KeyFrame(Duration.seconds(1), e2 -> System.exit(0)));
					chiusura.setCycleCount(1);
					chiusura.play();
				}

				resetInvasori();
			}
			return;
		}

		for (ImageView invasore : invasori) {
			if (muoviVersoDestra) {
				invasore.setX(invasore.getX() + 15);
			} else {
				invasore.setX(invasore.getX() - 15);
			}
		}

		double minimo = Double.MAX_VALUE;
		double massimo = Double.MIN_VALUE;

		for (ImageView inv : invasori) {
			if (inv.getX() < minimo) minimo = inv.getX();
			if (inv.getX() > massimo) massimo = inv.getX();
		}

		if (massimo >= 440) {
			muoviVersoDestra = false;
			for (ImageView inv : invasori) {
				inv.setY(inv.getY() + 25);
			}
		}
		if (minimo <= 10) {
			muoviVersoDestra = true;
			for (ImageView inv : invasori) {
				inv.setY(inv.getY() + 25);
			}
		}

		if (invasoriRaggiungonoGiocatore()) {
			ondataInCorso = false;
			ondate.setText("Ondata persa. Totali vinte: " + ondateVinte);
			resetInvasori();
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

			invasore.setFitWidth(60);
			invasore.setFitHeight(70);
			invasore.setX(70 + colonna * 75);
			invasore.setY(10 + riga * 65);

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