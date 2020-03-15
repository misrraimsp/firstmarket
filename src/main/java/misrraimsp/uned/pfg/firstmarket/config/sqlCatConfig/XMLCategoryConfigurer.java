package misrraimsp.uned.pfg.firstmarket.config.sqlCatConfig;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XMLCategoryConfigurer {

    private static final String XMLCategoriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/categories";
    private static final String BuiltQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtQueries.txt";

    public static void main(String[] args) throws JDOMException, IOException {
        configure();
    }

    private static void configure() throws JDOMException, IOException {

        Document document = parse(XMLCategoriesPath);
        IdManager idManager = new IdManager();
        LevelManager levelManager = new LevelManager();
        QueryManager queryManager = new QueryManager();
        Element rootCategory = document.getRootElement().getChild("Category");

        completeXML(rootCategory, idManager, levelManager);
        buildCategorySQL(rootCategory, queryManager);
        queryManager.addNewLine();
        queryManager.addNewLine();
        idManager.reset();
        buildCatpathSQL(rootCategory, queryManager, idManager);

        outputXML(document, XMLCategoriesPath);
        outputSQL(queryManager.getSql(), BuiltQueriesPath);
    }

    private static Document parse(String fileName) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();//Get JDOM document from SAX Parser
        return saxBuilder.build(new File(fileName));
    }

    private static void completeXML(Element element, IdManager idManager, LevelManager levelManager){
        element.getChild("Id").setText(String.valueOf(idManager.getId()));
        element.setAttribute("level", String.valueOf(levelManager.getLevel()));
        idManager.increment();
        levelManager.increment();
        for (Element child : element.getChild("SubCategories").getChildren()){
            completeXML(child, idManager, levelManager);
        }
        levelManager.decrement();
    }

    private static void buildCategorySQL(Element element, QueryManager queryManager){
        String id = element.getChild("Id").getText();
        if (id.equals("1")){
            //self-parenthood
            queryManager.addInsertCategoryQuery(id, element.getChild("Name").getText(), id);
        } else {
            queryManager.addInsertCategoryQuery(
                    id,
                    element.getChild("Name").getText(),
                    element.getParentElement().getParentElement().getChild("Id").getText());
        }
        queryManager.addNewLine();
        for (Element child : element.getChild("SubCategories").getChildren()) {
            buildCategorySQL(child,queryManager);
        }
    }

    private static void buildCatpathSQL(Element element, QueryManager queryManager, IdManager idManager) {
        String id = element.getChild("Id").getText();
        queryManager.addInsertCatpathQuery(String.valueOf(idManager.getId()),"0",id,id);
        queryManager.addNewLine();
        idManager.increment();
        for (Element descendant : getDescendants(element)){
            int descendantLevel = Integer.parseInt(descendant.getAttribute("level").getValue());
            int elementLevel = Integer.parseInt(element.getAttribute("level").getValue());
            queryManager.addInsertCatpathQuery(
                    String.valueOf(idManager.getId()),
                    String.valueOf(descendantLevel - elementLevel),
                    id,
                    descendant.getChild("Id").getText());
            queryManager.addNewLine();
            idManager.increment();
        }
        for (Element child : element.getChild("SubCategories").getChildren()){
            buildCatpathSQL(child,queryManager,idManager);
        }
    }

    private static List<Element> getDescendants(Element element) {
        List<Element> descendants = new ArrayList<>();
        for (Content content : element.getDescendants()){
            if (content.getCType().equals(Content.CType.Element)){
                Element e = (Element) content;
                if (e.getName().equals("Category")) {
                    descendants.add(e);
                }
            }
        }
        return descendants;
    }

    private static void outputXML(Document document, String fileName) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(fileName));
        //xmlOutputter.output(document, System.out);
    }

    private static void outputSQL(String sql, String fileName) throws IOException {
        Files.write(Paths.get(fileName), sql.getBytes());
        //System.out.println(sql);
    }
}
