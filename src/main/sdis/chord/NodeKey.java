package main.sdis.chord;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents a given chord node's key created from its InetSocketAddress
 */
public class NodeKey implements Key{

	private byte[] value;

	/**
	 * Constructs a Key for a given InetSocketAddress by applying the
	 * SHA-256 hashing algorithm on an address:port_number string
	 * @param address
	 */
	public NodeKey(InetSocketAddress address) {
		MessageDigest digest;

		try {
			digest = MessageDigest.getInstance(ChordSettings.HASH_FUNCTION);
			String addressStr = address.getAddress().toString() + ":" + address.getPort();
	
			value = digest.digest(addressStr.getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the byte array representation of the Key
	 * @return byte array representation of the Key
	 */
	public byte[] getValue() {
		return value;
	}
}