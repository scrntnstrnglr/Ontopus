package com.tcd.ds.kde.ontps.model;

public class Queries {
	public static String basicQuery1 =
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX ont: <http://lab.Jena.Kdeg.ie/Ontology1#>\n" +
					"SELECT ?x ?y WHERE {\n" +
					"?x a ?y.\n" +
					"}";
}
