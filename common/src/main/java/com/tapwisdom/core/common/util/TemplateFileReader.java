package com.tapwisdom.core.common.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TemplateFileReader {
    private Map<String, String> fileNameToContent = new HashMap<String, String>();
    
    private static final String templatesPathSuffix = "templates";
    private static final Logger LOG = Logger.getLogger(TemplateFileReader.class);
    private static TemplateFileReader INSTANCE = null;
    
    public static TemplateFileReader getInstance() {
        if (INSTANCE == null) {
            synchronized (TemplateFileReader.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemplateFileReader();
                }
            }
        }
        return INSTANCE;
    }
    
    public void reload() {
        loadTemplates();
    }
    
    public String getTemplate(String key, String defaultContent) {
        return fileNameToContent.containsKey(key) ? fileNameToContent.get(key) : defaultContent;
    }
    
    public String getTemplate(String key) {
        return getTemplate(key, null);
    }
    
    private TemplateFileReader() {
        loadTemplates();
    }
    
    private void loadTemplates() {
        synchronized (fileNameToContent) {
            try {
                String configPath = System.getProperty("config.path", "config" + File.separator);
                configPath += templatesPathSuffix + File.separator;
                LOG.debug("config path: " + configPath);
                File folder = new File(configPath);
                final File[] templateFiles = folder.listFiles();
                if (templateFiles != null) {
                    for (File templateFile : templateFiles) {
                        String content = FileUtils.readFileToString(templateFile);
                        fileNameToContent.put(templateFile.getName(), content);
                    }
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("template files => " + fileNameToContent);
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
