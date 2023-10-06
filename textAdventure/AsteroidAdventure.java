package textAdventure;
/**
 * Creating a java based text adventure game, that demomstrates the use of essential java concepts
 * By Anish jami, June 2022
 * <p>
 *
 * SPACE ASTEROIDS ADVENTURE GAME
 **/

import java.util.*;

import static textAdventure.Item.ItemType.*;

public class AsteroidAdventure {


    // Player variables

    final static int MAX_FUEL = 25, INVENTORY_SIZE = 30;
    static int health = 50, armour, fuel = MAX_FUEL;
    static int attackDamage = 10, swordDamage;
    static int numHealthPotions = 1;
    static int healthPotionHealAmount = 30, healthPotionDropChance = 50;


    // Asteroid objects

    final static String[][] directions = {
            {"north", "south", "west", "east"},
            {"n", "s", "w", "e"},
            {"go north", "go south", "go west", "go east"},
            {"go n", "go s", "go w", "go e"},
    };
    static Room spaceship = new Room("Spaceship", new String[]{null, "Asteroid A", null, null}, "You are inside your spaceship. The control panel shows that your jetpack has " + fuel/ MAX_FUEL * 100 + "% fuel." +
            "\nYour current inventory space is " + (INVENTORY_SIZE - currentInventorySpace ())),
    asteroidA = new Room("Asteroid A", new String[]{"Spaceship", null, null, "Asteroid B"}, "A rocky asteroid with scattered debris."),
    asteroidB = new Room("Asteroid B", new String[]{null, "Asteroid C","Asteroid A", "Asteroid D"}, "A desolate asteroid with a faint hum in the air."),
    asteroidC = new Room("Asteroid C", new String[]{"Asteroid B", null, null, "Asteroid E"}, "A spooky asteroid with mysterious symbols carved into the walls."),
    asteroidD = new Room("Asteroid D", new String[]{null, "Asteroid E", "Asteroid B", null }, "An asteroid filled with a strange energy."),
    asteroidE = new Room("Asteroid E", new String[]{null, "Asteroid F", "Asteroid C", null}, "A metallic asteroid emitting a protective force field."),
    asteroidF = new Room("Asteroid F", new String[]{"Asteroid E", null, "Asteroid G", null}, "An asteroid containing old spaceship parts and broken spaceship."),
    asteroidG = new Room("Asteroid G", new String[]{"Asteroid C", null, null, "Asteroid F"}, "An asteroid pulsating with destructive energy.");

    static Room[] rooms = { spaceship, asteroidA, asteroidB, asteroidC, asteroidD, asteroidE, asteroidF, asteroidG };


    static ArrayList<Item> inventory = new ArrayList<>();
    static ArrayList<Item> shipStorage = new ArrayList<>();

    // Game variables
    static ArrayList<Enemy> enemies = new ArrayList<>(Arrays.asList(
            new Enemy("Alien Drone", 25, 20),
            new Enemy("Space Spider", 30, 30),
            new Enemy("Space Monkey", 50, 5),
            new Enemy("Zombie Alien", 10, 30),
            new Enemy("Robotic Snake", 20, 10),
            new Enemy("Galactic Centipede", 18, 12),
            new Enemy("SPACE WIZARD", 100, 30)
    ));


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Random rand = new Random();

        addParts();
        addEnemies();

        System.out.println("Welcome to the Asteroid Adventure!");
        System.out.println("-----------------------------------------------------");
        System.out.println("You are a space adventurer whose ship has crashed in a belt of asteroids.");
        System.out.println("After some time for the shock to settle in, you notice your ship in shambles.");
        System.out.println("You run a status report, and find that your spaceship requires immense repair.");
        System.out.println("Luckily, the asteroid belt you landed on, might have some useful items you can use.");
        System.out.println("type -- help -- to find all the commands");

        // Start the game in the Spaceship
        Room currentRoom = rooms[0];


