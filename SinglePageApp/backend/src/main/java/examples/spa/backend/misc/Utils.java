package examples.spa.backend.misc;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Utils {
	public static String objectToString(Object o) {
		return ReflectionToStringBuilder.toString(o, ToStringStyle.SHORT_PREFIX_STYLE);	
	}
	
	public static <T> T nvl(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}
}
