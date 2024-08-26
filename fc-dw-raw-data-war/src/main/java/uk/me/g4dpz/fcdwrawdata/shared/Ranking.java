package uk.me.g4dpz.fcdwrawdata.shared;

import java.util.ArrayList;
import java.util.List;

public class Ranking {

    int draw = 0;
    int recordsTotal = 0;
    int recordsFiltered = 0;
    private int columnToSortAndDirection;

    List<Data> data = new ArrayList<>();

    public Ranking() {
    }

    public Ranking(int draw, List<Data> data, int columnToSortAndDirection, int recordsTotal, int recordsFiltered) {
        this.draw = draw;
        this.data = data;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.columnToSortAndDirection = columnToSortAndDirection;
    }

    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }
}
