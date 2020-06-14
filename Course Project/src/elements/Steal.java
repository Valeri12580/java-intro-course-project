package elements;


public class Steal extends Element {
    private int reward;
    private boolean isActivated;

    public Steal() {
        super("St");
        this.isActivated=false;
    }



    public void setActivated(boolean activated) {
        isActivated = activated;
    }


    public boolean isActivated() {
        return isActivated;
    }


}
