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

import org.fross.library.Format;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

/**
 * Help(): Display the help page when users enters 'h' or '?' command.
 * 
 * @author michael.d.fross
 *
 */
public class Help {
	/**
	 * Display(): Show help information
	 */
	public static void Display() {
		int helpWidth = 80;

		Output.printColor(Ansi.Color.CYAN, "\n+" + "-".repeat(helpWidth) + "+\n+");
		Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, "WakeMeUp - Wake-On-Lan Utility  v" + Main.VERSION));
		Output.printColor(Ansi.Color.CYAN, "+\n+");
		Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, Main.COPYRIGHT));
		Output.printColorln(Ansi.Color.CYAN, "+\n+" + "-".repeat(helpWidth) + "+");
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "WakeMeUp is a utility to power on a WOL enabled device on standby"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "https://github.com/frossm/wakemeup"));

		Output.printColorln(Ansi.Color.YELLOW, "\nUsage:");
		Output.printColorln(Ansi.Color.WHITE, "java -jar wakemeup [-OPTIONS] Name1 [Name2...]");
		Output.printColorln(Ansi.Color.WHITE, "\nSNAP: wakemeup [-OPTIONS] Name1 [Name2...]");

		Output.printColorln(Ansi.Color.YELLOW, "\nCommand Line Options:");
		Output.printColorln(Ansi.Color.WHITE, " -D       Start in debug mode.  Same as using the 'debug' command");
		Output.printColorln(Ansi.Color.WHITE, " -d NAME  Delete the provided name");
		Output.printColorln(Ansi.Color.WHITE, " -c       Clear all saved names");
		Output.printColorln(Ansi.Color.WHITE, " -l       List all favorites");
		Output.printColorln(Ansi.Color.WHITE, " -v       Display version information as well as latest GitHub release");
		Output.printColorln(Ansi.Color.WHITE, " -z       Disable colorized output");
		Output.printColorln(Ansi.Color.WHITE, " -h | ?   Show this help information.  Either key will work.");

		Output.printColorln(Ansi.Color.YELLOW, "\nNotes:");
		Output.printColorln(Ansi.Color.WHITE, " - If the name provided is not saved, it will prompt to add it");
		Output.printColorln(Ansi.Color.WHITE, " - Favorite names are saved in the Java preferences system and can be edited directly");
		Output.printColorln(Ansi.Color.WHITE, " - Names are not case sensitive");
	}
}