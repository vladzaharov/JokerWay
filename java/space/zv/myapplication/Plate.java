package space.zv.myapplication;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Plate {
    private Bitmap bm;
    int x,y;
    private int width, height;
    private Rect r;

    public Plate(Bitmap bm, int x, int y, int width, int height) {
        this.bm = bm;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rect getR() {
        return new Rect(this.x, this.y,this.x+GameView.sizeOfMap,this.y+ GameView.sizeOfMap);
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
