package hr.algebra.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.stream.Stream;

public class FileUtils {
    private static final String UPLOAD = "Choose image";
    public static File uploadFileDialog(Window owner, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        Stream.of(extensions).forEach(ext -> {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(ext.toUpperCase(), "*." + ext));
        });
        fileChooser.setTitle(UPLOAD);
        return fileChooser.showOpenDialog(owner);
    }

}
