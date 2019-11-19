package com.tcd.ds.kde.ontps.model;

import java.util.ArrayList;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

public class QueryModel {

	private static OntModel ontModel;

	public QueryModel(OntModel ontModel) {
		this.ontModel = ontModel;
	}

	public static ResultSet executeQuery(String query) {
		ResultSet resultSet = null;
		try {
			Dataset dataset = DatasetFactory.create(ontModel);
			Query q = QueryFactory.create(query);
			QueryExecution qexec = QueryExecutionFactory.create(q, dataset);
			resultSet = qexec.execSelect();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return resultSet;

	}

	public static void displayResult(ResultSet resultSet) {

		while (resultSet.hasNext()) {
			QuerySolution row = (QuerySolution) resultSet.next();
			RDFNode nextMarried = row.get("x");
			System.out.print(nextMarried.toString());
			System.out.print(" is of type ");
			RDFNode nextSpouse = row.get("y");
			System.out.println(nextSpouse.toString());
		}
	}

	public static ArrayList<ArrayList<String>> agglomerateResults(ResultSet resultSet) {
		ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		String results = "";
		// Add columns to result
		ArrayList<String> coldata = new ArrayList<>();
		for (String col : resultSet.getResultVars()) {
			coldata.add(col);
		}
		result.add(coldata);
		while (resultSet.hasNext()) {
			ArrayList<String> rowdata = new ArrayList<>();
			QuerySolution row = (QuerySolution) resultSet.next();
			for (String col : resultSet.getResultVars()) {
				RDFNode node = row.get(col);
				rowdata.add(node.toString());
			}
			result.add(rowdata);
		}
		return result;
	}

}
