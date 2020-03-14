package misrraimsp.uned.pfg.firstmarket.config.sqlConfig;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLCategoryConfigurer {
    public static void main(String[] args) throws JDOMException, IOException {
        configure();
    }

    private static void configure() throws JDOMException, IOException {
        Document document = parse("/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/categories");
        setIds(document.getRootElement().getChild("Category"), new IdManager());
        outputXML(document, "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/output.xml");
        outputSQL(document, "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/queries");
    }

    private static void outputSQL(Document document, String fileName){
        //INSERT INTO category (id,name,parent_id) VALUES (1,'fm',1);
        //INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES (1,0,1,1);

        String sql = "";
        String categorySingleStaticPart = "INSERT INTO category (id,name,parent_id) VALUES ";
        String catpathSingleStaticPart = "INSERT INTO catpath (id,size,ancestor_id,descendant_id) VALUES ";


    }

    private static void outputXML(Document document, String fileName) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(fileName));
        //xmlOutputter.output(document, System.out);
    }

    private static void setIds(Element element, IdManager idManager){
        element.getChild("Id").setText(String.valueOf(idManager.getId()));
        idManager.increment();
        for (Element child : element.getChild("SubCategories").getChildren()){
            setIds(child, idManager);
        }
    }

    private static Document parse(String fileName) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();//Get JDOM document from SAX Parser
        return saxBuilder.build(new File(fileName));
    }
}
