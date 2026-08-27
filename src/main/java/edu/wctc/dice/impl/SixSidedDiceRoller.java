package edu.wctc.dice.impl;

import edu.wctc.dice.iface.DiceRoller;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SixSidedDiceRoller implements DiceRoller {

    @Override
    public int roll() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
}
