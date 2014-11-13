package com.cement.misc;

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Code borrowed from
 * http://www.burnison.ca/articles/redirecting-derbylog-to-an-slf4j-logger
 * 
 * set the java or Derby property
 * derby.stream.error.method=com.cement.misc.DerbySlf4jBridge.bridge
 */
public class DerbySlf4jBridge {
	private static final Logger logger = LoggerFactory.getLogger(DerbySlf4jBridge.class);

	private DerbySlf4jBridge() {
	}

	/**
	 * A basic adapter that funnels Derby's logs through an SLF4J logger.
	 */
	public static final class LoggingWriter extends Writer {
		public void write(final char[] cbuf, final int off, final int len) {
			if (len > 1) {
				logger.error(new String(cbuf, off, len));
			}
		}

		public void flush() {
		}

		public void close() {
		}
	}

	public static Writer bridge() {
		return new LoggingWriter();
	}
}
