package space.zv.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Ball {
    private Bitmap bm_ball;
    private int x,y;
    public Ball(Bitmap bm, int x, int y) {
        this.bm_ball = bm;
        this.x = x;
        this.y = y;
        bm_ball = Bitmap.createBitmap(bm);
    }

    public Rect getrBody() {
        return new Rect(this.x+GameView.sizeOfMap/4, this.y+GameView.sizeOfMap/4, this.x+GameView.sizeOfMap/4, this.y+GameView.sizeOfMap/4);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bm_ball, this.x, this.y, null);
    }

    public Bitmap getBm() {
        return bm_ball;
    }

    public void setBm(Bitmap bm) {
        this.bm_ball = bm;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
