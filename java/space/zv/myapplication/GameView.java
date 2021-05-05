package space.zv.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    private Bitmap bmPlate, bmPlate_back, bmBall;
    public static int sizeOfMap = 180 * Constants.SCREEN_WIDTH / 1080;
    private int index, indexRight, indexLeft, indexUp, indexDown;
    public int[][] a = new int[Constants.h][Constants.w];
    private ArrayList<Plate> arrPlate = new ArrayList<>();
    private Ball ball;
    private boolean move = false;
    private float mx, my;
    private boolean move_left, move_right, move_up, move_down;
    private Handler handler = new Handler();
    private Runnable r;
    private int speed = 25;
    private int nStart = 5, mStart = 2;
    Dialog window;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bmPlate = BitmapFactory.decodeResource(this.getResources(), R.drawable.plate);
        bmPlate = Bitmap.createScaledBitmap(bmPlate, sizeOfMap, sizeOfMap, true);
        bmPlate_back = BitmapFactory.decodeResource(this.getResources(), R.drawable.plate_back);
        bmPlate_back = Bitmap.createScaledBitmap(bmPlate_back, sizeOfMap, sizeOfMap, true);
        bmBall = BitmapFactory.decodeResource(this.getResources(), R.drawable.ball);
        bmBall = Bitmap.createScaledBitmap(bmBall, sizeOfMap/2, sizeOfMap/2, true);

        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        map();

        for (int i = 0; i < Constants.h; i++)
            for (int j = 0; j < Constants.w; j++) {
                if (a[i][j] == 1)
                    arrPlate.add(new Plate(bmPlate, (Constants.SCREEN_WIDTH / 12 + j * sizeOfMap),
                            i * sizeOfMap + 200 * Constants.SCREEN_HEIGHT / 1920 / 2, sizeOfMap, sizeOfMap));
                else
                    arrPlate.add(new Plate(bmPlate_back, (Constants.SCREEN_WIDTH / 12 + j * sizeOfMap),
                            i * sizeOfMap + 200 * Constants.SCREEN_HEIGHT / 1920 / 2, sizeOfMap, sizeOfMap));
            }

        ball = new Ball(bmBall, arrPlate.get(mStart).getX() + sizeOfMap / 4, arrPlate.get(nStart * nStart + mStart).getY() + sizeOfMap / 4);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int a = event.getActionMasked();
        switch (a) {
            case MotionEvent.ACTION_MOVE: {
                if (move == false) {
                    mx = event.getX();
                    my = event.getY();
                    move = true;
                } else {

                    if (mx - event.getX() > 100) {
                        mx = event.getX();
                        my = event.getY();
                        noMove();
                        move_left = true;
                    }
                    if (event.getX() - mx > 100) {
                        mx = event.getX();
                        my = event.getY();
                        noMove();
                        move_right = true;
                    }
                    if (event.getY() - my > 100) {
                        mx = event.getX();
                        my = event.getY();
                        noMove();
                        move_down = true;
                    }
                    if (my - event.getY() > 100) {
                        mx = event.getX();
                        my = event.getY();
                        noMove();
                        move_up = true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mx = 0;
                my = 0;
                move = false;
                break;
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        boolean outside = false;

        if(arrPlate.get(0).getX()-sizeOfMap/8 > ball.getX() | (arrPlate.get(29).getX()+sizeOfMap*5/8) < ball.getX())
            outside = true;

        if(arrPlate.get(0).getY()-sizeOfMap/8 > ball.getY() | (arrPlate.get(29).getY()+sizeOfMap*5/8) < ball.getY())
            outside = true;

        for (int i = 0; i < arrPlate.size(); i++) {
            if(a[i/Constants.w][i%Constants.w] == 1)
                canvas.drawBitmap(arrPlate.get(i).getBm(), arrPlate.get(i).getX(), arrPlate.get(i).getY(), null);
        }

        for (int i = 0; i < arrPlate.size(); i++)
            if (ball.getrBody().intersect(arrPlate.get(i).getR())) {
                index = i;
                indexRight = (i<Constants.h*Constants.w-1)? i + 1: i;
                indexLeft = (i>0)? i - 1: i;
                indexUp = (i>Constants.w-1)? i - 5: i;
                indexDown = (i < (Constants.h-1)*Constants.w)? i + 5: i;
                break;
            }

        if (move_right) {

            if (outside) dialog();

            ball.setBm(bmBall);
            ball.setX(ball.getX() + GameView.sizeOfMap / speed);

                if (ball.getrBody().intersect(arrPlate.get(indexRight).getR())) {
                    if (arrPlate.get(indexRight).getBm() == bmPlate_back) {
                        dialog();
                    }

                    if (index != indexRight) {
                        arrPlate.get(index).setBm(bmPlate_back);
                    }
                }
        }
        if (move_left) {

            if (outside) dialog();

            ball.setBm(bmBall);
                ball.setX(ball.getX() - GameView.sizeOfMap / speed);

                if (ball.getrBody().intersect(arrPlate.get(indexLeft).getR())) {
                    if (arrPlate.get(indexLeft).getBm() == bmPlate_back) {
                        dialog();
                    }

                    if (index != indexLeft) {
                        arrPlate.get(index).setBm(bmPlate_back);
                    }

                }

            }
        if (move_up) {

            if (outside) dialog();

            ball.setBm(bmBall);
                ball.setY(ball.getY() - GameView.sizeOfMap / speed);

                if (ball.getrBody().intersect(arrPlate.get(indexUp).getR())) {
                    if (arrPlate.get(indexUp).getBm() == bmPlate_back) {
                        dialog();
                    }

                    if (index != indexUp) {
                        arrPlate.get(index).setBm(bmPlate_back);
                    }

                }

            }
        if (move_down) {

            if (outside) dialog();

            ball.setBm(bmBall);
                ball.setY(ball.getY() + GameView.sizeOfMap / speed);

                if (ball.getrBody().intersect(arrPlate.get(indexDown).getR())) {
                    if (arrPlate.get(indexDown).getBm() == bmPlate_back) {
                        dialog();
                    }

                    if (index != indexDown) {
                        arrPlate.get(index).setBm(bmPlate_back);
                    }

                }
            }

        ball.draw(canvas);
        handler.postDelayed(r, 1 );

        }

    private void noMove() {
        move_right = false;
        move_left = false;
        move_down = false;
        move_up = false;
    }

    private void map () {

        List <String>  mv = new ArrayList<>(); //"left", "right", "top", "bottom";

        for (int i = 0; i < Constants.h; i++)
            for (int j = 0; j < Constants.w; j++)
                a[i][j] = 0;

        int nStart = 5, mStart = 2;
        a[nStart][mStart] = 1;
        int n = 1;
        Random ran = new Random();
        int nPlate = 10 + ran.nextInt(11);

        do {

            if (a[nStart][(mStart - 1 > -1) ? mStart - 1 : mStart] == 0)
                mv.add("left");
            if (a[nStart][(mStart + 1 < 5) ? mStart + 1 : mStart] == 0)
                mv.add("right");
            if (a[(nStart - 1 > -1) ? nStart - 1 : nStart][mStart] == 0)
                mv.add("top");
            if (a[(nStart + 1 < 6) ? nStart + 1 : nStart][mStart] == 0)
                mv.add("bottom");

            if (mv.size() > 0) {
                switch (mv.get(ran.nextInt(mv.size()))) {
                    case "left":
                        if (a[nStart][mStart - 1] != 1) {
                            mStart = mStart - 1;
                            a[nStart][mStart] = 1;
                            n++;
                            mv.clear();
                            break;
                        }
                    case "right":
                        if (a[nStart][mStart + 1] != 1) {
                            mStart = mStart + 1;
                            a[nStart][mStart] = 1;
                            n++;
                            mv.clear();
                            break;
                        }
                    case "top":
                        if (a[nStart - 1][mStart] != 1) {
                            nStart = nStart - 1;
                            a[nStart][mStart] = 1;
                            n++;
                            mv.clear();
                            break;
                        }
                    case "bottom":
                        if (a[nStart + 1][mStart] != 1) {
                            nStart = nStart + 1;
                            a[nStart][mStart] = 1;
                            n++;
                            mv.clear();
                            break;
                        }
                }
            }else
                if (mv.size() == 0) {
                mv.clear();
                for (int i = 0; i < Constants.h; i++)
                    for (int j = 0; j < Constants.w; j++)
                        a[i][j] = 0;

                nStart = 5;
                mStart = 2;
                a[nStart][mStart] = 1;
                n = 1;
                nPlate = 10 + ran.nextInt(11);
            }

            }
            while (n < nPlate) ;
        }

    private void dialog(){

        int nPlateBack = 0;
        noMove();

        window = new Dialog(getContext());
        window.requestWindowFeature(Window.FEATURE_NO_TITLE);

        for (int i = 0; i < arrPlate.size(); i++)
            if(arrPlate.get(i).getBm() == bmPlate)
                nPlateBack++;

        if(nPlateBack == 1)
            window.setContentView(R.layout.activity_window_good);
        else
            window.setContentView(R.layout.activity_window_bed);

        window.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setCancelable(false);

        window.findViewById(R.id.repeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getContext().startActivity(new Intent(getContext(), GameActivity.class));
                } catch (Exception e) {

                }
            }
        });
        window.show();

    }

    }

