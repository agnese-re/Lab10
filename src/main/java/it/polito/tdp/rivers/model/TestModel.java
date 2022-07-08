package it.polito.tdp.rivers.model;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		River river = new River(1);
		System.out.println(m.getFirstDate(river));
		System.out.println(m.getLastDate(river));
		System.out.println(m.getFMedio(river));
		System.out.println(m.getNMisurazioni(river));

	}

}
