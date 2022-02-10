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

import java.util.Scanner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

public class Favorites {
	// Class Constants
	private static final String PREFS_PATH = "/org/fross/wakemeup";

	// Class Variables
	private static Preferences prefs = Preferences.userRoot().node(PREFS_PATH);

	/**
	 * addName(): Query information and add the favorite
	 * 
	 * @param name
	 */
	public static boolean addName(String name) {
		boolean cancelOperation = false;
		String mac = "";
		String broadcastIP = "";

		// Make the name non-case sensitive
		name = name.trim().toLowerCase();

		Output.printColorln(Ansi.Color.WHITE, "Adding Favorite: '" + name + "'");

		// Ensure the name doesn't already exist
		if (nameExists(name) == true) {
			Output.printColorln(Ansi.Color.RED, "ERROR: '" + name + "' already exists");
			return false;
		}

		// Input information from user
		Scanner scanner = new Scanner(System.in);
		Output.printColor(Ansi.Color.YELLOW, "Enter MAC Address (blank will cancel):  ");
		mac = scanner.nextLine();
		if (mac.isEmpty()) {
			cancelOperation = true;
		}

		if (cancelOperation == false) {
			Output.printColor(Ansi.Color.YELLOW, "Enter Broadcast Address (ex: 10.0.0.255):  ");
			broadcastIP = scanner.nextLine();
			if (broadcastIP.isBlank()) {
				cancelOperation = true;
			}
		}

		scanner.close();

		// Add name to preferences in format: macAddress!broadcastIP
		if (cancelOperation == false) {
			prefs.put(name, mac + "!" + broadcastIP);
			return true;

		} else {
			Output.printColor(Ansi.Color.RED, "Adding Favorite was Canceled");
			return false;
		}

	}

	/**
	 * getNameDetails(): Return a string array with the MAC Address and Broadcast IP
	 * 
	 * Return: MAC Address is element [0]. Broadcast IP is element [1]
	 * 
	 * @param name
	 * @return
	 */
	public static String[] getNameDetails(String name) {
		// Make the name non-case sensitive
		name = name.trim().toLowerCase();

		String[] returnInfo = {};
		String nameDetail = prefs.get(name, "ERROR");

		if (nameDetail != "ERROR") {
			returnInfo = nameDetail.split("!");
		} else {
			Output.fatalError("Error returning name details", 3);
		}

		return returnInfo;
	}

	/**
	 * nameExists(): Return true if provided name exists as a favorite
	 * 
	 * @param name
	 * @return
	 */
	public static boolean nameExists(String name) {
		// Make the name non-case sensitive
		name = name.trim().toLowerCase();

		if (prefs.get(name, "ERROR") == "ERROR") {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * deleteName(): Remove the provided name from the favorites
	 * 
	 * @param name
	 */
	public static void deleteName(String name) {
		// Make the name non-case sensitive
		name = name.trim().toLowerCase();
		prefs.remove(name);
	}

	/**
	 * clearAll(): Remove all saved favorites
	 */
	public static void clearAll() {
		try {
			prefs.clear();

		} catch (BackingStoreException ex) {
			Output.printColorln(Ansi.Color.RED, "Could not clear favorites");
			ex.getMessage();
		}
	}

	/**
	 * queryFavorites(): Return string array of all favorite names
	 * 
	 * @return
	 */
	public static String[] queryFavorites() {
		String[] keys = {};
		try {
			keys = prefs.keys();

		} catch (BackingStoreException ex) {
			Output.printColorln(Ansi.Color.RED, "ERROR: Could not retrieve favorite names from preferences");
		}

		return keys;
	}

}
