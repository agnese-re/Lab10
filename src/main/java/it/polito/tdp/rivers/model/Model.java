package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.List;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {

	private RiversDAO dao;
	private Simulatore sim;
	
	public Model() {
		dao = new RiversDAO();
	}
	
	public List<River> getAllRivers() {
		return dao.getAllRivers();
	}
	
	public LocalDate getFirstDate(River river) {
		return this.dao.getAllFlows(river).get(0).getDay();
	}
	
	public LocalDate getLastDate(River river) {
		return this.dao.getAllFlows(river).get(this.dao.getAllFlows(river).size()-1).getDay();
	}
	
	public int getNMisurazioni(River river) {
		return this.dao.getAllFlows(river).size();
	}
	
	public double getFMedio(River river) {
		List<Flow> flows = this.dao.getAllFlows(river);
		double sommaFlows = 0.0;
		for(int i = 0; i < flows.size(); i++)
			sommaFlows += flows.get(i).getFlow();
		return sommaFlows/flows.size();
	}
	
	public List<Flow> getAllFlows(River river) {
		return this.dao.getAllFlows(river);
	}
	
	public void simulate(River river, double k) {
		sim = new Simulatore(this.getAllFlows(river),this.getFMedio(river));
		sim.init(k);
		sim.run();
	}
	
	public int getGiorniCarenza() {
		return sim.getGiorniCarenza();
	}
	
	public double getOccupazioneMedia() {
		return sim.getOccupazioneMedia();
	}
}
