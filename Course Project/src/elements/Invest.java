package elements;

import java.util.Objects;

public class Invest extends Element {
    private int minimalInvestmentPrice;
    private double returnRatio;
    private int riskIntervalMin;
    private int riskIntervalMax;

    public Invest() {
        super("I");
    }

    public Invest(String name) {
        super(name, "I");
    }

    public int getMinimalInvestmentPrice() {
        return minimalInvestmentPrice;
    }

    public double getReturnRatio() {
        return returnRatio;
    }

    public int getRiskIntervalMin() {
        return riskIntervalMin;
    }

    public int getGetRiskIntervalMax() {
        return this.riskIntervalMax;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Invest invest = (Invest) o;
        return minimalInvestmentPrice == invest.minimalInvestmentPrice &&
                Double.compare(invest.returnRatio, returnRatio) == 0 &&
                riskIntervalMin == invest.riskIntervalMin &&
                riskIntervalMax == invest.riskIntervalMax;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minimalInvestmentPrice, returnRatio, riskIntervalMin, riskIntervalMax);
    }
}
