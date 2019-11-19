package com.tcd.ds.kde.ontps.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.log4j.BasicConfigurator;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.ResultSet;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.JenaException;
import com.tcd.ds.kde.ontps.utils.FileManager;

public class CreateModel {

	public static String ontologiesBase = "http://lab.Jena.Kdeg.ie/";

	public static String relationshipBase = "http://relationships.lab.Jena.Kdeg.ie/";

	public static String baseNs;

	public static String ontologyName = "Ontology1";

	public static OntModel ontology;

	public static OntModel createModel() {

		// ontologyName=args[0];
		baseNs = ontologiesBase + ontologyName + "#";
		ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		BasicConfigurator.configure();

		OntClass Human = ontology.createClass(baseNs + "Human");
		OntClass Animal = ontology.createClass(baseNs + "Animal");
		OntClass Dog = ontology.createClass(baseNs + "Dog");
		OntClass Cat = ontology.createClass(baseNs + "Cat");
		OntClass Bird = ontology.createClass(baseNs + "Bird");
		Animal.addSubClass(Dog);
		Animal.addSubClass(Cat);
		Animal.addSubClass(Bird);

		OntClass Man = ontology.createClass(baseNs + "Man");
		OntClass Woman = ontology.createClass(baseNs + "Woman");

		Human.addSubClass(Man);
		Human.addSubClass(Woman);

		OntClass Burmese = ontology.createClass(baseNs + "Burmese");
		OntClass Siamese = ontology.createClass(baseNs + "Siamese");
		Cat.addSubClass(Burmese);
		Siamese.addSuperClass(Cat);

		Dog.addDisjointWith(Cat);

		OntProperty chases = ontology.createObjectProperty(baseNs + "chases");
		chases.addDomain(Dog);
		chases.addRange(Cat);

		OntProperty chasedby = ontology.createObjectProperty(baseNs + "chased_by");
		chasedby.addInverseOf(chases);

		OntProperty runsAfter = ontology.createObjectProperty(baseNs + "runs_after");
		runsAfter.addEquivalentProperty(chases);

		OntProperty eats = ontology.createObjectProperty(baseNs + "eats");
		chases.addSubProperty(eats);

		Dog.createIndividual(baseNs + "Snoopy");
		Dog.createIndividual(baseNs + "Snowy");
		Dog.createIndividual(baseNs + "Airbud");
		Dog.createIndividual(baseNs + "Kujo");
		Dog.createIndividual(baseNs + "Scooby");
		Dog.createIndividual(baseNs + "Spike");
		Cat.createIndividual(baseNs + "Garfield");

		try {
			writeToFile(ontologyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			ReadModel.loadAllClassesOnt(ontologyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return ontology;
	}

	public static OntModel importFromFilex(String file) throws FileNotFoundException {
		//ontology = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		ontology=ModelFactory.createOntologyModel();
		InputStream inx = new FileInputStream(new File(file));
		ontology.read(inx,null,"TTL");
		try {
			//InputStream in = FileManager.get().open(file);
			InputStream in = new FileInputStream(new File(file));
			try {
				ontology.read(in, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JenaException je) {
			System.err.println("ERROR" + je.getMessage());
			je.printStackTrace();
			System.exit(0);
		}
		return ontology;
	}
	
	public static OntModel importFromFile(String file) throws FileNotFoundException {
		ontology=ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		try {
			InputStream in = new FileInputStream(new File(file));
			try {
				if(FileManager.getExtension(file).equalsIgnoreCase("ttl")) {
					ontology.read(in, null,"TTL");}
				else
					ontology.read(in,null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JenaException je) {
			System.err.println("ERROR" + je.getMessage());
			je.printStackTrace();
			System.exit(0);
		}
		return ontology;
	}

	public static void writeToFile(String filename) throws FileNotFoundException {
		try {
			ontology.write(new FileOutputStream(new File(filename)), "RDF/XML-ABBREV");
			System.out.println("Ontology written to file.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void joinmultiple(String f1, String f2) {
		Model model = RDFDataMgr.loadModel(f1) ;
		RDFDataMgr.read(model, f2) ;
		if(model!=null) {
			System.out.println("Model created!");
		}
		ontology=ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		ontology.add(model);
		if(ontology!=null) {
			System.out.println("Ontology created!");
		}
		
		
		QueryModel queryModel = new QueryModel(ontology);
		/*String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX ont: <http://lab.Jena.Kdeg.ie/Ontology1#>\n" + 
				"SELECT ?x ?y WHERE {\n" + 
				"?x a ?y.\n" + 
				"}"; */
		
		String query="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX ont: <http://lab.Jena.Kdeg.ie/Ontology1#>\n" + 
				"PREFIX can: <http://www.example.org/candidateconst/>\n" + 
				"PREFIX con: <https://electionsireland.org/results/const/>\n" + 
				"PREFIX cr: <https://electionsireland.org/results/general/candidacyresult/>\n" + 
				"SELECT ?x ?y WHERE {\n" + 
				"?x <http://www.example.org/candidateconst/constID> ?z .\n" + 
				"?z  <http://xmlns.com/foaf/0.1/hasName> ?y .\n" + 
				"}";
		
		
		ResultSet rs=queryModel.executeQuery(query);
		queryModel.displayResult(rs);
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		CreateModel modelCreator = new CreateModel();
		//OntModel ont = modelCreator.importFromFile("Ontology1");
		modelCreator.joinmultiple("candidate.ttl", "const.ttl");
		modelCreator.writeToFile("myowl.owl");

	}

}
