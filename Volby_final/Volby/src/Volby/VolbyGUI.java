package Volby;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Trieda pre grafické užívateľské rozhranie volieb.
 */
public class VolbyGUI extends Application {

    private Volby volby; // Inštancia objektu Volby
    private TextArea textArea; // Textové pole v hlavnom okne pre zobrazenie výsledkov volieb
    private Scene mscene; // Inštancia premennej pre hlavné okno aplikácie
    private int indstrana; // Id vybranej strany
    private ArrayList<Integer> idkandidati; // Zoznam vybraných kandidátov
    private AnalyzaVoliebStrategy analyzaStrategy; // Aká stratégia sa použije na zobrazenie výsledkov
    private boolean kondialog = false; // Pomocná premenná pre dialóg ukončenia hlasovania
    private Label labelO; // LAbel - pre zobrazenie údajov o prihlásenom občanovi

    /**
     * Metóda pre spustenie grafického rozhrania.
     * 
     * @param primaryStage Hlavné okno aplikácie.
     */
    @Override
    public void start(Stage primaryStage) {
        // Inicializácia premenných a načítanie dát
        volby = new Volby(); // Inštancia objektu Volby
        // ak ešte nie sú uložené dáta v súbore volby.ser, tak toto slúži na počiatočné nastavenie
        // ide o stav ak sa aplikácia spustí po prvý raz, keď sa ešte nehlasovalo
        volby.import_obcan(); // naimportovanie obcanov do pola objektov typu Obcan
        volby.import_pstrany(); // naimportovanie obcanov do pola objektov typu Pstrana
        indstrana = 0;
        idkandidati = null;
        this.init(); // Počiatočná inicializácia stratégie na základnú

        // Vytvorenie hlavného okna
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Vytvorenie menu
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Menu");
        MenuItem hlasovanieItem = new MenuItem("Hlasovanie");
        hlasovanieItem.setOnAction(e -> hlasovanie(primaryStage));

        MenuItem analyzaZItem = new MenuItem("Výsledky -> Základná analýza (stratégia)");
        analyzaZItem.setOnAction(e -> zakladnaAnalyza());

        MenuItem analyzaDItem = new MenuItem("Výsledky -> Detailná analýza (stratégia)");
        analyzaDItem.setOnAction(e -> detailnaAnalyza());

        MenuItem saveItem = new MenuItem("Uložiť údaje");
        saveItem.setOnAction(e -> savetofile());

        MenuItem loadItem = new MenuItem("Načítať uložené údaje");
        loadItem.setOnAction(e -> loadfromFile());

        fileMenu.getItems().addAll(hlasovanieItem, analyzaZItem, analyzaDItem, saveItem, loadItem);
        menuBar.getMenus().addAll(fileMenu);

        // Vytvorenie panelu pre zobrazenie textu
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(400);
        loadfromFile(); // pri spustení aplikacie načíta uložený stav hlasovania zo súboru

        centerBox.getChildren().addAll(textArea);

        root.setTop(menuBar);
        root.setCenter(centerBox);

        mscene = new Scene(root, 800, 600);
        primaryStage.setScene(mscene);
        primaryStage.setTitle("Voľby");
        primaryStage.show();
    }

    /**
     * Metóda pre prihlásenie sa k hlasovaniu.
     * 
     * @param primaryStage Hlavné okno aplikácie.
     */
    private void hlasovanie(Stage primaryStage) {
        // Vytvorenie komponentov pre prihlásenie
        labelO = new Label("Prihlásený: ");
        Label label = new Label("Zadajte svoje ID:");
        Label label2 = new Label("");
        TextField idField = new TextField();
        Button loginButton = new Button("Prihlásiť sa");

        // Akcia pre tlačidlo prihlásenia
        loginButton.setOnAction(e -> {
            try { // ošetrenie, ak sa do políčka zadá iná hodnota ako číslo
                int login_id = Integer.parseInt(idField.getText());
                if (overenie_obcana(login_id, label2)) {
                    labelO.setText("Prihlásený: "+volby.getPrihlaseny().getInfo());
                    zoznam_p_stran(primaryStage);
                    ((Stage) loginButton.getScene().getWindow()).close();
                    vykonatAnalyzuVolieb();
                }
            } catch (NumberFormatException e1) {
                label2.setText("Údaj musí byť číslo!");
                idField.clear();
            }
        });

        // Vytvorenie rozloženia pre komponenty
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        HBox buttonsBox = new HBox();
        buttonsBox.getChildren().addAll(loginButton);
        layout.getChildren().addAll( label, idField, label2, buttonsBox);

        // Vytvorenie a zobrazenie modálneho okna pre prihlásenie
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.setScene(new Scene(layout));
        modalStage.setTitle("Prihlásenie na hlasovanie");
        modalStage.showAndWait();
    }

