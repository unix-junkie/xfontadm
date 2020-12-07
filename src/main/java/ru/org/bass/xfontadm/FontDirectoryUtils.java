package ru.org.bass.xfontadm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isRegularFile;
import static java.util.Arrays.asList;

public final class FontDirectoryUtils {
	/**
	 * {@code fonts.dir} and {@code fonts.scale}.
	 */
	private static final List<String> FONT_INDEX_FILES = asList("fonts.dir",
								    "fonts.scale");

	/**
	 * Non-instantiable.
	 */
	private FontDirectoryUtils() {
		assert false;
	}

	public static boolean hasFontIndex(final Path directory) throws IOException {
		return isDirectory(directory)
		       && FONT_INDEX_FILES.stream().anyMatch(fontIndexFile -> isRegularFile(directory.resolve(fontIndexFile)));
	}
}
