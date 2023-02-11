import acsse.csc2b.ui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception 
    {
        ClientPane clientPane = new ClientPane();
        primaryStage.setScene(new Scene(clientPane));
        primaryStage.setTitle("My Network Project :-)");
        primaryStage.show();
    }
}