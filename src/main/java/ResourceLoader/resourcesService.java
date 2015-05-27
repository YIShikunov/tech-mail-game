package ResourceLoader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class resourcesService {
    private static resourcesService instance;

    public static synchronized resourcesService getInstance() {
        if (instance == null) {
            instance = new resourcesService();
        }
        return instance;
    }

    private DocumentBuilder builder = null;
    private HashMap<String, GSResources> directories = new HashMap<>(2);
    private HashMap<String, String> statusText = new HashMap<>(1);
    private Boolean operationOK = true;

    private resourcesService()
    {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try
        {
            statusText.put("__status__", "OK");
            operationOK = true;
            builder = builderFactory.newDocumentBuilder();

        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
            statusText.replace("__status__", "parserInitializeFailure");
            operationOK = false;
        }

    }

    public Boolean getStatus()
    {
        return operationOK;
    }

    public GSResources getResources(String directory)
    {
        if (directories.containsKey(directory))
        {
            return directories.get(directory);
        }
        else
        {
            Path filePath = Paths.get("resources/" + directory + ".xml");
            if (Files.exists(filePath))
            {
                try {
                    Document settingsDoc = builder.parse(filePath.toFile());
                    HashMap<String, String> settings = new HashMap<>(2);
                    Element docRoot = settingsDoc.getDocumentElement();
                    NodeList settingsList = docRoot.getChildNodes();
                    for (int i = 0; i < settingsList.getLength(); i++) {
                        if (!settingsList.item(i).getNodeName().equals("#text"))
                            settings.put(settingsList.item(i).getNodeName(), settingsList.item(i).getTextContent());
                    }
                    statusText.replace("__status__", "OK");
                    operationOK = true;
                    settings.putAll(statusText);
                    GSResources newDirectory = new GSResources(settings);
                    directories.put(directory, newDirectory);
                    return newDirectory;
                }
                catch (SAXException | IOException e)
                {
                    e.printStackTrace();
                    statusText.replace("__status__", "XML Parsing failed");
                    operationOK = false;
                    return new GSResources(statusText);
                }
            }
            else
            {
                statusText.replace("__status__", "File " + filePath.toAbsolutePath().toString() + " not found");
                operationOK = false;
                return new GSResources(statusText);
            }
        }
    }

}
