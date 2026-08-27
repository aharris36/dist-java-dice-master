package edu.wctc.dice.impl;

import edu.wctc.dice.iface.DiceRoller;
import java.util.Random;

public class SixSidedDiceRoller implements DiceRoller {

    @Override
    public int roll() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
