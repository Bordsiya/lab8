package utils;

import comparators.AchievementsComparator;
import comparators.WeaponTypeComparator;
import data.SpaceMarine;
import exceptions.EmptyCollectionException;
import utils.database.DatabaseCollectionManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Class for working with collection
 * @author NastyaBordun
 * @version 1.1
 */

public class CollectionManager {
    /**
     * Collection of elements {@link SpaceMarine}
     */
    private Stack<SpaceMarine> stack;

    /**
     * Last initialization time for the collection
     */
    private LocalDateTime lastInit = null;

    private DatabaseCollectionManager databaseCollectionManager;

    /**
     * Creating a class to work with the collection
     * @see CollectionManager#lastInit
     */
    public CollectionManager(DatabaseCollectionManager databaseCollectionManager){
        this.databaseCollectionManager = databaseCollectionManager;
        this.lastInit = LocalDateTime.now();
        loadCollection();
    }

    public void loadCollection(){
        try{
            stack = databaseCollectionManager.getCollection();
            ResponseBuilder.appendln("Коллекция загружена");
            ServerLauncher.logger.log(Level.INFO, "Коллекция загружена");
        }
        catch (SQLException e){
            stack = new Stack<>();
            ResponseBuilder.appendError("Коллекция не может быть загружена");
            ServerLauncher.logger.log(Level.SEVERE, "Коллекция не может быть загружена");
        }
    }

    /**
     * Getting collection last initialization time
     * @return {@link CollectionManager#lastInit}
     */
    public LocalDateTime getLastInit(){
        return this.lastInit;
    }
    /**
     * Getting collection
     * @return {@link CollectionManager#stack}
     */
    public Stack<SpaceMarine> getCollection(){
        return this.stack;
    }

    /**
     * Getting collection size
     * @return collection size
     */
    public int collectionSize(){
        return stack.size();
    }

    /**
     * Printing {@link CollectionManager#stack} collection
     * @throws EmptyCollectionException if collection is empty
     * @see CollectionManager#getCollection()
     */
    public String getStringCollection() throws EmptyCollectionException {
        if(this.getCollection().size() == 0) throw new EmptyCollectionException("Коллекция пуста");
        String ans = "";
        for (SpaceMarine sm : this.getCollection()){
            ans += "Космический корабль\n" + sm.toStringSafety() + "\n";
        }
        return ans;
    }

    /**
     * Adding new element to collection {@link CollectionManager#stack}
     * @param spaceMarine new element with type {@link SpaceMarine}
     * @see SpaceMarine#setId(Integer)
     * @see SpaceMarine#setCreationDate(LocalDateTime)
     */
    public void addElement(SpaceMarine spaceMarine){
        this.stack.push(spaceMarine);
    }

    /**
     * Searching element with type {@link SpaceMarine} in collection {@link CollectionManager#stack} by ID
     * @param id ID of the searching element
     * @return founded element with type {@link SpaceMarine}
     * @see SpaceMarine#getId()
     */
    public SpaceMarine searchById(Integer id){
        for(SpaceMarine sm : this.stack){
            if(sm.getId().equals(id)){
                return sm;
            }
        }
        return null;
    }

