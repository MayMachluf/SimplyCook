package com.example.simplycook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplycook.placeholder.MainContent;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowRecipe extends Fragment {

    public interface listenerChild {
        void backToMain();
        boolean loadData();
        void startSearch();
        void startSearchByName();
        void startSearchByIng();
        void startSearchInInternet();

        void doSearchByIng();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public MainContent.Recepie recipe;
    private listenerChild listener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowRecipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowRecipe.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowRecipe newInstance(String param1, String param2) {
        ShowRecipe fragment = new ShowRecipe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_recipe, container, false);

        ImageView image = view.findViewById(R.id.recipePicture);
        TextView name = view.findViewById(R.id.recipeNameText);
        TextView category = view.findViewById(R.id.categoryText);
        TextView ingredients = view.findViewById(R.id.ingredientsText);
        ingredients.setMovementMethod(new ScrollingMovementMethod());

        name.setText(recipe.name);
        category.setText(recipe.getCategory());
        ingredients.setText(recipe.getIngredients());
        if (recipe.picture != null) {
            Picasso.get().load(recipe.getPictureUri()).into(image);
        } else {

            Uri imageUri = Uri.fromFile(new File(getContext().getFilesDir()+"/" + recipe.name.replace(' ', '_') + ".jpg"));
            InputStream imageStream = null;
            try {
                imageStream = getContext().getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(selectedImage);

        }

        Button delButton = view.findViewById(R.id.deleteButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                MainContent.RECIPES.remove(recipe);
                MainContent.saveToFile(getContext());
                listener.backToMain();
                MainContent.saveToFile(getContext());
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof listenerChild) {
            listener = (listenerChild) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}