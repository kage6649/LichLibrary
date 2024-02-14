package com.AI.lichlibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecylePinjam extends RecyclerView.Adapter<RecylePinjam.ViewHolder>{
    private List<String> mData,Bgen,Bpen,Bjdl;
    private List<Integer> mdays;
    private LayoutInflater mInflater;
    private RecyleBook.ItemClickListener mClickListener;

    // data is passed into the constructor
    RecylePinjam(Context context, List<String> data, List<String> Judul, List<String> Genre, List<String> Penulis,List<Integer> Days) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.Bjdl = Judul;
        this.Bgen = Genre;
        this.Bpen = Penulis;
        this.mdays = Days;
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
        int days = mdays.get(position);
        holder.myTextView.setText(jdl);
        holder.Gen.setText("Waktu : "+gen);
        holder.Pen.setText("Tenggat : "+pen);
        if (days>0){
            holder.log.setImageResource(R.drawable.baseline_warning_24);
        }else {
            holder.log.setImageResource(R.drawable.baseline_book_24_wh);
        }


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
    void setClickListener(RecyleBook.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
