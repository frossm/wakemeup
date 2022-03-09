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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.fross.library.Output;

public class WakeOnLan {

	/**
	 * buildMagicPacket(): Given the MAC address, construct a byte array with the WOL packet specification
	 * 
	 * Wake-On-Lan Reference: https://en.wikipedia.org/wiki/Wake-on-LAN
	 * 
	 * @param mac
	 * 
	 * @return
	 */
	public static byte[] buildMagicPacket(String mac) {
		byte[] magicPacket = new byte[102];
		
		// Remove any colons or dashes in the MAC. It should just be the hex values
		mac = mac.replace(":", "").replace("-", "");

		// The first 6 bytes are 0xFF, and then add 16 copies of the MAC Address to create the magic packet
		String magicPacketBuilder = "";
		magicPacketBuilder = "FF".repeat(6);
		magicPacketBuilder += mac.repeat(16);

		// Convert the string to a byte array
		try {
			magicPacket = hexStringToByteArray(magicPacketBuilder);

		} catch (Exception ex) {
			Output.fatalError("Could not build magic packet\nAre you sure the entered MAC address is correctly? '" + mac + "'", 2);
		}

		// Verify the size of the magic packet is 102 bytes
		if (magicPacket.length != 102) {
			Output.fatalError("The size of Magic Packet is not 102 bytes. It is " + magicPacket.length
					+ " bytes\nAre you sure the entered MAC address is correctly? '" + mac + "'", 2);
		}

		return (magicPacket);
	}

	/**
	 * wakeDevice(): Broadcast the magic packet to wake the device
	 * 
	 * @param macAddress
	 * @param broadcastIP
	 * @param port
	 */
	public static void wakeDevice(String macAddress, String broadcastIP, int port) {
		byte[] magicPacket = buildMagicPacket(macAddress);
		DatagramSocket socket = null;
		InetAddress destAddress = null;

		try {
			destAddress = InetAddress.getByName(broadcastIP);
			socket = new DatagramSocket();

			// Create the packet and broadcast it
			DatagramPacket packet = new DatagramPacket(magicPacket, magicPacket.length, destAddress, port);
			socket.send(packet);

			socket.close();

		} catch (Exception ex) {
			Output.fatalError("Could not broadcast magic packet to destination", 3);
		}

	}

	/**
	 * hexStringToByteArray(): Convert the provided string to a byte array
	 * 
	 * Note: Snapcraft can't use java 17 yet which is needed for java.util.HexFormat.of().parseHex. So this should cover it
	 * 
	 * Thanks to Dave L. over at Stackoverflow for this function
	 * https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}

		return data;
	}

}
