package it.polito.tdp.rivers.model;

import java.time.LocalDate;

public class Evento implements Comparable<Evento> {

	private LocalDate date;
	private double flow;
	
	public Evento(LocalDate date, double flow) {
		this.date = date;
		this.flow = flow;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getFlow() {
		return flow;
	}

	@Override
	public int compareTo(Evento o) {
		return this.getDate().compareTo(o.getDate());
	}

	@Override
	public String toString() {
		return date + "  " + flow;
	}
	
	
	
}