        GAME:
        while (true) {



            if (currentRoom.hasEnemy()) {
                Enemy currentEnemy = currentRoom.getEnemy();
                System.out.println("\t# " + currentEnemy.getName() + " has appeared! #\n");
                System.out.println("\t# You must defeat it before you move forward! #\n");

                label:
                while (currentEnemy.health > 0) {
                    System.out.println("\tYour HP: " + health);
                    System.out.println("\t" + currentEnemy.getName() + "'s HP: " + currentEnemy.health);

                    System.out.print (">  ");
                    String input = in.nextLine();

                    input = input.toLowerCase ().trim();

                    switch ( input ) {
                        case "attack":
                        case "damage":
                        case "strike":
                        case "use sword":
                        case "attack enemy":
                            attackEnemy (rand, currentEnemy);
                            if ( health < 1 ) {
                                System.out.println ("\t> You have taken too much damage, you are too weak to go on!");
                                break label;
                            }
                            break;
                        case "heal":
                        case "use potion":
                        case "potion":
                        case "heal myself":
                        case "use heal":
                        case "drink heal":
                        case "drink potion":
                        case "drink healing potion":
                        case "use healing potion":
                            usePotion ();
                            break;
                        case "help":
                        case "help me":
                        case "need help":
                        case "show commands":
                            showHelp ();
                            break;
                        case "quit":
                        case "quit game":
                        case "stop playing":
                        case "quit playing":
                            break GAME;
                        default:
                            System.out.println ("\tInvalid command! Type -- help -- for all commands");
                            break;
                    }
                }

                if (health < 1) {
                    System.out.println("You limp out of the asteroid belt, weak from battle. You die on " + currentRoom.getName ());
                    break;
                }


                System.out.println("-----------------------------------------------------");
                System.out.println(" # " + currentEnemy.getName() + " was defeated! # ");
                System.out.println("You have " + health + " HP left.");

                // Remove the defeated enemy from the room and spawn potion
                dropPotion (rand, currentEnemy);
                currentRoom.removeEnemy();
            }

            // Display room information and options
            System.out.println("\n-----------------------------------------------------");
            System.out.println("You are now in " + currentRoom.getName() + ".");
            System.out.println(currentRoom.getDescription());

            if (currentRoom.getName ().equals("Spaceship")) {
                fuel = MAX_FUEL;
               moveInventoryToShip();
            }

            System.out.print (">  ");
            String input1 = in.nextLine().toLowerCase ().trim();

            switch ( input1 ) {
                case "explore":
                case "check room":
                case "search room":
                case "explore room":
                case "explore asteroid":
                case "check asteroid":
                case "search asteroid":
                case "look around": // Check if the room has a part
                    if ( !currentRoom.containsItem () ) {
                        System.out.println ("Room does not contain anything else");
                        System.out.println ("\n-----------------------------------------------------");
                        continue;
                    }


                    ArrayList<Item> parts = currentRoom.parts;
                    checkRoom:
                    for ( int j = 0 ; j < parts.size () ; j++ ) {
                        Item p = parts.get (j);
                        System.out.println ("You found a " + p.getName () + "!");
                        System.out.println ("[" + p.getDescription () + "]");

                        for ( Item i : inventory ) {
                            if ( i.type != PART && i.type == p.type && i.getStatBoost () > p.getStatBoost () ) {
                                System.out.println ("You notice that your current " + i.getName () + " is better, so you ignore");
                                j++;
                            }
                        }


                        while (true) {

                            System.out.print (">   ");
                            String input = in.nextLine ();
                            switch ( input ) {
                                case "pickup", "pick up", "take", "take it", "pick", "equip", "use it", "equip it" -> {
                                    if ( ( currentInventorySpace () + p.getInventorySpace () ) > INVENTORY_SIZE ) {
                                        System.out.println ("You do not have enough inventory space to pick up this item. Go back to the spaceship to clear your inventory");
                                        System.out.println ("\n-----------------------------------------------------");
                                    } else {
                                        Iterator<Item> itemIterator = inventory.iterator ();
                                        // pick up an item, and drop the same type of item in the current room
                                        while (itemIterator.hasNext ()) {
                                            Item i = itemIterator.next ();
                                            if ( p.equals (i) ) continue;
                                            if ( i.type != PART && i.type == p.type ) {
                                                currentRoom.parts.add (i);
                                                j++;
                                                itemIterator.remove ();
                                            }
                                        }
                                        inventory.add (p);
                                        currentRoom.parts.remove (p);

                                        System.out.println ("You picked up the " + p.getName () + ".");
                                        System.out.println (p.getDescription ());
                                        System.out.println ("You have " + ( INVENTORY_SIZE - currentInventorySpace () ) + " space left in your inventory");
                                        System.out.println ("\n-----------------------------------------------------");
                                    }
                                    continue checkRoom;
                                }
                                case "leave", "no", "dont", "don't", "no use", "don't use", "dont use", "ignore", "explore" -> {
                                    System.out.println ("You decided to leave the " + p.getName () + " behind.");
                                    continue checkRoom;
                                }
                                case "help", "help me", "need help", "show commands" -> showHelp ();
                                case "inventory", "show inventory", "my inventory", "show my inventory" ->
                                        showInventory ();
                                default -> System.out.println ("\tInvalid command! Type -- help -- for all commands");
                            }

                        }
                    }
                    break;
                case "move room":
                case "next room":
                case "exit room":
                case "move asteroid":
                case "next asteroid":
                case "exit asteroid":
                    currentRoom = nextRoom (in, currentRoom);
                    break;
                case "inventory":
                case "show inventory":
                case "my inventory":
                case "show my inventory":
                    showInventory ();
                    break;
                case "help":
                case "help me":
                case "need help":
                case "show commands":
                    showHelp ();
                    break;
                case "quit":
                case "quit game":
                case "stop playing":
                case "quit playing":
                    break GAME;
                case "repair ship":
                case "repair":
                case "finish ship":
                case "repair it":
                    if ( currentRoom.getName ().equals ("Spaceship") && allPartsCollected () ) {
                        System.out.println ("\n-----------------------------------------------------");
                        System.out.println ("You repair your spaceship using the collected parts.");
                        System.out.println ("With your spaceship now fully functional, you blast off into space!");
                        System.out.println ("Congratulations! You have completed the game.");
                        break GAME;
                    } else if ( allPartsCollected () ) {
                        System.out.println ("\n-----------------------------------------------------");
                        System.out.println ("You can only repair your spaceship inside the spaceship.");
                        System.out.println ("Please go back to the spaceship to repair it.");
                        ;
                    } else
                        System.out.println ("You have not collected all of the parts required to repair the ship, continue exploring");
                    break;
            }
            // not created
//            checkPowerup (in, currentRoom);



            // Check if all ship parts have been collected or player has died, or ran out of fuel
            if ( checkIfGameDone (currentRoom) ) break;
            updateStats();

        }

