package roulette; /**
 *
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
import java.util.*;

import static roulette.NumberGroups.*;

public enum Number {
        One(1, Red),
        Two(2, Black),
        Three(3, Red),
        Four(4, Black),
        Five(5, Red),
        Six(6, Black),
        Seven(7, Red),
        Eight(8, Black),
        Nine(9, Red),
        Ten(10, Black),
        Eleven(11, Black),
        Twelve(12, Red),
        Thirteen(13, Black),
        Fourteen(14, Red),
        Fifteen(15,Black),
        Sixteen(16,Red),
        Seventeen(17, Black),
        Eightteen(18, Red),
    
        Nineteen(19, Red),
        Twenty(20, Black),
        TwentyOne(21, Red),
        TwentyTwo(22, Black),
        TwentyThree(23, Red),
        TwentyFour(24, Black),
        TwentyFive(25, Red),
        TwentySix(26, Black),
        TwentySeven(27, Red),
        TwentyEight(28, Black),
        TwentyNine(29, Black),
        Thirty(30, Red),
        ThirtyOne(31, Black),
        ThirtyTwo(32, Red),
        ThirtyThree(33, Black),
        ThirtyFour(34, Red),
        ThirtyFive(35, Black),
        ThirtySix(36, Red),

        ZERO(37, Green),
        ZEROZERO(38, Green),


    ;

    private int number;
    private EnumSet<NumberGroups> groups;
    private static Map<Integer, Number> map = new HashMap<Integer, Number>();

    static {
        for (Number number : EnumSet.allOf(Number.class)) {
            map.put(number.number, number);
        }
    }

    private Number(int number, NumberGroups ... groups) {
        this.number = number;
        this.groups = EnumSet.noneOf(NumberGroups.class);
        if (number < 37) {
            this.groups.add(number % 2 == 0 ? Even : Odd);
            this.groups.add(number % 3 == 0 ? ThirdV12 :
                    number % 3 == 1 ? FirstV12 : SecondV12);
            this.groups.add(number <= 18 ? First18 : Second18);
            this.groups.add(number <= 12 ? First12 :
                    number <= 24 ? Second12 : Third12);

        }
        Collections.addAll(this.groups, groups);
    }


    public int getNumber() {
        return number;
    }

    public EnumSet<NumberGroups> getGroups() {
        return groups;
    }

    public static Number get(int number) {
        return map.get(number);
    }
}