    /**
     * Metóda pre overenie občana.
     * 
     * @param login_id ID občana, ktorý sa prihlasuje.
     * @param label2 Label pre zobrazenie informácií o úspešnosti prihlásenia.
     * @return True, ak je občan úspešne overený, inak false.
     */
    private boolean overenie_obcana(int login_id, Label label2) {
        int stav = volby.overenie_volica(login_id);
        // podľa návratovej hodnoty overenia sa vypíše info 
        if (stav == -2) {
            label2.setText("Nie ste v zozname občanov SR.");
            return false;
        }
        if (stav == -1) {
            label2.setText("Nie ste plnoletý.");
            return false;
        }
        if(!volby.zapis_do_volicov()) { // zisťovanie či už volil, ak nie, zapíše občana do zoznamu voličov
            label2.setText("Už ste hlasovali.");
            return false;
        }
        return true;
    }

    /**
     * Metóda pre výpis pol strán s následným výberom jednej strany.
     *
     * @param primaryStage hlavné okno aplikácie
     */
    private void zoznam_p_stran(Stage primaryStage) {
        labelO.setText("Prihlásený: "+volby.getPrihlaseny().getInfo());
        Label label = new Label("Zoznam politických strán:");
        Label label2 = new Label("");

        Map<Integer, String> strany = volby.zoznam_stran_asc();

        ObservableList<String> items = FXCollections.observableArrayList(strany.values());
        ListView<String> stranyListView = new ListView<>(items);

        stranyListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            indstrana = -1;
            if (newVal != null) {
                int index = getKeyFromValue(strany, newVal);
                if(index != -1) {
                    indstrana = index;
                }
            }
        });

        Button vyberButton = new Button("OK");
        vyberButton.setOnAction(event -> {
            if(indstrana == -1) {
                label2.setText("Nevybrali ste žiadnu stranu!");
            } else {
            	// ak bola zvolená politická strana, tak sa zozbrazí zoznam kandidátov strany
                zoznam_kandidatov(primaryStage, indstrana);
                ((Stage) vyberButton.getScene().getWindow()).close();
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(labelO, label, stranyListView, label2, vyberButton);

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.setScene(new Scene(layout));
        modalStage.setTitle("Výber politickej strany");
        modalStage.showAndWait();
    }

    /**
     * Metóda pre získanie kľúča z mapy podľa hodnoty (asociatívneho poľa).
     *
     * @param map   mapa, z ktorej sa má získať kľúč
     * @param value hodnota, pre ktorú sa má získať kľúč
     * @return kľúč k hodnote v mape, alebo -1, ak sa hodnota nenašla
     */
    public static int getKeyFromValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Metóda pre zobrazenie kandidátov strany s následným výberom max štyroch kandidátov
     *
     * @param primaryStage hlavné okno aplikácie
     * @param idstrana      identifikátor politického strany
     */
    private void zoznam_kandidatov(Stage primaryStage, int idstrana) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        ArrayList<Kandidat> kand = volby.getKandidati_by_ids(idstrana);
        Map<Integer, String> itemsMap = new HashMap<>();

        for (Kandidat k : kand) {
            itemsMap.put(k.getIdkandidat(), k.getIdkandidat() + ". " + k.getInfo());
        }

        ObservableList<String> items = FXCollections.observableArrayList(itemsMap.values());
        ListView<String> listView = new ListView<>(items);
        ObservableList<String> selectedItems = FXCollections.observableArrayList();

        ArrayList<Integer> pomvk = new ArrayList<>();
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            if (newVal != null) {
                int index = getKeyFromValue(itemsMap, newVal);
                if(index != -1) {
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

        Button hlasovatbutton = new Button("Hlasovať");
        hlasovatbutton.setOnAction(event -> {
            idkandidati = pomvk;
            if(potvrdenie_hlasovania(primaryStage)) ((Stage) hlasovatbutton.getScene().getWindow()).close();
        });

        ListView<String> selectedListView = new ListView<>();
        selectedListView.setItems(selectedItems);

        labelO.setText("Prihlásený: "+volby.getPrihlaseny().getInfo());
        Label labell = new Label("Zoznam kandidátov:");
        Label labelp = new Label("Vybraní:");

        VBox vboxl = new VBox(labell, listView);
        VBox vboxp = new VBox(labelp, selectedListView, hlasovatbutton);

        HBox hbox = new HBox(vboxl, vboxp);

        layout.getChildren().addAll(labelO, hbox);

        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(primaryStage);
        modalStage.setScene(new Scene(layout));
        modalStage.setTitle("Zoznam kandidátov:");
        modalStage.showAndWait();
    }

    /**
     * Metóda pre potvrdenie hlasovania, stačí ak je vybraná pol. strana aj bez kandidátovov.
     *
     * @param primaryStage hlavné okno aplikácie
     * @return true, ak bolo hlasovanie potvrdené, inak false
     */
    private boolean potvrdenie_hlasovania(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ukončenie hlasovania");
        alert.setHeaderText("Ukončiť hlasovanie a odoslať?");
        kondialog = false;

        ButtonType anoButton = new ButtonType("Áno");
        ButtonType nieButton = new ButtonType("Nie");
        alert.getButtonTypes().setAll(anoButton, nieButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == anoButton) {
                if(volby.zapis_hlasovanie( indstrana, idkandidati)) {
                    savetofile();  // uloženie výsledku hlasovania do súboru
                    textArea.clear();
                    textArea.setText("Ďakujeme za účasť vo voľbách.");
                    primaryStage.setScene(mscene);
                    this.textArea.appendText(analyzaStrategy.vykonajAnalyzu(volby));
                    labelO.setText("Prihlásený: ");
                    kondialog = true;
                }
            } else if (buttonType == nieButton) {
                kondialog = false;
            }
        });
        return kondialog;
    }

    /**
     * Metódy pre nastavenie a vykonanie analýzy volieb.
     */
    /**
     * Základná analýza.
     */
    private void zakladnaAnalyza() {
        nastavitZakladnuAnalyzu();
        vykonatAnalyzuVolieb();
    }

    /**
     * Detailná analýza.
     */
    private void detailnaAnalyza() {
        nastavitDetailnuAnalyzu();
        vykonatAnalyzuVolieb();
    }

    /**
     * Metódy pre uloženie a načítanie údajov zo súboru.
     */
    private void savetofile() {
        try {
            FileOutputStream fileOut = new FileOutputStream("volby.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(volby);
            out.close();
            fileOut.close();
            textArea.setText("Dáta boli uložené do súboru 'volby.ser'.\n");
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
            textArea.setText("Dáta boli načítané zo súboru 'volby.ser'.\n");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicializácia a nastavenie stratégie pri inicializácii aplikácie.
     */
    @Override
    public void init() {
        analyzaStrategy = new ZakladnaAnalyzaStrategy();
    }

    /**
     *  Nastavenie stratégie na základnej analýze.
     */
    private void nastavitZakladnuAnalyzu() {
        analyzaStrategy = new ZakladnaAnalyzaStrategy();
    }

    /**
     *  Nastavenie stratégie na detailnej analýze.
     */
    private void nastavitDetailnuAnalyzu() {
        analyzaStrategy = new DetailnaAnalyzaStrategy();
    }

    /**
     *  Pri vykonaní analýzy volieb voláme metódu vykonajAnalyzu() z aktuálne nastavenej stratégie.
     */
    private void vykonatAnalyzuVolieb() {
        this.textArea.clear();
        this.textArea.appendText(analyzaStrategy.vykonajAnalyzu(volby));
    }

    /**
     *  Spustenie aplikácie
     *  
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
