package antonkozyriatskyi.circularprogressindicatorexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static antonkozyriatskyi.circularprogressindicatorexample.R.layout.row;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(row, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

        String[] results = (list.get(position).toString()).split(",");
        holder.nameTextView.setText(results[0]);
        holder.typeTextView.setText(results[1]);
//        holder.locationTextView.setText(results[2]);
//        holder.latinNameTextView.setText(results[3]);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;
        public TextView typeTextView;
//        public TextView locationTextView;
//        public TextView latinNameTextView;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            nameTextView = (TextView) itemView.findViewById(R.id.plantNameEntry);
            typeTextView = (TextView) itemView.findViewById(R.id.plantTypeEntry);
//            locationTextView = (TextView) itemView.findViewById(R.id.locationTypeEntry);
//            latinNameTextView = (TextView) itemView.findViewById(R.id.latinNameTypeEntry);


            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked " + ((TextView) view.findViewById(R.id.plantNameEntry)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
        }


    }
}
