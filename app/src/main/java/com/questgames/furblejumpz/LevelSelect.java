package com.questgames.furblejumpz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class LevelSelect extends Activity {

    GridView levelButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select);

        levelButtons = (GridView) findViewById(R.id.levelGrid);
        levelButtons.setAdapter( new levelAdapter(this));
        levelButtons.setOnItemClickListener(new GridView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent mIntent = new Intent(LevelSelect.this, MainActivity.class);
                mIntent.putExtra("levelNum",position +1);
                startActivity(mIntent);
                finish();
            }
        });

    }
}

class levelAdapter extends BaseAdapter{
    private Context mContext;

    public levelAdapter(Context c){
        mContext = c;
    }
    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView iView;
        if (view == null){
            iView = new ImageView(mContext);
            iView.setLayoutParams(new GridView.LayoutParams(250, 250));
            iView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iView.setPadding(8, 8, 8, 8);

        }else {
            iView = (ImageView) view;
        }

        iView.setImageResource(mThumbIds[i]);
        return iView;
    }

    private Integer[] mThumbIds = {
            R.drawable.level1, R.drawable.level2,
            R.drawable.level3, R.drawable.level4,
            R.drawable.level5, R.drawable.level6,
            R.drawable.level7, R.drawable.level8,
            R.drawable.level9    };
    }










/*
        b.setOnClickListener(buttonClicker);

    }


    View.OnClickListener buttonClicker = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(GameOver.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }; */