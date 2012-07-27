package roulette;

import java.util.EnumSet;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/7/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum BoardType {
    OneZero("One Zero"), TwoZeros("Two Zeros"),;
    private String description;

    BoardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static BoardType fromDescription(String description) {
        for (BoardType boardType : EnumSet.allOf(BoardType.class)) {
            if (boardType.getDescription().equals(description)) {
                return boardType;
            }
        }
        return null;
    }
}