        System.out.println("\n-----------------------------------------------------");
        System.out.println("Thank you for playing the Asteroid Adventure!");
    }

    private static boolean checkIfGameDone (Room currentRoom) {
        if (allPartsCollected()) {
            System.out.println ("\n-----------------------------------------------------");
            System.out.println ("Congratulations! You have collected all the ship parts.");
            System.out.println ("It's time to return to your spaceship and repair it!");
        }

        if (fuel < 1) {
            System.out.println("Your jetpack has no fuel left, you cannot move anymore and are stuck on " + currentRoom.getName ());
            return true;
        } else if (fuel < 10) System.out.println("Your jetpack is low on fuel, return to the spaceship to refuel");
        return false;
    }

    private static void dropPotion (Random rand, Enemy currentEnemy) {
        if ( rand.nextInt(100) < healthPotionDropChance) {
            numHealthPotions++;
            System.out.println(" # The " + currentEnemy.getName() + " dropped a health potion! # ");
            System.out.println(" # You now have " + numHealthPotions + " health potion(s). # ");
        }
    }

    // use the potion
    private static void usePotion () {
        if (numHealthPotions > 0) {
            health += healthPotionHealAmount;
            numHealthPotions--;
            System.out.println("\t> You drink a health potion, healing yourself for " + healthPotionHealAmount + "."
                                       + "\n\t> You now have " + health + " HP."
                                       + "\n\t> You have " + numHealthPotions + " health potions left.\n");
        } else {
            System.out.println("\t> You have no health potions left! Defeat enemies for a chance to get one!\n");
        }
    }

    private static void attackEnemy (Random rand, Enemy currentEnemy) {

        // damage to enemy
        int damageDealt = rand.nextInt(attackDamage) + swordDamage;

        // damage to player
        int damageTaken = rand.nextInt(currentEnemy.getAttackDamage()) - armour;

        // cannor gain health after attack
        if ( damageTaken < 0 ) damageTaken = 0;
        currentEnemy.getHit (damageDealt);
        health -= damageTaken;

        System.out.println("\t> You strike the " + currentEnemy.getName() + " for " + (damageDealt + swordDamage) + " damage.");
        System.out.println("\t> You receive " + damageTaken + " damage in retaliation!");
    }




    private static void showHelp() {
        System.out.println("\n-----------------------------------------------------");
        System.out.println("HELP:");
        System.out.println("Commands you can use:");
        System.out.println("- explore, check room, search room, explore room, explore asteroid, check asteroid, search asteroid, look around: to search for items in the current room/asteroid");
        System.out.println("- pickup, pick up, take, take it, pick, equip, use it, equip it: to pick up an item found in the room/asteroid");
        System.out.println("- leave, no, don't, no use, don't use, dont use, ignore, explore: to leave an item behind");
        System.out.println("- move room, next room, exit room, move asteroid, next asteroid, exit asteroid: to move to the next adjacent room/asteroid");
        System.out.println("- inventory, show inventory, my inventory, show my inventory: to view your current inventory");
        System.out.println("- attack, damage, strike, use sword, attack enemy: to attack the enemy in the current room/asteroid");
        System.out.println("- heal, use potion, potion, heal myself, use heal: to use a health potion and restore health");
        System.out.println("- help, help me, need help, show commands: to display this help menu");
        System.out.println("- quit, quit game, stop playing, quit playing: to quit the game");
        System.out.println("-----------------------------------------------------\n");
    }


    // show the users inventory
    private static void showInventory() {
        System.out.println("\n-----------------------------------------------------");
        System.out.println("INVENTORY:");

        if (inventory.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            for (Item item : inventory) {
                System.out.println("- " + item);

            }
        }

        System.out.println("-----------------------------------------------------\n");
    }

    // go to a different room in the map
    private static Room nextRoom(Scanner in, Room currentRoom) {
        // Check if the room has adjacent rooms
        if (currentRoom.getAdjacentRooms().length > 0) {
            for ( int i = 0 ; i < currentRoom.getAdjacentRooms ().length ; i++ )
                if ( currentRoom.getAdjacentRooms ()[ i ] != null )
                    System.out.println (directions[ 0 ][ i ] + ": Go to " + currentRoom.getAdjacentRooms ()[ i ]);
        }
        else System.out.println("There are no asteroids to go to.");

        int choice = -1;

        do {
            System.out.print ("> ");
            String input = in.nextLine ().trim ().toLowerCase ();

            for ( String[] direction : directions ) {
                for ( int j = 0 ; j < direction.length ; j++ ) {
                    if ( input.equals (direction[ j ]) ) choice = j;
                }
            }
            // check if choice is null, or out of range
            System.out.println ("Invalid command! P Please enter a valid direction.");

        } while (( choice >= 4 || choice <= -1 ) || ( currentRoom.getAdjacentRooms ()[ choice ] == null ));


        currentRoom = getRoomByName(currentRoom.getAdjacentRooms()[choice]);
        fuel--;

        return currentRoom;
    }


    // change the stats of the player
    private static void updateStats () {
        for ( Item i: inventory) {
            if (i.getType () == ARMOR ) armour = i.getStatBoost ();
            else if ( i.getType () == WEAPON ) swordDamage = i.getStatBoost ();
        }
    }

    // remove the item based on specified type (enum)
    private static void removeItem (Enum type, Room current, Item p) {
        Iterator<Item> j = inventory.iterator();
        while (j.hasNext()) {
            Item item = j.next();
            if (item.getType() == type) {
                current.parts.add (item);
                j.remove();
            }
        }
        inventory.add (p);
    }

    // when you go back to the ship, you clear most of your inventory except armor and weapon
    private static void moveInventoryToShip () {
        Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Item i = iterator.next();
            if (i.getType() == PART) {
                iterator.remove(); // Remove from the original list
                shipStorage.add(i); // Add to the destination list
            }
        }
    }

