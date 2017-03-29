package com.flying.util.math;

import org.apache.commons.lang3.time.DateFormatUtils;

public class IntegerUtils {
	public final static String INT_PATTERN = "yyyyMMdd";

	public final static int getDate(long date) {
		return Integer.parseInt(DateFormatUtils.format(date, INT_PATTERN));
	}
}
