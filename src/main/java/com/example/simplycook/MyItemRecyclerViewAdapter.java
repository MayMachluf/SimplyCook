package com.example.simplycook;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.media.tv.TvContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplycook.databinding.FragmentRecepiesBinding;
import com.example.simplycook.placeholder.MainContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.example.simplycook.placeholder.MainContent.Recepie}.
 * TODO: Replace the implementation with code for your data type.
 */



public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MainContent.Recepie item);
    }

    private OnItemClickListener listener;


    private final List<MainContent.Recepie> mValues;

    public MyItemRecyclerViewAdapter(List<MainContent.Recepie> items, OnItemClickListener listener) {
        mValues = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentRecepiesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues.get(position).favorite) {
            holder.mFavorite.setImageResource(R.drawable.favorite_on);
        }
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getCategory());
        holder.mContentView.setText(mValues.get(position).name);
        int p =  position;

        holder.bind(mValues.get(position), listener);

        holder.mFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mValues.get(p).setFavorite();
                if (mValues.get(p).favorite) {
                    holder.mFavorite.setImageResource(R.drawable.favorite_on);
                }
                else {
                    holder.mFavorite.setImageResource(R.drawable.favorite_off);
                }
                MainContent.saveToFile(view.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public ImageView mFavorite;
        public MainContent.Recepie mItem;

        public ViewHolder(FragmentRecepiesBinding binding) {
            super(binding.getRoot());
            mFavorite = binding.favoriteImage;
            mIdView = binding.category;
            mContentView = binding.content;
        }

        public void bind(final MainContent.Recepie item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}