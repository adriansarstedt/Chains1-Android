package com.example.adriansarstedt.words2;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adriansarstedt on 27/12/2016.
 */

public class Globals extends Application {
    public static ArrayList<String> Animals = new ArrayList<>(Arrays.asList("Aardvark", "Albatross", "Alligator", "Alpaca", "Ant", "Anteater", "Antelope",
            "Ape", "Armadillo", "Baboon", "Badger", "Barracuda", "Bat", "Bear", "Beaver", "Bee", "Bird", "Bison",
            "Boar", "Butterfly", "Camel", "Caribou", "Cassowary", "Cat", "Caterpillar", "Cattle",
            "Chamois", "Cheetah", "Chicken", "Chimpanzee", "Chinchilla", "Chough", "Coati", "Cobra", "Cockroach", "Cod",
            "Cormorant", "Coyote", "Crab", "Crane", "Crocodile", "Crow", "Curlew", "Deer", "Dog", "Dogfish",
            "Dolphin", "Donkey", "Dove", "Dragonfly", "Duck", "Dugong", "Dunlin", "Eagle", "Echidna", "Eel",
            "Eland", "Elephant", "Elephantseal", "Elk", "Emu", "Falcon", "Ferret", "Finch", "Fish", "Flamingo", "Fly", "Fox",
            "Frog", "Gaur", "Gazelle", "Gerbil", "Giantpanda", "Giraffe", "Goat", "Goldfinch",
            "Goose", "Gorilla", "Grasshopper", "Guineapig", "Gull", "Hamster",
            "Hare", "Hawk", "Hedgehog", "Heron", "Herring", "Hippopotamus", "Hornet", "Horse", "Hummingbird", "Hyena",
            "Ibex", "Ibis", "Jackal", "Jaguar", "Jay", "Jellyfish", "Kangaroo", "Koala", "Komododragon", "Kudu", "Lapwing", "Lizard", "Lemur", "Leopard", "Lion", "Llama", "Lobster", "Locust", "Loris",
            "Louse", "Lyrebird", "Magpie", "Mallard", "Manatee", "Mandrill", "Mink", "Mole", "Meercat",
            "Monkey", "Moose", "Mouse", "Mosquito", "Narwhal", "Newt", "Nightingale", "Octopus", "Okapi", "Opossum",
            "Ostrich", "Otter", "Owl", "Oyster", "Panther", "Parrot", "Peacock", "Pelican", "Penguin",
            "Pheasant", "Pig", "Pigeon", "Polarbear", "Pony", "Porcupine", "Porpoise", "Prairiedog", "Quail",
            "Quetzal", "Rabbit", "Raccoon", "Ram", "Rat", "Raven", "Redpanda", "Reindeer", "Rhinoceros", "Rook",
            "Salamander", "Salmon", "Sanddollar", "Sandpiper", "Sardine", "Seastar", "Sealion", "Seaurchin", "Seahorse", "Seal",
            "Shark", "Sheep", "Skunk", "Sloth", "Snail", "Snake", "Spider", "Squirrel", "Starling", "Swan",
            "Tapir", "Tarsier", "Termite", "Tiger", "Toad", "Turkey", "Turtle", "Wallaby", "Walrus", "Wasp", "Waterbuffalo",
            "Weasel", "Whale", "Wolf", "Wolverine", "Wombat", "Wren", "Yak", "Zebra"));

    public static ArrayList<String> Letters = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "W", "Y", "Z"));

    public static String User = "adriansarstedt", Difficulty = "Fast";

    public static float dipToPixels(Context context, int dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal, float sat) {
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

    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
