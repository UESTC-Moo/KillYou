package org.seckill.seckill.enums;

public enum SeckillStatEnum {
    SUCCESS(1,"Sucess kill"),
    END(0,"End kill"),
    REPEAT_KILL(-1,"Repeat kill"),
    INNER_ERROR(-2,"Inner error"),
    DATA_REWRITE(-3,"Data rewrite");

    private int state;

    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum state : values()){
            if(state.getState() == index){
                return state;
            }
        }
        return null;
    }

}
