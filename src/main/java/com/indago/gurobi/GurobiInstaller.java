package com.indago.gurobi;

import ij.IJ;

import javax.swing.*;
import java.io.File;

/**
 * Author: HongKee Moon (moon@mpi-cbg.de), Robert Haase(rhaase@mpi-cbg.de) Scientific Computing Facility
 * Organization: MPI-CBG Dresden
 * Date: October 2016
 */
public class GurobiInstaller {

	public static boolean install() {

		if(testGurobi())
			return true;

		boolean actuallyCopiedGurobiFiles = NativeLibrary.copyLibraries();

		if (!hasLicenceFile()) {
			GurobiDialog gurobiDialog = new GurobiDialog();
			gurobiDialog.show();
			if (gurobiDialog.wasCanceled())
				return false;
			Exec.runGrbgetkey(gurobiDialog.key());
		}

		boolean running = testGurobi();

		if (actuallyCopiedGurobiFiles && !running) {
			JOptionPane.showMessageDialog(
					null,
					"Installation of a module (Gurobi Optimizer) is requesting for a restart. Please restart ImageJ/FIJI.",
					"Gurobi Installation",
					JOptionPane.ERROR_MESSAGE);
		}

		return running;
	}

	private static boolean hasLicenceFile() {
		final String path = System.getProperty("user.home") + File.separator + "gurobi.lic";
		return new File(path).exists();
	}

	public static boolean testGurobi() {
		try {
			// NB: The next lines calls "new GRBEnv( "MoMA_gurobi.log" );"
			Object grbEnv = Class.forName("gurobi.GRBEnv").newInstance();
			return grbEnv != null;
		} catch (final UnsatisfiedLinkError | NoClassDefFoundError | ClassNotFoundException e) {
			return false;
		} catch (final Throwable e) {
			boolean isGRBException = e.getClass().getName() == "gurobi.GRBException";
			if (isGRBException)
				IJ.log(e.getMessage());
			return false;
		}
	}

	public static void main(String... args) {
		System.out.println(System.getProperty("java.library.path"));
		GurobiInstaller.install();
	}
}
