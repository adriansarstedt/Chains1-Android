package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game extends AppCompatActivity {

    ArcView arcView;
    ArcAngleAnimation animationShrink;

    EditText InputTextEdit;
    TextView InputText1, ScoreDisplay, HighScoreDisplay;
    ImageView AnimalImageView;
    ImageButton HelpButton;
    View MainDisplay, FlipContainer;

    Character lastLetter, inputLetter;
    int score = 0, viewWidth = 2000, HighScore, turnTime;
    boolean run = true, Sound;

    Bitmap dr;
    Animation FlipStart, FlipEnd;
    MediaPlayer CorrentSound;
    Handler focusHandler = new Handler(), messageHandler = new Handler();

    public ArrayList<String> InputAnimalList, NewlyDiscoveredAnimals, PreviouslyDiscoveredAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(Color.parseColor("#CC0033"));
        }

        InputAnimalList = new ArrayList<>();

        arcView = (ArcView) findViewById(R.id.ArcTimerView);
        AnimalImageView = (ImageView) findViewById(R.id.AnimalImageView);
        HelpButton = (ImageButton) findViewById(R.id.help_button);
        ScoreDisplay = (TextView) findViewById(R.id.ScoreDisplayView);
        HighScoreDisplay = (TextView) findViewById(R.id.highscore_display);
        MainDisplay = (View) findViewById(R.id.MainDisplay);
        FlipContainer = (View) findViewById(R.id.FlipContainer);

        InputTextEdit = (EditText) findViewById(R.id.InputTextEdit);
        InputText1 = (TextView) findViewById(R.id.InputText1);

        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Thin.ttf");

        ScoreDisplay.setTypeface(custom_font_hairline);
        InputTextEdit.setTypeface(custom_font_hairline);
        InputText1.setTypeface(custom_font_hairline);
        HighScoreDisplay.setTypeface(custom_font_hairline);

        SharedPreferences HighScoreManager = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        HighScore = HighScoreManager.getInt(Globals.User+Globals.Difficulty, 0);
        String Difficulty = PreferenceManager.getDefaultSharedPreferences(this).getString("Difficulty", "Slow");
        String SoundText = PreferenceManager.getDefaultSharedPreferences(this).getString("Sound", "On");
        String DiscoveredAnimalsStorage = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "defaultStringIfNothingFound");
        PreviouslyDiscoveredAnimals = new ArrayList<String>(Arrays.asList(DiscoveredAnimalsStorage.split("-")));
        NewlyDiscoveredAnimals = new ArrayList<String>();

        if (Difficulty.equals("Fast")) {
            turnTime = 10000;
        } else {
            turnTime = 20000;
        } if (SoundText.equals("On")) {
            Sound = true;
        } else {
            Sound = false;
        }

        HighScoreDisplay.setText(String.valueOf(HighScore));
        ScoreDisplay.setText(String.valueOf(score));

        FlipStart = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip_start);
        FlipEnd = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip_end);

        FlipStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimalImageView.setImageBitmap(dr);
                ScoreDisplay.setText(String.valueOf(score));
                ScoreDisplay.setTextSize(100);
                FlipContainer.startAnimation(FlipEnd);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        final Handler gameStartHandler = new Handler();
        gameStartHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Random randomGenerator = new Random();
                int startIndex = randomGenerator.nextInt(Globals.Letters.size());
                lastLetter = Globals.Letters.get(startIndex).charAt(0);

                InputTextEdit.setText(Character.toString(lastLetter));
                InputTextEdit.requestFocus();

                StartIntroAnimation();
                gameStartHandler.removeCallbacks(this);
            }
        }, 1000);

    }

    public void setupAnimations() {
        animationShrink = new ArcAngleAnimation(arcView, 360);
        animationShrink.setDuration(turnTime);
        arcView.startAnimation(animationShrink);

        animationShrink.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                GameOver();
            }
        });

        InputTextEdit.setSelection(1);
        InputTextEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                enter();
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                    return true;
                }
                return false;
            }
        });
    }

    public int animal(String Input) {
        ArrayList<Float> posibilities = new ArrayList<Float>();
        int i=-1; float lead = 0;
        for (int a = 0; a<Globals.Animals.size(); a++) {
            if (Globals.similarity(Input, Globals.Animals.get(a)) > 0.6) {
                posibilities.add((float) a); posibilities.add((float) Globals.similarity(Input, Globals.Animals.get(a)));
            }
        }

        for (int a = 0; a<posibilities.size()/2; a++) {
            if (posibilities.get(a*2+1) > lead) {
                lead = posibilities.get(a*2+1); i = Math.round(posibilities.get(a*2));
            }
        }
        return i;
    }

    public void enter() {

        String inputAnimal = InputTextEdit.getText().toString().trim();

        if ((run) && (!inputAnimal.equals(""))) {
            inputLetter = Character.toUpperCase(inputAnimal.charAt(0));
            int A = animal(inputAnimal);

            if ((inputLetter == lastLetter) && (A != -1) && !InputAnimalList.contains(Globals.Animals.get(A)) &&
                    (Globals.Animals.get(A).charAt(0) == inputLetter)) {

                animateNewText();
                messageHandler.removeCallbacksAndMessages(null);

                if (Sound) {
                    CorrentSound = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                    CorrentSound.start();
                }

                String lastAnimal = Globals.Animals.get(A);
                InputAnimalList.add(lastAnimal);
                lastLetter = Character.toUpperCase(lastAnimal.charAt(lastAnimal.length() - 1));

                InputTextEdit.setText(Character.toString(lastLetter).toUpperCase());
                InputTextEdit.setSelection(1);

                score = score + 1;
                FlipContainer.startAnimation(FlipStart);

                if (!PreviouslyDiscoveredAnimals.contains(Globals.Animals.get(A))) {
                    NewlyDiscoveredAnimals.add(Globals.Animals.get(A));
                }

                Animation animationGrow = new ArcGrowAnimation(arcView);
                animationGrow.setDuration(1000);
                arcView.startAnimation(animationGrow);

                animationGrow.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        arcView.startAnimation(animationShrink);
                    }
                });

                dr = BitmapFactory.decodeResource(getResources(),
                        getResources().getIdentifier(Globals.Animals.get(A).toLowerCase() + "imagesmall", "drawable",
                                getPackageName()));

                focusHandler.removeCallbacksAndMessages(null);
                focusHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage("Time is running out!", false);

                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.timer);
                        mp.start();

                        focusHandler.removeCallbacks(this);
                    }
                }, turnTime-3000);

            } else if (A == -1) {
                displayMessage("Is that an animal?", true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }

            } else if (inputLetter != lastLetter) {
                displayMessage("Doesn't start with a " + Character.toString(lastLetter).toUpperCase() + "!", true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }

            } else if (InputAnimalList.contains(Globals.Animals.get(A))) {
                displayMessage("Already got that one!", true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }

            } else if (Globals.Animals.get(A).charAt(0) == inputLetter) {
                displayMessage("Sneaky!", true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }
            }
        }
    }

    private RoundedBitmapDrawable createRoundedBitmapDrawable(Bitmap bitmap){

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int bitmapRadius = Math.min(bitmapWidth,bitmapHeight)/2;
        int bitmapSquareWidth = Math.min(bitmapWidth,bitmapHeight);

        Bitmap roundedBitmap = Bitmap.createBitmap(bitmapSquareWidth, bitmapSquareWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);

        int x = bitmapSquareWidth - bitmapWidth;
        int y = bitmapSquareWidth - bitmapHeight;

        canvas.drawBitmap(bitmap, x, y, null);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),roundedBitmap);
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);
        roundedBitmapDrawable.setAntiAlias(true);
        return roundedBitmapDrawable;

    }

    public void displayMessage(final String Message, Boolean highlight) {

        final Animation MessageFlipStart = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip_start);
        final Animation MessageFlipEnd = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flip_end);

        MessageFlipStart.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                ScoreDisplay.setTextSize(40);
                ScoreDisplay.setText(Message);
                ScoreDisplay.startAnimation(MessageFlipEnd);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        ScoreDisplay.startAnimation(MessageFlipStart);

        if (highlight) {
            InputTextEdit.selectAll();
        }

        messageHandler.removeCallbacksAndMessages(null);
        messageHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MessageFlipStart.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ScoreDisplay.setTextSize(80);
                        ScoreDisplay.setText(String.valueOf(score));
                        ScoreDisplay.startAnimation(MessageFlipEnd);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                ScoreDisplay.startAnimation(MessageFlipStart);
                messageHandler.removeCallbacks(this);
            }
        }, 2000);

        Animation Shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        InputTextEdit.startAnimation(Shake);
        HelpButton.startAnimation(Shake);

        Animation Focus = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.grow_shrink);
        MainDisplay.startAnimation(Focus);
    }

    public void animateNewText() {
        String text = InputTextEdit.getText().toString().trim();
        String firstPart = text.substring(0, text.length()-1), secondPart = text.substring(text.length()-1, text.length());

        InputText1.setText(firstPart);
        InputTextEdit.setText(secondPart);

        InputTextEdit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                InputTextEdit.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int translationA = viewWidth/2-(InputText1.getWidth()+InputTextEdit.getWidth())/2,
                        translationB = translationA + InputText1.getWidth();

                ObjectAnimator outAnimation = ObjectAnimator.ofFloat(InputText1, "X", translationA, -1000);
                outAnimation.setDuration(1000);
                outAnimation.start();

                ObjectAnimator inAnimation = ObjectAnimator.ofFloat(InputTextEdit, "X", translationB, viewWidth/2 - InputTextEdit.getWidth()/2);
                inAnimation.setDuration(500);
                inAnimation.start();
            }
        });
    }

    public void GameOver() {
        run = false;

        Intent GameOverIntent = new Intent(this, GameOver.class);
        GameOverIntent.putExtra("score", score);
        GameOverIntent.putExtra("newlyDiscoveredAnimals", TextUtils.join("-", NewlyDiscoveredAnimals));
        startActivity(GameOverIntent);

        overridePendingTransition(R.anim.pull_in_right, R.anim.pull_out_left);
    }

    public void StartIntroAnimation() {

        setupAnimations();

        ObjectAnimator titleAnimator= ObjectAnimator.ofFloat(InputTextEdit, "translationX", 1000, 0);
        titleAnimator.setDuration(1000);
        titleAnimator.start();
    }
}
