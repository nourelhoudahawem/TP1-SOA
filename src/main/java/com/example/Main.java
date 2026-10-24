package com.example;



import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class Main {

    private static final String XML = "/ecole.xml";
    private static final String XSD = "/ecole.xsd";

    public static void main(String[] args) throws Exception {
        System.out.println("===== 1. Validation XML/XSD =====");
        if (!validerXML()) {
            System.out.println("Document invalide, arret du programme.");
            return;
        }

        System.out.println("\n===== 2. Interrogation XPath =====");
        Document doc = chargerDocument();
        XPath xpath = XPathFactory.newInstance().newXPath();

        afficher(xpath, doc, "Tous les etudiants (nom)",
                "//etudiant/nom");
         //recherche les éléments correspondants à n'importe quel niveau du document.
        afficher(xpath, doc, "Etudiants actifs",
                "//etudiant[@actif='true']/nom");
        //Le filtre conserve uniquement les étudiants dont la valeur est true.

        afficher(xpath, doc, "Etudiants de la classe 3LSI",
                "//etudiant[@classe='3LSI']/prenom");
        //La requête filtre les étudiants selon leur attribut classe.+

        afficher(xpath, doc, "Etudiants avec moyenne > 14.5",
                "//etudiant[moyenne > 14.5]/nom");
       //Rechercher les étudiants ayant une moyenne supérieure à 14,5
        afficher(xpath, doc, "Etudiants de specialite RT",
                "//etudiant[specialite='RT']/nom");
        //  Rechercher les étudiants de spécialité RT

        afficher(xpath, doc, "Enseignants du departement Informatique",
                "//enseignant[departement='Informatique']/nom");
        //Rechercher les enseignants du département Informatique

        afficher(xpath, doc, "Matieres avec coefficient >= 3",
                "/ecole/matieres/matiere[coefficient >= 3]/nom");
        //Rechercher les matières avec un coefficient supérieur ou égal à 3
        afficher(xpath, doc, "Salles de type labo",
                "//salle[@type='labo']/nom");
        //Rechercher les salles de type laboratoire
        afficher(xpath, doc, "Salles avec capacite > 100",
                "//salle[capacite > 100]/nom");
        //Rechercher les salles de capacité supérieure à 100
        afficher(xpath, doc, "Classes avec effectif > 29",
                "//classe[effectif > 29]/nom");
        //Rechercher les classes avec un effectif supérieur à 29

        double nb = (Double) xpath.evaluate("count(//etudiant)", doc, XPathConstants.NUMBER);
        System.out.println("\nNombre d'etudiants : " + (int) nb);

        double moy = (Double) xpath.evaluate(
                "sum(//etudiant/moyenne) div count(//etudiant)", doc, XPathConstants.NUMBER);
        System.out.println("Moyenne generale : " + String.format("%.2f", moy));
    }

    // ---------- Validation XML/XSD ----------
    private static boolean validerXML() {
        try (InputStream xml = Main.class.getResourceAsStream(XML);
             InputStream xsd = Main.class.getResourceAsStream(XSD)) {

            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));

            System.out.println("Le document XML est VALIDE.");
            return true;
        } catch (SAXException e) {
            System.out.println("Document INVALIDE : " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- Chargement DOM ----------
    private static Document chargerDocument() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try (InputStream xml = Main.class.getResourceAsStream(XML)) {
            return dbf.newDocumentBuilder().parse(xml);
        }
    }

    // ---------- Execution d'une requete XPath ----------
    private static void afficher(XPath xpath, Document doc, String titre, String requete)
            throws XPathExpressionException {
        System.out.println("\n-> " + titre + "   [" + requete + "]");
        XPathExpression expr = xpath.compile(requete);
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println("   - " + nodes.item(i).getTextContent());
        }
    }
}