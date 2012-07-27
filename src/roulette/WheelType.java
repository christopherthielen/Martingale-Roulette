package roulette;

import java.util.EnumSet;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/7/12
 * Time: 3:29 PM
 * Defines the type of roulette wheel.  One or two zeros (green)
 */
public enum WheelType {
    OneZero("One Zero"), TwoZeros("Two Zeros"),;
    private String description;

    WheelType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static WheelType fromDescription(String description) {
        for (WheelType wheelType : EnumSet.allOf(WheelType.class)) {
            if (wheelType.getDescription().equals(description)) {
                return wheelType;
            }
        }
        return null;
    }
}