/*
    private static void checkPowerup (Scanner in, Room currentRoom) {
        // Check if the room has a power-up
        if ( currentRoom.hasPowerUp()) {
            System.out.println("You found a power-up!");
            System.out.println("Would you like to use it?");
            System.out.println("1. Use power-up");
            System.out.println("2. Ignore");
            System.out.print  (">   ");

            String input = in.nextLine();
            if (input.equals("1")) {
                currentRoom.removePowerUp();
                System.out.println("You used the power-up!");
                // Add the desired effect of the power-up here
            } else if (input.equals("2")) {
                System.out.println("You decided to leave the power-up behind.");
            } else {
                System.out.println("Invalid command! Please enter a number.");
            }
        }
    }
*/


    static void addParts () {
        spaceship.setPart(new Item ("Basic Sword", "To attack the enemies (+5 attack)", WEAPON, 5, 2));

        asteroidA.setPart(new Item ("Basic Armour", "Provides protection from the enemies (+2 armour)", ARMOR, 2, 2));

        asteroidA.setPart(new Item ("Engine Piston", "To repair damaged engine", PART, 10));
        asteroidB.setPart(new Item ("Speakers", "New sound system for the ship, so you can listen to some tunes", PART, 10));
        asteroidB.setPart(new Item ("Good Armour", "Provides protection from the enemies (+5 armour)", ARMOR, 5, 2));

        asteroidC.setPart(new Item ("Navigation system", "Replaces the damaged GPS system", PART, 10));
        asteroidD.setPart(new Item ("Energy Chrystal", "Fuels the ship with mysterious energy", PART, 10));
        asteroidD.setPart(new Item ("Good Sword", "To attack the enemies (+10 attack)", WEAPON, 10, 2));

        asteroidE.setPart(new Item ("Shield Orb", "Protects the ship from space debris and asteroids, so you dont land in the same situation again", PART, 10));
        asteroidE.setPart(new Item ("Cool armour", "Defend from the enemies (+10 shield)", ARMOR, 10, 2));

        asteroidF.setPart(new Item ("Ship Thruster", "To enable the ship to escape the asteroid belt's gravity", PART, 10));
        asteroidF.setPart(new Item ("Ultimate Sword", "To attack the enemies with cool swag (+20 attack)", WEAPON, 20, 2));

        asteroidG.setPart(new Item ("Treasure chest", "A treasure chest containing priceless artifacts and jewelery", PART, 20));

    }

    // Helper method to add random enemies to rooms based on a certain chance
    private static void addEnemies () {
        for ( Room room: rooms) {
            if (room.getName ().equals ("Spaceship")) continue;

            // add final boss to asteroid G
            if ( room.getName ().equals ("Asteroid G")){
                room.setEnemy (enemies.get (enemies.size () - 1));
                enemies.remove (enemies.size () - 1);
                continue;
            }
            if ( Math.random () <= 0.75 ) {
                int index = ( int ) (Math.random () * enemies.size ());
                room.setEnemy (enemies.get (index));
                enemies.remove (index);
            }

        }

    }

    // check if all ship parts have been collected
    private static boolean allPartsCollected() {
        for (Room room : rooms) {
            if (room.getName().equals("Spaceship")) continue;
            if ( room.hasPart ()) return false;
        }
        return true;
    }

    private static Room getRoomByName(String name) {
        for (Room room : rooms) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    public static int currentInventorySpace (){
        int space = 0;
        if (inventory == null) return 0;
        for (Item i: inventory) space += i.getInventorySpace();
        return space;
    }


}
