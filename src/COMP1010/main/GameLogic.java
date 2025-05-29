package COMP1010.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class GameLogic {
    static Scanner scanner = new Scanner(System.in);//checks for user input
    static ArrayList<Character> playerTeam = new ArrayList<>();// list of player controlled characters
    static ArrayList<Character> enemyTeam = new ArrayList<>();// list of enemy-controlled characters 
    
    public static int readInputInt(String prompt, int numOptions) {
        int input;
        do {
            System.out.println(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());//parsing
            } catch (Exception e) {
                input = -1;
                System.out.println("Invalid input! Please try again.");//alternative for anything other then integer
            }
        } while (input < 1 || input > numOptions);//validate range 
        return input;
    }
    //reads a string input 
    public static String readInputString(String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine();
        return input;
        
    }
    //clear console 
    public static void clearConsole() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }
    //pauses game and waits for user to press ENTER to proceed 
    public static void pauseGame(String message) {
        System.out.println(message);
        scanner.nextLine();
    }
    //Formatting 
    public static void textSeperator(int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
    //formatting 
    public static void headingCreator(String heading) {
        textSeperator(30);
        System.out.println(heading);
        textSeperator(30);
    }
    public static void gameStart() throws IOException{
        // add game name/dev names etc.
        int exportCounter =0;
        int importCounter =0;
        headingCreator("You are standing in an open field west of a white house.");
        pauseGame("Press ENTER to start the game: ");
        headingCreator("Create 3 characters to fight with to begin the game: ");
        while (playerTeam.size() < 3) {
            headingCreator("would you like to import character builds?, (1) Yes, (any key) No");
            if (scanner.nextLine().equalsIgnoreCase("1")) {
                    playerTeam.add(BuildIO.importCharacter("test"+importCounter));
                    importCounter++;
                    exportCounter++;
                    //System.out.println(importCounter);
            }
            else {
                characterCreation();
                headingCreator("Do you wish to export your character, (1) Yes, (any key) No");
                if (scanner.nextLine().equalsIgnoreCase("1")) {
                    BuildIO.exportCharacter(playerTeam.get(exportCounter), "test"+exportCounter);
                    exportCounter++;
                    //System.out.println(exportCounter);
                }
            }
        }    
        //Enemy Creation 
        // need to randomise to some degree
        //Enemy 1
        Character enemy1 = new Character("Goblin", 10, 10, 5, 6, 4);
        enemy1.Race = new Race("race1", new StatMod(2, 2), new StatMod(4, 1));
        enemy1.Class = new Class("class1");
        enemy1.Equipment = new Equipment("weapon1", new StatMod(2, 2));
        enemy1.identifier = 1;
        enemyTeam.add(enemy1);
        //Enemy 2
        Character enemy2 = new Character("Kobold", 10, 12, 3, 7, 6);
        enemy2.Race = new Race("race2", new StatMod(1, 5), new StatMod(2, 2));
        enemy2.Class = new Class("class2");
        enemy2.Equipment = new Equipment("weapon2", new StatMod(2, 3));
        enemy2.identifier = 1;
        enemyTeam.add(enemy2);
        //Enemy
        Character enemy3 = new Character("Witch", 10, 12, 3, 5, 6);
        enemy3.Race = new Race("race3", new StatMod(1, 5), new StatMod(2, 2));
        enemy3.Class = new Class("class3");
        enemy3.Equipment = new Equipment("weapon3", new StatMod(2, 3));
        enemy3.identifier = 1;
        enemyTeam.add(enemy3);
        //Turn order st up based on Speed(Dex)
        ArrayList<Character> turnOrder = new ArrayList<>();
        turnOrder.addAll(playerTeam);
        turnOrder.addAll(enemyTeam);
        Collections.sort(turnOrder, (a, b) -> Integer.compare(a.dexterity, b.dexterity));//Comaprison 

        System.out.println(turnOrder); //need to removed at somepoint
        

        // start battle
        headingCreator("BATTLE STARTS!");
        System.out.println("Your team will now face: ");
        for (Character enemy : enemyTeam) {
            System.out.println("- " + enemy.name);
            }
        pauseGame("Press ENTER to begin the fight: ");
        
        Node n1 = battleOrderCreator(turnOrder);//Managing the turn based order
       //rounds 
        int roundCount = 1;
        int roundResult;
        System.out.println("Round 1!");
        while (true) {
            //Removal of defence buffs each round
            for (Character obj : turnOrder) {
                if (obj.defenceBuff > 0) {
                    obj.defence -= obj.defenceBuff;
                    obj.defenceBuff = 0;
                }
            }
            roundResult = battleLoop(n1, playerTeam, enemyTeam);
            if (roundResult < 0) {
                break;//End game if less then 0 so -negatives
            }
            else if (roundResult == 0) {
                continue;//Continue to next round 
            }
            else {
                roundCount += roundResult;
                pauseGame("Round " + roundCount + "!\nPress ENTER to continue: ");//Next step prompt 
            }
        }
        //Outcome of the battle based on win or loss
        if (roundResult == -1) {
            System.out.println("You Win!");
        }
        else {
            System.out.println("You Lose!");
        }
    }
    public static void characterCreation() throws IOException{
        Character playerChar = new Character(null, 0, 0, 0, 0, 0);
        Class playerClass = new Class(null);
        Race playerRace = new Race(null, null, null);
        Equipment playerEquip = new Equipment(null,null);
        String numPostfix;
        if (playerTeam.size() == 0) {
            numPostfix = "st";
        }
        else if (playerTeam.size() == 1) {
            numPostfix = "nd";
        }
        else {
            numPostfix = "rd";
        }
        headingCreator("Create your " + (playerTeam.size() + 1) + numPostfix + " character");

        playerChar.name = getCharName();

        int rolledStats[] = rollStats();
        playerChar.health = rolledStats[0];
        playerChar.currenthp = playerChar.health;
        playerChar.strength = rolledStats[1];
        playerChar.intelligence = rolledStats[2];
        playerChar.dexterity = rolledStats[3];
        playerChar.defence = rolledStats[4];

        ArrayList<Integer> raceStats = getRace();
        playerRace.name = getRaceName(raceStats);
        playerRace.stat1 = new StatMod(raceStats.get(1), raceStats.get(2));
        playerRace.stat2 = new StatMod(raceStats.get(3), raceStats.get(4));
        raceStatMod(playerChar, playerRace.stat1, playerRace.stat2);
        playerChar.Race = playerRace;

        Returnpair classRpair  = getClassName();
        playerClass.name = classRpair.name;
        playerChar.Class = playerClass;

        Returnpair equipRpair = getEquip(classRpair.num);
        playerEquip.name = equipRpair.name;
        playerEquip.stat1 = equipRpair.stat1;
        equipStatMod(playerChar, playerEquip.stat1);
        playerChar.Equipment = playerEquip;
        playerChar.identifier = 0;
  
        headingCreator("Your character is ready!");
        System.out.println("Character Name: " + playerChar.name);
        System.out.println("Health: " + playerChar.health);
        System.out.println("Strength: " + playerChar.strength);
        System.out.println("Intelligence: " + playerChar.intelligence);
        System.out.println("Dexterity: " + playerChar.dexterity);
        System.out.println("Defence: " + playerChar.defence);
        System.out.println("Race: " + playerChar.Race);
        System.out.println("Class: " + playerChar.Class);
        System.out.println("Equipment: " + playerChar.Equipment);

        playerTeam.add(playerChar);

    }

    public static Node battleOrderCreator(ArrayList<Character> turnOrder) {
        Node n6 = new Node(turnOrder.get(5), null);
        Node n5 = new Node(turnOrder.get(4), n6);
        Node n4 = new Node(turnOrder.get(3), n5);
        Node n3 = new Node(turnOrder.get(2), n4);
        Node n2 = new Node(turnOrder.get(1), n3);
        Node n1 = new Node(turnOrder.get(0), n2);
        return n1;
    }

    public static int battleLoop(Node start, ArrayList<Character> playerTeam, ArrayList<Character> enemyTeam) {
        int playerTeamHealth = playerTeam.get(0).currenthp + playerTeam.get(1).currenthp + playerTeam.get(2).currenthp;
        int enemyTeamHealth = enemyTeam.get(0).currenthp + enemyTeam.get(1).currenthp + enemyTeam.get(2).currenthp;

        if (start == null) {
            return 1;
        }
        if (enemyTeamHealth == 0){
            return -1;
        }
        else if (playerTeamHealth == 0) {
            return -2;
        }

        if (start.data.currenthp <= 0) {
            start.data.identifier = 3;
        }
        if (start.data.identifier == 0) {
            System.out.println(start.data.name + "'s turn: " );
            System.out.println(start.data.name + " is on " + start.data.currenthp + " health: ");
            int battleChoice = readInputInt("Would you like to attack (1), defend (2) or heal (3): ", 3);
            switch (battleChoice) {
                case 1:
                    int attacktarget = readInputInt("Who would you like to attack " + enemyTeam.get(0).name + " (1), " + enemyTeam.get(1).name + " (2) or " 
                                                    + enemyTeam.get(2).name + " (3): ", 3);
                    Character.attack(start.data, enemyTeam.get(attacktarget - 1));
                    battleLoop(start.next, playerTeam, enemyTeam);
                    break;
                case 2:
                    Character.defend(start.data);
                    battleLoop(start.next, playerTeam, enemyTeam);
                    break;
                case 3:
                    Character.heal(start.data);
                    battleLoop(start.next, playerTeam, enemyTeam);
                    break;
            }
        }
        else if (start.data.identifier == 1) {
            int choiceMod;
            if (start.data.currenthp == start.data.health) {
                choiceMod = 2;
            }
            else {
                choiceMod = 3;
            }
            int battleChoice = (int)(Math.random() * choiceMod + 1);
            System.out.println(start.data.name + "'s turn: " );
            System.out.println(start.data.name + " is on " + start.data.currenthp + " health: ");
            switch (battleChoice) {
                case 1:
                    Character.attack(start.data, playerTeam.get((int)(Math.random() * 3)));
                    battleLoop(start.next, playerTeam, enemyTeam);
                    break;
                case 2:
                    Character.defend(start.data);
                    battleLoop(start.next, playerTeam, enemyTeam);
                    break;
                case 3:
                    Character.heal(start.data);
                    battleLoop(start.next, playerTeam, enemyTeam);
                    break;
            }
        }
        else {
            battleLoop(start.next, playerTeam, enemyTeam);
        }
        return 0;
    }
    // move all of this to character file probably
    public static String getCharName() {
        String name;
        boolean nameConfirm = false;
        do{
            name = readInputString("Whats your characters name: ");
            headingCreator("Your name is " + name + ".\nIs that correct?");
            System.out.println("(1) Yes");
            System.out.println("(2) No");
            int input = readInputInt("->", 2);
            if (input == 1) {
                nameConfirm = true;
            }
        }while (!nameConfirm);
        return name;
    }

    public static int[] rollStats() {
        int[] stats = new int[5];
        pauseGame("Press ENTER to roll for health: ");
        int rolledHealth = (int)(Math.random() * 50 + 1);
        stats[0] = rolledHealth;
        System.out.println("You rolled: " + rolledHealth);
        pauseGame("Press ENTER to roll for strength: ");
        int rolledStrength = (int)(Math.random() * 10);
        stats[1] = rolledStrength;
        System.out.println("You rolled: " + rolledStrength);
        pauseGame("Press ENTER to roll for intelligence: ");
        int rolledInt = (int)(Math.random() * 10);
        stats[2] = rolledInt;
        System.out.println("You rolled: " + rolledInt);
        pauseGame("Press ENTER to roll for dexterity: ");
        int rolledDex = (int)(Math.random() * 10);
        stats[3] = rolledDex;
        System.out.println("You rolled: " + rolledDex);
        pauseGame("Press ENTER to roll for defence: ");
        int rolledDefence = (int)(Math.random() * 10);
        stats[4] = rolledDefence;
        System.out.println("You rolled: " + rolledDefence);
        return stats;
    }
    public static ArrayList<Integer> getRace(){
        int raceChoice = readInputInt("Choose a race: (1)'Human', (2)'Orc', (3)'Elf', (4)'Dwarf', (5)'Undead'", 5);
        ArrayList<Integer> raceArray = new ArrayList<Integer>(); // INDEXES: (0) = Race, (1) = First stat to mod, (2) = Amount to mod, (3) Second stat to mod, (4) Amount to mod
        raceArray.add(raceChoice);
        switch (raceChoice) {
            case 1:
                int[] hstats = humanStatChoice();
                Collections.addAll(raceArray, hstats[0], 1 , hstats[1], 1);
                return raceArray;
            case 2:
                Collections.addAll(raceArray, 2, 2, 3, -1);
                return raceArray;
            case 3:
                Collections.addAll(raceArray, 4, 2, 2, -1);
                return raceArray;
            case 4:
                Collections.addAll(raceArray, 5, 2, 4, -1);    
                return raceArray;
            case 5:
                Collections.addAll(raceArray, 3, 2, 5, -1);    
                return raceArray;
            default:
                return null; //Unreachable
        }
        
    }
    public static String getRaceName(ArrayList<Integer> list) {
        switch (list.get(0)) {
            case 1:
                return "Human";
            case 2:
                return "Orc";
            case 3:
                return "Elf";
            case 4:
                return "Dwarf";
            case 5:
                return "Undead";
            default:
                return null; // Unreachable
        }
    }
    public static int[] humanStatChoice() {
        int[] statChoice = new int[2];
        statChoice[0] = readInputInt("Pick the first stat to '+1': (1)'Health', (2)'Strength', (3)'Intelligence', (4)'Dexterity', (5)'Defence'", 5);
        statChoice[1] = readInputInt("Pick the second stat to '+1': (1)'Health', (2)'Strength', (3)'Intelligence', (4)'Dexterity', (5)'Defence'", 5);
        return statChoice;
    }
    public static void raceStatMod(Character player, StatMod stat1, StatMod stat2) {
        switch (stat1.statToMod) {
            case 1:
                player.health += stat1.valueOfMod;
                System.out.println("Your Health was changed by " + stat1.valueOfMod);
                break;
            case 2:
                player.strength += stat1.valueOfMod;
                System.out.println("Your Strength was changed by " + stat1.valueOfMod);
                break;
            case 3:
                player.intelligence += stat1.valueOfMod;
                System.out.println("Your Intelligence was changed by " + stat1.valueOfMod);
                break;
            case 4:
                player.dexterity += stat1.valueOfMod;
                System.out.println("Your Dexterity was changed by " + stat1.valueOfMod);
                break;
            case 5:
                player.defence += stat1.valueOfMod;
                System.out.println("Your Defence was changed by " + stat1.valueOfMod);
                break;
            default:
                break; //Unreachable
        }
        switch (stat2.statToMod) {
            case 1:
                player.health += stat2.valueOfMod;
                System.out.println("Your Health was changed by " + stat2.valueOfMod);
                break;
            case 2:
                player.strength += stat2.valueOfMod;
                System.out.println("Your Strength was changed by " + stat2.valueOfMod);
                break;
            case 3:
                player.intelligence += stat2.valueOfMod;
                System.out.println("Your Intelligence was changed by " + stat2.valueOfMod);
                break;
            case 4:
                player.dexterity += stat2.valueOfMod;
                System.out.println("Your Dexterity was changed by " + stat2.valueOfMod);
                break;
            case 5:
                player.defence += stat2.valueOfMod;
                System.out.println("Your Defence was changed by " + stat2.valueOfMod);
                break;
            default:
                break; //Unreachable
        }
    }
    public static Returnpair getClassName() {
        int classChoice = readInputInt("Choose a class: (1)'Paladin', (2)'Rogue', (3)'Druid', (4)'Wizard', (5)'Barbarian'", 5);
        switch (classChoice) {
            case 1:
                return new Returnpair("Paladin", 1);
            case 2:
                return new Returnpair("Rogue", 2);
            case 3:
                return new Returnpair("Druid", 3);
            case 4:
                return new Returnpair("Wizard", 4);
            case 5:
                return new Returnpair("Barbarian", 5);  
            default:
                return null; //Unreachable
        }
    }
    public static Returnpair getEquip(int classNum) {
        Returnpair equipPair = new Returnpair(null, null);
        switch (classNum) {
            case 1:
                int paladinEquip = readInputInt("Choose a Paladin Equipment: (1)'Holy Hammer', (2)'Divine Doublet', (3)'Flame of Faith'", 3);
                switch (paladinEquip) {
                    case 1:
                        equipPair.name = "Holy Hammer";
                        equipPair.stat1 = new StatMod(5, 4);
                        return equipPair;
                    case 2:
                        equipPair.name = "Divine Doublet";
                        equipPair.stat1 = new StatMod(2, 3);
                        return equipPair;
                    case 3:
                        equipPair.name = "Flame of Faith";
                        equipPair.stat1 = new StatMod(3, 3);
                        return equipPair;
                    }
            break;
            case 2:
                int rogueEquip = readInputInt("Choose a Rogue Equipment: (1)'Stealthy Shoes', (2)'Dangerous Dagger', (3)'Cunning Caltrops'", 3);
                switch (rogueEquip) {
                    case 1:
                        equipPair.name = "Stealthy Shoes";
                        equipPair.stat1 = new StatMod(4, 4);
                        return equipPair;
                    case 2:
                        equipPair.name = "Dangerous Dagger";
                        equipPair.stat1 = new StatMod(2, 3);
                        return equipPair;
                    case 3:
                        equipPair.name = "Cunning Caltrops";
                        equipPair.stat1 = new StatMod(5, 2);
                        return equipPair;
                    }
            break;
            case 3:
                int druidEquip = readInputInt("Choose a Druid Equipment: (1)'Faerie Flask', (2)'Brair Bindings', (3)'Willow Whistle'", 3);
                switch (druidEquip) {
                    case 1:
                        equipPair.name = "Faerie Flask";
                        equipPair.stat1 = new StatMod(1, 10);
                        return equipPair;
                    case 2:
                        equipPair.name = "Brair Bindings";
                        equipPair.stat1 = new StatMod(5, 3);
                        return equipPair;
                    case 3:
                        equipPair.name = "Willow Whistle";
                        equipPair.stat1 = new StatMod(4, 2);
                        return equipPair;
                    }
            break;
            case 4:
                int wizardEquip = readInputInt("Choose a Wizard Equipment: (1)'Crystal Cauldron', (2)'Glowing Gemstone', (3)'Arcane Amulet'", 3);
                switch (wizardEquip) {
                    case 1:
                        equipPair.name = "Crystal Cauldron";
                        equipPair.stat1 = new StatMod(3, 4);
                        return equipPair;
                    case 2:
                        equipPair.name = "Glowing Gemstone";
                        equipPair.stat1 = new StatMod(4, 2);
                        return equipPair;
                    case 3:
                        equipPair.name = "Arcane Amulet";
                        equipPair.stat1 = new StatMod(1, 5);
                        return equipPair;
                    }
            break;
            case 5:
                int barbarianEquip = readInputInt("Choose a Barbarian Equipment: (1)'Rage Remedy', (2)'Brutal Broadaxe', (3)'Savage Shield'", 3);
                switch (barbarianEquip) {
                    case 1:
                        equipPair.name = "Rage Remedy";
                        equipPair.stat1 = new StatMod(1, 10);
                        return equipPair;
                    case 2:
                        equipPair.name = "Brutal Broadaxe";
                        equipPair.stat1 = new StatMod(2, 5);
                        return equipPair;
                    case 3:
                        equipPair.name = "Savage Shield";
                        equipPair.stat1 = new StatMod(5, 4);
                        return equipPair;
                    }
            break;    
            default:
                break; //Unreachable
        }
        return equipPair;
    }
    public static void equipStatMod(Character player, StatMod stat1) {
        switch (stat1.statToMod) {
            case 1:
                player.health += stat1.valueOfMod;
                System.out.println("Your Health was changed increased " + stat1.valueOfMod);
                break;
            case 2:
                player.strength += stat1.valueOfMod;
                System.out.println("Your Strength was changed increased " + stat1.valueOfMod);
                break;
            case 3:
                player.intelligence += stat1.valueOfMod;
                System.out.println("Your Intelligence was changed increased " + stat1.valueOfMod);
                break;
            case 4:
                player.dexterity += stat1.valueOfMod;
                System.out.println("Your Dexterity was changed increased " + stat1.valueOfMod);
                break;
            case 5:
                player.defence += stat1.valueOfMod;
                System.out.println("Your Defence was changed increased " + stat1.valueOfMod);
                break;
            default:
                break; //Unreachable
        }
    }
}


