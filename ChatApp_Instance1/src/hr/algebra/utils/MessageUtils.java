package hr.algebra.utils;

import hr.algebra.model.Message;
import hr.algebra.model.UserProvider;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MessageUtils {

    public static void createMessage(Message message, VBox vbChat, UserProvider user){
        HBox hBox = new HBox();
        hBox.setPrefWidth(vbChat.getWidth());
        Label messageLabel = new Label();
        if (message.isImage()) {
            ByteArrayInputStream bais = new ByteArrayInputStream(message.getMessageContent());
            BufferedImage image;
            try {
                image = ImageIO.read(bais);
                ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(image.getHeight());
                imageView.setFitWidth(vbChat.getWidth() / 1.5);
                messageLabel.setGraphic(imageView);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid image");
                alert.show();
            }
        } else
            messageLabel.setText(new String(message.getMessageContent()));
        if (message.getFromId() == user.getUser().getIdContact()) {
            messageLabel.getStyleClass().add("myMessage");
            hBox.getChildren().add(messageLabel);
            hBox.setAlignment(Pos.CENTER_RIGHT);
        }
        else {
            messageLabel.getStyleClass().add("contactMessage");
            hBox.getChildren().add(messageLabel);
            hBox.setAlignment(Pos.CENTER_LEFT);
        }
        messageLabel.setWrapText(true);
        vbChat.getChildren().add(hBox);
    }
}
