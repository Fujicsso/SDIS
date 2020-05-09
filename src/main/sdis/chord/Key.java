package main.sdis.chord;

import java.io.Serializable;

/**
 * Represents a Key used in a chord ring
 */
public interface Key extends Serializable {

    long getValue();
}