package it.edu.iisgubbio.profinvaders;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
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
	List<Rectangle> invasori = new ArrayList<>(); 

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


		for(int cInvasore=0;cInvasore<10;cInvasore++) {
			int riga=cInvasore/5;
			int colonna=cInvasore%5;
			Rectangle invasore = new Rectangle(50, 30, Color.BLUE);

			invasore.setX(70+colonna*75);
			invasore.setY(40+riga*40);

			invasori.add(invasore);
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
			Rectangle colpo = new Rectangle(5, 10, Color.RED);
			colpo.setX(eddyNico.getX() + 22); 
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

		List<Rectangle> fuori = new ArrayList<>();
				
		for (int c=0; c< colpi.size();c++) {
			Rectangle colpo=colpi.get(c);
			colpo.setY(colpo.getY() - velocitaColpi); 


			if (colpo.getY() < 0) {
				fuori.add(colpo);
			}

			for(int j=0;j<colpi.size();j++) {

				Bounds b= colpi.get(j).getBoundsInParent();

				for(int i=0;i<invasori.size();i++) {
					Rectangle invasore=invasori.get(i);
					Bounds b1= invasore.getBoundsInParent();

					if(b.intersects(b1)) {
						invasori.remove(i);
						colpi.remove(j);
						sfondo.getChildren().remove(invasore);
						sfondo.getChildren().remove(colpo);
					}
				}
			}         
		}
		
		colpi.removeAll(fuori);
		sfondo.getChildren().removeAll(fuori);
	}

	
	boolean muoviVersoDestra = true;
	private void MuoviInvasori() {

		for(Rectangle invasore:invasori) {
			
			if(muoviVersoDestra){
				invasore.setX(invasore.getX()+15);
			}else {
				invasore.setX(invasore.getX()-15);
				
			}
			
			
		}
		if(!invasori.isEmpty() && invasori.get(invasori.size()-1).getX() >= 440) {
			muoviVersoDestra=false;
			for(Rectangle inv:invasori){
				inv.setY(inv.getY()+20);
			}
			
		}
		if(!invasori.isEmpty() && invasori.get(0).getX() <= 10) {
			muoviVersoDestra = true;
			for(int a=0; a<invasori.size();a++){
				Rectangle inv=invasori.get(a);
				inv.setY(inv.getY()+20);
			}
		}
		
		if(invasoriInFondo()) {
			resetInvasori();
		}
	}
	
	private boolean invasoriInFondo() {
		for (Rectangle invasore : invasori) {
	        if (invasore.getY() <= 500) {
	            return false;
	        }
	    }
		return true;
	}
	
	private void resetInvasori() {
		invasori.clear();
		
		for(int cInvasore=0;cInvasore<10;cInvasore++) {
			int riga=cInvasore/5;
			int colonna=cInvasore%5;
			Rectangle invasore = new Rectangle(50, 30, Color.BLUE);

			invasore.setX(70+colonna*75);
			invasore.setY(40+riga*40);

			invasori.add(invasore);
			sfondo.getChildren().add(invasore);

		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}  

