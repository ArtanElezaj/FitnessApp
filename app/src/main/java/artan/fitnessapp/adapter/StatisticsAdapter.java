package artan.fitnessapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import artan.fitnessapp.R;
import artan.fitnessapp.UserDetails;
import io.realm.RealmResults;


public class StatisticsAdapter extends RecyclerView.Adapter<Holder> {
    private LayoutInflater mInflater;

    RealmResults<UserDetails> mResult;

    public static ArrayList<String> generateItems() {

        ArrayList<String> values = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            values.add(i + "");
        }

        return values;
    }

    public StatisticsAdapter(Context context, RealmResults<UserDetails> results) {

        mInflater = LayoutInflater.from(context);
        mResult = results;
        Log.e("TEST11111", mResult.toString());
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_item_rv, parent, false);
        Holder holder = new Holder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        UserDetails userDetails = mResult.get(position);

        //the next 3 lines converts miliseconds from our database to a readable date format
        long dbTimeMilliseconds = userDetails.getTimeAdded();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(dbTimeMilliseconds);

        holder.mTvSteps.setText(userDetails.getSteps() + "");
        holder.mTvDistance.setText(userDetails.getDistanceFeet() + "");
        holder.mTvTime.setText(sdf.format(resultdate) + "");
    }

    @Override
    public int getItemCount() {
        return mResult.size();
    }
}
