package com.textprocessing.assignment.ontology.lookup;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlResponseHandler {

	private HashMap<String, String> operatorResponseContentMap = new HashMap<String, String>();
	private Document doc;
	private XPathFactory xFactory ;
	private XPath xpath;
	//private static XmlResponseHandler xmlHttpResponseHandler;
	
	
	public HashMap<String, String> processXmlResponseContent(HashMap<String, String> xmlOperatorKeyandPatterns) throws ParserConfigurationException, XPathExpressionException, SAXException, IOException {
		System.out.println(String.format("Process Xml response content - %s", xmlOperatorKeyandPatterns.toString()));
		Set<String> keyWords = xmlOperatorKeyandPatterns.keySet();
		Iterator<String> keyWordsIterator = keyWords.iterator();
		while (keyWordsIterator.hasNext()) {
			String key = (String) keyWordsIterator.next();
			String value = getValuesFromXmlBasedOnPattern(xmlOperatorKeyandPatterns.get(key));
			System.out.println(String.format("Xml Http Response : Key - %s, Value - %s", key, value));
			operatorResponseContentMap.put(key, value);
		}
		return operatorResponseContentMap;
	}
	
	public void parseXmlContent(String responseBodyAsString) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("Xpath parsing initiated");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		/*InputSource inputSource = new InputSource();
		inputSource.setEncoding(responseBodyAsString);*/
		doc = builder.parse(new InputSource(new StringReader(responseBodyAsString)));
		xFactory = XPathFactory.newInstance();
		xpath = xFactory.newXPath();
	}
	
	public String getValuesFromXmlBasedOnPattern(String xmlOperatorKeyandPatterns) throws XPathExpressionException {
	//	System.out.println(String.format("Getting the xml content with pattern - %s", xmlOperatorKeyandPatterns));
		XPathExpression expr = null;
		expr = xpath.compile(xmlOperatorKeyandPatterns);
		String value = (String) expr.evaluate(doc, XPathConstants.STRING);
	//	System.out.println(String.format("Xml Handler - Fetched Value - %s", value));
		return value;
	}
	
	public List<String> getAllValuesFromXmlBasedOnPattern(String xmlOperatorKeyandPatterns) throws XPathExpressionException {
	//	System.out.println(String.format("Getting the xml content with pattern - %s", xmlOperatorKeyandPatterns));
		List<String> values = new ArrayList<String>();
		XPathExpression expr = null;
		expr = xpath.compile(xmlOperatorKeyandPatterns);
		Object result =  expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
	    for (int i=0; i<nodes.getLength();i++){
		//	System.out.println(String.format("Xml Handler - Fetched Value - %s", nodes.item(i).getNodeValue()));
			values.add(nodes.item(i).getNodeValue());
	    }
	    return values;
	}
}
