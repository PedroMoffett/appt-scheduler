package moffett.scheduler.helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import moffett.scheduler.Main;
import moffett.scheduler.model.Appointment;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Abstract class contains helper utilities used within the program.
 */
public abstract class HelperFunctions {

    /**
     * The approach modifies the stage to match the fxml file and aligns the stage accordingly.
     * @param fxmlFileName targets fxml file to change.
     * @param node node for the event that calls the changeStage method used for defining current stage.
     * @throws IOException
     */
    public static void changeStage(String fxmlFileName, Node node) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFileName));
        Object root = fxmlLoader.load();
        Scene scene = new Scene((Parent) root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }


    /**
     * Method for trivializing sending of alerts.
     * @param alertType type of alert to be sent.
     * @param title alert title.
     * @param content alert content.
     * @return
     */
    public static boolean sendAlert(Alert.AlertType alertType, String title, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() != ButtonType.CANCEL;
    }

    /**
     * Method for trivializing sending of alerts.
     * @param alertType type of alert to be sent.
     * @param title alert title.
     * @param headerText header text for alert.
     * @param content content text.
     * @return
     */
    public static boolean sendAlert(Alert.AlertType alertType, String title, String headerText, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(headerText);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() != ButtonType.CANCEL;
    }

    /**
     * Method for sending alert to user once logged in, message depends on upcoming appointments and their status.
     * <p>
     * Queries the database to get appointment information and checks to see if there are appointments starting
     * in the next 15 minutes,
     * sends an alert to the user with relevant information.
     */
    public static void greeting() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            appointments.setAll(Queries.selectAppointments());
            for (Appointment appointment : appointments) {
                if (appointment.getAppointmentStart().isAfter(LocalDateTime.now()) &&
                        appointment.getAppointmentStart().isBefore(LocalDateTime.now().plusMinutes(15))) {
                    HelperFunctions.sendAlert(Alert.AlertType.INFORMATION, "Upcoming Appointment",
                            "You have an appointment starting very soon!",
                            "Appointment ID: " + appointment.getAppointmentID() +
                                    "\nAppointment Time: " + appointment.getStartString());
                    return;
                }
            }
            HelperFunctions.sendAlert(Alert.AlertType.INFORMATION, "Hello!",
                    "Welcome to the Scheduler.", "You have no upcoming appointments.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
