package other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import elements.*;
import utils.RandomNumberGenerator;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Repository {
    private Map<Integer, Trap> traps;
    private List<Chance> chances;
    private List<Steal> steals;
    private Map<Integer, PartyHard> parties;
    private Map<Integer, Invest> invests;
    private Map<Integer,Invest>randomInvests;
    private Gson gson;

    public Repository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        init();
    }

    public Map<Integer, Invest> getRandomInvests() {
        return randomInvests;
    }

    public void setRandomInvests(Map<Integer, Invest> randomInvests) {
        this.randomInvests = randomInvests;
    }

    public Map<Integer, Trap> getTraps() {
        return traps;
    }

    public List<Chance> getChances() {
        return chances;
    }

    public List<Steal> getSteals() {
        return steals;
    }

    public Map<Integer, PartyHard> getParties() {
        return parties;
    }

    /**
     * generates 3  random invests
     * @return 3  random invests
     */
    public Map<Integer, Invest> generateThreeRandomInvests() {
        Map<Integer, Invest> result = new LinkedHashMap<>();
        int counter = 0;

        while (result.size() != 3) {
            int randomInt = RandomNumberGenerator.generateRandomIntegerNumber(this.invests.size());

            Invest randomInvest=this.invests.get(randomInt);

            if(result.containsValue(randomInvest)){
                continue;
            }

            result.put(counter++,randomInvest);

        }

        result.put(counter,new Invest("I don't want to invest more"));

        return result;
    }

    /**
     * repo init
     */
    private void init() {
        try {
            this.traps = this.generateMap("src\\data\\traps", Trap.class);
            this.steals = this.generateList("src\\\\data\\\\steals", Steal.class);
            this.parties = this.generateMap("src\\\\data\\\\parties", PartyHard.class);
            this.invests = this.generateMap("src\\\\data\\\\investments", Invest.class);
            this.chances = this.generateList("src\\\\data\\\\chances", Chance.class);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * generates map
     * @param path file path
     * @param clazz class of the generated elements
     * @param <T> type
     * @return Map
     * @throws FileNotFoundException
     */
    private <T> Map<Integer, T> generateMap(String path, Class<? extends Element> clazz) throws FileNotFoundException {
        Map<Integer, T> result = new LinkedHashMap<>();

        List<? extends Element> data = this.gson.fromJson(new FileReader(path), TypeToken.getParameterized(List.class, clazz).getType());
        for (int i = 0; i < data.size(); i++) {
            result.put(i, (T) data.get(i));
        }

        return result;

    }

    /**
     * generates list
     * @param path file path
     * @param clazz class of the generated elements
     * @param <T> type
     * @return list with elements
     * @throws FileNotFoundException
     */
    private <T> List<T> generateList(String path, Class<? extends Element> clazz) throws FileNotFoundException {
        return this.gson.fromJson(new FileReader(path), TypeToken.getParameterized(List.class, clazz).getType());
    }

    /**
     * get repository by type
     * @param element
     * @return repo by type
     */
    public  Map<Integer, ? extends Element> getRepositoryByType(Element element) {
        if (element instanceof Trap) {
            return this.getTraps();
        } else if (element instanceof Invest) {
            this.setRandomInvests(this.generateThreeRandomInvests());
            return this.getRandomInvests();
        } else if (element instanceof PartyHard) {
            return this.getParties();
        } else {

        }

        return null;
    }

    /**
     * reset steals
     */
    public void resetSteals(){
        try {
            this.steals= this.generateList("src\\\\data\\\\steals", Steal.class);
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }
    }

    /**
     * get fresh copy of trap from repository
     * @param choice the number of chosen trap
     * @return Trap
     */
    public Trap getCopyOfTrapFromRepositoryWithNewReference(int choice){
        return this.gson.fromJson(this.gson.toJson(this.traps.get(choice)),Trap.class);
    }


}
