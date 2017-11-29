package com.indago.gurobi;

import ij.IJ;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Author: HongKee Moon (moon@mpi-cbg.de), Robert Haase(rhaase@mpi-cbg.de) Scientific Computing Facility
 * Organization: MPI-CBG Dresden
 * Date: October 2016
 */
public class GurobiInstaller {

	public static boolean install() {

		boolean actuallyCopiedGurobiFiles = false;

		try {
			actuallyCopiedGurobiFiles = NativeLibrary.copyLibraries();
		} catch (URISyntaxException e) {
			IJ.log("Native library allocation for Fiji failed.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			IJ.log("The given class URL is wrong.");
			e.printStackTrace();
		}

		final String gurobiLicFilePath = System.getProperty("user.home") + File.separator + "gurobi.lic";
		final File gurobiLicFile = new File(gurobiLicFilePath);

		if (!gurobiLicFile.exists()) {
			GurobiDialog gurobiDialog = new GurobiDialog();
			gurobiDialog.show();
			if (gurobiDialog.wasCanceled())
				return false;

			final String grbkeygetString = gurobiDialog.key();

			Exec.runGrbgetkey(grbkeygetString.split(" "));
		}

		if (actuallyCopiedGurobiFiles) {
			if (isRestartNecessary()) {
				JOptionPane.showMessageDialog(
						null,
						"Installation of a module (Gurobi Optimizer) is requesting for a restart. Please restart ImageJ/FIJI.",
						"Gurobi Installation",
						JOptionPane.ERROR_MESSAGE);
			}
		}

		return gurobiLicFile.exists();
	}

	static boolean isRestartNecessary() {
		try {
			// NB: The next lines calls "new GRBEnv( "MoMA_gurobi.log" );"
			Class.forName("gurobi.GRBEnv").newInstance();
			return false;
		} catch (final UnsatisfiedLinkError | NoClassDefFoundError | ClassNotFoundException e) {
			return true;
		} catch (final Throwable e) {
			boolean isGRBException = e.getClass().getName() == "gurobi.GRBException";
			if (isGRBException)
				IJ.log(e.getMessage());
			return isGRBException;
		}
	}

	public static void main(String... args) {
		System.out.println(System.getProperty("java.library.path"));
		GurobiInstaller.install();
	}
}
