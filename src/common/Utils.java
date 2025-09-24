package common;

import java.io.Closeable;
import java.io.IOException;

public final class Utils {
	private Utils() {
	}

	public static void safeClose(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (IOException ignored) {}
	}
}