/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.rivers;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.rivers.model.Model;
import it.polito.tdp.rivers.model.River;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxRiver"
    private ComboBox<River> boxRiver; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtNumMeasurements"
    private TextField txtNumMeasurements; // Value injected by FXMLLoader

    @FXML // fx:id="txtFMed"
    private TextField txtFMed; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void setData(ActionEvent event) {
    	River riverScelto = this.boxRiver.getValue();
    	if(riverScelto != null) {
	    	this.txtStartDate.setText(this.model.getFirstDate(riverScelto).toString());
	    	this.txtEndDate.setText(this.model.getLastDate(riverScelto).toString());
	    	this.txtFMed.setText(String.format("%.3f",this.model.getFMedio(riverScelto)));
	    	this.txtNumMeasurements.setText("" + this.model.getNMisurazioni(riverScelto));
	    	// this.txtNumMeasurements.setText(String.valueOf(this.model.getNMisurazioni(riverScelto)));
    	}
    }
    
    @FXML
    void simula(ActionEvent event) {
    	txtResult.clear();
    	River riverScelto = this.boxRiver.getValue();
    	if(riverScelto != null) {
    		String fattoreS = this.txtK.getText();
    		try {
    			double fattore = Double.parseDouble(fattoreS);
    			if(fattore > 0) {
    				this.model.simulate(riverScelto, fattore);
    				txtResult.appendText("# giorni carenza = " + this.model.getGiorniCarenza() + "\n");
    				txtResult.appendText("# occupazione media = " + String.format("%.3f", this.model.getOccupazioneMedia()));
    				txtResult.setText("Simulazione terminata!\n");
    			} else {
    				txtResult.setText("Il fattore di scala deve essere un numero maggiore di 0");
    				return;
    			}
    		} catch(NumberFormatException nfe) {
    			txtResult.setText("Il fattore di scala (k) deve essere un numero positivo. Non stringhe");
    			return;
    		}
    	} else {
    		txtResult.appendText("Selezionare un river dal menu' a tendina");
    		return;
    	}
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxRiver != null : "fx:id=\"boxRiver\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtStartDate != null : "fx:id=\"txtStartDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtEndDate != null : "fx:id=\"txtEndDate\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNumMeasurements != null : "fx:id=\"txtNumMeasurements\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtFMed != null : "fx:id=\"txtFMed\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxRiver.getItems().addAll(this.model.getAllRivers());
    }
}
