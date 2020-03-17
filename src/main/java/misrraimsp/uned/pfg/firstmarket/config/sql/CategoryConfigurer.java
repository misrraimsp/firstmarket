package misrraimsp.uned.pfg.firstmarket.config.sql;

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

public class CategoryConfigurer {

    private static final String XMLCategoriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/categories";
    private static final String BuiltCatQueriesPath = "/Users/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/docs/builtCatQueries.txt";

    public static void main(String[] args) throws JDOMException, IOException {
        configure();
    }

    /**
     * Este método establece el workflow para producir las queries necesarias
     * que insertan en la base de datos las categorias y los catpath
     * @throws JDOMException
     * @throws IOException
     */
    private static void configure() throws JDOMException, IOException {

        Document document = parse(XMLCategoriesPath);
        Element rootCategory = document.getRootElement().getChild("Category");
        IdHolder idHolder = new IdHolder();
        CategoryLevelHolder categoryLevelHolder = new CategoryLevelHolder();
        QueryHolder queryHolder = new QueryHolder();

        completeXML(rootCategory, idHolder, categoryLevelHolder);
        buildCategorySQL(rootCategory, queryHolder);
        queryHolder.addNewLine();
        queryHolder.addNewLine();
        idHolder.reset();
        buildCatpathSQL(rootCategory, queryHolder, idHolder);

        outputXML(document, XMLCategoriesPath);
        outputSQL(queryHolder.getSql(), BuiltCatQueriesPath);
    }

    /**
     * @param fileName
     * @return the JDOM parsed Document
     * @throws JDOMException
     * @throws IOException
     */
    private static Document parse(String fileName) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();//Get JDOM document from SAX Parser
        return saxBuilder.build(new File(fileName));
    }

    /**
     * Este método recorre el JDOM Document y establece el elemento 'Id' y el atributo 'Level'
     * de todos los elementos 'Category'
     * @param element
     * @param idHolder
     * @param categoryLevelHolder
     */
    private static void completeXML(Element element, IdHolder idHolder, CategoryLevelHolder categoryLevelHolder){
        element.getChild("Id").setText(String.valueOf(idHolder.getId()));
        element.setAttribute("level", String.valueOf(categoryLevelHolder.getLevel()));
        idHolder.increment();
        categoryLevelHolder.increment();
        for (Element child : element.getChild("SubCategories").getChildren()){
            completeXML(child, idHolder, categoryLevelHolder);
        }
        categoryLevelHolder.decrement();
    }

    /**
     * Este método genera las queries necesarias para insertar las categorias
     * @param element
     * @param queryHolder
     */
    private static void buildCategorySQL(Element element, QueryHolder queryHolder){
        String id = element.getChild("Id").getText();
        if (id.equals("1")){
            //self-parenthood
            queryHolder.addInsertCategoryQuery(id, element.getChild("Name").getText(), id);
        } else {
            queryHolder.addInsertCategoryQuery(
                    id,
                    element.getChild("Name").getText(),
                    element.getParentElement().getParentElement().getChild("Id").getText());
        }
        queryHolder.addNewLine();
        for (Element child : element.getChild("SubCategories").getChildren()) {
            buildCategorySQL(child, queryHolder);
        }
    }

    /**
     * Este método genera las queries necesarias para insertar los catpaths
     * @param element
     * @param queryHolder
     * @param idHolder
     */
    private static void buildCatpathSQL(Element element, QueryHolder queryHolder, IdHolder idHolder) {
        String id = element.getChild("Id").getText();
        queryHolder.addInsertCatpathQuery(String.valueOf(idHolder.getId()),"0",id,id);
        queryHolder.addNewLine();
        idHolder.increment();
        for (Element descendant : getDescendants(element)){
            int descendantLevel = Integer.parseInt(descendant.getAttribute("level").getValue());
            int elementLevel = Integer.parseInt(element.getAttribute("level").getValue());
            queryHolder.addInsertCatpathQuery(
                    String.valueOf(idHolder.getId()),
                    String.valueOf(descendantLevel - elementLevel),
                    id,
                    descendant.getChild("Id").getText());
            queryHolder.addNewLine();
            idHolder.increment();
        }
        for (Element child : element.getChild("SubCategories").getChildren()){
            buildCatpathSQL(child, queryHolder, idHolder);
        }
    }

    /**
     * @param element
     * @return all 'Category' elements under input parameter element
     */
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

    /**
     * Este método escribe el JDOM Document en un fichero
     * @param document
     * @param fileName
     * @throws IOException
     */
    private static void outputXML(Document document, String fileName) throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        xmlOutputter.output(document, new FileOutputStream(fileName));
        //xmlOutputter.output(document, System.out);
    }

    /**
     * Este método escribe el input parameter sql en un fichero
     * @param sql
     * @param fileName
     * @throws IOException
     */
    private static void outputSQL(String sql, String fileName) throws IOException {
        Files.write(Paths.get(fileName), sql.getBytes());
        //System.out.println(sql);
    }
}
