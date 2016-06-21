package logistic.compare.comparelogistic;

/**
 * Created by Sony on 9/11/2015.
 */
public class Model {
    public static Model m = new Model();

    public static Model getInstance() {
        return m;
    }

    MyPageAdapter adapter;

    public MyPageAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(MyPageAdapter adapter) {
        this.adapter = adapter;
    }
}

