package base;

import java.util.HashMap;

public class SettingsService {
    private static SettingsService instance;

    public static synchronized SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }
        return instance;
    }

    private HashMap<String, HashMap<String, String>> directories;

    private SettingsService()
    {
        HashMap<String, String> root = new HashMap<>(2);
        directories = new HashMap<>(2);
        root.put("path", "settings.xml");
        directories.put("root", root);
    }

    public String getSetting(String directory, String setting)
    {
        if (directories.containsKey(directory) && directories.get(directory).containsKey(setting))
        {
            return "+" + directories.get(directory).get(setting);
        }
        else
        {
            return "-";
        }
    }

}
