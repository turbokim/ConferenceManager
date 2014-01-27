package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XMLHelper class helps with the redundancy of reading and appending to XML
 * files.
 * 
 * @author Kim Howard November 28, 2012
 * 
 */
public class XMLHelper {
	private XMLHelper() {
		throw new AssertionError();
	}

	/**
	 * Returns the node value of the Element with assigned tagName;
	 * 
	 * @param tagName
	 *            The tagname that the element references.
	 * @param e
	 *            The element in the XML file.
	 * @return The string node value.
	 */
	public static String nodeToString(String tagName, Element e) {

		NodeList nlist = e.getElementsByTagName(tagName);
		Element listElement = (Element) nlist.item(0);
		return listElement.getChildNodes().item(0).getNodeValue();

	}
	
	/**
	 * Adds the review Directory.
	 * 
	 * @param the_doc
	 * @param the_dir
	 * @param reviewerID
	 * @param xml_dir
	 */
	public static void addReviewDir(Document the_doc, String the_dir, String reviewerID, String xml_dir)
	{		
		Element xmlDoc = the_doc.getDocumentElement();
		NodeList listOfReviews = xmlDoc.getElementsByTagName("review");
		for(int i = 0; i < listOfReviews.getLength(); i++)
		{
			Node reviewNode = listOfReviews.item(i);
			Element reviewElement = (Element)reviewNode;					
			if(XMLHelper.nodeToString("reviewerID", reviewElement).equals(reviewerID))
			{
				NodeList nlist = reviewElement.getElementsByTagName("reviewFile");
				Node oldChild = nlist.item(0);
				
				Element newChild = the_doc.createElement("reviewFile");
				newChild.appendChild(the_doc.createTextNode(the_dir));
				reviewElement.replaceChild(newChild, oldChild);
				break;
			}
		}
		writeDoc(the_doc, xml_dir);		
	}
	/**
	 * Adds Directory for the Recommendation.
	 * 
	 * @param the_doc
	 * @param title
	 * @param the_dir
	 * @param xml_dir
	 */
	public static void changeRecDirectory(Document the_doc, String title, String the_dir, String xml_dir)
	{
		Element xmlDoc = the_doc.getDocumentElement();
		NodeList listOfManuscripts = xmlDoc.getElementsByTagName("manuscript");
		for(int i = 0; i < listOfManuscripts.getLength(); i++)
		{
			Node manuscriptNode = listOfManuscripts.item(i);
			Element manuscriptElement = (Element) manuscriptNode;
			
			if(title.equals(XMLHelper.nodeToString("title", manuscriptElement)))
			{
				NodeList recList = manuscriptElement.getElementsByTagName("recommendation");
				Element recElement = (Element)recList.item(0);
				NodeList nlist = recElement.getElementsByTagName("recFile");
				if(nlist.item(0)!= null)
				{
					Node oldChild = nlist.item(0);					
					Element newChild = the_doc.createElement("recFile");
					newChild.appendChild(the_doc.createTextNode(the_dir));
					recElement.replaceChild(newChild, oldChild);
				}
				
				break;				
			}
		}
		writeDoc(the_doc, xml_dir);		
	}
	/**
	 * Replaces the String representation of the Decision for a manuscript.
	 * @param the_doc
	 * @param title
	 * @param changedDecision
	 * @param xml_dir
	 */
	public static void changeManDecision(Document the_doc, String title, String changedDecision, String xml_dir)
	{
		Element xmlDoc = the_doc.getDocumentElement();
		NodeList listOfManuscripts = xmlDoc.getElementsByTagName("manuscript");
		for(int i = 0; i < listOfManuscripts.getLength(); i++)
		{
			Node manuscriptNode = listOfManuscripts.item(i);		
			Element manuscriptElement = (Element) manuscriptNode;
			if(title.equals(XMLHelper.nodeToString("title", manuscriptElement)))
			{
				NodeList nlist = manuscriptElement.getElementsByTagName("decision");
				Node oldChild = nlist.item(0);			
				
				Element newChild = the_doc.createElement("decision");
				newChild.appendChild(the_doc.createTextNode(changedDecision));
				manuscriptElement.replaceChild(newChild, oldChild);	
				break;
			}			
		}
		writeDoc(the_doc, xml_dir);
	}
	/**
	 * Adds a Subprogram Chair to the Manuscript xml
	 * @param the_doc
	 * @param manTitle
	 * @param userID
	 * @param xml_dir
	 */
	public static void addSubprogramChair(Document the_doc, String manTitle, String userID, String xml_dir)
	{
		Element xmlDoc = the_doc.getDocumentElement();
		NodeList listOfManuscripts = xmlDoc.getElementsByTagName("manuscript");
		for(int i = 0; i < listOfManuscripts.getLength(); i++)
		{
			Node manuscriptNode = listOfManuscripts.item(i);
			Element manuscriptElement = (Element) manuscriptNode;
			if(manTitle.equals(XMLHelper.nodeToString("title", manuscriptElement)))
			{
				NodeList nlist = manuscriptElement.getElementsByTagName("recommendation");
				Node recommendationNode = nlist.item(0);
				Element recommendation = (Element)recommendationNode;
				appendElement(the_doc, recommendation, "subprogramID", userID);		
				
				Element recFile = the_doc.createElement("recFile");
				recFile.appendChild(the_doc.createTextNode("null"));
				recommendation.appendChild(recFile);
				break;
				
			}
		}
		
		writeDoc(the_doc, xml_dir);
	}
	/**
	 * Adds a review element to the reviews.
	 * @param the_doc
	 * @param userID
	 * @param xml_dir
	 */
	public static void addReviewer(Document the_doc, String userID, String xml_dir)
	{
		Element xmlDoc = the_doc.getDocumentElement();
		Element review = the_doc.createElement("review");
		xmlDoc.appendChild(review);
		
		Element reviewerID = the_doc.createElement("reviewerID");
		reviewerID.appendChild(the_doc.createTextNode(userID));
		review.appendChild(reviewerID);
		
		Element reviewFile = the_doc.createElement("reviewFile");
		reviewFile.appendChild(the_doc.createTextNode("null"));
		review.appendChild(reviewFile);
		
		writeDoc(the_doc, xml_dir);
	}
	
