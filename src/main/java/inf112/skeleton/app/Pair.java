package inf112.skeleton.app;

public class Pair {
    private int key;
    private int value;

    public Pair(int k, int v){
        key = k;
        value = v;
    }

    public void setKey(int k){
        key = k;
    }

    public void setValue(int v){
        value = v;
    }

    public int getKey(){
        return key;
    }

    public int getValue(){
        return value;
    }
}
