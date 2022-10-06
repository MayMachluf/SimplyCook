package com.example.simplycook;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplycook.databinding.FragmentRecepiesBinding;
import com.example.simplycook.placeholder.MainContent;
import com.example.simplycook.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.simplycook.databinding.FragmentItemBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter2 extends RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder> {

    private final List<MainContent.Recepie> mValues;
    private OnItemClickListener2 listener;

    public interface OnItemClickListener2 {
        void onItemClick(MainContent.Recepie item);
    }


    public MyItemRecyclerViewAdapter2(List<MainContent.Recepie> items, OnItemClickListener2 listener) {
        mValues = items;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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

        public ViewHolder(@NonNull FragmentItemBinding binding) {
            super(binding.getRoot());
            mFavorite = binding.favoriteImageInSearch;
            mIdView = binding.categoryInSearch;
            mContentView = binding.contentInSearch;
        }

        public void bind(final MainContent.Recepie item, final OnItemClickListener2 listener) {
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