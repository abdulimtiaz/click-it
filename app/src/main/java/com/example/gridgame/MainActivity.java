package com.example.gridgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] grid = new Button[3][3];
    private int score = 0;
    private boolean gameActive = false;
    private boolean gameLost = false;
    private boolean timeUp = false;
    private int green = Color.TRANSPARENT;
    private CountDownTimer timer = null;
    TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        green = ResourcesCompat.getColor(getResources(), R.color.colorGreen, null);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                String buttonId = "button_" + i + j;
                int resourceId = getResources().getIdentifier(buttonId, "id", getPackageName());
                grid[i][j] = findViewById(resourceId);
                grid[i][j].setTag(0);
                grid[i][j].setOnClickListener(this);
                grid[i][j].getBackground().setColorFilter(new BlendModeColorFilter(Color.BLACK, BlendMode.SRC_ATOP));
            }
        }

        timeTextView = findViewById(R.id.time_textview);
        timeTextView.setText(getString(R.string.time_display, 1000));

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gameActive = true;
                v.setVisibility(View.INVISIBLE);

                TextView titleTextView = findViewById(R.id.title_textview);
                titleTextView.setVisibility(View.INVISIBLE);

                score = 0;
                TextView scoreTextView = findViewById(R.id.score_textview);
                scoreTextView.setText(getString(R.string.score_heading, score));
                scoreTextView.setVisibility(View.VISIBLE);

                if (gameLost) {
                    for (int i = 0; i < 3; ++i) {
                        for (int j = 0; j < 3; ++j) {
                            grid[i][j].setEnabled(true);
                            grid[i][j].getBackground().setColorFilter(new BlendModeColorFilter(Color.BLACK, BlendMode.SRC_ATOP));
                            grid[i][j].setTag(0);
                        }
                    }
                }

                Random random = new Random();
                int initialRow = random.nextInt(3);
                int initialColumn = random.nextInt(3);

                grid[initialRow][initialColumn].getBackground().setColorFilter(new BlendModeColorFilter(green, BlendMode.SRC_ATOP));
                grid[initialRow][initialColumn].setTag(1);


                timer = new CountDownTimer(1000, 1) {

                    public void onTick(long millisUntilFinished) {
                        timeTextView.setText(getString(R.string.time_display, millisUntilFinished));
                    }

                    public void onFinish() {
                        timeUp = true;
                        timeTextView.setText(getString(R.string.time_display, 0));
                        gameLost();
                    }
                }.start();
            }

        });

    }


    @Override
    public void onClick(View v) {
        if (gameActive) {

            if (Integer.parseInt(v.getTag().toString()) == 1) {
                v.getBackground().setColorFilter(new BlendModeColorFilter(Color.BLACK, BlendMode.SRC_ATOP));
                v.setTag(0);
                ++score;
                timer.start();
                TextView scoreTextView = findViewById(R.id.score_textview);
                scoreTextView.setText(getString(R.string.score_heading, score));

                Random random = new Random();
                int newRow = random.nextInt(3);
                int newColumn = random.nextInt(3);

                String currentId = v.getResources().getResourceEntryName(v.getId());
                currentId = currentId.replace("button_", "");
                int currentRow = Character.getNumericValue(currentId.charAt(0));
                int currentColumn = Character.getNumericValue(currentId.charAt(1));

                while (currentRow == newRow && currentColumn == newColumn) {
                    newRow = random.nextInt(3);
                    newColumn = random.nextInt(3);
                }

                grid[newRow][newColumn].getBackground().setColorFilter(new BlendModeColorFilter(green, BlendMode.SRC_ATOP));
                grid[newRow][newColumn].setTag(1);
            }

            else if (Integer.parseInt(v.getTag().toString()) == 0) {
                gameLostWrongButton(v);
            }
        }
    }

    public void gameLostWrongButton(View v) {
        v.getBackground().setColorFilter(new BlendModeColorFilter(Color.RED, BlendMode.SRC_ATOP));
        timeUp = false;
        gameLost();
    }

    public void gameLost() {
        timer.cancel();
        gameActive = false;
        gameLost = true;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                grid[i][j].setEnabled(false);

                if (timeUp)
                    grid[i][j].getBackground().setColorFilter(new BlendModeColorFilter(Color.RED, BlendMode.SRC_ATOP));
            }
        }

        TextView titleTextView = findViewById(R.id.title_textview);
        titleTextView.setText("Game Over");
        titleTextView.setVisibility(View.VISIBLE);

        TextView scoreTextView = findViewById(R.id.score_textview);
        scoreTextView.setText(getString(R.string.final_score, score));
        titleTextView.setVisibility(View.VISIBLE);

        Button startButton = findViewById(R.id.start_button);
        startButton.setText("RESTART");
        startButton.setVisibility(View.VISIBLE);
    }

}
