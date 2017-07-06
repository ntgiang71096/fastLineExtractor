import com.sun.org.apache.xerces.internal.xs.StringList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giang on 7/6/2017.
 */
public class Application {
    final static String FILE_PATH="C:\\Users\\nguye\\Desktop\\Sample\\";
    final static String FILE_NAME="1567-1-3068-1-10-20160729";
    public static void main(String[] args) {

        File file = new File(FILE_PATH + FILE_NAME + ".pdf");
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
            String[] lines = text.split("\\r?\\n");
            for (String line : lines){
                System.out.println(line);
            }
            File output = new File(FILE_PATH + "lineXml.xml");
            output.getParentFile().mkdir();
            output.createNewFile();
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder =
                    dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            int count = 0;
            Element rootElement = doc.createElement("xml");
            doc.appendChild(rootElement);
            for (String line : lines){
                count ++;
                Element newLine = doc.createElement("line");
                Attr attr = doc.createAttribute("id");
                attr.setValue(String.valueOf(count));
                newLine.setAttributeNode(attr);
                newLine.appendChild(doc.createTextNode(lines[count-1]));
                rootElement.appendChild(newLine);
            }
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);
            transformer.transform(source,result);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
