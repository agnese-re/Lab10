package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
	List<River> rivers;
	//gli eventi sono i Flow
	private PriorityQueue<Flow> queue;

	public Model() {
		RiversDAO riversDao = new RiversDAO();
		// Get all the data from the DB
		rivers = riversDao.getAllRivers();
		for (River river : rivers) {
			riversDao.getFlows(river);
		}
	}
	
	public List<River> getRivers() {
		return rivers;
	}
	
	public LocalDate getStartDate(River river) {
		if (!river.getFlows().isEmpty()) {
			return river.getFlows().get(0).getDay();
		}
		return null;
	}

	public LocalDate getEndDate(River river) {
		if (!river.getFlows().isEmpty()) {
			return river.getFlows().get(river.getFlows().size() - 1).getDay();
		}
		return null;
	}
	
	public int getNumMeasurements(River river) {
		return river.getFlows().size();
	}

	public double getFMed(River river) {
		double avg = 0;
		for (Flow f : river.getFlows())
			avg += f.getFlow();
		avg /= river.getFlows().size();
		river.setFlowAvg(avg);
		return avg;
	}
	
	public SimulationResult simulate(River river, double k) {
		//inizializzo la coda
		this.queue = new PriorityQueue<Flow>();
		this.queue.addAll(river.getFlows());
		
		List<Double> capacity = new ArrayList<Double>();
		
		double Q = k * 30 * convertM3SecToM3Day(river.getFlowAvg());
		double C = Q / 2;
		double fOutMin = convertM3SecToM3Day(0.8 * river.getFlowAvg());
		int numberOfDays = 0;

		System.out.println("Q: " + Q);
		
		Flow flow;
		while((flow = this.queue.poll()) != null) {
			System.out.println("Date: " + flow.getDay());
			
			double fOut = fOutMin;

			// C'è il 5% di possibilità che fOut sia 10 volte fOutMin
			if (Math.random() > 0.95) {
				fOut = 10 * fOutMin;
				System.out.println("10xfOutMin");
			}

			System.out.println("fOut: " + fOut);
			System.out.println("fIn: " + convertM3SecToM3Day(flow.getFlow()));

			// Aggiungo fIn a C
			C += convertM3SecToM3Day(flow.getFlow());

			// Se C è maggiore della capacità massima
			if (C > Q) {
				// Tracimazione
				// La quantità in più esce.
				C = Q;
			}

			if (C < fOut) {
				// Non riesco a garantire la quantità minima.
				numberOfDays++;
				// Il bacino comunque si svuota
				C = 0;
			} else {
				// Faccio uscire la quantità giornaliera
				C -= fOut;
			}

			System.out.println("C: " + C + "'\n");

			// Mantengo un lista della capacità giornaliere del bacino
			capacity.add(C);
		}
		

		// Calcolo la media della capacità
		double CAvg = 0;
		for (Double d : capacity) {
			CAvg += d;
		}
		CAvg = CAvg / capacity.size();
		return new SimulationResult(CAvg, numberOfDays);
	}
	
	public double convertM3SecToM3Day(double input) {
		return input * 60 * 60 * 24;
	}

	public double convertM3DayToM3Sec(double input) {
		return input / 60 / 60 / 24;
	}


		
}
