package ru.org.bass.xfontadm.fontDirectoryProviders;

import ru.org.bass.xfontadm.FontDirectory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Consumer;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isRegularFile;
import static java.nio.file.Files.newDirectoryStream;
import static java.util.Arrays.asList;
import static java.util.stream.StreamSupport.stream;
import static ru.org.bass.xfontadm.FontDirectoryUtils.hasFontIndex;

/**
 * Walks a file tree, invoking {@link #accept} for any font directory found.
 */
abstract class FontDirectoryVisitor extends SimpleFileVisitor<Path> implements Consumer<FontDirectory> {
	/**
	 * Font file extensions, in lower case.
	 */
	private static final List<String> FONT_FILE_EXTENSIONS = asList("pcf",    // bitmap fonts, mkfontdir only
									"pcf.gz",
									"pfa",    // scalable fonts, mkfontscale followed by mkfontdir
									"pfb",
									"ttf",
									"ttc",
									"otf",
									"woff");

	@Override
	public final FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
		if (isFontDirectory(dir)) {
			accept(new FontDirectory(dir));
		}

		return super.preVisitDirectory(dir, attrs);
	}

	private static boolean isFontDirectory(final Path directory) throws IOException {
		if (!isDirectory(directory)) {
			return false;
		}

		if (hasFontIndex(directory)) {
			return true;
		}

		try (final DirectoryStream<Path> entries = newDirectoryStream(directory)) {
			return stream(entries.spliterator(), false).anyMatch(FontDirectoryVisitor::isFontFile);
		}
	}

	private static boolean isFontFile(final Path file) {
		final String fileNameLowerCase = file.getFileName().toString().toLowerCase();

		final boolean hasFontExtension =  FONT_FILE_EXTENSIONS.stream()
								      .anyMatch(extension -> fileNameLowerCase.endsWith('.' + extension));

		return hasFontExtension && isRegularFile(file);
	}
}
