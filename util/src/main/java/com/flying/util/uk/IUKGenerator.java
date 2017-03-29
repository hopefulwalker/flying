/*
  File: UKGenerator.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.8     walker  0.1.0    Create this file.
  2015.1.12    walker  0.2.0    Add reset method. for easy to test.
 */
package com.flying.util.uk;

/**
 * The interface for unique key generator.
 * 
 * @author Walker.Zhang
 *
 */
public interface IUKGenerator {
	/**
   * This method generate the unique key for the name. if it the first time to generate. the key should be cache. 
	 * @param name name of Unique key.
	 * @throws UKGenException a runtime exception.
	 */
	public long generate(String name) throws UKGenException;

	/**
	 * This method erases the unique key. using this mehtod should be careful.
	 * @param name name of unique key.
	 * @throws UKGenException a runtime exception
	 */
	public void erase(String name) throws UKGenException;
	
}
