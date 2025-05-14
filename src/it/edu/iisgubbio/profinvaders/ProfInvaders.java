package it.edu.iisgubbio.profinvaders;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

	@Override
	public void start(Stage primaryStage) throws Exception {
		sfondo.setPrefSize(500, 500);
		
		Image imgGiocatore = new Image("file:eddyNico.png"); 
	    eddyNico.setImage(imgGiocatore);
	    eddyNico.setFitWidth(70);
	    eddyNico.setFitHeight(85);
		
		sfondo.getChildren().add(eddyNico);
		eddyNico.setY(410);
		eddyNico.setX(sX);

		Scene scena = new Scene(sfondo);
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
			colpo.setY(eddyNico.getY()-17);

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

		for(ImageView invasore:invasori) {
			
			if(muoviVersoDestra){
				invasore.setX(invasore.getX()+15);
			}else {
				invasore.setX(invasore.getX()-15);
				
			}
		}
		
		double minimo = Double.MAX_VALUE;
	    double massimo = Double.MIN_VALUE;

	    for (ImageView inv : invasori) {
	        if (inv.getX() < minimo) {
	            minimo = inv.getX();
	        }
	        if (inv.getX() > massimo) {
	            massimo = inv.getX();
	        }
	    }
	    
		if(!invasori.isEmpty() && massimo >= 440) {
			muoviVersoDestra=false;
			for(ImageView inv:invasori){
				inv.setY(inv.getY()+25);
			}
			
		}
		if(!invasori.isEmpty() && minimo <= 10) {
			muoviVersoDestra = true;
			for(int a=0; a<invasori.size();a++){
				ImageView inv=invasori.get(a);
				inv.setY(inv.getY()+25);
			}
		}
		
		if(invasoriInFondo()) {
			resetInvasori();
		}
	}
	
	private boolean invasoriInFondo() {
		for (ImageView invasore : invasori) {
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

			String nomeFile = "invasore" + cInvasore + ".png";
			Image immagineInvasore = new Image("file:" + nomeFile);
			ImageView invasore=new ImageView(immagineInvasore);
			
			invasore.setFitWidth(60);
			invasore.setFitHeight(70);
			invasore.setX(70+colonna*75);
			invasore.setY(10+riga*65);

			invasori.add(invasore);
			immaginiInvasori.add(invasore);
			sfondo.getChildren().add(invasore);

		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}  

