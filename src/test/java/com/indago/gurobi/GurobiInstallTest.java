package com.indago.gurobi;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class GurobiInstallTest {

	@Ignore("this is just an example not a test")
	@Test
	public void test() throws MalformedURLException, URISyntaxException {
		URL url = new URL("jar:file:/home/arzt/Applications/Fiji.app/plugins/gurobi-installer-0.0.1-SNAPSHOT.jar!/");
		System.out.println(NativeLibrary.getParent(url));
	}

	@Ignore("this is just an example not a test")
	@Test
	public void testResart() {
		System.out.println(GurobiInstaller.isRestartNecessary());
	}

	@Ignore("this is just an example not a test")
	@Test
	public void testCopy() throws MalformedURLException {
		File from = new File("/home/arzt/devel/gurobi-installer/src/main/resources/linux64");
		File to = new File("/home/arzt/devel/gurobi-installer/lib/linux64");
		FileUtils.copyResourcesRecursively(new URL("file:" + from), to);
	}

}
