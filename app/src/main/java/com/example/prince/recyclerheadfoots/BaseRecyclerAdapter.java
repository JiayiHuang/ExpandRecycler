package com.example.prince.recyclerheadfoots;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
   private static final String TAG = "BaseRecyclerAdapter";
   private Map<Integer, View> headerMap;
   private List<Integer> headerIndex;
   private Map<Integer, View> footerMap;
   private List<Integer> footerIndex;
   private static final int HEADER_INDEX = 1000;
   private static final int FOOTER_INDEX = 2000;
   public static final int TYPE_NORMAL = 0x103;
   public static final int TYPE_LOAD_MORE = 0x102;
//   private int contentNum = 20;

   private Context context;
   private List<T> dataList;
   private boolean loadMoreEnable = true;
   private LoadState loadState = LoadState.LOADING;
   private View loadMoreView;

   public BaseRecyclerAdapter(Context context, List<T> dataList) {
      this.context = context;
      this.dataList = dataList;
      headerIndex = new ArrayList<>();
      headerMap = new HashMap<>();
      footerIndex = new ArrayList<>();
      footerMap = new HashMap<>();
   }

   /**
    * @param index index与之前相同则不添加
    * @param view
    */
   public void addHeader(int index, View view) {
      if (!headerIndex.contains(index + HEADER_INDEX)) {
         headerMap.put(index + HEADER_INDEX, view);
         headerIndex.add(index + HEADER_INDEX);
         notifyDataSetChanged();
      }
   }

   public void removeHeader(int index) {
      View removeView = headerMap.remove(index + HEADER_INDEX);
      if (removeView != null) {
         headerIndex.remove(headerIndex.indexOf(index + HEADER_INDEX));
         notifyDataSetChanged();
      }
   }

   /**
    * @param index index与之前index相同则不添加
    * @param view
    */
   public void addFooter(int index, View view) {
      if (!footerIndex.contains(index + FOOTER_INDEX)) {
         footerMap.put(index + FOOTER_INDEX, view);
         footerIndex.add(index + FOOTER_INDEX);
         notifyDataSetChanged();
      }
   }

   public void removeFooter(int index) {
      View removeView = footerMap.remove(index + FOOTER_INDEX);
      if (removeView != null) {
         footerIndex.remove(footerIndex.indexOf(index + FOOTER_INDEX));
         notifyDataSetChanged();
      }
   }

   public void setLoadMore(boolean enable) {
      this.loadMoreEnable = enable;
      if (loadMoreEnable) {

      } else {

      }
   }

   enum LoadState {
      NORMAL, SUCCESS, ERROR, LOADING, NOMORE
   }

   RotateAnimation rotate;

   public void setLoadMoreState(LoadState state) {
      loadState = state;
      ImageView imgLoading = (ImageView) loadMoreView.findViewById(R.id.img_loading);
      TextView tvLoading = (TextView) loadMoreView.findViewById(R.id.tv);
      switch (loadState) {
         case NORMAL:
            imgLoading.setImageResource(R.mipmap.arrow_down);
            tvLoading.setText("下拉加载更多");
            if (null != rotate && !rotate.hasEnded()) {
               rotate.cancel();
            }
            break;
         case SUCCESS:
            imgLoading.setImageResource(R.mipmap.load_succeed);
            tvLoading.setText("加载成功");
            if (null != rotate && !rotate.hasEnded()) {
               rotate.cancel();
            }
            break;
         case ERROR:
            imgLoading.setImageResource(R.mipmap.load_failed);
            tvLoading.setText("加载失败");
            if (null != rotate && !rotate.hasEnded()) {
               rotate.cancel();
            }
            break;
         case LOADING:
            rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                  0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(500);
            rotate.setInterpolator(new LinearInterpolator(context, null));
            rotate.setRepeatCount(Animation.INFINITE);
            imgLoading.setImageResource(R.mipmap.loading_2);
            imgLoading.startAnimation(rotate);
            tvLoading.setText("正在加载更多...");
            break;
         case NOMORE:
            imgLoading.setImageResource(R.mipmap.load_failed);
            tvLoading.setText("没有更多数据");
            if (null != rotate && !rotate.hasEnded()) {
               rotate.cancel();
            }
            break;
      }
   }

   @Override
   public int getItemViewType(int position) {
      if (headerMap.size() > 0 && position <= headerMap.size() - 1) {//Header
         return headerIndex.get(position);
      } else if (loadMoreEnable && position == getItemCount() - 1) {
         return TYPE_LOAD_MORE;
      } else if (position >= headerMap.size() + dataList.size() && footerMap.size() > 0) {//Footer
         return footerIndex.get(position - dataList.size() - headerMap.size());
      } else {
         return TYPE_NORMAL;
      }
   }

   @Override
   public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      if (headerIndex.contains(viewType)) {
         return  new BaseRecyclerHolder(/*viewType,*/ headerMap.get(viewType));
      } else if (footerIndex.contains(viewType)) {
         return  new BaseRecyclerHolder(/*viewType, */footerMap.get(viewType));
      } else if (TYPE_LOAD_MORE == viewType) {
         loadMoreView = View.inflate(context, R.layout.load_more, null);
         return  new BaseRecyclerHolder(/*viewType, */loadMoreView);
      } else {
         return onCreateRecyclerViewHolder(parent, viewType);
      }
   }

   protected abstract BaseRecyclerHolder onCreateRecyclerViewHolder(ViewGroup parent, int viewType);

   @Override
   public void onBindViewHolder(BaseRecyclerHolder holder, int position) {
      if (position >= headerMap.size() && position < headerMap.size() + dataList.size()) {
         onBindRecyclerViewHolder( holder, position);
      }
      if (loadMoreEnable && position == getItemCount() - 1) {
         setLoadMoreState(LoadState.LOADING);
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View view) {
               switch (i % 5) {
                  case 0:
                     setLoadMoreState(LoadState.NOMORE);
                     break;
                  case 1:
                     setLoadMoreState(LoadState.NORMAL);
                     break;
                  case 2:
                     setLoadMoreState(LoadState.SUCCESS);
                     break;
                  case 3:
                     setLoadMoreState(LoadState.ERROR);
                     break;
                  case 4:
                     setLoadMoreState(LoadState.LOADING);
                     break;
               }
               i++;
            }
         });
      }
   }

   protected abstract void onBindRecyclerViewHolder(BaseRecyclerHolder holder, int position);

   @Override
   public int getItemCount() {
//      return loadMoreEnable ? dataList.size() + headerMap.size() + footerMap.size() + 1 : dataList.size() + headerMap.size() + footerMap.size();
      return loadMoreEnable ? getRecyclerItemCount() + headerMap.size() + footerMap.size() + 1 : getRecyclerItemCount() + headerMap.size() + footerMap.size();
   }
   protected abstract int getRecyclerItemCount();


   @Override
   public void onAttachedToRecyclerView(RecyclerView recyclerView) {
      super.onAttachedToRecyclerView(recyclerView);
      RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
      if (manager instanceof GridLayoutManager) {
         final GridLayoutManager gridManager = ((GridLayoutManager) manager);
         gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
               if (loadMoreEnable) {
                  return (position < headerMap.size() || position >= (getItemCount() - footerMap.size() - 1)) ? gridManager.getSpanCount() : 1;
               } else {
                  return (position < headerMap.size() || position >= (getItemCount() - footerMap.size())) ? gridManager.getSpanCount() : 1;
               }
            }
         });
      }
   }

   @Override
   public void onViewAttachedToWindow(BaseRecyclerHolder holder) {
      super.onViewAttachedToWindow(holder);
      ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
      if (loadMoreEnable && holder.getLayoutPosition() == getItemCount() - 1) {
         lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
      }
      if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
         StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
         if (loadMoreEnable) {
            p.setFullSpan(holder.getLayoutPosition() < headerMap.size()
                  || holder.getLayoutPosition() >= (getItemCount() - footerMap.size() - 1));
         } else {
            p.setFullSpan(holder.getLayoutPosition() < headerMap.size()
                  || holder.getLayoutPosition() >= (getItemCount() - footerMap.size()));
         }
      }
   }

}

class BaseRecyclerHolder extends RecyclerView.ViewHolder {
   public BaseRecyclerHolder( View itemView) {
      super(itemView);
   }
}