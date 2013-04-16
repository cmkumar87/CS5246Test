package com.textprocessing.assignment.processor;

import java.util.List;

public class OutputContent {

	public String original;
	public List<String> reformulatedQueries;
	
	public OutputContent(String original, List<String> reformulatedQueries) {
		this.original = original;
		this.reformulatedQueries = reformulatedQueries;
	}
	
	@Override
	public String toString() {
		return original + " || " + reformulatedQueries.toString() + "\n";
	}
}
