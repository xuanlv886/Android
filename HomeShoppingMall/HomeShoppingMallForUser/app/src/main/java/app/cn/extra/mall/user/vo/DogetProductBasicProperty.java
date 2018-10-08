package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/2/24 0024.
 */

public class DogetProductBasicProperty {

    /**
     * errorString :
     * flag : true
     * data : {"propertysList1":[{"values":[{"value":"东南亚","ppId":"8aeda3aa-3487-11e7-add7-00163e2e094a"}],"name":"风格","chooseState":0,"required":0,"pp_tag":"144"},{"values":[{"value":"否","ppId":"8aedfb7e-3487-11e7-add7-00163e2e094a"}],"name":"是否可定制","chooseState":0,"required":0,"pp_tag":"160"}],"propertysList2":[{"values":[{"value":"箱框结构","ppId":"8aedfce0-3487-11e7-add7-00163e2e094a"},{"value":"曲木结构","ppId":"8aedfd53-3487-11e7-add7-00163e2e094a"},{"value":"不规则体结构","ppId":"8aedffa0-3487-11e7-add7-00163e2e094a"}],"name":"家具结构","chooseState":1,"required":0,"pp_tag":"161"},{"values":[{"value":"艺术风格型","ppId":"8aede28e-3487-11e7-add7-00163e2e094a"},{"value":"经济型","ppId":"8aede219-3487-11e7-add7-00163e2e094a"}],"name":"款式定位","chooseState":0,"required":0,"pp_tag":"155"},{"values":[{"value":"玫红色","ppId":"3f051e79-33ec-11e7-add7-00163e2e094a"},{"value":"浅棕色","ppId":"ceff286b-33d5-11e7-add7-00163e2e094a"},{"value":"透明","ppId":"ceff3092-33d5-11e7-add7-00163e2e094a"},{"value":"藏青色","ppId":"ceff2501-33d5-11e7-add7-00163e2e094a"},{"value":"白色","ppId":"3f051ad7-33ec-11e7-add7-00163e2e094a"},{"value":"深卡其布色","ppId":"ceff2e90-33d5-11e7-add7-00163e2e094a"}],"name":"颜色分类","chooseState":1,"required":0,"pp_tag":"20"},{"values":[{"value":"原木","ppId":"8aede466-3487-11e7-add7-00163e2e094a"},{"value":"罗马柱","ppId":"8aede79e-3487-11e7-add7-00163e2e094a"},{"value":"古董","ppId":"8aede4de-3487-11e7-add7-00163e2e094a"}],"name":"设计元素","chooseState":1,"required":0,"pp_tag":"156"},{"values":[{"value":"提供简单安装工具","ppId":"8aede0b0-3487-11e7-add7-00163e2e094a"},{"value":"提供安装说明视频","ppId":"8aede038-3487-11e7-add7-00163e2e094a"}],"name":"安装说明详情","chooseState":1,"required":0,"pp_tag":"154"}]}
     */

    private String errorString;
    private String flag;
    private DataBean data;

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<PropertysList1Bean> propertysList1;
        private List<PropertysList2Bean> propertysList2;

        public List<PropertysList1Bean> getPropertysList1() {
            return propertysList1;
        }

        public void setPropertysList1(List<PropertysList1Bean> propertysList1) {
            this.propertysList1 = propertysList1;
        }

        public List<PropertysList2Bean> getPropertysList2() {
            return propertysList2;
        }

        public void setPropertysList2(List<PropertysList2Bean> propertysList2) {
            this.propertysList2 = propertysList2;
        }

        public static class PropertysList1Bean {
            /**
             * values : [{"value":"东南亚","ppId":"8aeda3aa-3487-11e7-add7-00163e2e094a"}]
             * name : 风格
             * chooseState : 0
             * required : 0
             * pp_tag : 144
             */

            private String name;
            private int chooseState;
            private int required;
            private String pp_tag;
            private List<ValuesBean> values;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getChooseState() {
                return chooseState;
            }

            public void setChooseState(int chooseState) {
                this.chooseState = chooseState;
            }

            public int getRequired() {
                return required;
            }

            public void setRequired(int required) {
                this.required = required;
            }

            public String getPp_tag() {
                return pp_tag;
            }

            public void setPp_tag(String pp_tag) {
                this.pp_tag = pp_tag;
            }

            public List<ValuesBean> getValues() {
                return values;
            }

            public void setValues(List<ValuesBean> values) {
                this.values = values;
            }

            public static class ValuesBean {
                /**
                 * value : 东南亚
                 * ppId : 8aeda3aa-3487-11e7-add7-00163e2e094a
                 */

                private String value;
                private String ppId;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getPpId() {
                    return ppId;
                }

                public void setPpId(String ppId) {
                    this.ppId = ppId;
                }
            }
        }

        public static class PropertysList2Bean {
            /**
             * values : [{"value":"箱框结构","ppId":"8aedfce0-3487-11e7-add7-00163e2e094a"},{"value":"曲木结构","ppId":"8aedfd53-3487-11e7-add7-00163e2e094a"},{"value":"不规则体结构","ppId":"8aedffa0-3487-11e7-add7-00163e2e094a"}]
             * name : 家具结构
             * chooseState : 1
             * required : 0
             * pp_tag : 161
             */

            private String name;
            private int chooseState;
            private int required;
            private String pp_tag;
            private List<ValuesBeanX> values;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getChooseState() {
                return chooseState;
            }

            public void setChooseState(int chooseState) {
                this.chooseState = chooseState;
            }

            public int getRequired() {
                return required;
            }

            public void setRequired(int required) {
                this.required = required;
            }

            public String getPp_tag() {
                return pp_tag;
            }

            public void setPp_tag(String pp_tag) {
                this.pp_tag = pp_tag;
            }

            public List<ValuesBeanX> getValues() {
                return values;
            }

            public void setValues(List<ValuesBeanX> values) {
                this.values = values;
            }

            public static class ValuesBeanX {
                /**
                 * value : 箱框结构
                 * ppId : 8aedfce0-3487-11e7-add7-00163e2e094a
                 */

                private String value;
                private String ppId;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getPpId() {
                    return ppId;
                }

                public void setPpId(String ppId) {
                    this.ppId = ppId;
                }
            }
        }
    }
}
