package net.minebr.spawners.object.config.objects;

import lombok.Getter;

@Getter
public class EconomiesConfig {
    private final String miniature;
    private final String economieType;
    private final String provider;
    private final double dropPrice;
    private final String dropName;

    public EconomiesConfig(String miniature, String economieType, String provider, double dropPrice, String dropName) {
        this.miniature = miniature;
        this.economieType = economieType;
        this.provider = provider;
        this.dropPrice = dropPrice;
        this.dropName = dropName;
    }
}
