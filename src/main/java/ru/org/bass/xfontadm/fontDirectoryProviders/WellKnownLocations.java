package ru.org.bass.xfontadm.fontDirectoryProviders;

import ru.org.bass.xfontadm.FontDirectory;
import ru.org.bass.xfontadm.LocalFontDirectoryProvider;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.walkFileTree;
import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

/**
 * Searches some well-known locations such as {@code /usr/share/fonts} for font
 * files.
 */
public final class WellKnownLocations implements LocalFontDirectoryProvider {
	private static final List<String> PREFIXES = asList("/usr", "/usr/local");

	private static final List<String> PREDEFINED_FONT_DIRECTORIES = asList("share/fonts",
									       "X11R6/lib/X11/fonts",
									       "X11/lib/X11/fonts",
									       "X/lib/X11/fonts",
									       "openwin/lib/X11/fonts");

	@Override
	public List<FontDirectory> getFontDirectories() throws IOException {
		final List<Path> existingFontRoots = PREDEFINED_FONT_DIRECTORIES.stream()
										.flatMap(directory -> PREFIXES.stream()
													    .map(Paths::get)
													    .map(prefix -> prefix.resolve(directory)))
										.filter(Files::isDirectory)
										.distinct()
										.collect(toList());

		final List<FontDirectory> fontDirectories = new ArrayList<>();

		final FileVisitor<Path> fileVisitor = new FontDirectoryVisitor() {
			@Override
			public void accept(final FontDirectory fontDirectory) {
				fontDirectories.add(fontDirectory);
			}
		};
		for (final Path existingFontRoot : existingFontRoots) {
			walkFileTree(existingFontRoot, fileVisitor);
		}

		sort(fontDirectories);

		return unmodifiableList(fontDirectories);
	}
}
