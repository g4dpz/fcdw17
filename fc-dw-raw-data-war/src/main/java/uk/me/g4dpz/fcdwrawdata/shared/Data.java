package uk.me.g4dpz.fcdwrawdata.shared;

public class Data {
    long id;
    String siteId;
    long total;
    long satellite1;
    long satellite2;
    long satellite3;
    long satellite4;
    long satellite5;
    long satellite6;

    public Data() {
    }

    public Data(long id, String siteId, long total, long satellite1, long satellite2, long satellite3, long satellite4, long satellite5, long satellite6) {
        this.id = id;
        this.siteId = siteId;
        this.total = total;
        this.satellite1 = satellite1;
        this.satellite2 = satellite2;
        this.satellite3 = satellite3;
        this.satellite4 = satellite4;
        this.satellite5 = satellite5;
        this.satellite6 = satellite6;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSatellite1() {
        return satellite1;
    }

    public void setSatellite1(long satellite1) {
        this.satellite1 = satellite1;
    }

    public long getSatellite2() {
        return satellite2;
    }

    public void setSatellite2(long satelite2) {
        this.satellite2 = satelite2;
    }

    public long getSatellite3() {
        return satellite3;
    }

    public void setSatellite3(long satellite3) {
        this.satellite3 = satellite3;
    }

    public long getSatellite4() {
        return satellite4;
    }

    public void setSatellite4(long satellite4) {
        this.satellite4 = satellite4;
    }

    public long getSatellite5() {
        return satellite5;
    }

    public void setSatellite5(long satellite5) {
        this.satellite5 = satellite5;
    }

    public long getSatellite6() {
        return satellite6;
    }

    public void setSatellite6(long satellite6) {
        this.satellite6 = satellite6;
    }
}
