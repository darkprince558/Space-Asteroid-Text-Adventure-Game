package textAdventure;

class Enemy {
    String name;
    int health;
    int attackDamage;

    public Enemy(String name, int health, int attackDamage) {
        this.name = name;
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public String getName() {
        return name;
    }

    public void getHit(int damage){
        health-=damage;
    }

    public int getAttackDamage() {
        return attackDamage;
    }
}