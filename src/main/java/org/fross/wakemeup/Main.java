/******************************************************************************
 * WakeMeUp - A Tiny Wake-On-Lan Utility
 * 
 * WakeMeUp is a utility to power on a Wake-On-Lan enabled device on standby
 * 
 *  Copyright (c) 2022 Michael Fross
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *           
 ******************************************************************************/
package org.fross.wakemeup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.fross.library.Debug;
import org.fross.library.GitHub;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

import gnu.getopt.Getopt;

public class Main {
	// Class constants or pseudo constants
	protected static String VERSION;
	protected static String COPYRIGHT;

	/**
	 * main(): The main program entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final String PROPERTIES_FILE = "app.properties";
		final int DESTPORT = 9;
		int optionEntry;
		boolean addSuccessful = true;

		String broadcastIP = "";
		String macAddress = "";

		// Process application level properties file
		// Update properties from Maven at build time:
		// https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
		try {
			int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
			InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties prop = new Properties();
			prop.load(iStream);
			VERSION = prop.getProperty("Application.version");
			COPYRIGHT = "Copyright (C) " + prop.getProperty("Application.inceptionYear") + "-" + year + " by Michael Fross";
		} catch (IOException ex) {
			Output.fatalError("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
		}

		// Process Command Line Options and set flags where needed
		Getopt optG = new Getopt("RPNCalc", args, "Dd:clvzh?");
		while ((optionEntry = optG.getopt()) != -1) {
			switch (optionEntry) {

			// Debug Mode
			case 'D':
				Debug.enable();
				break;

			// Delete Favorite
			case 'd':
				Favorites.deleteName(optG.getOptarg());
				System.exit(0);
				break;

			// Clear All Favorites
			case 'c':
				Output.printColorln(Ansi.Color.WHITE, "Clearing all saved favorites");
				Favorites.clearAll();
				System.exit(0);
				break;

			// List all favorites
			case 'l':
				Output.printColorln(Ansi.Color.WHITE, "Current Favorites");
				for (String i : Favorites.queryFavorites()) {
					String[] nameDetail = Favorites.getNameDetails(i);
					macAddress = nameDetail[0];
					broadcastIP = nameDetail[1];
					Output.printColorln(Ansi.Color.CYAN, i + "\t" + macAddress + "  " + broadcastIP);
				}
				System.exit(0);
				break;

			// Display Version and exit
			case 'v':
				Output.printColorln(Ansi.Color.WHITE, "WakeMeUp Version: v" + VERSION);
				Output.printColorln(Ansi.Color.CYAN, COPYRIGHT);
				Output.printColorln(Ansi.Color.WHITE, "\nLatest Release on GitHub: " + GitHub.updateCheck("wakemeup"));
				Output.printColorln(Ansi.Color.CYAN, "HomePage: https://github.com/frossm/wakemeup");
				System.exit(0);
				break;

			// Disable colorization of output
			case 'z':
				Output.enableColor(false);
				break;

			// Show help
			case '?':
			case 'h':
				Help.Display();
				System.exit(0);
				break;

			default:
				Output.printColorln(Ansi.Color.RED, "ERROR: Unknown Command Line Option: '" + (char) optionEntry + "'\n");
				Help.Display();
				System.exit(0);
				break;
			}
		}

		// Must have one favorite name entered
		if (args.length - optG.getOptind() < 1) {
			Output.printColorln(Ansi.Color.RED, "ERROR: At least one favorite name must be provided\n");
			Help.Display();
			System.exit(1);
		}

		// Build an array list of symbols entered on the command line
		Output.debugPrint("Number of names entered on command line: " + (args.length - optG.getOptind()));
		Output.printColorln(Ansi.Color.YELLOW, "WakeMeUp v" + Main.VERSION);
		try {
			for (int i = optG.getOptind(); i < args.length; i++) {
				String name = args[i].trim().toLowerCase();
				Output.debugPrint("Processing Name: " + name);

				// If the favorite doesn't exist, add it
				if (Favorites.nameExists(name) == false) {
					addSuccessful = Favorites.addName(name);
				}

				if (addSuccessful == true) {
					// Pull info from preferences and execute
					String[] nameDetail = Favorites.getNameDetails(name);
					macAddress = nameDetail[0];
					broadcastIP = nameDetail[1];

					Output.printColorln(Ansi.Color.WHITE, "\nWaking up " + name);
					Output.printColorln(Ansi.Color.CYAN, "  MAC:          " + macAddress);
					Output.printColorln(Ansi.Color.CYAN, "  Broadcast IP: " + broadcastIP);
					Output.printColorln(Ansi.Color.CYAN, "  Port:         " + DESTPORT);

					// Send the magic packet
					WakeOnLan.wakeDevice(macAddress, broadcastIP, DESTPORT);
				}
			}

		} catch (ArrayIndexOutOfBoundsException ex) {
			Output.fatalError("There must be at least one name entered on the command line.  See help with -h", 1);
		}

	}

}