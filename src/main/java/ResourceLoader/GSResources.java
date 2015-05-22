package ResourceLoader;

import java.util.ArrayList;
import java.util.HashMap;
import base.Resources;

public class GSResources implements Resources
{
    private HashMap<String,String> settings = new HashMap<>(2);
    private ArrayList<GSResources> content = new ArrayList<>(2);
    private String name = new String();
    private boolean noContents;
    private boolean noSettings = true;
    private String statusText;
    private ResourceStatus status;

    public GSResources(HashMap<String,String> resources, ArrayList<GSResources> inner, String name, ResourceStatus readStatus, String readStatusText)
    {
        statusText = readStatusText;
        status = readStatus;
        if (resources != null)
            settings.putAll(resources);
        noSettings = (resources == null);
        if (inner != null)
            content.addAll(inner);
        noContents = (inner == null);
        this.name = name;
    }

    public String getSetting(String settingName) { return settings.getOrDefault(settingName, "none"); }
    public ArrayList<GSResources> getContents() { return content; }
    public String getName() { return name; }
    public String getStatusText() {return statusText;}
    public ResourceStatus getStatus() {return status;}
    public boolean hasContents()
    {
        return !noContents;
    }
    public boolean hasSettings()
    {
        return !noSettings;
    }

    public ArrayList<GSResources> getContentByName (String name)
    {
        ArrayList<GSResources> ret = new ArrayList<>();
        for (GSResources i : content)
        {
            if (i.getName().equals(name))
            {
                ret.add(i);
            }
        }
        return ret;
    }
}
