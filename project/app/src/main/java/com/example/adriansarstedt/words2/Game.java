package com.example.adriansarstedt.words2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game extends AppCompatActivity {

    EditText InputTextEdit;
    TextView InputText1, HighScoreDisplay;
    ImageButton HelpButton;

    Character lastLetter, inputLetter;
    int score = 0, viewWidth = 2000, HighScore, turnTime, NewEggCount = 0, EggCount;
    Random RandomGenerator;
    boolean run = true, Sound, PreviouslyDiscovered;

    MediaPlayer CorrentSound;
    PopupWindow popup;

    Handler focusHandler = new Handler();
    GameDial gameDial;

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
        RandomGenerator = new Random();

        gameDial = (GameDial) findViewById(R.id.game_dial);
        HelpButton = (ImageButton) findViewById(R.id.help_button);
        HighScoreDisplay = (TextView) findViewById(R.id.highscore_display);

        InputTextEdit = (EditText) findViewById(R.id.InputTextEdit);
        InputText1 = (TextView) findViewById(R.id.InputText1);

        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Thin.ttf");

        InputTextEdit.setTypeface(custom_font_hairline);
        InputText1.setTypeface(custom_font_hairline);
        HighScoreDisplay.setTypeface(custom_font_hairline);

        SharedPreferences HighScoreManager = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        HighScore = HighScoreManager.getInt(Globals.User+Globals.Difficulty, 0);
        String Difficulty = PreferenceManager.getDefaultSharedPreferences(this).getString("Difficulty", "Slow");
        String SoundText = PreferenceManager.getDefaultSharedPreferences(this).getString("Sound", "On");
        String DiscoveredAnimalsStorage = PreferenceManager.getDefaultSharedPreferences(this).getString("DiscoveredAnimals", "researchcenter");
        PreviouslyDiscoveredAnimals = new ArrayList<>(Arrays.asList(DiscoveredAnimalsStorage.split("-")));
        NewlyDiscoveredAnimals = new ArrayList<>();
        EggCount = PreferenceManager.getDefaultSharedPreferences(this).getInt("EggCount", 0);

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

        gameDial.setGame(this);

    }

    public void setupAnimations() {

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

                score = score + 1;
                String lastAnimal = Globals.Animals.get(A);

                gameDial.regenerate(score, lastAnimal);
                animateNewText();

                int random = RandomGenerator.nextInt(50);
                if (random<=10) {
                    NewEggCount += 1;
                    EggCount += 1;
                    animateIcon();
                }

                if (Sound) {
                    CorrentSound = MediaPlayer.create(getApplicationContext(), R.raw.correct);
                    CorrentSound.start();
                }

                InputAnimalList.add(lastAnimal);
                lastLetter = Character.toUpperCase(lastAnimal.charAt(lastAnimal.length() - 1));

                InputTextEdit.setText(Character.toString(lastLetter).toUpperCase());
                InputTextEdit.setSelection(1);

                if (!PreviouslyDiscoveredAnimals.contains(Globals.Animals.get(A))) {
                    PreviouslyDiscovered = false;

                    NewlyDiscoveredAnimals.add(Globals.Animals.get(A));

                } else {
                    PreviouslyDiscovered = true;
                }

                focusHandler.removeCallbacksAndMessages(null);
                focusHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameDial.displayMessage("Time is running out!", null, false);

                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.timer);
                        mp.start();

                        focusHandler.removeCallbacks(this);
                    }
                }, turnTime-3000);

            } else if (A == -1) {
                gameDial.displayMessage("Is that an animal?", null, true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }

            } else if (inputLetter != lastLetter) {
                gameDial.displayMessage("Doesn't start with a " + Character.toString(lastLetter).toUpperCase() + "!", null, true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }

            } else if (InputAnimalList.contains(Globals.Animals.get(A))) {
                gameDial.displayMessage("Already got that one!", null, true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }

            } else if (Globals.Animals.get(A).charAt(0) != inputLetter) {
                gameDial.displayMessage("Sneaky!", null, true);

                if (Sound) {
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.no);
                    mp.start();
                }
            }
        }
    }

    public void animateNewText() {
        String text = InputTextEdit.getText().toString().trim();
        String firstPart = text.substring(0, text.length()-1), secondPart = text.substring(text.length()-1, text.length());

        InputText1.setText(firstPart);
        InputTextEdit.setText(secondPart);

        final int initialWidth = InputTextEdit.getWidth();

        InputTextEdit.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                InputTextEdit.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int translationA = viewWidth/2-(InputText1.getWidth()+InputTextEdit.getWidth())/2;
                int diff = InputText1.getWidth()+InputTextEdit.getWidth()-initialWidth;
                int translationB = translationA+InputText1.getWidth()-diff;

                ObjectAnimator outAnimation = ObjectAnimator.ofFloat(InputText1, "X", translationA, -1000);
                outAnimation.setDuration(1000);
                outAnimation.start();

                ObjectAnimator inAnimation = ObjectAnimator.ofFloat(InputTextEdit, "X", translationB, viewWidth/2 - InputTextEdit.getWidth()/2);
                inAnimation.setDuration(500);
                inAnimation.start();
            }
        });
    }

    public void animateIcon() {
        System.out.println("------ displaying icon ------");
    }

    public void GameOver() {
        run = false;

        Intent GameOverIntent = new Intent(this, GameOverRevised.class);
        GameOverIntent.putExtra("score", score);
        GameOverIntent.putExtra("NewEggCount", NewEggCount);
        GameOverIntent.putExtra("newlyDiscoveredAnimals", TextUtils.join("-", NewlyDiscoveredAnimals));
        startActivity(GameOverIntent);
    }

    public void StartIntroAnimation() {

        setupAnimations();

        gameDial.startTimer(new Runnable() {
            @Override
            public void run() {
                GameOver();
            }
        });

        ObjectAnimator textAnimator= ObjectAnimator.ofFloat(InputTextEdit, "translationX", 1000, 0);
        textAnimator.setDuration(1000);
        textAnimator.start();
    }

    private Bitmap toGrayscale(Bitmap bmpOriginal, float sat) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(sat);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public void help(View view) {
        Animation Shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        HelpButton.startAnimation(Shake);

        final Activity context = Game.this;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.research_popup_layout);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.research_popup_layout, viewGroup);

        // Creating the PopupWindow
        popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(getWindowManager().getDefaultDisplay().getWidth()-100);
        popup.setHeight(600);
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new BitmapDrawable());

        Button ItemTitle1 = (Button) layout.findViewById(R.id.ItemTitle1);
        Button ItemTitle2 = (Button) layout.findViewById(R.id.ItemTitle2);
        Button ItemTitle3 = (Button) layout.findViewById(R.id.ItemTitle3);

        Typeface custom_font_hairline = Typeface.createFromAsset(getAssets(), "fonts/Lato-Thin.ttf");

        ItemTitle1.setTypeface(custom_font_hairline);
        ItemTitle2.setTypeface(custom_font_hairline);
        ItemTitle3.setTypeface(custom_font_hairline);

        // Displaying the popup at the specified location
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 50, (int) Globals.dipToPixels(this, 80));

    }

    public void GetHint(View view) {

        if (EggCount>=4) {
            popup.dismiss();

            EggCount -= 4;
            NewEggCount -= 4;
            animateIcon();

            String suggestedAnimal = FindAnimal(Character.toString(lastLetter).toUpperCase());


            if (suggestedAnimal != null) {

                Bitmap drSuggested = BitmapFactory.decodeResource(getResources(),
                        getResources().getIdentifier(suggestedAnimal.toLowerCase() + "imagesmall", "drawable",
                                getPackageName()));

                if (!PreviouslyDiscoveredAnimals.contains(suggestedAnimal)) {
                    drSuggested = toGrayscale(drSuggested, 0);
                }

                gameDial.displayMessage("This animal would work!", drSuggested, true);
            } else {
                gameDial.displayMessage("No animals start with a " + Character.toString(lastLetter).toUpperCase() + "?!", null, true);
            }
        } else {
            gameDial.displayMessage("Not Enough Eggs!", null, false);
        }
    }

    public void GetMoreTime(View view) {

        if (EggCount>=6) {
            popup.dismiss();

            EggCount -= 6;
            NewEggCount -= 6;
            animateIcon();

            //////////////////////////////////////
            // reload time                      //
            //////////////////////////////////////
        } else {
            gameDial.displayMessage("Not Enough Eggs!", null, false);
        }
    }

    public void Skip(View view) {

        if (EggCount>=20) {
            popup.dismiss();

            EggCount -= 20;
            NewEggCount -= 20;
            animateIcon();

            String suggestedAnimal = FindAnimal(Character.toString(lastLetter).toUpperCase());

            if (suggestedAnimal != null) {
                //displayMessage("What about a " + suggestedAnimal + "?", true);
            } else {
                //displayMessage("No animals start with a " + Character.toString(lastLetter).toUpperCase() + "?!", false);
            }
        } else {
            gameDial.displayMessage("Not Enough Eggs!", null, false);
        }
    }

    public String FindAnimal(String FirstLetter) {
        ArrayList<String> FilteredAnimals = new ArrayList<>();

        boolean firstFound = false, lastFound = false;

        for (int i=0; i<Globals.Animals.size(); i++) {
            if (!firstFound && !lastFound) {
                if (Globals.Animals.get(i).startsWith(FirstLetter) && !InputAnimalList.contains(Globals.Animals.get(i))) {
                    FilteredAnimals.add(0, Globals.Animals.get(i));
                    firstFound = true;
                }
            } else if (firstFound && !lastFound) {
                if (Globals.Animals.get(i).startsWith(FirstLetter) && !InputAnimalList.contains(Globals.Animals.get(i))) {
                    FilteredAnimals.add(0, Globals.Animals.get(i));
                } else {
                    lastFound = true;
                }
            }
        }

        if (FilteredAnimals.size() != 0) {
            return FilteredAnimals.get(RandomGenerator.nextInt(FilteredAnimals.size()));
        } else {
            return null;
        }
    }
}
