package ResourceLoader;

import base.Resources;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ResourcesService
{
    private static ResourcesService instance;

    public static synchronized ResourcesService getInstance() {
        if (instance == null) {
            instance = new ResourcesService();
        }
        return instance;
    }

    private DocumentBuilder builder = null;
    private HashMap<String, GSResources> directories = new HashMap<>(2);

    private ResourcesService()
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try
        {
            builder = builderFactory.newDocumentBuilder();

        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

    }

    public GSResources getResources(String directory)
    {
        if (directories.containsKey(directory))
        {
            return directories.get(directory);
        }
        else
        {
            Path filePath = Paths.get("resources/" + directory);
            if (Files.exists(filePath))
            {
                try {
                    Document settingsDoc = builder.parse(filePath.toFile());
                    //Class<?> resType = Class.forName("Resources.GSResources").getClass();
                    Element docRoot = settingsDoc.getDocumentElement();
                    if (docRoot.hasAttribute("class"))
                    {

                    }
                    if (docRoot.hasChildNodes()) {
                        ArrayList<GSResources> nodes = new ArrayList<>();
                        NodeList childNodes = docRoot.getChildNodes();
                        for (int i = 0; i < childNodes.getLength(); i++) {
                            nodes.add(parseLevel(childNodes.item(i)));
                        }
                        //resType.getConstructor(HashMap<String, String>, ArrayList<Resources>, String.class, ResourceStatus.class, String.class)
                        GSResources newDirectory = new GSResources(null, nodes, directory, ResourceStatus.OK,  "OK");
                        directories.put(directory, newDirectory);
                        return newDirectory;
                    }
                }
                catch (SAXException | IOException e)//| ClassNotFoundException e)
                {
                    e.printStackTrace();
                    return new GSResources(null, null, directory, ResourceStatus.ParseError, "XML Parsing failed");
                }
            }
            else
            {
                return new GSResources(null, null, directory, ResourceStatus.NotFoundError, "File Not Found" + filePath.toUri().toString());
            }
        }
        return null;
    }

    private GSResources parseLevel(Node node)
    {
        GSResources ret;

        HashMap<String, String> attrib = new HashMap<>(2);
        if (node.hasAttributes())
        {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0 ; i < attributes.getLength(); i++)
            {
                if (attributes.item(i).getNodeType() == Node.ATTRIBUTE_NODE)
                    attrib.put(attributes.item(i).getNodeName(), attributes.item(i).getNodeValue());
            }
        }
        ArrayList<GSResources> child = new ArrayList<>();
        if (node.hasChildNodes())
        {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++)
            {
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
                    child.add(parseLevel(childNodes.item(i))) ;
            }
        }
        ret = new GSResources(attrib, child, node.getNodeName(), ResourceStatus.OK, "OK");
        return ret;
    }

}
