package elements;


import java.util.Objects;

public  abstract class Element {
    private String name;
    private String symbol;
    private String description;


    public Element( String symbol) {
        this.symbol = symbol;
    }

    public Element(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s",symbol);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element element = (Element) o;
        return name.equals(element.name) &&
                symbol.equals(element.symbol) &&
                description.equals(element.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, symbol, description);
    }
}

