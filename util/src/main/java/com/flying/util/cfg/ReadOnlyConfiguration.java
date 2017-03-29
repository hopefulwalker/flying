/*
  File: ReadOnlyConfiguration.java
  Originally written by Walker.
  Rivision History:
  Date        Who      Version  What
  2005.11.08  Walker   2.1.4    Create this file.  
  2014.12.25   walker  1.0.0    Pull this package from flygroup.  
 */
package com.flying.util.cfg;

import java.util.*;

/**
 * this class implements read only configuration. it only impelments some configuration method that do not need to
 * change data. This class uses hashmap as it backend data, so it's access speed is faster than other implementations.
 * It's perfermance problem that we consider to generate a new class to impelements Interface Configuraiton
 */
public class ReadOnlyConfiguration implements IConfiguration {
	private HashMap cache = new HashMap();

	/**
	 * Create read only configuration. we should build backend HashMap in constructors.
	 *
	 * @param cfg
	 *          initial configurations.
	 */
	public ReadOnlyConfiguration(IConfiguration cfg) {
		cache.putAll(cfg.getCfgsAsProps());
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public int size() {
		return cache.size();
	}

	public String getValue(String key) {
		return (String) cache.get(key);
	}

	public String getValue(String key, String defaultValue) {
		if (!cache.containsKey(key)) {
			return defaultValue;
		}
		return getValue(key);
	}

	public Properties getCfgsAsProps() {
		Properties props = new Properties();
		props.putAll(cache);
		return props;
	}

	public void put(String key, String value) {}

	public void remove(String key) {}

	public void reload() {}

	public void save() {}
}
