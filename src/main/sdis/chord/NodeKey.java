package main.sdis.chord;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import main.sdis.common.Utils;

/**
 * Represents a given chord node's key created from its InetSocketAddress
 */
public class NodeKey implements Key {

	private long value;

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
	
			byte[] hash = digest.digest(addressStr.getBytes());
			value = Utils.truncateHash(hash, ChordSettings.M);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the long representation of the Key
	 * @return long representation of the Key
	 */
	public long getValue() {
		return value;
	}
}