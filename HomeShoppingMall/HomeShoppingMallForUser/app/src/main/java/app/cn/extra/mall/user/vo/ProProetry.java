package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class ProProetry {
    private double value;
    private String name;

    public ProProetry(String name, String value) {
        this.name = name;
        this.value = Double.parseDouble(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
