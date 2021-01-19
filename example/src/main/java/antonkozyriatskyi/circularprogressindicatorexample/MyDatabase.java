package antonkozyriatskyi.circularprogressindicatorexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by helmine on 2015-02-04.
 */
public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase (Context c){
        context = c;
        helper = new MyHelper(context);
    }

    public long insertData (String name, String type)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
//        contentValues.put(Constants.LOCATION, location);
//        contentValues.put(Constants.LATIN, latin);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    public Cursor getSelectedData(String type)
    {
        //select plants from database of type 'herb'
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE};

        String selection = Constants.TYPE + "='" +type+ "'";  //Constants.TYPE = 'type'
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
        return cursor;
//        StringBuffer buffer = new StringBuffer();
//        while (cursor.moveToNext()) {
//
//            int index1 = cursor.getColumnIndex(Constants.NAME);
//            int index2 = cursor.getColumnIndex(Constants.TYPE);
//            int index3 = cursor.getColumnIndex(Constants.LOCATION);
//            int index4 = cursor.getColumnIndex(Constants.LATIN);
//            String plantName = cursor.getString(index1);
//            String plantType = cursor.getString(index2);
//            String locationType = cursor.getString(index3);
//            String latinType = cursor.getString(index4);
//            buffer.append(plantName + " " + plantType + " "  + locationType + " " + latinType+ "\n");
//        }
//        Log.d("buffer", buffer.toString());
//        return buffer.toString();
    }

}
