package edu.wctc.dice;

import edu.wctc.dice.iface.DiceRoller;
import edu.wctc.dice.iface.GameInput;
import edu.wctc.dice.iface.GameOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class DiceGame {
    private GameInput in;
    private GameOutput out;
    private DiceRoller dice;

    private List<Player> players = new ArrayList<>();
    private int currentRound = 1;


    @Autowired
    public DiceGame(GameInput in, GameOutput out, DiceRoller dice) {
        this.in = in;
        this.out = out;
        this.dice = dice;
        System.out.println("DiceGame created");
    }

    private void beginRound() {
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player currentPlayer = playerIterator.next();
            String choice = getPlayerChoice(currentPlayer.getName());
            if (choice.equals("2")) {
                playerIterator.remove();
            } else {
                int bet = getBet(currentPlayer.getCash());
                currentPlayer.setBet(bet);
            }
        }
    }

    private void endRound(boolean evens) {
        Iterator<Player> playerIterator = players.iterator();

        while (playerIterator.hasNext()) {
            Player currentPlayer = playerIterator.next();
            if (evens) {
                currentPlayer.wins();
            } else {
                currentPlayer.loses();
                if (currentPlayer.isBankrupt()) {
                    playerIterator.remove();
                }
            }
        }

        playerReport();
    }

    private int getBet(int cash) {
        String bet = in.getInput("Bet how much? ($" + cash + " available): ");
        return Integer.parseInt(bet);
    }

    private String getPlayerChoice(String playerName) {
        String menu = playerName.toUpperCase() + "'s turn:\n"
                + "  1. Place Bet\n"
                + "  2. Cash Out (Quit)\nChoice: ";
        return in.getInput(menu);
    }

    private void initPlayers() {
        String playerName = in.getInput("Enter player name: ");
        while (players.isEmpty() || !playerName.equalsIgnoreCase("Q")) {
            players.add(new Player(playerName));
            playerName = in.getInput("Enter player name (Q to quit): ");
        }
    }

    public void play() {
        initPlayers();

        while (!players.isEmpty()) {
            beginRound();
            boolean evens = rollDice();
            endRound(evens);
            currentRound++;
        }

        out.output("GAME OVER");
    }

    private void playerReport() {
        String report = "Player Report (Round " + currentRound + ")\n";
        if (players.isEmpty()) {
            report += "  No players remain!\n";
        } else {
            for (Player player : players) {
                report += "  " + player.toString() + "\n";
            }
        }
        out.output(report);
    }

    private boolean rollDice() {
        int die1 = rollDie();
        int die2 = rollDie();

        // Players win on even totals
        boolean even = (die1 + die2) % 2 == 0;

        String outcome = "Roll was " + die1 + ", " + die2;

        out.output(outcome + (even ? "\nPlayers WIN!" : "\nPlayers LOSE!"));

        return even;
    }

    private int rollDie() {
        return dice.roll();
    }
}
