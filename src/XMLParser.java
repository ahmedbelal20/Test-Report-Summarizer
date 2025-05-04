import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

	public static ArrayList<TestReport> parseXML(String directoryPath) {
		ArrayList<File> files = new ArrayList<>();
		files = getFiles(directoryPath);
		ArrayList<Document> documents = new ArrayList<>();
		documents = getDocuments(files);
		ArrayList<TestReport> testReports = new ArrayList<>();
		testReports = getTestReports(documents);
		return testReports;
	}

	private static ArrayList<File> getFiles(String directoryPath) {
		ArrayList<File> files = new ArrayList<>();
		File directory = new File(directoryPath);
		// Check if the given path is a directory
		File[] fileList = directory.listFiles();
		if (fileList != null) {
			for (File file : fileList) {
				if (file.isFile() && file.getName().toLowerCase().endsWith(".xml"))
					files.add(file);
			}
		}
		return files;
	}

	private static ArrayList<Document> getDocuments(ArrayList<File> files) {
		ArrayList<Document> parsedReports = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document report = builder.parse(files.get(i));
				parsedReports.add(report);
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return parsedReports;
	}

	private static ArrayList<TestReport> getTestReports(ArrayList<Document> documents) {
		ArrayList<TestReport> testReports = new ArrayList<>();
		for (int doc = 0; doc < documents.size(); doc++) {
			TestReport testReport = new TestReport();
			NodeList testCases = documents.get(doc).getElementsByTagName("testcase");
			for (int i = 0; i < testCases.getLength(); i++) {
				if (testCases.item(i).getNodeType() == Node.ELEMENT_NODE) {
					TestCase testCase = new TestCase();
					// Downcast the Node to an Element in case it was an Element Node
					Element testCaseElement = (Element) testCases.item(i);
					testCase.setNumber(testCaseElement.getAttribute("tcnumber"));
					// Get child Nodes under the Element testCaseElement.
					NodeList testCaseChildren = testCaseElement.getChildNodes();
					for (int j = 0; j < testCaseChildren.getLength(); j++) {
						if (testCaseChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
							Element testCaseChildElement = (Element) testCaseChildren.item(j);
							if (testCaseChildElement.getTagName() == "title")
								testCase.setTitle(testCaseChildElement.getTextContent());
							else if (testCaseChildElement.getTagName() == "verdict")
								testCase.setResult(testCaseChildElement.getAttribute("result"));
						}
					}
					testReport.addTestCase(testCase);
				}
			}
			NodeList testReportNodeList = documents.get(doc).getElementsByTagName("testreport");
			for (int i = 0; i < testReportNodeList.getLength(); i++) {
				if (testReportNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element testReportElement = (Element) testReportNodeList.item(i);
					// Get child Nodes under the Element testCaseElement.
					NodeList testReportChildren = testReportElement.getChildNodes();
					for (int j = 0; j < testReportChildren.getLength(); j++) {
						if (testReportChildren.item(j).getNodeType() == Node.ELEMENT_NODE) {
							Element testReportChildElement = (Element) testReportChildren.item(j);
							if (testReportChildElement.getTagName() == "title")
								testReport.setTitle(testReportChildElement.getTextContent());
						}
					}
				}
			}
//			testReport.printTestReport();
			testReports.add(testReport);
		}
		return testReports;
	}
}
