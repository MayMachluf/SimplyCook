package com.example.simplycook;

import static com.example.simplycook.placeholder.MainContent.addItem;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.simplycook.placeholder.MainContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchInInternet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchInInternet extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean ready = false;
    private String name;
    private String image;
    public List<String> ingredients = new ArrayList<>();
    private MainContent.Category recipeCategory;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private String searchQuery;
    private Spinner spinner;
    private ShowRecipe.listenerChild listener;
    private static final String[] paths = {"breakfast", "launch", "dinner", "desert"};

    private TextView nameFromInternet;
    private TextView ingFromInternet;

    public SearchInInternet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchInInternet.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchInInternet newInstance(String param1, String param2) {
        SearchInInternet fragment = new SearchInInternet();
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
        view = inflater.inflate(R.layout.fragment_search_in_internet, container, false);


        spinner = (Spinner)view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button btn = view.findViewById(R.id.doSearchInInternetBtn);
        EditText searchTerm = view.findViewById(R.id.searchInInternetTerm);
        Button addRecipe = view.findViewById(R.id.addRecipeFromInternet);

        nameFromInternet = view.findViewById(R.id.recipeNameFromInternet);
        ingFromInternet = view.findViewById(R.id.ingredientsTextFromInternet);
        ingFromInternet.setMovementMethod(new ScrollingMovementMethod());

        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                searchQuery = searchTerm.getText().toString();
                if (!searchQuery.equals("")) {
                    nameFromInternet.setText("Loading...");
                    try {
                        new doIT().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ready) {
                    MainContent.Recepie recipe = new MainContent.Recepie(name, image, ingredients, recipeCategory, false);
                    addItem(recipe);
                    MainContent.saveToFile(getContext());
                    listener.backToMain();
                }
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

    public class doIT extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = "https://www.yummly.com/recipes?q=" + searchQuery;
                Document document = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .timeout(7000)
                        .get();

                Elements results = document.getElementsByClass("card-title two-line-truncate p2-text font-normal");
                Element link = results.get(0).select("a[href]").first();
                String url2 = "https://yummly.com" + link.attr("href");

                Document document2 = Jsoup.connect(url2)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .timeout(7000)
                        .get();

                //String url2 = results.get(0).getElementsByAttribute("href");
                name = (String) document2.getElementsByClass("recipe-title font-bold h2-text primary-dark").get(0).text().subSequence(0, 20);
                String image_code = document2.select("div.structured-data-info").toString();
                image = image_code.substring(image_code.indexOf("[") + 2, image_code.indexOf(',', image_code.indexOf("[")) - 2);
                Elements x = document2.getElementsByClass("IngredientLine");
                x.forEach(i -> ingredients.add(i.text()));
                ready = true;

            } catch (Exception e) {
                name = "Not found";
            }
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nameFromInternet.setText(name);
            ingredients.forEach(j -> ingFromInternet.append(j + "\n"));

        }
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