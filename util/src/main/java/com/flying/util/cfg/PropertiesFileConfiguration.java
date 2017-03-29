/*
  File: PropertiesFileConfiguration.java
  Originally written by Walker.
  Rivision History:
  Date         Who     Version  What
  2004.07.26   Walker  2.0.0    Create this file, and get most function from BaseConfig.
  2004.08.09   Walker  2.0.1    Add implementation: getCfgsAsProps.
  2004.08.19   Walker  2.0.2    Remove load configuration using ResourceBundle methods because of the cache problem.
                                Replace String "+" with StringBuffer's append.
                                Add auto search for properties file using language code & country code.
  2004.11.11   Walker  2.0.6    Fix bug: can not save file under unix environment.('\\'->'/')
  2005.03.04   Walker  2.0.11   Add remove & size method.
                                sort the properties when save to file!
                                Change Properties member to Map&SortedSet. we could reserve the old format when
                                saving or removing the properties.
  2005.05.25   Walker  2.1.0    Change the saving method. do not get file from current directory. Get file path
                                from URL instead.
  2006.11.28   Walker  2.1.10   Fix bug of CfgLine's setValue method.(using String.replaceAll).
  2014.12.25   walker  1.0.0    Pull this package from flygroup.  
 */
package com.flying.util.cfg;

import com.flying.util.exception.LoggableRuntimeException;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * <p>
 * Description: This class provide a mechanism to load config from a properties files. it will use InputStreamReader to
 * read configuration from file.
 * </p>
 * The configuration line number start from 1.
 */
public final class PropertiesFileConfiguration implements IConfiguration {
    private final static String DEFAULT_CHARSET_NAME = "EUC_CN";
    private final static String FILE_POSTFIX = ".properties";
    private final static char SEPARATOR_CHAR = '=';
    private final static char COMMENT_CHAR = '#';
    private final static String SAVE_SUPPORT_PROTOCOL_NAME = "file";
    // all lines in the configuration file.
    private final SortedSet allLines;
    // Properties file's fully qualified name.
    private String bundleName;
    // The properties file encoding
    private String charsetName;
    // Properties File's URL.
    private URL bundleURL;
    // validated configuration lines
    private Map cfgLines;

    // Constructor

    /**
     * load from the bundle & default charset name="EUC_CN"
     *
     * @param bundleName name of bundle.
     */
    public PropertiesFileConfiguration(String bundleName) {
        this(bundleName, DEFAULT_CHARSET_NAME);
    }

    /**
     * using charset to load props from bundleName .
     *
     * @param bundleName  Name of bundle.
     * @param charsetName Name of charset.
     */
    public PropertiesFileConfiguration(String bundleName, String charsetName) {
        this.bundleName = bundleName;
        this.charsetName = charsetName;
        this.allLines = Collections.synchronizedSortedSet(new TreeSet());
        this.cfgLines = Collections.synchronizedMap(new HashMap());
        this.bundleURL = findBundleURL();
        loadConfiguration();
    }

    private URL findBundleURL() {
        final ClassLoader loader = this.getClass().getClassLoader();
        // Next search for a Properties file.
        StringBuilder fileName = new StringBuilder();
        fileName.append(bundleName.replace('.', '/')).append(FILE_POSTFIX);
        URL url = loader.getResource(fileName.toString());
        if (url == null) {
            fileName.delete(fileName.length() - FILE_POSTFIX.length(), fileName.length());
            fileName.append("_").append(Locale.getDefault().getLanguage()).append(FILE_POSTFIX);
            url = loader.getResource(fileName.toString());
        }
        if (url == null) {
            fileName.delete(fileName.length() - FILE_POSTFIX.length(), fileName.length());
            fileName.append("_").append(Locale.getDefault().getCountry()).append(FILE_POSTFIX);
            url = loader.getResource(fileName.toString());
        }
        if (url == null) {
            throw new MissingResourceException("Missing Resource" + bundleName, bundleName, charsetName);
        }
        return url;
    }

