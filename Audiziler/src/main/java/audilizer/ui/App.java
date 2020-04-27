/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audilizer.ui;

import audilizer.domain.FileManager;
import audilizer.domain.Service;
import audilizer.domain.SettingsService;
import audiziler.dao.FileSettingDao;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
/**
 *
 * @author vesuvesu
 */
public class App extends Application {
    FileSettingDao settingdao;
    SettingsService settingsService;
    Service service;
    FileManager filemanager;
    Player player;
    @Override
    public void init() throws IOException{
        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        String settingsFile = properties.getProperty("settingsFile");
        
        settingdao = new FileSettingDao(settingsFile);
        settingsService = new SettingsService(settingdao);
        filemanager = new FileManager();
        service = new Service(settingsService, filemanager);
        player = new Player(service, settingsService);
    }
    @Override
    public void start(Stage stage) throws Exception {
        player.setStage(stage);
        player.getScene().setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.F)
                stage.setFullScreen(true);
        });
        
        stage.setTitle("Audilizer");
        stage.setScene(player.getScene());
        
        stage.show();
        
        Popup helpPopup = new Popup();
        helpPopup.setX(300);
        helpPopup.setY(200);
        Label helptext = new Label("Hover mouse over left opener to access file manager and select an audiofile to be played"
                + "\nPress f to go fullscreen"
                + "\nControl playback from bottom opener or click screen"
                + "\nControl parameters from left opener");
        Button closebutton = new Button("close");
        closebutton.setOnAction((ActionEvent ae) -> {
           helpPopup.hide();
        });
        VBox box = new VBox(helptext, closebutton);
        box.setAlignment(Pos.CENTER);
        helpPopup.getContent().add(box);
        helpPopup.show(stage);
        
    }
    @Override
    public void stop() {
        try {
            settingdao.save();
        } catch (IOException ioe) {
            System.out.println("Could not save settings: " + ioe.getMessage());
        }
    }
    public static void main(String[] args) {
        launch(args);
    }    
}
