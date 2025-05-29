package COMP1010.main;
import java.io.*;

public class BuildIO {
    public static void exportCharacter(Character character, String filename) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write("name="+ character.name);
        writer.newLine();
        writer.write("health="+ character.health);
        writer.newLine();
        writer.write("strength="+ character.strength);
        writer.newLine();
        writer.write("intelligence="+ character.intelligence);
        writer.newLine();
        writer.write("dexterity="+ character.dexterity);
        writer.newLine();
        writer.write("defence="+ character.defence);
        writer.newLine();
        writer.write("class="+ character.Class);
        writer.newLine();
        writer.write("race="+ character.Race);
        writer.newLine();
        writer.write("equipment="+ character.Equipment);
        writer.newLine();
        writer.close();
    }

    public static Character importCharacter(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String name ="", Race= "", Class = "", Equipment="", line;
        while ((line =reader.readLine()) != null) {
            String[] parts = line.split("=");
            switch (parts[0]) {
                case "name": name = parts[1]; break;
                case "race": Race = parts[1]; break;
                case "class": Class = parts[1]; break;
                case "equipment": Equipment = parts[1]; break;
            }
        }

        reader.close();

        int health = (int)(Math.random() * 50 + 1); 
        int strength =(int)(Math.random() * 10);
        int intelligence =(int)(Math.random() * 10); 
        int dexterity =(int)(Math.random() * 10);
        int defence=(int)(Math.random() * 10); 

        //weapon modifiers
        String normalisedEquipment = Equipment.split(" \\(")[0].trim();
        switch(normalisedEquipment) {
            case "Holy Hammer":
            defence +=4;
            break;
            case "Divine Doublet":
            strength+=3;
            break;
            case "Flame of Faith":
            intelligence +=3;
            break;
            case "Stealthy shoes":
            dexterity +=4;
            break;
            case "Dangerous Dagger":
            strength +=3;
            break;
            case "Cunning Caltrops":
            defence +=2;
            break;
            case "Faerie Flask":
            health +=10;
            break;
            case "Brair Bindings":
            defence +=3;
            break;
            case "Willow Whistle":
            dexterity +=2;
            break;
            case "Crystal Cauldron":
            intelligence +=4;
            break;
            case "Glowing Gemstone":
            dexterity +=2;
            break;
            case "Arcane Amulet":
            health +=5;
            break;
            case "Rage Remedy":
            health +=10;
            break;
            case "Brutal Broadaxe":
            strength +=5;
            break;
            case "Savage Shield":
            defence +=4;
            break;
        default:
            System.out.println(normalisedEquipment);
        }
        // return new Character(name, health, strength, intelligence, dexterity, defence);
        Character importedCharacter = new Character(name, health, strength, intelligence, dexterity, defence);
        return importedCharacter;
        //GameLogic.playerTeam.add(importedCharacter);


    }



}   

