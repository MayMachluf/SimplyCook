package com.example.simplycook;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simplycook.placeholder.PlaceholderContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchRecipeByIngredient#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRecipeByIngredient extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<String> ingList = new ArrayList<>();
    private ShowRecipe.listenerChild listener;

    public SearchRecipeByIngredient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchRecipeByIngredient.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRecipeByIngredient newInstance(String param1, String param2) {
        SearchRecipeByIngredient fragment = new SearchRecipeByIngredient();
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
        View view = inflater.inflate(R.layout.fragment_search_recipe_by_ingredient, container, false);

        EditText ingName = view.findViewById(R.id.ingName2);
        Button addIng = view.findViewById(R.id.addIng2);
        Button startSearchByIng = view.findViewById(R.id.searchByIngBtn2);
        TextView listOfIngredients2 = view.findViewById(R.id.listOfIngredients2);

        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingName.getText().toString().equals("")) {
                    return;
                }
                ingList.add(ingName.getText().toString());

                String str = ingName.getText().toString() + "\n";
                listOfIngredients2.append(str);
                ingName.setText("");
            }
        });

        startSearchByIng.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                PlaceholderContent.searchType = PlaceholderContent.SearchType.Ingredient;
                PlaceholderContent.ingList = ingList;
                PlaceholderContent.search();
                listener.doSearchByIng();
            }
        });

        return view;
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
}