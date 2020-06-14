package other;

import elements.Element;
import elements.Invest;
import elements.PlayerType;
import elements.Steal;

import java.util.*;

public class Player extends Element {
    private double money;
    private int currentPositionIndex;
    private List<String> punishments;
    private Map<Invest,Double> investments;
    private Steal evilPlan;
    private PlayerType playerType;



    public Player(String name, String symbol, int money,PlayerType playerType) {
        super(name,symbol);
        this.money = money;
        this.punishments = new ArrayList<>();
        this.evilPlan=null;
        this.currentPositionIndex=0;
        this.investments=new HashMap<>();
        this.playerType=playerType;
    }



    public double getMoney() {
        return money;
    }

    public boolean isInCycle() {
        return this.currentPositionIndex<20;
    }

    public void resetPunishments(){
        this.punishments=new ArrayList<>();
    }

    public int getCurrentPositionIndex() {
        return currentPositionIndex;
    }

    public void removeMoney(double amount) {
        this.money = this.getMoney() - amount;
    }

    public  void addMoney(double amount){
        this.money=this.getMoney()+amount;
    }

    public String getStats() {
        return String.format("Име: %s, Пари: %.2f", super.getName(), this.money);
    }

    public void setCurrentPositionIndex(int currentPositionIndex) {
        this.currentPositionIndex = currentPositionIndex;
    }

    public void setEvilPlan(Steal evilPlan) {
        this.evilPlan = evilPlan;
    }



    public Steal getEvilPlan() {
        return evilPlan;
    }

    public List<String> getPunishments() {
        return punishments;
    }

    public Map<Invest, Double> getInvestments() {
        return investments;
    }

    public void addPunishment(String punishmentName) {
        this.punishments.add(punishmentName);
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    /**
     * activates the evil plan
     * @param steal the plan
     */
    public void activateEvilPlan(Steal steal){
        if(this.getEvilPlan().getName().equals("Големия банков обир") && this.getEvilPlan().isActivated()){
            this.addMoney(100);
        }

        if(this.getPunishments().contains("Проглеждане")){
            System.out.println("Губите право да реализирате Steal план");
            this.getPunishments().remove("Проглеждане");
            return;
        }


        if(steal.isActivated() || this.getEvilPlan().isActivated() ){
            System.out.println("Вече е реализиран зъл план в това поле/вече сте реализирал Steal план");
            return;
        }

        this.evilPlan.setActivated(true);
        steal.setActivated(true);
        System.out.printf("%s is activated!.%s\n",this.getEvilPlan().getName(),this.getEvilPlan().getDescription());

    }

    public void invest(double money, Invest investment){

        if(this.getEvilPlan().getName().equals("Заложници")){
            this.addMoney(100);
        }

        if(!this.investments.containsKey(investment)){
            this.investments.put(investment,0.0);
        }

        this.investments.put(investment,this.investments.get(investment)+money);
        this.removeMoney(money);
    }


    @Override
    public String toString() {
        return String.format("%s", super.getSymbol());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        if (!super.equals(o)) return false;
        Player player = (Player) o;
        return Double.compare(player.money, money) == 0 &&
                currentPositionIndex == player.currentPositionIndex &&
                Objects.equals(punishments, player.punishments) &&
                Objects.equals(investments, player.investments) &&
                Objects.equals(evilPlan, player.evilPlan) &&
                playerType == player.playerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), money, currentPositionIndex, punishments, investments, evilPlan, playerType);
    }
}
