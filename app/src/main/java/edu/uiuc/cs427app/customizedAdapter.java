package edu.uiuc.cs427app;

import static edu.uiuc.cs427app.MainActivity.INTENT_THEME_NAME;
import static edu.uiuc.cs427app.R.id.mBtn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class customizedAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    private List<String> data;
    private Intent intent;
    public static final String SHOW_MAP = "Show Map";
    public static final String SHOW_WEATHER = "Show Weather";


    public customizedAdapter(List<String> data, Context context, Intent intent) {
        this.context = context;
        this.data = data;
        this.intent = intent;
    }

    /**
     * @param data the city list
     * reset the city list after add/remove city
     */
    public void refresh(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<String> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * @param i the ith fragment item in list
     * @param view pass in the view
     * @param viewGroup pass in the view group
     * set the tag, text and listener for each button
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if(context == null)
            context = viewGroup.getContext();
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mTv = (TextView)view.findViewById(R.id.mTv);
            viewHolder.mBtn = (Button)view.findViewById(mBtn);
            viewHolder.mBtnMap = (Button)view.findViewById(R.id.mBtnMap);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder)view.getTag();

        viewHolder.mBtn.setTag(mBtn,i);
        viewHolder.mBtn.setOnClickListener(this);
        viewHolder.mBtn.setId(mBtn - i);

        viewHolder.mBtnMap.setTag(R.id.mBtnMap, i);
        viewHolder.mBtnMap.setOnClickListener(this);
        viewHolder.mBtnMap.setId(R.id.mBtnMap + i);

        viewHolder.mTv.setText(data.get(i));
        viewHolder.mTv.setTag(R.id.mTv,i);
        viewHolder.mTv.setOnClickListener(this);
        return view;
    }

    /**
     * when button gets clicked, pop up events
     */
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        boolean darkThemeChecked = intent.getBooleanExtra(INTENT_THEME_NAME, false);
        Button button = (Button) view;
        switch (button.getText().toString()){
            case SHOW_MAP:
                int i = (int) view.getTag(R.id.mBtnMap);

                Intent intent=new Intent(context, MapActivity.class);
                intent.putExtra("userName", this.intent.getStringExtra("userName"));
                intent.putExtra("theme", darkThemeChecked);
                intent.putExtra("city", this.data.get(i));
                context.startActivity(intent);
                break;
            case SHOW_WEATHER:
                int m = (int) view.getTag(mBtn);
                intent = new Intent(context, WeatherActivity.class);
                intent.putExtra("userName", this.intent.getStringExtra("userName"));
                intent.putExtra("theme", darkThemeChecked);
                intent.putExtra("city", this.data.get(m));
                context.startActivity(intent);
                break;
        }
    }

    /**
     * bind button and text
     */
    static class ViewHolder{
        TextView mTv;
        Button mBtn;
        Button mBtnMap;
    }
}
