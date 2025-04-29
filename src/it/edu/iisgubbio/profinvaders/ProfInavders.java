package it.edu.iisgubbio.profinvaders;




import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class ProfInavders extends Application{
	Pane sfondo=new Pane();
	Button gioca=new Button("Gioca");
	Rectangle eddyNico=new Rectangle(50,20);
	
	int sX=225;
	

	
	@Override
	public void start(Stage primaryStage) throws Exception{
		
		
		sfondo.setPrefSize(500, 500);
		sfondo.getChildren().add(eddyNico);
		
		eddyNico.setY(470);
		eddyNico.setX(sX);
		
		Scene scena = new Scene(sfondo);
		primaryStage.setScene(scena);
		primaryStage.setTitle("Prof Invaders");
		primaryStage.show();
		
		
		scena.setOnKeyPressed(e -> TastoPremuto(e));
	}
	
	private void TastoPremuto(KeyEvent tasto){
		
		if(tasto.getCode()==KeyCode.RIGHT) {
			sX=sX+7;
			if(sX>450) {
				sX=sX-7;
			}
		}
		eddyNico.setX(sX);
		if(tasto.getCode()==KeyCode.LEFT) {
			sX=sX-7;
			if(sX<0) {
				sX=sX+7;
			}
		}
		eddyNico.setX(sX);
		
	}
	private void colpo() {
		boolean sparo=true;
		colpo=new Rectangle(5,10,Color.RED);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
