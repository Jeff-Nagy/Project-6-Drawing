import java.util.Optional;

import javafx.application.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class SaveFileButton extends Application {

	private TextInputDialog inputDialog;
	private Optional<String> userInput;
	
	private Alert resultAlert;
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		inputDialog = new TextInputDialog();
		inputDialog.setHeaderText(null);
		inputDialog.setTitle("Save File");
		inputDialog.setContentText("Choose destination for saved file");
		inputDialog.showAndWait();
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
