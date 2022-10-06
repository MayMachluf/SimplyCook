package com.example.simplycook;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplycook.placeholder.MainContent;

/**
 * A fragment representing a list of Items.
 */



public class RecepiesFragment extends Fragment {

    private listenerRecipeClickOnMain listener;

    public interface listenerRecipeClickOnMain {
        void sendRecipe(MainContent.Recepie r);
    }

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    public RecepiesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecepiesFragment newInstance(int columnCount) {
        RecepiesFragment fragment = new RecepiesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recepies_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(MainContent.RECIPES, new MyItemRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(MainContent.Recepie item) {
                    listener.sendRecipe(item);

                }
            }));
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof listenerRecipeClickOnMain) {
            listener = (listenerRecipeClickOnMain) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}