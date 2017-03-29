package com.flying.util.math;

public final class DoubleUtils {
	public  static final double DEFAULT_DELTA = 0.0001d;

	public final static boolean equals(double source, double target, double delta) {
		if (Double.compare(source, target) == 0)
			return true;
		if (Math.abs(source - target) <= delta)
			return true;
		return false;
	}

	public final static boolean equals(double source, double target) {
		return equals(source, target, DEFAULT_DELTA);
	}

}
