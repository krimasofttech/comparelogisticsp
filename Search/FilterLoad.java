package logistic.compare.comparelogistic.Search;

/**
 * Created by Sony on 5/28/2016.
 */
public class FilterLoad {
    boolean ftl = false;
    boolean ltl = false;
    String date = "false";

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FilterLoad(boolean ftl, boolean ltl, String date) {
        this.ftl = ftl;
        this.ltl = ltl;
        this.date = date;
    }

    public FilterLoad() {
    }

    public static FilterLoad filterLoad = new FilterLoad();

    public static FilterLoad getFilterLoad() {
        return filterLoad;
    }

    public static void setFilterLoad(FilterLoad filterLoad) {
        FilterLoad.filterLoad = filterLoad;
    }
}
