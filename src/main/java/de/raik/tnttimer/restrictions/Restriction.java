package de.raik.tnttimer.restrictions;

/**
 * Interface indicating a restriction
 * of the use of the addon
 *
 * @author Raik
 * @version 1.0
 */
public interface Restriction {

    /**
     * Method to indicate whether it's restricted
     * or not
     *
     * @return The state of the restriction
     */
    boolean isRestricted();

}