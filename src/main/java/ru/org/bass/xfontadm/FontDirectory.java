package ru.org.bass.xfontadm;

import java.io.IOException;
import java.nio.file.Path;

import static java.lang.String.format;
import static ru.org.bass.xfontadm.FontDirectoryUtils.hasFontIndex;

/**
 * <p>Font directory with the path and metadata.</p>
 *
 * <p>Only {@linkplain #getPath() paths} participate in comparison.</p>
 */
public final class FontDirectory implements Comparable<FontDirectory> {
	private final Path path;

	private final boolean indexed;

	public FontDirectory(final Path path) throws IOException {
		this.path = path;
		indexed = hasFontIndex(path);
	}

	public Path getPath() {
		return path;
	}

	public boolean isIndexed() {
		return indexed;
	}

	@Override
	public int compareTo(final FontDirectory that) {
		return path.compareTo(that.path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public boolean equals(final Object that) {
		return that instanceof FontDirectory
		       && path.equals(((FontDirectory) that).path);
	}

	@Override
	public String toString() {
		return format("%s (indexed = %b)", path, indexed);
	}
}
