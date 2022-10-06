package com.example.simplycook.placeholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.simplycook.HomeActivity;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ArrayListMultimap;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Multimap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContent implements Serializable {


    public static List<Recepie> RECIPES = new ArrayList<Recepie>();
    public static final Map<String, Recepie> ITEM_MAP = new HashMap<String, Recepie>();

    private static final int COUNT = 25;

    static {
    }

    public static void saveToFile(Context context) {
        try {
            OutputStream file = new FileOutputStream(context.getFilesDir()+"//" + HomeActivity.userId + "_save.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(RECIPES);
            }
            finally {
                output.close();
            }
        } catch(IOException ex) {
            Log.e("Exception", "File write failed: " + ex.toString());
        }
    }

    public static void saveImageToFiles(Context context, Uri image, String name) throws FileNotFoundException {
        File myDir = new File(context.getFilesDir()+"//" );
        myDir.mkdirs();

        String fname = name + ".jpg";
        File file = new File (myDir, fname);

        Bitmap finalBitmap = getContactBitmapFromURI(context, image);

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getContactBitmapFromURI(Context context, Uri uri) {
        try {

            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input == null) {
                return null;
            }
            return BitmapFactory.decodeStream(input);
        }
        catch (FileNotFoundException e)
        {

        }
        return null;

    }


    public static boolean loadFromFile(Context context) {
        try {
            //use buffering
            InputStream file = new FileInputStream(context.getFilesDir()+"//" + HomeActivity.userId + "_save.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            try {
                //deserialize the List
                List<Recepie> r = (List<Recepie>)input.readObject();
                RECIPES = r;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally{
                input.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return false;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return false;
        }
        return true;
    }


    public static void putDefaultRecipes(){
        String pic = "https://thebakermama.com/wp-content/uploads/2021/01/IMG_8512-scaled.jpg";
        List<String> ing = new ArrayList<>();
        ing.add("2kg wheat");
        ing.add("1 egg");
        ing.add("1 cup of milk");
        ing.add("2 cups of vanilla");
        ing.add("1 cup of sugar");
        Recepie rec = new Recepie("French Toast", pic, ing, Category.Breakfast, false);
        addItem(rec);

        pic = "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F43%2F2022%2F04%2F22%2F20334-Banana-Pancakes-mfs_01_1x1.jpg";
        ing = new ArrayList<>();
        ing.add("3 bananas");
        ing.add("2 eggs");
        ing.add("2 tablespoon of oatmeal");
        rec = new Recepie("Banana Pancake", pic, ing, Category.Dinner, false);
        addItem(rec);

        pic = "https://www.feastingathome.com/wp-content/uploads/2016/04/easy-authentic-pad-thai-recipe-100-8.jpg";
        ing = new ArrayList<>();
        ing.add("2 bags of noodles");
        ing.add("1kg of chicken");
        ing.add("2 pepper");
        ing.add("2 carrot");
        rec = new Recepie("Pad Thai", pic, ing, Category.Lunch, false);
        addItem(rec);

        pic = "https://www.cookingclassy.com/wp-content/uploads/2020/02/chocolate-mousse-3-768x1152.jpg";
        ing = new ArrayList<>();
        ing.add("300 grams of chocolate");
        ing.add("1 cup of whipping cream");
        rec = new Recepie("Chocolate Mousse", pic, ing, Category.Desert, false);
        addItem(rec);

        pic = "https://yossefish.co.il/wp-content/uploads/2022/04/fish-chips-with-french-fries.jpg";
        ing = new ArrayList<>();
        ing.add("1kg of cod fillets");
        ing.add("1kg of sliced potatoes");
        rec = new Recepie("Fish and chips", pic, ing, Category.Lunch, false);
        addItem(rec);
    }

    public static void addItem(Recepie item) {
        RECIPES.add(item);
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

    public static class Recepie implements Serializable {

        public String name;
        public String picture;
        public List<String> ingredients;
        public Category category;
        public Boolean favorite;

        public Recepie(String name, String picture, List<String> ingredients, Category category, Boolean favorite) {
            this.name = name;
            this.picture = picture;
            this.ingredients = ingredients;
            this.category = category;
            this.favorite = favorite;
        }

        public Uri getPictureUri() {
            return Uri.parse(picture);
        }

        public String getName() {
            return name;
        }

        public Boolean getFavorite() {
            return favorite;
        }

        public String getCategory() {
            return category.toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public String getIngredients() {
            StringBuilder result = new StringBuilder();
            ingredients.forEach(x -> result.append(x + "\n"));
            return result.toString();
        }

        public void setFavorite() {
            if (favorite == true) {
                favorite = false;
            }
            else {
                favorite = true;
            }
        }

    }

    public enum Category {
        Breakfast,
        Lunch,
        Dinner,
        Desert
    }
}

