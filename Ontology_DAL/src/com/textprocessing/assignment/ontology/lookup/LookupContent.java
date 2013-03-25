package com.textprocessing.assignment.ontology.lookup;

import java.util.List;

public class LookupContent {

	private String label;
	private String URI;
	private List<String> classes;
	private List<String> categories;
	private int refCount;
	
	public LookupContent(String label, String uri, List<String> classes, List<String> categories, int refcount) {
		this.label = label;
		this.URI = uri;
		this.classes = classes;
		this.categories = categories;
		this.refCount = refcount;
	}
	
	@Override
	public String toString() {
		String content = "";
		if(classes != null && classes.size() > 0 && categories != null && categories.size() > 0 ){ 
			content = String.format("Label - %s || URI - %s || Total Class - %s || Class_1 - %s || Class_2 - %s || Total Categories - %s || Categories_1 - %s || Count - %s\n", label,
					URI, classes.size(), classes.get(0), classes.get(1), categories.size(), categories.get(0), refCount);
		}else if (classes != null && classes.size() > 0) {
			content = String.format("Label - %s || URI - %s || Total Class - %s || Class_1 - %s  || Count - %s\n", label,
					URI, classes.size(), classes.get(0), refCount);
		}else if(categories != null && categories.size() > 0 ){ 
			content = String.format("Label - %s || URI - %s || Total Categories - %s || Categories_1 - %s || Count - %s\n", label,
					URI, categories.size(), categories.get(0), refCount);
		}else{
			content = String.format("Label - %s || URI - %s || Count - %s\n", label,
					URI, refCount);
		}
		return content;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String uRI) {
		URI = uRI;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public int getRefCount() {
		return refCount;
	}

	public void setRefCount(int refCount) {
		this.refCount = refCount;
	}
	
}
