package de.rpgstupe.travellite;

/**
 * Created by Fabian on 20.08.2017.
 */

public class CountryListDataObject {
    private String countryName;
    private boolean activated;
    private String countryCode;

    public CountryListDataObject(String countryName, String countryCode, boolean activated) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.activated = activated;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
