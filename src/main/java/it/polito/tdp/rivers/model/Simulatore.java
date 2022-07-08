package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulatore {

	// coda degli eventi
	private PriorityQueue<Evento> queue;
	
	// modello del mondo
	private Map<LocalDate,Double> occupazioni;
	
	// input
	private double fattore;			// k
	private double capacita;		// Q (m^3)
	private double occupazione;		// C iniziale
	
	private double flussoMinimo;	// f_out_min
	private List<Flow> flows;
	private double fmed;
	
	// output
	private int giorniCarenza;
	private double occupazioneMedia;
	
	/* COSTRUTTORE SIMULATORE */
	/* Si prevede la possibilità di effettuare più simulazioni, modificando
	 * il solo fattore di scala k. Simulazioni su uno stesso fiume, river */
	public Simulatore(List<Flow> flows, double fmed) {
		this.flows = flows;
		this.fmed = fmed;
	}
	
	/* INIZIALIZZAZIONE SIMULATORE */
	public void init(double fattore) {
		
		// inizializzazione parametri in input
		this.fattore = fattore;
		this.capacita = fattore*secToDay(this.fmed)*30;
		this.occupazione = this.capacita/2;
		
		this.flussoMinimo = 0.8*secToDay(this.fmed);
		
		// inizializzazione output
		giorniCarenza = 0;
		
		// inizializzazione stato del mondo
		this.occupazioni = new HashMap<LocalDate,Double>();
		this.occupazioni.put(flows.get(0).getDay().minusDays(1), this.occupazione);
		
		// inizializzazione coda prioritaria e precaricamento
		this.queue = new PriorityQueue<Evento>();	
		for(Flow f: flows)
			this.queue.add(new Evento(f.getDay(), secToDay(f.getFlow())));
	}
	
	/* SIMULAZIONE */
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
	}
	
	/* Supponiamo di avere un'occupazione iniziale pari a 30 il 31/12/2020. L'1 Gennaio abbiamo
	 * un flusso in entrata di 20. Il flusso in uscita minimo è pari a 10. Se quello che avevo nel
	 * bacino ieri più quello che mi è entrato oggi è minore del flusso minimo richiesto, allora non
	 * garantisco l'erogazione minima. In questo caso 30 + 20 > 10, quindi OK */
	private void processEvent(Evento e) {
		
		double caso = Math.random();
		if(caso < 0.05) {	// 5% di probabilità -> richiesto un flusso minimo maggiore di 10x
			if(this.occupazioni.get(e.getDate().minusDays(1)) + e.getFlow() >= 10*this.flussoMinimo) {
				if(this.occupazioni.get(e.getDate().minusDays(1)) + e.getFlow() - 10*this.flussoMinimo < this.capacita)
					this.occupazioni.put(e.getDate(), 
						this.occupazioni.get(e.getDate().minusDays(1)) + 
							e.getFlow() - 10*this.flussoMinimo);
				else 
					this.occupazioni.put(e.getDate(), this.capacita);
			} else {	// se non sufficiente occupazione pari a 0.0
				this.occupazioni.put(e.getDate(), 0.0);
				this.giorniCarenza++;
			}
		} else {	// nel 95% flusso minimo 'normale'
			if(this.occupazioni.get(e.getDate().minusDays(1)) + e.getFlow() >= this.flussoMinimo) {	// il flusso e' garantito
				if(this.occupazioni.get(e.getDate().minusDays(1)) + e.getFlow() - this.flussoMinimo < this.capacita)
					this.occupazioni.put(e.getDate(), 
							this.occupazioni.get(e.getDate().minusDays(1)) + 
								e.getFlow() - this.flussoMinimo);
				else	// tracimazione
					this.occupazioni.put(e.getDate(), this.capacita);
			} else {
				this.occupazioni.put(e.getDate(), 0.0);
				this.giorniCarenza++;
			}
		}
		
		
	}

	private double secToDay(double flow) {
		return flow*60*60*24;
	}

	public int getGiorniCarenza() {
		return giorniCarenza;
	}
	
	public double getOccupazioneMedia() {
		double sommaOcc = 0.0;
		for(Double occupazione: this.occupazioni.values())
			sommaOcc += occupazione;
		return sommaOcc/this.occupazioni.size();
	}
}
