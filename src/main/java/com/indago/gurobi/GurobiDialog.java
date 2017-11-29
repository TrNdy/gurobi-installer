package com.indago.gurobi;

import fiji.util.gui.GenericDialogPlus;
import ij.gui.GenericDialog;
import ij.gui.MultiLineLabel;
import ij.plugin.BrowserLauncher;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GurobiDialog {

	private final GenericDialogPlus dialog;
	private String key = "";

	public GurobiDialog() {
		dialog = new GenericDialogPlus("Getting Gurobi License File");
		dialog.addMessage("There was no gurobi license file found in your users home directory.");
		dialog.addMessage("Please acquire a license at ");
		addHyperLink(dialog, "http://www.gurobi.com/downloads/licenses/license-center", "http://www.gurobi.com/downloads/licenses/license-center");
		dialog.addMessage("Afterwards, please copy and paste the string starting with \"grbgetkey\":");
		dialog.addStringField("", "", 45);
	}

	public void show() {
		dialog.showDialog();
		key = dialog.getNextString();
	}

	public boolean wasCanceled() {
		return dialog.wasCanceled();
	}

	public String key() {
		return key;
	}

	private static final void addHyperLink(final GenericDialog gd, final String msg, final String url) {
		gd.addMessage(msg + "\n", new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 12));
		MultiLineLabel text = (MultiLineLabel) gd.getMessage();
		addHyperLinkListener(text, url);
	}

	private static final void addHyperLinkListener(final MultiLineLabel text, final String myURL) {
		if (text != null && myURL != null) {
			text.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					try {
						BrowserLauncher.openURL(myURL);
					} catch (Exception ex) {
						// ignore exception during browser launch
					}
				}

				@Override
				public void mouseEntered(final MouseEvent e) {
					text.setForeground(Color.BLUE);
					text.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(final MouseEvent e) {
					text.setForeground(Color.BLACK);
					text.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			});
		}
	}
}
