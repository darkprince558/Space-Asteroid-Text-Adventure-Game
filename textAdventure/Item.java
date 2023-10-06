package textAdventure;

class Item {
    final String name, description;
    final ItemType type;
    private final int inventorySpace, statBoost;


    public enum ItemType {
        PART,
        WEAPON,
        ARMOR
    }

    public Item(String name, String description, ItemType type, int inventorySpace) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.statBoost = 0;
        this.inventorySpace = inventorySpace;
    }

    public Item(String name, String description, ItemType type, int statBoost, int inventorySpace) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.statBoost = statBoost;
        this.inventorySpace = inventorySpace;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    // how much will this item improve your player stats (regardless of type)
    public int getStatBoost() {
        return statBoost;
    }

    public int getInventorySpace () {
        return inventorySpace;
    }

    @Override
    public String toString() {
        return "item: " + name + "\n" +
                "     - Description: " + description + "\n" +
                "     - Type: " + type + "\n" +
                "     - Stat Boost: " + statBoost + "\n" +
                "     - Inventory Space: " + inventorySpace + "\n";
    }

}