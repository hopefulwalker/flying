/*
 File: Configuration.java
 Originally written by Walker.
 Rivision History:
 Date         Who     Version  What
 2004.07.26   walker  2.0.0    Create this file.
 2004.08.09   walker  2.0.1    Add Implementation for getCfgsAsProps.
 2004.09.10   walker  2.0.3    Mark this class as final class.
 2005.01.21   walker  2.0.11   Add remove & size method.
 2014.12.25   walker  1.0.0    Pull this package from flygroup. 
 */
package com.flying.util.cfg;

import java.util.*;

/**
 * This class provide dynamic impelmentation for <code>Configuration</code>.
 * Tester could use the class to simulate the configuration.
 */
public final class DynamicConfiguration implements IConfiguration {
  private Properties cache = new Properties();

  public String getValue(String key) {
    return cache.getProperty(key);
  }

  public Properties getCfgsAsProps() {
    return cache;
  }

  public String getValue(String key, String defaultValue) {
    if (cache.containsKey(key)) {
      return cache.getProperty(key);
    }
    else {
      return defaultValue;
    }
  }

  public boolean containsKey(String key) {
    return cache.containsKey(key);
  }

  public void put(String key, String value) {
    cache.put(key, value);
  }

  public void remove(String key) {
    cache.remove(key);
  }

  public int size() {
    return cache.size();
  }

  public void reload() throws Exception {
  }

  public void save() throws Exception {
  }
}
