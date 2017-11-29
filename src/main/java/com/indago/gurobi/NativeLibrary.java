package com.indago.gurobi;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: HongKee Moon (moon@mpi-cbg.de), Scientific Computing Facility
 * Organization: MPI-CBG Dresden
 * Date: October 2016
 */
public class NativeLibrary {
	public static boolean copyLibraries() throws MalformedURLException {
		final String arch = getArchitectureShortcut();
		final File imageJPluginsDirectory = getImageJPluginsDir();
		final File dest = getLibraryDestination();
		URL thisJar = getThisJar();
		dest.mkdirs();
		boolean a = FileUtils.copyResourcesRecursively(new URL(thisJar + arch), dest);
		boolean b = FileUtils.copyResourcesRecursively(new URL(thisJar + "jar"), imageJPluginsDirectory);
		return a || b;
	}

	public static File getLibraryDestination() {
		return new File(getImageJPluginsDir().getParentFile(),
				"lib" + File.separator + getArchitectureShortcut());
	}

	private static File getImageJPluginsDir() {
		return getParent(getThisJar());
	}

	private static URL getThisJar() {
		return getJarUrlContainingClass(GurobiInstaller.class);
	}

	public static String getArchitectureShortcut() {
		final String osName = System.getProperty("os.name").toLowerCase();
		final String archName = System.getProperty("os.arch").toLowerCase();

		if (osName.startsWith("mac")) {
			if (archName.startsWith("x86_64"))
				return "macosx";
			else
				throw new UnsupportedOperationException("32bit MacOSX is not supported.");
		} else if (osName.startsWith("windows")) {
			if (archName.startsWith("amd64"))
				return "win64";
			else if (archName.startsWith("x86"))
				return "win32";
		} else if (osName.startsWith("linux")) {
			if (archName.startsWith("amd64"))
				return "linux64";
			else if (archName.startsWith("i386"))
				throw new UnsupportedOperationException("32bit Linux is not supported.");
		}
		throw new UnsupportedOperationException("Your OS is not supported.");
	}

	private static URL getJarUrlContainingClass(Class<?> clazz) {
		String resourcePath = clazz.getName().replace('.', '/') + ".class";
		final String url = clazz.getResource("/" + resourcePath).toString();
		final String pluginsDir = url.substring(0, url.length() - resourcePath.length());
		try {
			return new URL(pluginsDir);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static File getParent(URL url) {
		String parent = new File(url.getFile()).getParent();
		if (parent.startsWith("file:"))
			parent = parent.substring("file:".length());
		return new File(parent);
	}
}
