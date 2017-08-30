package com.djavid.bitcoinrate.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected Context context;
    protected LayoutInflater inflater;
    private List<T> data;

    public BaseRecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<T>();
        this.context = context;
    }

    protected abstract void bindSingleItem(VH viewHolder, T item);
    protected abstract VH createVH(android.view.View view);
    protected abstract int getLayoutId();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        android.view.View view = inflater.inflate(getLayoutId(), parent, false);
        return createVH(view);
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        bindSingleItem(viewHolder, data.get(position));
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void appendData(List<T> data) {
        this.data.addAll(data);
    }

    public void setDataWithNotify(List<T> data) {
        setData(data);
        notifyDataSetChanged();
    }

    public void appendDataWithNotify(List<T> data) {
        appendData(data);
        notifyDataSetChanged();
    }
    public void clear(){
        data.clear();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
