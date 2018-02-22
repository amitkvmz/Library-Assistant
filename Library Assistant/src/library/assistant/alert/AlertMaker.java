/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.alert;

import javafx.scene.control.Alert;

/**
 *
 * @author Amit
 */
public class AlertMaker {

    public static void showSimpleAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();

    }

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setTitle(title);
        alert.showAndWait();

    }

    public static void showErrorMessage(Exception ex, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(ex+"  :  "+ content);
        alert.setTitle(title);
        alert.showAndWait();

    }
}
