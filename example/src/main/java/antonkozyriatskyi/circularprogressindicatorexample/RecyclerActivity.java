package antonkozyriatskyi.circularprogressindicatorexample;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by helmine on 2017-02-09.
 */

public class RecyclerActivity extends Activity implements AdapterView.OnItemClickListener{
    RecyclerView myRecycler;
    MyDatabase db;
    MyAdapter myAdapter;
    MyHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        myRecycler = (RecyclerView) findViewById(R.id.recycler);

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        Cursor cursor = db.getData();

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.TYPE);
//        int index3 = cursor.getColumnIndex(Constants.LOCATION);
//        int index4 = cursor.getColumnIndex(Constants.LATIN);

        ArrayList<String> mArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String plantName = cursor.getString(index1);
            String plantType = cursor.getString(index2);
//            String locationType = cursor.getString(index3);
//            String latinType = cursor.getString(index4);
            String s = plantName +"," + plantType;
            mArrayList.add(s);
            cursor.moveToNext();
        }

        myAdapter = new MyAdapter(mArrayList);
        myRecycler.setAdapter(myAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout clickedRow = (LinearLayout) view;
        TextView plantNameTextView = (TextView) view.findViewById(R.id.plantNameEntry);
        TextView plantTypeTextView = (TextView) view.findViewById(R.id.plantTypeEntry);
//        TextView locationTypeTextView = (TextView) view.findViewById(R.id.locationTypeEntry);
//        TextView latinTypeTextView = (TextView) view.findViewById(R.id.latinNameTypeEntry);
        Toast.makeText(this, "row " + (1+position) + ":  " + plantNameTextView.getText() +" "+plantTypeTextView.getText(), Toast.LENGTH_LONG).show();
    }
}