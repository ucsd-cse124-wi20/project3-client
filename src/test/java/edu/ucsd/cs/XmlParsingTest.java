package edu.ucsd.cs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParsingTest {

    @Test
    public void testXMLParsing() {

        try {

            File fXmlFile = new File("src/test/java/edu/ucsd/cs/stream.mpd");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            assertEquals(doc.getDocumentElement().getNodeName(), "MPD");

            NodeList repList = doc.getElementsByTagName("Representation");
            for (int repnum = 0; repnum < repList.getLength(); repnum++) {

                Node rNode = repList.item(repnum);
                assertEquals(rNode.getNodeName(), "Representation");
                assertEquals(rNode.getNodeType(), Node.ELEMENT_NODE);
                
                Element representation = (Element) rNode;

                NodeList segmentlists = representation.getChildNodes();
                for (int i = 0; i < segmentlists.getLength(); i++) {
                    Node segmentlist = segmentlists.item(i);

                    if (segmentlist.getNodeType() == Node.ELEMENT_NODE) {
                        assertEquals(segmentlist.getNodeName(), "SegmentList");

                        NodeList segments = segmentlist.getChildNodes();
                        for (int j = 0; j < segments.getLength(); j++) {
                            Node segmentNode = segments.item(j);

                            if (segmentNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element segment = (Element) segmentNode;
                                assertTrue(segment.getNodeName() == "Initialization" || segment.getNodeName() == "SegmentURL");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
