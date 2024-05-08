package Volby;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VolbyGUI extends Application {

    private Volby volby;
    private TextArea textArea;
    private Scene mscene;
    private int indstrana;
    private ArrayList<Integer> idkandidati; 
    

    @Override
    public void start(Stage primaryStage) {
        volby = new Volby();
        volby.import_obcan();
        volby.import_pstrany();
        indstrana = 0;
        idkandidati = null;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Menu");
        MenuItem hlasovanieItem = new MenuItem("Hlasovanie");
        hlasovanieItem.setOnAction(e -> hlasovanie(primaryStage));

        MenuItem analyzaZItem = new MenuItem("Výsledky -> Základná analýza");
        analyzaZItem.setOnAction(e -> zakladnaAnalyza());

        MenuItem analyzaDItem = new MenuItem("Výsledky -> Detailná analýza");
        analyzaDItem.setOnAction(e -> detailnaAnalyza());

        MenuItem saveItem = new MenuItem("Uložiť údaje");
        saveItem.setOnAction(e -> savetofile());

        MenuItem loadItem = new MenuItem("Načítať uložené údaje");
        loadItem.setOnAction(e -> loadfromFile());

        fileMenu.getItems().addAll(hlasovanieItem, analyzaZItem, analyzaDItem, saveItem, loadItem);
        menuBar.getMenus().addAll(fileMenu);

        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(400);

        //Button hlasovanieButton = new Button("Hlasovanie");
        //hlasovanieButton.setOnAction(e -> hlasovanie(primaryStage, textArea));
        //Button vysledkyButton = new Button("Výsledky -> Základná analýza");
        //vysledkyButton.setOnAction(e -> zakladnaAnalyza());
        //Button vysledkyDetailButton = new Button("Výsledky -> Detailná analýza");
        //vysledkyDetailButton.setOnAction(e -> detailnaAnalyza());

        //centerBox.getChildren().addAll(textArea, hlasovanieButton, vysledkyButton, vysledkyDetailButton);
        centerBox.getChildren().addAll(textArea);

        root.setTop(menuBar);
        root.setCenter(centerBox);

        mscene = new Scene(root, 800, 600);
        primaryStage.setScene(mscene);
        primaryStage.setTitle("Voľby");
        primaryStage.show();
    }

    private void hlasovanie(Stage primaryStage) {
        Label label = new Label("Zadajte svoje ID:");
        Label label2 = new Label("");
        TextField idField = new TextField();
        Button loginButton = new Button("Prihlásiť sa");
        Button spatButton = new Button("Späť");

        loginButton.setOnAction(e -> {
        	try {
	        	int login_id = Integer.parseInt(idField.getText());
	            if (overenie_obcana(login_id, label2)) {
	                String strana = zoznam_p_stran(primaryStage, textArea);
	            }
        	} catch (NumberFormatException e1) {
        	    // Handle the case where the text entered is not a valid integer
        	    // For example, you can display an error message to the user
        	    label2.setText("Udaj musí byť čislo!");
        	    idField.clear();
        	}
        });
        spatButton.setOnAction(e -> {
        	primaryStage.setScene(mscene);
        });
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        HBox buttonsBox = new HBox();
        buttonsBox.getChildren().addAll(loginButton, spatButton);

        //layout.getChildren().addAll(label, idField, loginButton, label2, spatButton);
        layout.getChildren().addAll(label, idField, label2, buttonsBox);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
    }

    private boolean overenie_obcana(int login_id, Label label2) {
        int stav = volby.overenie_volica(login_id);
        if (stav == -2) {
        	label2.setText("Nie ste v zozname občanov SR.");
            return false;
        }
        if (stav == -1) {
            label2.setText("Nie ste plnoletý.");
            return false;
        }
        if(!volby.zapis_do_volicov()) {
        	label2.setText("Uz ste hlasovali.");
        	return false;
        }
        return true;
    }

    private String zoznam_p_stran(Stage primaryStage, TextArea textArea) {
    	 VBox layout = new VBox(10);
    	 layout.setPadding(new Insets(10));
         HBox buttonsBox = new HBox();
         
         Label label = new Label("Zoznam politických strán:");
         Label label2 = new Label("");
         Scene pscene = new Scene(layout, 400, 300);
         
         
         Map<Integer, String> strany = volby.zoznam_stran_asc();
    	
         ObservableList<String> items = FXCollections.observableArrayList(strany.values());
         ListView<String> stranyListView = new ListView<>(items);

         stranyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        	 indstrana = -1;
        	 if (newVal != null) {
                 int index = getKeyFromValue(strany, newVal);
                 if(index!=-1) {
                	 indstrana = index;
                 }
             }        	 
         });
         
         // Vytvoríme tlačidlo na získanie vybranej hodnoty z ListView
         Button vyberButton = new Button("OK");
         vyberButton.setOnAction(event -> {
             // Získame vybrany index z ListView           
             if(indstrana==-1) {
             	label2.setText("Nevybrali ste žiadnu stranu!");
             } else {
             	zoznam_kandidatov(primaryStage, pscene, indstrana);
             }
         });
         
         Button spatButton = new Button("Späť");
         spatButton.setOnAction(e -> {
         	primaryStage.setScene(mscene);
         });

         buttonsBox.getChildren().addAll(vyberButton, spatButton);
         layout.getChildren().addAll(label, stranyListView, label2, buttonsBox);

         primaryStage.setScene(pscene);
         primaryStage.setTitle("Výber politickej strany");
         return null;
    }
    
    public static int getKeyFromValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1; // Ak položka nie je nájdená
    }

    private String zoznam_kandidatov(Stage primaryStage, Scene pscene, int idstrana) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
    	ArrayList<Kandidat> kand = volby.getKandidati_by_ids(idstrana);
    	Map<Integer, String> itemsMap = new HashMap<>();

		for (Kandidat k : kand) {
			itemsMap.put(k.getIdkandidat(), k.getIdkandidat()+". "+k.getInfo());
		}
        
        ArrayList<Integer> pomvk = new ArrayList<>();

        ObservableList<String> items = FXCollections.observableArrayList(itemsMap.values());

        ListView<String> listView = new ListView<>(items);
        ObservableList<String> selectedItems = FXCollections.observableArrayList();

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        	String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (newVal != null) {
                int index = getKeyFromValue(itemsMap, newVal);
                if(index!=-1) {
	                if (selectedItems.contains(selectedItem)) {
	                    if (selectedItems.size() > 4) {
	                        selectedItems.remove(selectedItem);
	                    }
	                } else {
	                    if (selectedItems.size() < 4) {
	                        selectedItems.add(selectedItem);
	                        pomvk.add(index);
	                    }
	                }
                }
            }
        });
        
        // Vytvoríme tlačidlo na získanie vybraných položiek
        Button hlasovatbutton = new Button("Hlasovať");
        hlasovatbutton.setOnAction(event -> {
        	idkandidati = pomvk;
        	potvrdenie_hlasovania(primaryStage);
        });
        
        ListView<String> selectedListView = new ListView<>();
        selectedListView.setItems(selectedItems);
        
        Label labell = new Label("Zoznam andidátov:");
        
        Label labelp = new Label("Vybraní:");
        Button spatButton = new Button("Späť");
        spatButton.setOnAction(e -> {
        	primaryStage.setScene(pscene);
        });
        
        VBox vboxl = new VBox(labell, listView, spatButton);
        VBox vboxp = new VBox(labelp, selectedListView, hlasovatbutton);
        
        HBox hbox = new HBox(vboxl, vboxp);
        		
        layout.getChildren().addAll(hbox);
        
        // Inicializujeme scénu a nastavíme ju na hlavnú plochu
        Scene kscene = new Scene(layout, 500, 400);

        primaryStage.setScene(kscene);
        primaryStage.setTitle("Zoznam kandidátov:");
        return null;
    }
    
    private void potvrdenie_hlasovania(Stage primaryStage) {
	    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    alert.setTitle("Ukončenie hlasovania");
	    alert.setHeaderText("Ukončiť hlasovanie a odoslať?");
	    
        // Nastaviť typy tlačidiel (Áno/Nie) a upraviť texty
        ButtonType anoButton = new ButtonType("Áno");
        ButtonType nieButton = new ButtonType("Nie");
        alert.getButtonTypes().setAll(anoButton, nieButton);
	
        // Zobraziť dialogové okno a čakať na výsledok
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == anoButton) {
    			if(volby.zapis_hlasovanie( indstrana, idkandidati)) {
    				savetofile();
    				textArea.clear();
    				textArea.setText("Ďakujem za účasť vo voľbách.");
    				primaryStage.setScene(mscene);
    			}
            } else if (buttonType == nieButton) {

            }
        });
    }
    
    
    private void zakladnaAnalyza() {
        ZakladnaAnalyzaVolieb analyza = new ZakladnaAnalyzaVolieb(volby);
        textArea.clear(); // Vymažeme predchádzajúci obsah

        // Výpis počtu odovzdaných hlasov do textArea
        textArea.appendText("Počet všetkých odovzdaných hlasov vo voľbách: " + analyza.pocetHlasov() + "\n\n");

        ArrayList<String> poradieStrd = analyza.poradieStran();

        // Ak je zoznam strán neprázdny, vypíšeme poradie strán a zvolených poslancov
        if (poradieStrd != null && !poradieStrd.isEmpty()) {
            textArea.appendText("Poradie strán podla počtu získaných hlasov:\n");
            for (String pstr : poradieStrd) {
                textArea.appendText(pstr + "\n");
            }

            textArea.appendText("\nZoznam kandidatov podla počtu získaných hlasov:\n");
            ArrayList<String> poradieKd = analyza.poradieKandidatov();
            if (poradieKd != null && !poradieKd.isEmpty()) {
                for (String pk : poradieKd) {
                    textArea.appendText(pk + "\n");
                }
            } else {
                textArea.appendText("Málo hlasov na zostavenie parlamentu.\n");
            }
        } else {
            textArea.appendText("Málo hlasov na zostavenie parlamentu.\n");
        }
    }


    private void detailnaAnalyza() {
        DetailnaAnalyzaVolieb analyzaD = new DetailnaAnalyzaVolieb(volby);
        textArea.clear(); // Vymažeme predchádzajúci obsah

        textArea.appendText("Výsledky volieb a rozdelenie " + analyzaD.getPocetM() + " mandátov.\n\n");
        // Výpis počtu odovzdaných hlasov do textArea
        textArea.appendText("Počet všetkých odovzdaných hlasov vo voľbách: " + analyzaD.pocetHlasov() + "\n\n");

        if(analyzaD.getPocetM()>analyzaD.pocetHlasov()) {
        	textArea.appendText("Málo hlasov na zostavenie parlamentu.\n");
        } else {
        	ArrayList<String> poradieStr = analyzaD.poradieStran();
        	ArrayList<String> poradieKd = analyzaD.poradieKandidatov();
	
	        // Ak je zoznam strán neprázdny, vypíšeme poradie strán a zvolených poslancov
	        if (poradieStr != null && !poradieStr.isEmpty() && poradieKd != null && !poradieKd.isEmpty()) {
	            textArea.appendText("Poradie strán:\n");
	            for (String pstr : poradieStr) {
	                textArea.appendText(pstr + "\n");
	            }
	
	            textArea.appendText("\nZoznam zvolených poslancov:\n");
	            for (String pk : poradieKd) {
	            	textArea.appendText(pk + "\n");
	            }
	        } else {
	            textArea.appendText("Málo hlasov na zostavenie parlamentu.\n");
	        }
        }
    }

    private void savetofile() {
        try {
            FileOutputStream fileOut = new FileOutputStream("volby.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(volby);
            out.close();
            fileOut.close();
            appendToTextArea("Data boli uložen do souboru 'volby.ser'.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadfromFile() {
        try {
            FileInputStream fileIn = new FileInputStream("volby.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            volby = (Volby) in.readObject();
            in.close();
            fileIn.close();
            appendToTextArea("Data boli načítané zo souboru 'volby.ser'.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }        
    }
    
    private void appendToTextArea(String message) {
        textArea.appendText(message + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
