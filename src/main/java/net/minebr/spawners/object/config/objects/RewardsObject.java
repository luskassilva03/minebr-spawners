package net.minebr.spawners.object.config.objects;

public class RewardsObject {

    private String miniature;
    private String rewardType;
    private double rewardAmount;
    private String rewardCommand;
    public RewardsObject(String miniature, String rewardType, double rewardAmount, String rewardCommand) {
        this.miniature = miniature;
        this.rewardType = rewardType;
        this.rewardAmount = rewardAmount;
        this.rewardCommand = rewardCommand;
    }

    public String getMiniature() {
        return miniature;
    }

    public String getRewardType() {
        return rewardType;
    }

    public double getRewardAmount() {
        return rewardAmount;
    }

    public String getRewardCommand() {
        return rewardCommand;
    }
}
