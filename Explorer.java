/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;


class Explorer {
    String name;
    int hp;
    
    MyLinkedList<Integer> echoStones; 
    int paradoxStones;
    int treasurePoints;
    boolean isAlive;

    public Explorer(String name) {
        this.name = name;
        this.hp = 3;
    
        this.echoStones = new MyLinkedList<>(); 
        this.paradoxStones = 0;
        this.treasurePoints = 0;
        this.isAlive = true;
    }

   
    public Explorer(Explorer other) {
        this.name = other.name;
        this.hp = other.hp;
        this.isAlive = other.isAlive;
        this.paradoxStones = other.paradoxStones;
        this.treasurePoints = other.treasurePoints;
        this.echoStones = other.echoStones.copy(); 
    }

    public void takeDamage(int amount) {
        this.hp -= amount;
        if (this.hp <= 0) {
            this.hp = 0;
            this.isAlive = false;
            System.out.println("!!! " + name + " DEAD! !!!");
        }
    }

    public void heal(int amount) {
        if (isAlive) {
            this.hp += amount;
            if (this.hp > 3) this.hp = 3;
        }
    }
    
    public void addTreasure(int amount) {
        if (isAlive) {
            this.treasurePoints += amount;
            System.out.println("  + " + name + ", " + amount + " earned treasure points!");
        }
    }
    
    public boolean useEchoStone(int value) {
       
        boolean removed = echoStones.remove(value);
        if (removed) {
           System.out.println(name + " used Echo Stone number " + value + ".");
            return true;
        } else {
            System.out.println(name + " hasn't got Echo Stone number " + value + ".");
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name + " (HP: " + hp + ", Echos: " + echoStones.size() + ", Paradox: " + paradoxStones + ", Treasure: " + treasurePoints + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Explorer explorer = (Explorer) obj;
        return name.equals(explorer.name);
    }
}
