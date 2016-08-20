package com.example.prince.recyclerheadfoots;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   private static final String TAG = "MainActivity";
   private RecyclerView recycler;
   private BaseRecyclerAdapter adapter;
   private Context context;
   private List<String> dataList;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      context = this;
      setContentView(R.layout.activity_main);
      inits();

      recycler.setLayoutManager(new LinearLayoutManager(context));
      adapter = new MyRecyclerAdapter(context,dataList);
      recycler.setAdapter(adapter);

   }

   private void inits() {
      recycler = (RecyclerView) findViewById(R.id.recycler);
      findViewById(R.id.tvAddFoot).setOnClickListener(this);
      findViewById(R.id.tvAddHead).setOnClickListener(this);
      findViewById(R.id.tvRemoveFoot).setOnClickListener(this);
      findViewById(R.id.tvRemoveHead).setOnClickListener(this);
      findViewById(R.id.tvList).setOnClickListener(this);
      findViewById(R.id.tvGrid).setOnClickListener(this);
      findViewById(R.id.tvStaggered).setOnClickListener(this);
      dataList = Arrays.asList("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20".split(" "));
   }

   private int headerIndex = 0;
   private int footerIndex = 1000;

   @Override
   public void onClick(View view) {
      switch (view.getId()) {
         case R.id.tvAddHead:
            View header = View.inflate(context, R.layout.header, null);
            TextView tv = (TextView) header.findViewById(R.id.tv);
            tv.setText("Header index = " + headerIndex);
            adapter.addHeader(headerIndex++, header);
            break;
         case R.id.tvRemoveHead:
            adapter.removeHeader(--headerIndex);
            break;
         case R.id.tvAddFoot:
            View footer = View.inflate(context, R.layout.footer, null);
            TextView tv1 = (TextView) footer.findViewById(R.id.tv);
            tv1.setText("Footer Index = " + footerIndex);
            adapter.addFooter(footerIndex++, footer);
            break;
         case R.id.tvRemoveFoot:
            adapter.removeFooter(footerIndex--);
            break;
         case R.id.tvList:
            recycler.setLayoutManager(new LinearLayoutManager(context));
            recycler.setAdapter(adapter);
            break;
         case R.id.tvGrid:
            recycler.setLayoutManager(new GridLayoutManager(context, 2));
            recycler.setAdapter(adapter);
            break;
         case R.id.tvStaggered:
            recycler.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            recycler.setAdapter(adapter);
            break;
      }
   }
}
