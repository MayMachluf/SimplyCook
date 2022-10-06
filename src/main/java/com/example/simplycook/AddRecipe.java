package com.example.simplycook;

import static android.app.Activity.RESULT_OK;
import static com.example.simplycook.placeholder.MainContent.addItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simplycook.placeholder.MainContent;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.ArrayListMultimap;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.collect.Multimap;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecipe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipe extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private static final String[] paths = {"breakfast", "launch", "dinner", "desert"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String recipeName;
    private MainContent.Category recipeCategory;
    List<String> ingMap = new ArrayList<>();
    private ShowRecipe.listenerChild listener;
    String image;
    private EditText name;
    private Button addImage;

    public AddRecipe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipe.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipe newInstance(String param1, String param2) {
        AddRecipe fragment = new AddRecipe();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        spinner = (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button addIng = view.findViewById(R.id.addIng);
        EditText ingName = view.findViewById(R.id.ingName);
        TextView listOfIngredients = view.findViewById(R.id.listOfIngredients);
        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingName.getText().toString().equals("")) {
                    return;
                }
                ingMap.add(ingName.getText().toString());

                String str = listOfIngredients.getText().toString() +
                        ingName.getText().toString() + "\n";
                listOfIngredients.setText(str);
                ingName.setText("");

            }
        });

        Button addRecipe = view.findViewById(R.id.createRecipeButton);
        name = view.findViewById(R.id.recipeNameInput);

        addImage = view.findViewById(R.id.pickImageFromGallery);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.getText().equals("")) {
                    try {
                        Intent i = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 3);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        EditText url = view.findViewById(R.id.pictureUrlInput);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("") || ingMap.size() == 0) {
                    return;
                }
                recipeName = name.getText().toString();
//                recipeUrl = url.getText().toString();
                MainContent.Recepie rec = new MainContent.Recepie(recipeName, image, ingMap, recipeCategory, false);
                addItem(rec);
                MainContent.saveToFile(getContext());
                listener.backToMain();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                MainContent.saveImageToFiles(getContext(), selectedImage, name.getText().toString().replace(' ', '_'));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            addImage.setText("Image added!");
            addImage.setClickable(false);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShowRecipe.listenerChild) {
            listener = (ShowRecipe.listenerChild) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                recipeCategory = MainContent.Category.Breakfast;
                break;
            case 1:
                recipeCategory = MainContent.Category.Lunch;
                break;
            case 2:
                recipeCategory = MainContent.Category.Dinner;
                break;
            case 3:
                recipeCategory = MainContent.Category.Desert;
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}