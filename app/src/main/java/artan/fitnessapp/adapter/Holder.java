package artan.fitnessapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import artan.fitnessapp.R;


public class Holder extends RecyclerView.ViewHolder {
    TextView mTvSteps, mTvDistance, mTvTime;

    public Holder(View itemView) {
        super(itemView);

        mTvSteps = (TextView) itemView.findViewById(R.id.tv_steps);
        mTvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
        mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
    }
}
