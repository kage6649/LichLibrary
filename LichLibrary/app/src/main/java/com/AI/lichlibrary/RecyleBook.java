package com.AI.lichlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyleBook extends RecyclerView.Adapter<RecyleBook.ViewHolder>{
    private List<String> mData,Bgen,Bpen,Bjdl;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Home home;

    // data is passed into the constructor
    RecyleBook(Context context, List<String> data,List<String> Judul,List<String> Genre,List<String> Penulis) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.Bjdl = Judul;
        this.Bgen = Genre;
        this.Bpen = Penulis;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclebasic, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        String animal = mData.get(position);
        String jdl = Bjdl.get(position);
        String gen = Bgen.get(position);
        String pen = Bpen.get(position);
//        String uri = Bpen.get(position);
        holder.myTextView.setText(jdl);
        holder.Gen.setText("Kategori : "+gen);
        holder.Pen.setText("Penulis : "+pen);
//        holder.img.setImageURI(Uri.parse(uri));
        holder.log.setImageResource(R.drawable.baseline_menu_book_24);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView,Gen,Pen;
        ImageView log;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvAnimalName);
            Gen = itemView.findViewById(R.id.Bgenre);
            Pen = itemView.findViewById(R.id.Bpenulis);
            log = itemView.findViewById(R.id.log);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
