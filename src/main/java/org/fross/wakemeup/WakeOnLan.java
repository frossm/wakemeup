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
import java.util.HexFormat;

import org.fross.library.Output;

public class WakeOnLan {

	/**
	 * buildMagicPacket(): Given the MAC address, construct a byte array with the WOL packet specification
	 * 
	 * Wake-On-Lan Reference: https://en.wikipedia.org/wiki/Wake-on-LAN
	 * 
	 * @param macAddress
	 * 
	 * @return
	 */
	public static byte[] buildMagicPacket(String macAddress) {
		// Remove any colons or dashes in the MAC. It should just be the hex values
		macAddress = macAddress.replace(":", "").replace("-", "");

		// Build Magic Packet
		String magicPacketBuilder = "";
		byte[] magicPacket = new byte[102];

		// First 6 bytes are 0xff
		// Next are 16 copies of the MAC address of the machine to enable
		magicPacketBuilder = "FF".repeat(6);
		magicPacketBuilder += macAddress.repeat(16);

		// Convert to a byte array
		try {
			magicPacket = HexFormat.of().parseHex(magicPacketBuilder);

		} catch (Exception ex) {
			Output.fatalError("Could not build magic packet\nAre you sure the entered MAC address is correct? '" + macAddress + "'", 2);
		}

		// Verify the size of the magic packet is 102 bytes
		if (magicPacket.length != 102) {
			Output.fatalError("The size of Magic Packet is not 102 bytes. It is " + magicPacket.length
					+ " bytes\nAre you sure the entered MAC address is correct? '" + macAddress + "'", 2);
		}
		
		return (magicPacket);
	}
	
	
	/**
	 * wakeDevice(): Send the magic packet to wake the device
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
			Output.fatalError("Could not send magic packet to destination",  3);
		}
		
				
	}

}
