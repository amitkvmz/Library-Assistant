/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.util;

import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Amit
 */
public class LibraryAssistantUtil {
    private static final String IMAGE_LOC  ="/library/assistant/ui/icons/library.png";
    
    public static void setStageIcon(Stage stage) {
       stage.getIcons().add(new Image(IMAGE_LOC));
    }
    
    
    
}
