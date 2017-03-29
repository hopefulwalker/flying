/*
  File: Configuration.java
  Originally written by Walker.
  Rivision History:
  Date         Who     Version  What
  2004.07.26   walker  2.0.0    Create this file.
  2004.08.09   walker  2.0.1    Add method getCfgsAsProps.
  2005.01.21   walker  2.0.11   Add remove & size method.
  2005.11.08   walker  2.1.4    Rearrange this class interface & Add comments to public interface.
  2014.12.25   walker  1.0.0    Pull this package from flygroup.
 */
package com.flying.util.cfg;

import java.util.Properties;

/**
 * Interface for application configuration.
 */
public interface IConfiguration {
	/**
	 * judge whether the configuraiton contains key.
	 *
	 * @param key
	 *          whose presence in this Configuration is to be tested.
	 * @return true if this map contains a mapping for the specified key.
	 */
	public boolean containsKey(String key);

	/**
	 * @return the number of key-value mappings in this Configuration.
	 */
	public int size();

	/**
	 * @param key
	 *          whose associated value is to be returned.
	 * @return the value to which this Configuration maps the specified key, or null if the map contains no mapping for
	 *         this key
	 */
	public String getValue(String key);

	/**
	 * Searches for the property with the specified key in this property list. If the key is not found in this property
	 * list, the default property list, and its defaults, recursively, are then checked. The method returns the default
	 * value argument if the property is not found.
	 *
	 * @param key
	 *          whose associated value is to be returned.
	 * @param defaultValue
	 *          a default value
	 * @return the value in this Configuration list with the specified key value
	 */
	public String getValue(String key, String defaultValue);

	/**
	 * @return all configurations as properties.
	 */
	public Properties getCfgsAsProps();

	/**
	 * Save key-value mapping in this Configuration.
	 *
	 * @param key
	 *          a key string
	 * @param value
	 *          a value string
	 */
	public void put(String key, String value);

	/**
	 * remove key-value mapping in this Configuration.
	 *
	 * @param key
	 *          whose associated value is to be removed.
	 */
	public void remove(String key);

	/**
	 * reload Configuration
	 *
	 * @throws Exception
	 *           when error occurs.
	 */
	public void reload() throws Exception;

	/**
	 * save Configuration.
	 * 
	 * @throws Exception
	 *           when error occurs.
	 */
	public void save() throws Exception;
}
