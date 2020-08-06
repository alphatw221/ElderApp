package com.elderApp.ElderApp.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elderApp.ElderApp.Model_Class.Event_class;
import com.elderApp.ElderApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {       //本類別extends的Adapter<>型別給入 本類別的子類別(MyViewHolder)
    private String[] districts={"0","桃園","中壢","平鎮","八德","龜山","蘆竹","大園","觀音","新屋","楊梅","龍潭","大溪","復興"};
    private String[] categories={"0","1","2","3","4","歡樂旅遊","樂活才藝","健康課程","8","社會服務","天使培訓","長照據點","大型活動"};
    private List<Event_class> mDataset;             // 先宣告儲存資料的變數
    private List<Event_class> mDataset_copy;
    private IMyOnClickListener G_myOnClickListener;


    public interface IMyOnClickListener{
        void myOnClick(int position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {      //宣告子類別 MyViewHoler 暫存view的靜態類別
        public View view;
        IMyOnClickListener myOnClickListener;
        public MyViewHolder(@NonNull View v,IMyOnClickListener myOnClickListener) {
            super(v);
            view=v;
            this.myOnClickListener=myOnClickListener;
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            myOnClickListener.myOnClick(getAdapterPosition());
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Event_class> myDataset , IMyOnClickListener myOnClickListener) {
        mDataset = myDataset;
        mDataset_copy=new ArrayList<>();
        mDataset_copy.addAll(myDataset);
        this.G_myOnClickListener=myOnClickListener;
    }   //本類別的建構函數 把資料丟入變數



    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,            //複寫RecyclerView.Adapter內的方法  當建造viewholder的動作
                                           int viewType) {
                                                                    // 建立view 從要的layout填充進來
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_listview_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v,G_myOnClickListener);                  //裝進viewholder
        return vh;                                              //回傳viewholder
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {       //複寫RecyclerView.Adapter內的方法  當綁定viewholder內view資料 的動作

        ImageView imageView=holder.view.findViewById(R.id._event_image);
        TextView event_title=holder.view.findViewById(R.id._event_title);
        TextView event_category_location=holder.view.findViewById(R.id._event_category_location);
        TextView event_happybi=holder.view.findViewById(R.id._event_happybi);
//        TextView event_location=holder.view.findViewById(R.id._event_location);
        TextView event_people=holder.view.findViewById(R.id._event_people);
        TextView event_dateTime=holder.view.findViewById(R.id._event_dateTime);

        String url=mDataset.get(position).imgUrl;
        Picasso.get().load(url).into(imageView);
        event_title.setText(mDataset.get(position).name);
        event_category_location.setText(mDataset.get(position).cat+"("+mDataset.get(position).district+")");
//        event_location.setText(mDataset.get(position).location);
        event_people.setText(Integer.toString(mDataset.get(position).people)+"/"+Integer.toString(mDataset.get(position).maximum));
        event_happybi.setText(Integer.toString(mDataset.get(position).reward)+"獎勵");
        if(mDataset.get(position).dateTime==null){
            event_dateTime.setText("");
        }else{
            event_dateTime.setText(mDataset.get(position).dateTime);
        }

    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }



//    public void filter(String text,int category,int district) {
//        int size=mDataset_copy.size();
//        List<Event_class> temp =new ArrayList<>();
//
//        mDataset.clear();
//        mDataset.addAll(mDataset_copy);
//        if(text.isEmpty()&&district==0&&category==0){
//            //不做事
//        } else {
//            if(!text.isEmpty()){
//                for(int i=0;i<size;i++){
//                    if(mDataset_copy.get(i).title.indexOf(text)<0 ){
//                        mDataset.remove(mDataset_copy.get(i));
//
//                    }
//                }
//            }
//            if(district!=0){
//                size=mDataset.size();
//                temp.clear();
//                temp.addAll(mDataset);
//                for(int i=0;i<size;i++){
//                    if(temp.get(i).district_id!=district ){
//                        mDataset.remove(temp.get(i));
//
//                    }
//                }
//            }
//
//            if(category!=0){
//                size=mDataset.size();
//                temp.clear();
//                temp.addAll(mDataset);
//                for(int i=0;i<size;i++){
//                    if(temp.get(i).category_id!=category ){
//                        mDataset.remove(temp.get(i));
//
//                    }
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
}
