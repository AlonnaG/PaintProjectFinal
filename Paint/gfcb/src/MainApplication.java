import java.util.Iterator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Parent prnt=FXMLLoader.load(getClass().getResource("ViewSource.fxml"));
		Scene sn=new Scene(prnt);
		stage.setScene(sn);
		stage.show();
		
		
	}
	
}