	public static void updateReviewsXML(Document the_doc, User the_user, String file_dir, String xml_dir)
	{
		Node reviews = the_doc.getFirstChild();
		Element review = the_doc.createElement("review");
		reviews.appendChild(review);
		XMLHelper.appendElement(the_doc, review, "reviewerID", the_user.getUniqueID());
		XMLHelper.appendElement(the_doc, review, "reviewFile", file_dir);
		XMLHelper.writeDoc(the_doc, xml_dir);
	}
	
	/**
	 * Removes selected mauscript from a conference.	 * 
	 * @param title Title of the Manuscript being removed.
	 * @param conferenceManuscripts The Document containing the Manuscript.
	 */
	public static void removeManuscript(String title, Document conferenceManuscripts, String location)
	{		
		Element xmlDoc = conferenceManuscripts.getDocumentElement();
		NodeList listOfManuscripts = xmlDoc.getElementsByTagName("manuscript");
		for(int i = 0; i < listOfManuscripts.getLength(); i++)
		{
			Node manuscriptNode = listOfManuscripts.item(i);		
			Element manuscriptElement = (Element) manuscriptNode;
			if(title.equals(XMLHelper.nodeToString("title", manuscriptElement)))
			{
				xmlDoc.removeChild(manuscriptNode);
				break;
			}			
		}
		writeDoc(conferenceManuscripts, location);
		
	}
	/**
	 * UpdateManuscriptxXML
	 * Method appends the added manuscript to the XML file. A helper method is called
	 * to add the elements and write the document back to the file location. The helper
	 * method helps with redundancy.
	 *
	 * @author Kim Howard
	 */
	public static void updateManuscriptsXML(final String xml_dir, final String file_dir, Document the_doc, Manuscript the_manuscript) {   
		Node manuscripts = the_doc.getFirstChild();

		Element manuscript = the_doc.createElement("manuscript");
		manuscripts.appendChild(manuscript);

		XMLHelper.appendElement(the_doc, manuscript, "title", the_manuscript.getTitle());
		XMLHelper.appendElement(the_doc, manuscript, "authorID", the_manuscript.get_author().getUniqueID());
		XMLHelper.appendElement(the_doc, manuscript, "manFile", file_dir);
		XMLHelper.appendElement(the_doc, manuscript, "decision", "undecided");
		
		Element recommendation = the_doc.createElement("recommendation");				
		manuscript.appendChild(recommendation);
		
		//XMLHelper.appendElement(the_doc, manuscript, "recommendation", null);
		XMLHelper.writeDoc(the_doc, xml_dir);   
	} 
	
	

	/**
	 * Method appends an element to the passed in parent element along with the
	 * tagName and the textNode.
	 * 
	 * @param doc
	 *            The XML document.
	 * @param parent
	 *            The parent Element.
	 * @param tagName
	 *            The tag name for the element.
	 * @param textNode
	 *            The string text node.
	 */
	public static void appendElement(Document doc, Element parent,
			String tagName, String textNode) {
		Element e = doc.createElement(tagName);
		e.appendChild(doc.createTextNode(textNode));
		parent.appendChild(e);

	}

	/**
	 * Method takes in an XML file and parses it into a Document.
	 * 
	 * @param file
	 *            The XML file to be parsed.
	 * @return The parsed Document.
	 */
	public static Document getDoc(File file) {
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			builder = factory.newDocumentBuilder();
			doc = builder.parse(file);
		} catch (ParserConfigurationException e) {
			System.err.println("Serious Configuration Error!");
			e.getMessage();			
			e.printStackTrace();
		} catch (SAXException e) {
			e.getMessage();	
			e.printStackTrace();
		} catch (IOException e) {
			e.getMessage();	
			e.printStackTrace();
		}
		return doc;

	}

	/**
	 * Method writes a Document to a file location.
	 * 
	 * @param doc
	 *            The document to be written.
	 * @param location
	 *            The location where the file is to be written.
	 */
	public static void writeDoc(Document doc, String location) {
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(location));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Helper looks for reviewers in master user list
	 * 
	 * @param parent
	 * @return
	 */
	public static List<String> getNodeList(NodeList parent)
	{
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < parent.getLength(); i++)
		{
			Node userNode = parent.item(i);
			Element userElement = (Element) userNode;
			if(nodeToString("isReviewer", userElement).equals("yes"))
			{
				list.add(userElement.getAttribute("id"));
			}
		}
		return list;
	}
	
	/** 
	 * Helper method that returns an Element from a node list.
	 * 
	 * @param tagName The tagName
	 * @param e The element
	 * @param index The index of that element in the list
	 * @return The element.
	 */
	
	
	public static Element getListElement(String tagName, Element e, int index)
	{
		NodeList nlist = e.getElementsByTagName(tagName);
		return (Element)nlist.item(index);		
	}
}
