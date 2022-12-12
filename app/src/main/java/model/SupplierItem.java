package model;

public class SupplierItem {
    private String sno;
    private String names;
    private Double cummulative;

    public SupplierItem(String sno, String names, Double cummulative) {
        this.sno = sno;
        this.names = names;
        this.cummulative = cummulative;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public Double getCummulative() {
        return cummulative;
    }

    public void setCummulative(Double cummulative) {
        this.cummulative = cummulative;
    }
}