    /**
     * Load config from file using specified charset. This method will search classpath for properties file.
     */
    private void loadConfiguration() {
        InputStream inStream = null;
        InputStreamReader inStreamReader = null;
        BufferedReader inReader = null;
        try {
            allLines.clear();
            cfgLines.clear();
            inStream = bundleURL.openStream();
            inStreamReader = new InputStreamReader(inStream, charsetName);
            inReader = new BufferedReader(inStreamReader);
            String line = null;
            int lineNO = 1;
            CfgLine cfgLine;
            while ((line = inReader.readLine()) != null) {
                cfgLine = new CfgLine(lineNO, line);
                allLines.add(cfgLine);
                if (cfgLine.getKey() != null) {
                    cfgLines.put(cfgLine.getKey(), cfgLine);
                }
                lineNO++;
            }
        } catch (Exception e) {
            // IOException, UnsupportedEncodingException
            throw new LoggableRuntimeException("Error in loading configuration: " + bundleURL.toString(), e);
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (inStreamReader != null)
                    inStreamReader.close();
                if (inReader != null)
                    inReader.close();
            } catch (IOException ioe) {
            }
        }
    }

    /**
     * @param key retrive value from config according to the key
     * @return value that defined in file.
     */
    public String getValue(String key) {
        String value = null;
        if (containsKey(key)) {
            value = ((CfgLine) cfgLines.get(key)).getValue();
        }
        return value;
    }

    /**
     * @param key          retrieve value from config according to the key
     * @param defaultValue a default value
     * @return the value in this property list with the specified key value.
     */
    public String getValue(String key, String defaultValue) {
        String value;
        if (containsKey(key)) {
            value = getValue(key);
        } else {
            value = defaultValue;
        }
        return value;
    }

    /**
     * @return Properties that defined in file.
     */
    public synchronized Properties getCfgsAsProps() {
        Properties props = new Properties();
        Iterator iterator = cfgLines.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            props.setProperty((String) entry.getKey(), ((CfgLine) entry.getValue()).getValue());
        }
        return props;
    }

    /**
     * Returns true if the property file contains a mapping for the specified key. More formally, returns true if and only
     * if this map contains at a mapping for a key k such that (key==null ? k==null : key.equals(k)). (There can be at
     * most one such mapping.)
     *
     * @param key possible key
     * @return true if and only if some key maps to the value argument in this hashtable as determined by the equals
     * method; false otherwise.
     */
    public boolean containsKey(String key) {
        return cfgLines.containsKey(key);
    }

    /**
     * reload configuration from file.
     */
    public synchronized void reload() {
        loadConfiguration();
    }

    /**
     * put key-value pair to properties.
     *
     * @param key   key String
     * @param value value string
     */
    public synchronized void put(String key, String value) {
        if (containsKey(key)) {
            ((CfgLine) cfgLines.get(key)).setValue(value);
        } else {
            Iterator iterator = cfgLines.keySet().iterator();
            String curKey = null;
            int lineNO = 0;
            while (iterator.hasNext()) {
                curKey = (String) iterator.next();
                if (curKey.compareToIgnoreCase(key) <= 0) {
                    lineNO = ((CfgLine) cfgLines.get(curKey)).getLineNO();
                }
            }
            iterator = allLines.iterator();
            CfgLine cfgLine;
            for (int i = 0; i < lineNO; i++)
                iterator.next();
            while (iterator.hasNext()) {
                cfgLine = (CfgLine) iterator.next();
                cfgLine.setLineNO(cfgLine.getLineNO() + 1);
            }
            cfgLine = new CfgLine(lineNO + 1, key, value);
            allLines.add(cfgLine);
            cfgLines.put(key, cfgLine);
        }
    }

    public synchronized void remove(String key) {
        if (!containsKey(key))
            return;
        CfgLine cfgLine = (CfgLine) cfgLines.get(key);
        SortedSet tail = allLines.tailSet(cfgLine);
        // remove first because line number substract one will cause the equal situation in tree set.
        allLines.remove(cfgLine);
        cfgLines.remove(key);
        Iterator iterator = tail.iterator();
        while (iterator.hasNext()) {
            cfgLine = (CfgLine) iterator.next();
            cfgLine.setLineNO(cfgLine.getLineNO() - 1);
        }
    }

    public int size() {
        return cfgLines.size();
    }

    /**
     * Save properties.
     */
    public synchronized void save() throws Exception {
        if (allLines.size() <= 0)
            return;
        FileOutputStream outStream = null;
        try {
            if (!bundleURL.getProtocol().equalsIgnoreCase(SAVE_SUPPORT_PROTOCOL_NAME)) {
                throw new Exception("Saving operation does not supported! Protocol:" + bundleURL.getProtocol());
            }
            outStream = new FileOutputStream(bundleURL.getPath(), false);
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, charsetName);
            saveWithWriter(outStreamWriter);
            outStreamWriter.close();
        } finally {
            if (outStream != null)
                outStream.close();
        }
    }

    /**
     * Save properties file with Writer.
     *
     * @param writer Writer
     * @throws IOException If an I/O error occurs
     */
    private void saveWithWriter(Writer writer) throws IOException {
        Iterator iterator = allLines.iterator();
        CfgLine cfgLine;
        synchronized (allLines) {
            while (iterator.hasNext()) {
                cfgLine = (CfgLine) iterator.next();
                writer.write(cfgLine.getWholeLine());
                writer.write("\n");
            }
        }
    }

    private final class CfgLine implements Comparable {
        private int lineNO;
        private String wholeLine;
        private String key;
        private String value;

        public CfgLine(int lineNO, String wholeLine) {
            this.lineNO = lineNO;
            this.wholeLine = wholeLine.trim();
            if (this.wholeLine.startsWith(String.valueOf(COMMENT_CHAR))) {
                key = null;
                value = null;
                return;
            }
            int firstIndex = this.wholeLine.indexOf(SEPARATOR_CHAR);
            if ((firstIndex <= 0) || (firstIndex + 1 >= this.wholeLine.length())) {
                key = null;
                value = null;
                return;
            }
            key = this.wholeLine.substring(0, firstIndex).trim();
            value = this.wholeLine.substring(firstIndex + 1, this.wholeLine.length()).trim();
        }

        public CfgLine(int lineNO, String key, String value) {
            this.lineNO = lineNO;
            this.key = key;
            this.value = value;
            this.wholeLine = key + "=" + value;
        }

        public int getLineNO() {
            return this.lineNO;
        }

        public void setLineNO(int lineNO) {
            this.lineNO = lineNO;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
            this.wholeLine = key + "=" + value;
        }

        public String getWholeLine() {
            return wholeLine;
        }

        public int compareTo(Object obj) {
            if (!(obj instanceof CfgLine))
                return -1;
            return lineNO - ((CfgLine) obj).getLineNO();
        }
    }

}