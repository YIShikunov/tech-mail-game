package base;

import java.util.HashMap;

public class GSResources implements Resources
{
    private HashMap<String,String> settings = new HashMap<>(2);

    public GSResources(HashMap<String,String> resources)
    {
        settings.putAll(resources);
    }

    public String getSetting(String settingName) {
        return settings.getOrDefault(settingName, "none");
    }
}
