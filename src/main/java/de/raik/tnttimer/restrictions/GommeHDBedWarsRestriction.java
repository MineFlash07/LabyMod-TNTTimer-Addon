package de.raik.tnttimer.restrictions;

import de.raik.tnttimer.TNTTimerAddon;
import net.labymod.api.events.TabListEvent;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

/**
 * Restriction for being allowd on GommeHD
 * because TNT timers are forbidden in BedWars
 *
 * @author Raik
 * @version 1.0
 */
public class GommeHDBedWarsRestriction implements Restriction, TabListEvent, Consumer<ServerData> {

    /**
     * Indicator whether the user is playing bed wars or not
     */
    private boolean isBedWars = false;

    /**
     * Constructor to register events
     *
     * @param addon The addon instance
     */
    public GommeHDBedWarsRestriction(TNTTimerAddon addon) {
        addon.api.getEventManager().register(this);
        addon.api.getEventManager().registerOnQuit(this);
    }

    /**
     * Method to indicate whether it's restricted
     * or not
     *
     * @return The state of the restriction
     */
    @Override
    public boolean isRestricted() {
        return this.isBedWars;
    }

    /**
     * Event method called when the tablist gets updated to check gomme hd
     * and bedwars
     *
     * @param type The type whether is'ts the header or footer
     * @param formattedText The formatted text
     * @param unformattedText The unformatted text
     */
    @Override
    public void onUpdate(Type type, String formattedText, String unformattedText) {
        //Check type
        if (!type.equals(Type.HEADER)) {
            return;
        }

        unformattedText = unformattedText.toLowerCase();
        this.isBedWars = unformattedText.contains("gommehd.net")
                && unformattedText.contains("bedwars");
    }

    /**
     * Method call to reset on server quit
     *
     * @param serverData The server data
     */
    @Override
    public void accept(ServerData serverData) {
        this.isBedWars = false;
    }
}
