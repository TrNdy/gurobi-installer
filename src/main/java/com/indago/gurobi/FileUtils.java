package com.indago.gurobi;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Based on http://stackoverflow.com/questions/1386809/copy-directory-from-a-jar-file
 * Modified it to handle files by URI
 * <p>
 * Author: HongKee Moon (moon@mpi-cbg.de), Scientific Computing Facility
 * Organization: MPI-CBG Dresden
 * Date: October 2016
 */
public class FileUtils {
	public static boolean copyFile(final File toCopy, final File destFile) {
		try {
			return FileUtils.copyStream(new FileInputStream(toCopy), new FileOutputStream(destFile));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyFilesRecusively(final File from, final File to) {
		if (!from.isDirectory()) {
			return FileUtils.copyFile(from, to);
		} else {
			if (!to.exists() && !to.mkdir())
				return false;

			for (final File child : from.listFiles()) {
				final File newTo = new File(to, child.getName());
				if (!FileUtils.copyFilesRecusively(child, newTo))
					return false;
			}
		}
		return true;
	}

	public static boolean copyJarResourcesRecursively(final JarURLConnection from, final File to) throws IOException {
		final JarFile jarFile = from.getJarFile();

		for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements(); ) {
			final JarEntry entry = e.nextElement();

			if (entry.getName().startsWith(from.getEntryName())) {
				final String filename = StringUtils.removeStart(entry.getName(), //
						from.getEntryName());

				final File f = new File(to, filename);
				if (!entry.isDirectory()) {
					if (f.exists()) {
						return false;
					}
					final InputStream entryInputStream = jarFile.getInputStream(entry);
					if (!FileUtils.copyStream(entryInputStream, f)) {
						return false;
					}
					entryInputStream.close();
				} else {
					if (!FileUtils.ensureDirectoryExists(f)) {
						throw new IOException("Could not create directory: "
								+ f.getAbsolutePath());
					}
				}
			}
		}
		return true;
	}

	public static boolean copyResourcesRecursively( //
			final URL originUrl, final File destination) {
		try {
			final URLConnection urlConnection = originUrl.openConnection();
			if (urlConnection instanceof JarURLConnection) {
				return FileUtils.copyJarResourcesRecursively((JarURLConnection) urlConnection, destination);
			} else {
				return FileUtils.copyFilesRecusively(toFile(originUrl), destination);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static File toFile(URL originUrl) {
		try {
			return Paths.get(originUrl.toURI()).toFile();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private static boolean copyStream(final InputStream is, final File f) {
		try {
			return FileUtils.copyStream(is, new FileOutputStream(f));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean copyStream(final InputStream is, final OutputStream os) {
		try {
			final byte[] buf = new byte[1024];

			int len = 0;
			while ((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
			is.close();
			os.close();
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean ensureDirectoryExists(final File f) {
		return f.exists() || f.mkdir();
	}
}
