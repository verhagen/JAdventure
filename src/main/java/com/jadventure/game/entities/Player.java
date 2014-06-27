package com.jadventure.game.entities;

import java.util.ArrayList;
import java.util.List;

import com.jadventure.game.QueueProvider;
import com.jadventure.game.items.Item;
import com.jadventure.game.items.ItemStack;
import com.jadventure.game.items.Storage;
import com.jadventure.game.navigation.ILocation;
import com.jadventure.game.navigation.LocationType;

/**
 * This class deals with the player and all of its properties.
 * Any method that changes a character or interacts with it should
 * be placed within this class. If a method deals with entities in general or
 * with variables not unique to the player, place it in the entity class.
 */
public class Player extends Entity {
    private ILocation location;

    
    public Player() {
    }


    public void getStatistics() {
        String weaponName = getWeapon();
        if (weaponName != null && (! "hands".equalsIgnoreCase(weaponName))) {
            Item weapon = itemRepo.getItem(getWeapon());
            weaponName = weapon.getName();
        }
  
        QueueProvider.offer("\nPlayer name: " + getName() +
                            "\nCurrent weapon: " + weaponName +
                            "\nGold: " + getGold() +
                            "\nHealth/Max: " + getHealth() + "/" + getHealthMax() +
                            "\nDamage/Armour: " + getDamage() + "/" + getArmour() +
                            "\nStrength: " + getStrength() +
                            "\nIntelligence: " + getIntelligence() +
                            "\nDexterity: " + getDexterity() +
                            "\nLuck: " + getLuck() +
                            "\nStealth: " + getStealth() +
                            "\n" + getName() + "'s level: " + getLevel());
    }

    public void printBackPack() {
        this.storage.display();
    }

//    public void save(Player player) {
//        System.err.println("==    Moved to PlayerRepo    ==");
//    }

    public List<Item> searchItem(String itemName, List<Item> itemList) {
        List<Item> itemMap = new ArrayList<>();
        for (Item item : itemList) {
            String testItemName = item.getName();
            if (testItemName.equals(itemName)) {
                itemMap.add(item);
            }
        }
        return itemMap;
    }

    public List<Item> searchItem(String itemName, Storage storage) {
        List<Item> itemMap = new ArrayList<>();
        for (ItemStack item : storage.getItems()) {
            String testItemName = item.getItem().getName();
            if (testItemName.equals(itemName)) {
                itemMap.add(item.getItem());
            }
        }
        return itemMap;
    }

    public void pickUpItem(String itemName) {
        List<Item> itemMap = searchItem(itemName, getLocation().getItems());
        if (!itemMap.isEmpty()) {
            Item item = itemMap.get(0);
            Item itemToPickUp = itemRepo.getItem(item.getItemID());
            addItemToStorage(itemToPickUp);
            location.removePublicItem(itemToPickUp.getItemID());
            QueueProvider.offer("\n" + item.getName()+ " picked up");
        }
    }

    public void dropItem(String itemName) {
        List<Item> itemMap = searchItem(itemName, getStorage());
        if (!itemMap.isEmpty()) {
            Item item = itemMap.get(0);
            Item itemToDrop = itemRepo.getItem(item.getItemID());
            Item weapon = itemRepo.getItem(getWeapon());
            String wName = weapon.getName();

            if (itemName.equals(wName)) {
                dequipItem(wName);
            }
            removeItemFromStorage(itemToDrop);
            location.addPublicItem(itemToDrop.getItemID());
            QueueProvider.offer("\n" + item.getName()+ " dropped");
        }
    }

    public void equipItem(String itemName) {
        List<Item> itemMap = searchItem(itemName, getStorage());
        if (!itemMap.isEmpty()) {
            Item item = itemMap.get(0);
            setWeapon(item.getItemID());
            QueueProvider.offer("\n" + item.getName()+ " equipped");
        }
    }

    public void dequipItem(String itemName) {
        List<Item> itemMap = searchItem(itemName, getStorage());
        if (!itemMap.isEmpty()) {
            Item item = itemMap.get(0);
            setWeapon("hands");
            QueueProvider.offer("\n" + item.getName()+" dequipped");
        }
    }

    public void inspectItem(String itemName) {
        List<Item> itemMap = searchItem(itemName, getStorage());
        if (itemMap.isEmpty()) {
            itemMap = searchItem(itemName, getLocation().getItems());
        }
        if (!itemMap.isEmpty()) {
            Item item = itemMap.get(0);
            item.display();
        } else {
            QueueProvider.offer("Item doesn't exist within your view.");
        }
    }

    public ILocation getLocation() {
        return location;
    }

    public void setLocation(ILocation location) {
        this.location = location;
    }

    public LocationType getLocationType(){
    	return getLocation().getLocationType();
    }

}
