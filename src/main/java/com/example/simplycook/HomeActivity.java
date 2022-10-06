package com.example.simplycook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplycook.placeholder.MainContent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecepiesFragment.listenerRecipeClickOnMain, ShowRecipe.listenerChild, ItemFragment.OnRecipeClickListener2 {

    private ShowRecipe showRecipe;
    public RecepiesFragment recepiesFragment;
    private ImageView btn;
    private ImageView searchBtn;
    private ImageView homeBtn;

    public static String userId = "guest";
    public static String personName = "Guest";

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        showRecipe = new ShowRecipe();
        recepiesFragment = new RecepiesFragment();

        TextView username = findViewById(R.id.textViewUsername);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(HomeActivity.this);
        if (acct != null) {
            personName = acct.getDisplayName();
        }
        username.setText("Hi, " + personName + "!");


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainerView2, recepiesFragment);
        ft.commit();

        btn = findViewById(R.id.addButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClicked(0);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerView2, new AddRecipe());
                ft.commit();
            }
        });

        searchBtn = findViewById(R.id.searchButton);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClicked(2);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerView2, new BlankFragment());
                ft.commit();
            }
        });

        homeBtn = findViewById(R.id.homeButton);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClicked(1);
                backToMain();
            }
        });

        ImageView logout = findViewById(R.id.logoutBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void setClicked(int i) {
        switch (i){
            case 0:
                btn.setImageDrawable(getDrawable(R.drawable.plus_clicked));
                searchBtn.setImageDrawable(getDrawable(R.drawable.search_unclicked));
                homeBtn.setImageDrawable(getDrawable(R.drawable.home_unclicked));
                break;
            case 1:
                btn.setImageDrawable(getDrawable(R.drawable.add_unclicked));
                searchBtn.setImageDrawable(getDrawable(R.drawable.search_unclicked));
                homeBtn.setImageDrawable(getDrawable(R.drawable.home_clicked));
                break;
            case 2:
                btn.setImageDrawable(getDrawable(R.drawable.add_unclicked));
                searchBtn.setImageDrawable(getDrawable(R.drawable.search_clicked));
                homeBtn.setImageDrawable(getDrawable(R.drawable.home_unclicked));
                break;
        }
    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    public static boolean loadFromFile(Context context, String preferenceKey, Class<List<MainContent.Recepie>> classType) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
            if (sharedPreferences.contains(preferenceKey)) {
                final Gson gson = new Gson();
                MainContent.RECIPES = gson.fromJson(sharedPreferences.getString(preferenceKey, ""), (Type) classType);
            }
        } catch (Exception e) {
            Log.e("Failed to load", "Can not read file: " + e.toString());
            return false;
        }
        return true;
    }

    @Nullable
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void sendRecipe(MainContent.Recepie r) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, showRecipe);
        ft.commit();
        showRecipe.recipe = r;
    }

    @Override
    public void backToMain() {
        setClicked(1);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, recepiesFragment);
        ft.commit();
    }

    @Override
    public boolean loadData() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void startSearchByName() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void startSearchByIng() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, new SearchRecipeByIngredient());
        ft.commit();
    }

    @Override
    public void startSearchInInternet() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, new SearchInInternet());
        ft.commit();
    }

    @Override
    public void doSearchByIng() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, new ItemFragment());
        ft.commit();
    }

    @Override
    public void startSearch() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, new SearchRecipe());
        ft.commit();
    }

    @Override
    public void onItemClick(MainContent.Recepie item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView2, showRecipe);
        ft.commit();
        showRecipe.recipe = item;
    }
}