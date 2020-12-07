package ru.org.bass.xfontadm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Provides the list of font directories for a locally running <em>X11</em> or
 * {@code xfs} server.
 */
@FunctionalInterface
public interface LocalFontDirectoryProvider {
	/**
	 * Returns the list of font directories.
	 *
	 * @return the list of font directories.
	 * @throws IOException if an I/O error occurs while traversing the local
	 *         file system.
	 */
	List<FontDirectory> getFontDirectories() throws IOException;
}
