package de.raik.tnttimer;

import com.google.gson.JsonObject;
import de.raik.tnttimer.restrictions.GommeHDBedWarsRestriction;
import de.raik.tnttimer.restrictions.Restriction;
import de.raik.tnttimer.settingelements.DescribedBooleanElement;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.HashSet;
import java.util.List;

/**
 * Addon instance
 * to handle addon
 * and the instance
 *
 * @author Raik
 * @version 1.0
 */
public class TNTTimerAddon extends LabyModAddon {

    private boolean enabled = true;

    /**
     * If enabled the tag is colored
     * depending on the tnt fuse
     */
    private boolean colored = true;

    /**
     * HashSet containing the restrictions
     */
    private final HashSet<Restriction> restrictions = new HashSet<>();

    /**
     * Init method called by
     * the addon api to setup the addon
     */
    @Override
    public void onEnable() {
        //Registering render manger
        this.getApi().getEventManager().register(new ExplosionTimeRenderer(this));
        //Add restrictions
        this.restrictions.add(new GommeHDBedWarsRestriction(this));
    }

    /**
     * Method called by the addon api
     * to load the settings
     */
    @Override
    public void loadConfig() {
        JsonObject addonConfig = this.getConfig();

        this.enabled = addonConfig.has("enabled") ? addonConfig.get("enabled").getAsBoolean() : this.enabled;
        this.colored = addonConfig.has("colored") ? addonConfig.get("colored").getAsBoolean() : this.colored;
    }

    /**
     * Method called by the addon api
     * to add settings to the addon
     *
     * @param settings The list of settings for the addon
     */
    @Override
    protected void fillSettings(List<SettingsElement> settings) {
        //Enabled setting
        settings.add(new BooleanElement("Enabled", this, new ControlElement.IconData(Material.LEVER)
                , "enabled", this.enabled));
        //Colored
        settings.add(new DescribedBooleanElement("Colored Time", this
                , new ControlElement.IconData("labymod/textures/settings/settings/tabping_colored.png"), "colored", this.colored
                , "The time tag will change the color dynamically depending on remaining time until the explosion"));
    }

    /**
     * Method checking whether it is restricted
     * or not
     *
     * @return The state of the restiction
     */
    public boolean isRestricted() {
        return this.restrictions.stream().anyMatch(Restriction::isRestricted);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isColored() {
        return this.colored;
    }
}
