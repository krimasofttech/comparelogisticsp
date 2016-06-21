package logistic.compare.comparelogistic.Search;

/**
 * Created by Sony on 5/25/2016.
 */
public class Filter {
    boolean ftl = false;
    boolean ltl = false;
    boolean oneway = false;
    boolean roundtrip = false;
    String date = "false";

    public static Filter filter = new Filter();

    public static Filter getFilter() {
        return filter;
    }

    public static void setFilter(Filter filter) {
        Filter.filter = filter;
    }

    public Filter() {
    }

    public Filter(boolean ftl, boolean ltl, boolean oneway, boolean roundtrip, String date) {
        this.ftl = ftl;
        this.ltl = ltl;
        this.oneway = oneway;
        this.roundtrip = roundtrip;
        this.date = date;
    }

    public boolean isFtl() {
        return ftl;
    }

    public void setFtl(boolean ftl) {
        this.ftl = ftl;
    }

    public boolean isLtl() {
        return ltl;
    }

    public void setLtl(boolean ltl) {
        this.ltl = ltl;
    }

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean isRoundtrip() {
        return roundtrip;
    }

    public void setRoundtrip(boolean roundtrip) {
        this.roundtrip = roundtrip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
