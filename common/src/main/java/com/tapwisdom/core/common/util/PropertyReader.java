package com.tapwisdom.core.common.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
    
    private Map<String, String> propertyMap = new HashMap<String, String>();
    private static PropertyReader INSTANCE = null;
    private static final Logger LOG = Logger.getLogger(PropertyReader.class);
    private static String PROPERTIES_FILE_EXT = ".properties";

    private void loadProperties() {
        synchronized (propertyMap) {
            try {
                String configPath = System.getProperty("config.path", "config" + File.separator);
                LOG.debug("config path: " + configPath);
                File folder = new File(configPath);
                final File[] propFiles = folder.listFiles(new FilenameFilter() {
                    
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(PROPERTIES_FILE_EXT);
                    }
                });
                if (propFiles != null) {
                    for (File propFile : propFiles) {
                        Properties prop = new Properties();
                        FileInputStream in = new FileInputStream(propFile);
                        prop.load(in);
                        propertyMap.putAll((Hashtable)prop);
                    }
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
    
    private PropertyReader() {
        loadProperties();
    }
    
    public static PropertyReader getInstance() {
        if (INSTANCE == null) {
            synchronized (PropertyReader.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PropertyReader();
                }
            }
        }
        return INSTANCE;
    }

    public String getProperty(String key, String defaultValue) {
        return propertyMap.containsKey(key) ? propertyMap.get(key) : defaultValue;
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    /**
     * This API can be used to reload property files without any need to restart the service
     */
    public void reload() {
        loadProperties();
    }
    
}
