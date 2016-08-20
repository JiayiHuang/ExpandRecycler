package com.example.prince.recyclerheadfoots;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by princ on 2016/8/20.
 */
public class MyRecyclerAdapter extends BaseRecyclerAdapter {
   private Context context;
   private List dataList;

   public MyRecyclerAdapter(Context context, List dataList) {
      super(context, dataList);
      this.context = context;
      this.dataList = dataList;
   }

   @Override
   protected BaseRecyclerHolder onCreateRecyclerViewHolder(ViewGroup parent, int viewType) {
      View view = View.inflate(context,R.layout.item,null);
      return new RecyclerHolder(view);
   }

   @Override
   protected void onBindRecyclerViewHolder(BaseRecyclerHolder holder, int position) {
      ((RecyclerHolder)holder).tv.setText("Content Item = "+(dataList.size()+position));
      if (position == getItemCount()) {
         setLoadMoreState(LoadState.LOADING);
      }
   }

   @Override
   protected int getRecyclerItemCount() {
      return dataList.size();
   }
}

class RecyclerHolder extends BaseRecyclerHolder {
   public TextView tv;

   public RecyclerHolder(View itemView) {
      super(itemView);
      tv = (TextView) itemView.findViewById(R.id.tv);
   }
}