    /**
     * Deleting element with type {@link SpaceMarine} {@link CollectionManager#stack} collection by ID
     * @param id ID of the deleting element
     * @return operation result
     * @see CollectionManager#searchById(Integer)
     * @see CollectionManager#getCollection()
     */
    public boolean removeElementById(Integer id){
        SpaceMarine spaceMarine = searchById(id);
        if (spaceMarine != null){
            this.getCollection().remove(spaceMarine);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Collection clearing {@link CollectionManager#stack}
     */
    public void clearCollection(){
        stack.clear();
    }

    /**
     * Collection {@link CollectionManager#stack} sorting in reverse order
     * @see CollectionManager#getCollection()
     */
    public String getReorderedCollection() throws EmptyCollectionException {
        ArrayList<SpaceMarine> arrayList = new ArrayList<>(this.getCollection());
        if(arrayList.size() == 0) throw new EmptyCollectionException("Коллекция пуста");
        Collections.reverse(arrayList);
        Stack<SpaceMarine> newStack = new Stack<>();
        newStack.addAll(arrayList);
        String ans = "";
        for(SpaceMarine sp: newStack){
            ans += "Космический корабль\n" + sp.toStringSafety() + "\n";
        }
        return ans;
    }

    /**
     * Searching for the collection elements with type {@link SpaceMarine}, with achievements fields starts with the certain substring
     * @param achievement substring for searching
     * @return suitable SpaceMarines
     * @see CollectionManager#getCollection()
     */
    public ArrayList<SpaceMarine> startsWithAchievements(String achievement){
        ArrayList<SpaceMarine> spaceMarines = new ArrayList<>();
        for (SpaceMarine sm : this.getCollection()){
            if(sm.getAchievements().indexOf(achievement) == 0){
                spaceMarines.add(sm);
            }
        }
        return spaceMarines;
    }

    /**
     * Printing command for field weaponType of all collection elements, with types {@link SpaceMarine}, in ascending order
     * @see WeaponTypeComparator
     * @see CollectionManager#printWeaponTypes(Stack)
     * @see CollectionManager#getCollection()
     */
    public String[] ascendWeaponType(){
        WeaponTypeComparator cmp = new WeaponTypeComparator();
        ArrayList<SpaceMarine> list = new ArrayList<>(this.getCollection());
        list.sort(cmp);
        Stack<SpaceMarine> newStack = new Stack<>();
        newStack.addAll(list);
        return printWeaponTypes(newStack);
    }

    /**
     * Printing weaponType fields of collection {@link CollectionManager#stack} elements with types {@link SpaceMarine}
     * @param stack collection
     * @see SpaceMarine#getWeaponType()
     */
    public String[] printWeaponTypes(Stack <SpaceMarine> stack){
        ArrayList<String> ans = new ArrayList<>();
        for(SpaceMarine sm : stack){
            ans.add(sm.getWeaponType().toString());
        }
        String[] args = new String[ans.size()];
        args = ans.toArray(args);
        return args;
    }
    /**
     * Printing command for field achievements of all collection elements, with types {@link SpaceMarine}, in descending order
     * @see AchievementsComparator
     * @see CollectionManager#printAchievements(Stack)
     * @see CollectionManager#getCollection()
     */
    public String[] descendAchievements(){
        AchievementsComparator cmp = new AchievementsComparator();
        ArrayList<SpaceMarine> list = new ArrayList<>(this.getCollection());
        list.sort(cmp);
        Stack<SpaceMarine> newStack = new Stack<>();
        newStack.addAll(list);
        return printAchievements(newStack);
    }
    /**
     * Printing achievements fields of collection {@link CollectionManager#stack} elements with types {@link SpaceMarine}
     * @param stack collection
     */
    public String[] printAchievements(Stack<SpaceMarine> stack){
        ArrayList<String> ans = new ArrayList<>();
        for(SpaceMarine sm : stack){
            ans.add(sm.getAchievements());
        }
        String[] args = new String[ans.size()];
        args = ans.toArray(args);
        return args;
    }

    /**
     * Removing for collection {@link CollectionManager#stack} elements with type {@link SpaceMarine}, bigger than assigned element
     * @param spaceMarine assigned element
     * @return operation result
     * @see SpaceMarine#setId(Integer)
     * @see SpaceMarine#setCreationDate(LocalDateTime)
     * @see CollectionManager#getCollection()
     * @see SpaceMarine#getName()
     * @see SpaceMarine#getId()
     * @see CollectionManager#removeElementById(Integer)
     */
    public ArrayList<SpaceMarine> getGreater(SpaceMarine spaceMarine){
        ArrayList<SpaceMarine> results = new ArrayList<>();
        ArrayList<SpaceMarine> spaceMarines = new ArrayList<>(this.getCollection());
        if(!spaceMarines.contains(spaceMarine)){
            spaceMarines.add(spaceMarine);
        }
        Collections.sort(spaceMarines);
        boolean flag = false;
        for (SpaceMarine sm : spaceMarines){
            if (sm.getName().length() == spaceMarine.getName().length()){
                flag = true;
            }
            else if (flag){
                results.add(sm);
            }
        }
        return results;
    }
    /**
     * Removing for collection {@link CollectionManager#stack} elements with type {@link SpaceMarine}, lower than assigned element
     * @param spaceMarine assigned element
     * @return operation result
     * @see SpaceMarine#setId(Integer)
     * @see SpaceMarine#setCreationDate(LocalDateTime)
     * @see CollectionManager#getCollection()
     * @see SpaceMarine#getName()
     * @see SpaceMarine#getId()
     * @see CollectionManager#removeElementById(Integer)
     */
    public ArrayList<SpaceMarine> getLower(SpaceMarine spaceMarine){
        ArrayList<SpaceMarine> results = new ArrayList<>();
        ArrayList<SpaceMarine> spaceMarines = new ArrayList<>(this.getCollection());
        if(!spaceMarines.contains(spaceMarine)){
            spaceMarines.add(spaceMarine);
        }
        Collections.sort(spaceMarines);
        boolean flag = true;
        for (SpaceMarine sm : spaceMarines){
            if (sm.getName().length() == spaceMarine.getName().length()){
                flag = false;
            }
            else if (flag){
                results.add(sm);
            }
        }
        return results;
    }

    @Override
    public String toString(){
        if(stack.isEmpty()){
            return "Коллекция пуста";
        }
        String res = "Коллекция:\n";
        for(SpaceMarine sm : this.stack){
            res += sm;
            res += "\n";
        }
        res += "\nДата последнего сохранения:\n";
        res += "\nДата последнего взаимодействия:\n";
        res += lastInit.toString();
        return res;
    }

}
