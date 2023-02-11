package acsse.csc2b.ui;

import java.io.File;

import acsse.csc2b.server.ClientManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class ClientPane extends VBox
{
	GridPane gpLayout = new GridPane();
	final int BTN_WIDTH = 200;
	final int BTN_HEIGHT = 30;

    File chosenFile = null;
    ImageView preImageView = new ImageView();
    Image preImage = null;
    ImageView postImageView = new ImageView();
    Image postImage = null;

    public ClientPane()
    {
        ClientManager c = new ClientManager();

        // title
        Label titleLabel = new Label("Facial Recognition Software");
        titleLabel.setFont(new Font("Arial", 30));
        titleLabel.setAlignment(Pos.TOP_LEFT);
        titleLabel.setPadding(new Insets(10));

        // text area
		TextArea tArea = new TextArea();
		tArea.setEditable(false);
		tArea.setMinSize(200, 100);

		// connect
		Button btnConnect = new Button("Connect");
		btnConnect.setMinSize(200, BTN_HEIGHT);
		
		// select image
		Button btnImages = new Button("Select Image");
		btnImages.setMinSize(BTN_WIDTH, BTN_HEIGHT);

        btnImages.setOnAction(e -> {
            
            c.connect(5000);

            final FileChooser fc = new FileChooser();
            fc.setTitle("Choose Image to Open...");
            fc.setInitialDirectory(new File("data/images"));
            chosenFile = fc.showOpenDialog(null);

            preImage = new Image(chosenFile.toString());
            preImageView.setFitWidth(200);
            preImageView.setFitHeight(200);
            preImageView.setPreserveRatio(true);
            preImageView.setImage(preImage);
        });

        Button btnProcessImage = new Button("Process Image");
		btnProcessImage.setMinSize(BTN_WIDTH, BTN_HEIGHT);

        btnProcessImage.setOnAction(e -> {

            c.processImage("GrayScale", chosenFile, postImageView);

            postImageView.setFitWidth(200);
            postImageView.setFitHeight(200);
            postImageView.setPreserveRatio(true); 
        });

        Button btnClear = new Button("Clear");
        btnClear.setMinSize(BTN_WIDTH, BTN_HEIGHT);

        btnClear.setOnAction(e -> {
            preImageView.setImage(null);
            postImageView.setImage(null);
        });

        gpLayout.add(btnImages, 0, 0);
        gpLayout.add(btnProcessImage, 1, 0);
        gpLayout.add(btnClear, 2, 0);

        HBox imageViews = new HBox();
        imageViews.getChildren().addAll(preImageView, postImageView);
        imageViews.setSpacing(50);
        imageViews.setAlignment(Pos.CENTER);
		
        gpLayout.setHgap(10);
        gpLayout.setAlignment(Pos.CENTER);

		this.setPadding(new Insets(20));
		this.setSpacing(30);
		this.setAlignment(Pos.TOP_CENTER);

		this.getChildren().addAll(titleLabel, gpLayout, imageViews);		

		// size
		this.setPrefSize(700, 520);
    }   
}

