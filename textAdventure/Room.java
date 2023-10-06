package textAdventure;

import java.util.ArrayList;


class Room {
    ArrayList<Item> parts = new ArrayList<Item>();
    final String name;
    final String[] exits;
    final String description;
    String powerUp;
    boolean hasEnemy;
    Enemy enemy;

    public Room(String name, String[] exits, String description) {
        this.name = name;
        this.exits = exits;
        this.description = description;
        this.hasEnemy = false;
        this.enemy = null;
        this.powerUp = null;
    }

    public String getName() {
        return name;
    }



    public String getDescription() {
        return description;
    }

    public boolean hasEnemy() {
        return hasEnemy;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
        hasEnemy = true;
    }

    public String[] getAdjacentRooms () {
        return exits;
    }

    public void removeEnemy () {
        this.enemy = null;
        hasEnemy = false;
    }


    public void setPart (Item part) {
        parts.add(part);
    }

    public boolean containsItem() {
        return (parts.size() > 0);

    }

    public String getPowerUp() {
        return powerUp;
    }
    public String[] getExits() {
        return exits;
    }

    public String getPart() {
        return parts.get(parts.size() - 1).getName();
    }

    public Item getPartOBJ() {
        return parts.get(parts.size() - 1);
    }


    public boolean hasPowerUp() {
        return powerUp != null;
    }

    public void removePart() {
        parts.remove (parts.size() - 1);
    }

    public void setPowerUp(String powerUp) {
        this.powerUp = powerUp;
    }
    public void removePowerUp () {
        this.powerUp = null;
    }

    public void applyPowerUp() {
        powerUp = null;
    }

    public boolean hasPart() {
        for (Item part : parts) {
            if (part.getType() == Item.ItemType.PART) {
                return true;
            }
        }
        return false;
    }

}
