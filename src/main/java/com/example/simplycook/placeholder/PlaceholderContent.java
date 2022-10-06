package com.example.simplycook.placeholder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static List<MainContent.Recepie> RESULTS = new ArrayList<MainContent.Recepie>();
    public static String nameSearch;
    public static SearchType searchType;
    public static List<String> ingList;

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, MainContent.Recepie> ITEM_MAP = new HashMap<String, MainContent.Recepie>();

    private static final int COUNT = 25;

    static {
        if (nameSearch != null) {
            search();
        }
    }

    public static void search() {
        RESULTS.clear();
        switch (searchType){
            case Name:
                for (int i = 0; i < MainContent.RECIPES.size(); i++) {
                    if (MainContent.RECIPES.get(i).name.toLowerCase().contains(nameSearch.toLowerCase())) {
                        addItem(MainContent.RECIPES.get(i));
                    }
                }
                break;
            case Ingredient:
                RESULTS.addAll(MainContent.RECIPES);
                for (int i = 0; i < ingList.size(); i++) {
                    for (int j = 0; j < MainContent.RECIPES.size(); j++) {
                        if (!containsIngredient(ingList.get(i),MainContent.RECIPES.get(j))) {
                            RESULTS.remove(MainContent.RECIPES.get(j));
                        }
                    }
                }
                break;
        }
    }

    private static boolean containsIngredient(String s, MainContent.Recepie recipe) {
        for (int i = 0; i < recipe.ingredients.size(); i++) {
            if (recipe.ingredients.get(i).contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static void addItem(MainContent.Recepie item) {
        RESULTS.add(item);
        ITEM_MAP.put(item.name, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PlaceholderItem {
        public final String id;
        public final String content;
        public final String details;

        public PlaceholderItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public enum SearchType {
        Name,
        Ingredient,
        Internet
    }
}