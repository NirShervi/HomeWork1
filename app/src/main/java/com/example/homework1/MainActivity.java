package com.example.homework1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final int DELAY = 3000;
    // buttons left and right to move the battleship
    private ImageButton button_RIGHT;
    private ImageButton button_LEFT;
    // rocks images that will be manipulate accourding to random choice
    private ImageView[] rocks_Images;
    // heart images that will be decreased by the battleship touching the rocks
    private ImageView[] heart_Images;
    // battleship images changed by positions
    private ImageView[] battleship_Images;
    // on the top of the screen will be text messages that will show the game situation
    private TextView text;
    // clock starting from zero
    private int clock = 0;
    // three life as the number of hearts
    private int life=3;
    // represent  every column of rocks - set activation that would not active other column of rocks
    private int [] rock_Active = new int[3];
    // represent where the battleship is    -1 - left    0 - middle    1-right
    private int battleship_position=0;
    private Timer timer = new Timer();
    // gaps between columns in rock image array
    private int row=3;
    //
    private int endGame=0;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (endGame==0) {
            findViews();
            InitGame();
            moveBattleship();
            startTicker();
        }
    }


    // start generating a random number to decide witch column will activate with falling rocks
    private void startGame() throws InterruptedException {
        // only one tread can use this function
        synchronized (this) {
            int rnd = new Random().nextInt(3);
            Log.d("random number", "the number is " + rnd);
            if ((rnd == 0 || rock_Active[0] == 1) && (rock_Active[1] == 0 && rock_Active[2] == 0) && endGame==0) {
                Log.d("Game over", "End Game" + endGame + Thread.currentThread().getName());
                activateLeftColumn();
            } else if ((rnd == 1 || rock_Active[1] == 1) && (rock_Active[0] == 0 && rock_Active[2] == 0) && endGame==0) {
                activateMiddleColumn();

            } else if ((rnd == 2 || rock_Active[2] == 1) && (rock_Active[0] == 0 && rock_Active[1] == 0) && endGame==0) {
                activateRightColumn();
            }
        }
    }
    // activate left column of rocks with handeling the number of hearts
    private void activateLeftColumn() throws InterruptedException {
        rock_Active[0] = 1;
        while (rock_Active[0] != 0 && endGame==0) {
                Log.d("timeTic 1x1", "Tick" + clock + Thread.currentThread().getName());
                runOnUiThread(() ->
                {
                    rocks_Images[0].setVisibility(View.VISIBLE);
                });
                Thread.sleep(1000);

                Log.d("timeTic 1x2", "Tick" + clock + Thread.currentThread().getName());
                runOnUiThread(() ->
                {
                    rocks_Images[0].setVisibility(View.INVISIBLE);
                    rocks_Images[1].setVisibility(View.VISIBLE);
                });
                Thread.sleep(1000);

                Log.d("timeTic 1x3", "Tick" + clock + Thread.currentThread().getName());
                runOnUiThread(() ->
                {
                    rocks_Images[1].setVisibility(View.INVISIBLE);
                    rocks_Images[2].setVisibility(View.VISIBLE);
                });
                Thread.sleep(1000);
                Log.d("timeTic 1x4", "Tick" + clock + Thread.currentThread().getName());
                runOnUiThread(() ->
                {
                    rocks_Images[2].setVisibility(View.INVISIBLE);
                    rock_Active[0] = 0;
                });
                Thread.sleep(1000);
                if (battleship_position==-1) {
                    deleteHeart();
                    if (endGame==1){
                        break;
                    }
                }
        }
    }

    private void activateMiddleColumn() throws InterruptedException {
        rock_Active[1] = 1;
        while (rock_Active[1] != 0 && endGame==0) {

                Log.d("timeTic 2x1", "Tick" + clock + Thread.currentThread().getName());

                runOnUiThread(() ->
                {
                    rocks_Images[0 + row].setVisibility(View.VISIBLE);
                });
                Thread.sleep(1000);

                Log.d("timeTic 2x2", "Tick" + clock + Thread.currentThread().getName());
                runOnUiThread(() ->
                {
                    rocks_Images[0 + row].setVisibility(View.INVISIBLE);
                    rocks_Images[1 + row].setVisibility(View.VISIBLE);
                });
                Thread.sleep(1000);
                Log.d("timeTic 2x3", "Tick" + clock + Thread.currentThread().getName());
                runOnUiThread(() ->
                {
                    rocks_Images[1 + row].setVisibility(View.INVISIBLE);
                    rocks_Images[2 + row].setVisibility(View.VISIBLE);
                });
                Thread.sleep(1000);
                Log.d("timeTic 2x4", "Tick" + clock + Thread.currentThread().getName());

                runOnUiThread(() ->
                {
                    rocks_Images[2 + row].setVisibility(View.INVISIBLE);
                    rock_Active[1] = 0;
                });
                Thread.sleep(1000);
                if (battleship_position==0) {
                    deleteHeart();
                    if (endGame==1){
                        break;
                    }
                }


        }
    }

    private void activateRightColumn() throws InterruptedException {

            rock_Active[2] = 1;
            while (rock_Active[2] != 0 && endGame==0) {

                    runOnUiThread(() ->
                    {
                        rocks_Images[0 + (row * 2)].setVisibility(View.VISIBLE);
                        Log.d("timeTic 3x1", "Tick" + clock + Thread.currentThread().getName());
                    });
                    Thread.sleep(1000);
                    Log.d("timeTick3x2", "Tick" + clock + Thread.currentThread().getName());
                    runOnUiThread(() ->
                    {
                        rocks_Images[0 + (row * 2)].setVisibility(View.INVISIBLE);
                        rocks_Images[1 + (row * 2)].setVisibility(View.VISIBLE);
                    });
                    Thread.sleep(1000);
                    Log.d("timeTick3x3", "Tick" + clock + Thread.currentThread().getName());
                    runOnUiThread(() ->
                    {
                        rocks_Images[1 + (row * 2)].setVisibility(View.INVISIBLE);
                        rocks_Images[2 + (row * 2)].setVisibility(View.VISIBLE);
                    });
                    Thread.sleep(1000);
                    Log.d("timeTick3x4", "Tick" + clock + Thread.currentThread().getName());
                    runOnUiThread(() ->
                    {
                        rocks_Images[2 + (row * 2)].setVisibility(View.INVISIBLE);
                        rock_Active[2] = 0;
                    });
                    Thread.sleep(1000);
                    if (battleship_position==1){
                        deleteHeart();
                        if (endGame==1){
                            break;
                        }
                    }
            }
    }



    private void deleteHeart() throws InterruptedException {
        Log.d("heart", "delete heart" + Thread.currentThread().getName());
        if (life==3){
            heart_Images[2].setVisibility(View.INVISIBLE);
            vibration();
            life--;
            text.setText("Boom! You got a hit!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text.setText("Game On");
        }
        else if (life==2){
            heart_Images[1].setVisibility(View.INVISIBLE);
            vibration();
            life--;
            text.setText("Boom! You got a hit!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text.setText("Game On");
        }
        else if (life==1){
            heart_Images[0].setVisibility(View.INVISIBLE);
            vibration();
            life--;
            text.setText("Boom! You got a hit!");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            text.setText("Game On");
        }
        else if(life ==0){
            vibration();
            text.setText("Game Over");
            Thread.sleep(500);
            gameOver();
            //timer.cancel();
        }
    }

    private void gameOver(){
        button_RIGHT.setVisibility(View.INVISIBLE);
        button_LEFT.setVisibility(View.INVISIBLE);
            runOnUiThread(() ->
            {
                for (int i=0; i< rocks_Images.length;i++) {
                    rocks_Images[i].setVisibility(View.GONE);
                }
            });

            runOnUiThread(() ->
            {
                for (int i=0; i< battleship_Images.length;i++) {
                    battleship_Images[i].setVisibility(View.INVISIBLE);
                }
            });
            endGame=1;
            stopTicker();
    }
    private void moveBattleship() {
        button_RIGHT.setOnClickListener(v -> {
            if(battleship_position==0){
                battleship_Images[1].setVisibility(View.INVISIBLE);
                battleship_Images[2].setVisibility(View.VISIBLE);
                battleship_position++;
                Log.d("battleship position changed","the position is "+ battleship_position);
            }
            else if(battleship_position==-1){
                battleship_Images[0].setVisibility(View.INVISIBLE);
                battleship_Images[1].setVisibility(View.VISIBLE);
                battleship_position++;
                Log.d("battleship position changed","the position is "+ battleship_position);
            }
        });
        button_LEFT.setOnClickListener(v -> {
            if(battleship_position==1){
                battleship_Images[2].setVisibility(View.INVISIBLE);
                battleship_Images[1].setVisibility(View.VISIBLE);
                battleship_position--;
                Log.d("battleship position changed","the position is "+ battleship_position);
            }
            else if(battleship_position==0){
                battleship_Images[1].setVisibility(View.INVISIBLE);
                battleship_Images[0].setVisibility(View.VISIBLE);
                battleship_position--;
                Log.d("battleship position changed","the position is "+ battleship_position);
            }
        });

    }


    private void InitGame() {
        //disappear all rocks before the game starts
        for (ImageView rock:
             rocks_Images) {
            rock.setVisibility(View.INVISIBLE);
        }
        //start battleship from the middle
        battleship_Images[0].setVisibility(View.INVISIBLE);
        battleship_Images[2].setVisibility(View.INVISIBLE);
        text.setTextSize(32);
        text.setText("Game On");
    }

    private void findViews() {
        button_RIGHT = findViewById(R.id.button_Right);
        button_LEFT = findViewById(R.id.button_Left);
        text = findViewById(R.id.main_textView);
        rocks_Images = new ImageView[]
                {
                        findViewById(R.id.main_Rock_1x1),
                        findViewById(R.id.main_Rock_1x2),
                        findViewById(R.id.main_Rock_1x3),
                        findViewById(R.id.main_Rock_2x1),
                        findViewById(R.id.main_Rock_2x2),
                        findViewById(R.id.main_Rock_2x3),
                        findViewById(R.id.main_Rock_3x1),
                        findViewById(R.id.main_Rock_3x2),
                        findViewById(R.id.main_Rock_3x3)
                };
        heart_Images = new ImageView []
                {
                        findViewById(R.id.main_heart_1),
                        findViewById(R.id.main_heart_2),
                        findViewById(R.id.main_heart_3)
                };
        battleship_Images = new ImageView []
                {
                        findViewById(R.id.main_Battleship_1),
                        findViewById(R.id.main_Battleship_2),
                        findViewById(R.id.main_Battleship_3)
                };
    }



    @Override
    protected void onStart() {
        super.onStart();
        startTicker();
    }

    private void startTicker() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("timeTick","Tick" + clock+ Thread.currentThread().getName());
                try {
                    startGame();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(()-> {
                    Log.d("timeTick","Tick" + clock+ Thread.currentThread().getName());
                });
            }

        },0,DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void stopTicker() {
        timer.cancel();
    }

    private void vibration(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

    }


}