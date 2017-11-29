package com.indago.gurobi;

import ij.IJ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Spawn execution process of "grbgetkey"
 * <p>
 * Author: HongKee Moon (moon@mpi-cbg.de), Scientific Computing Facility
 * Organization: MPI-CBG Dresden
 * Date: October 2016
 */
public class Exec {

	public static void runGrbgetkey(String key) {
		String path = NativeLibrary.getLibraryDestination() + File.separator + "grbgetkey";
		makeExecutable(path);
		execute(path, key);
	}

	private static void makeExecutable(String path) {
		final File grbgetkey = new File(path);
		grbgetkey.setExecutable(true);
	}

	private static void execute(String... commands) {
		Process process = null;

		ProcessBuilder pb = new ProcessBuilder(commands);

		pb.redirectErrorStream(true);

		try {
			process = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

		try {
			bw.write(System.getProperty("line.separator"));
			bw.flush();
			if (IJ.isWindows()) {
				bw.write(System.getProperty("line.separator"));
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		try {
			while ((line = br.readLine()) != null) {
				IJ.log(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
