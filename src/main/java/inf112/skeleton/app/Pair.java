package inf112.skeleton.app;

import java.util.Objects;

public class Pair {
    private int x;
    private int y;

    public Pair(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
     * @param x Update x value of Pair.
     */
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public String toString() {
        return "Pair(" +
                x +
                "," + y +
                ')';
    }


    public Pair getCopy() {
        return new Pair(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return getX() == pair.getX() && getY() == pair.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public Pair getReverseDirection() {
        return new Pair(this.x * -1, this.y * -1);
    }
}

