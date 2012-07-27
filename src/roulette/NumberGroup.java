package roulette;

/**
 * Created with IntelliJ IDEA.
 * User: sone
 * Date: 7/6/12
 * Time: 12:14 PM
 *
 * This enumerates all the possible "outside" bets on the roulette table along with their potential returns
 */
public enum NumberGroup {
    Red(2), Black(2), Green(18), Even(2), Odd(2), First18(2), Second18(2), First12(3), Second12(3), Third12(3), FirstV12(3), SecondV12(3), ThirdV12(3);
    private double multiplier;

    NumberGroup(int multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
