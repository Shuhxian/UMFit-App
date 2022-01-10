package com.healthy.umfit;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthy.umfit.entity.Activity;
import com.healthy.umfit.entity.Sport;
import com.healthy.umfit.entity.User;
import com.healthy.umfit.utils.SharedPreferencesManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


import static com.healthy.umfit.TagName.KEY_ACTIVITIES;
import static com.healthy.umfit.TagName.KEY_EXERCISES;
import static com.healthy.umfit.TagName.KEY_LOGIN;
import static com.healthy.umfit.TagName.KEY_PREF_USER;
import static com.healthy.umfit.TagName.KEY_SPORTS;
import static com.healthy.umfit.TagName.KEY_USER;
import static com.healthy.umfit.TagName.hostUrl;
import static com.healthy.umfit.utils.CommonUtilities.getCurrentDateByPattern;
import static com.healthy.umfit.utils.CommonUtilities.getCurrentTimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class WorkoutFragment extends Fragment {
    private static final String TAG = WorkoutFragment.class.getSimpleName();

    private SharedPreferencesManager sharedPrefObj;

    private SwipeRefreshLayout srlWorkout, srlNoWorkout;
    private RecyclerView rvWorkout;
    private RecyclerView rvActivities;
    private RelativeLayout rlNoWorkout;
    private TextView tvNoResultFound;

    private ArrayList<Sport> sportList = new ArrayList<>();

    private User userObj;

    private OnFragmentInteractionListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefObj = new SharedPreferencesManager(Objects.requireNonNull(getActivity()));
        AndroidNetworking.initialize(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        findViewById(rootView);

        srlWorkout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isAdded()) {
                    updatePageData();
                }
            }
        });

        return rootView;
    }

    private void findViewById(View rootView) {
        srlWorkout = rootView.findViewById(R.id.srl_workout);
        srlNoWorkout = rootView.findViewById(R.id.srl_no_workout);
        rvWorkout = rootView.findViewById(R.id.rv_workout);
        rvActivities = rootView.findViewById(R.id.rv_activities);
        rlNoWorkout = rootView.findViewById(R.id.rl_no_workout);
        tvNoResultFound = rootView.findViewById(R.id.tv_no_result_found);

        userObj = new User(sharedPrefObj.getPref(KEY_LOGIN));

        updatePageData();

        srlNoWorkout.setVisibility(View.VISIBLE);
        srlWorkout.setVisibility(View.GONE);
        srlWorkout.setRefreshing(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void updatePageData(){

        Log.d(TAG, "User object is: " + userObj.getUserToken());
        Log.d(TAG, "URL is " + hostUrl + KEY_EXERCISES);

        AndroidNetworking.get(hostUrl + KEY_EXERCISES)
                .addHeaders("Authorization", "Bearer " + userObj.getUserToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "workout api result is: " + response.toString());
                        sportList.clear();
                        for (int i =0; i< response.length(); i++){
                            try {
                                sportList.add(new Sport(response.getJSONObject(i)));
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                        displayWorkOutData();
                        srlNoWorkout.setVisibility(View.GONE);
                        srlWorkout.setVisibility(View.VISIBLE);
                        srlWorkout.setRefreshing(false);
                    }
                    @Override
                    public void onError(ANError error) {
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());

                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            Log.d(TAG, "onError bodysi : " + error.getErrorBody());
                        }
                    }
                });
        srlWorkout.setRefreshing(false);
    }


    private void displayWorkOutData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvWorkout.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        WorkoutAdapter workoutAdapter = new WorkoutAdapter(sportList);
        workoutAdapter.notifyDataSetChanged();
        rvWorkout.setAdapter(workoutAdapter);
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.MyViewHolder> {
        ArrayList<Sport> sportsList;

        public WorkoutAdapter(ArrayList<Sport> sportsList) {
            this.sportsList = sportsList;
        }

        @Override
        public WorkoutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_workout_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            WorkoutAdapter.MyViewHolder vh = new WorkoutAdapter.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            if (sportsList != null)
            {
                int sportDuration = 0;
                Sport exerciseObj = sportsList.get(position);

                if (exerciseObj.getSportTime() != null)
                {
                    sportDuration = Math.round(Integer.parseInt(exerciseObj.getSportTime()));
                }
                // set the data in items
                holder.tvTitle.setText(exerciseObj.getType());
                holder.tvAverageHeartRate.setText(getResources().getString(R.string.txt_heart_rate_value2).replace("[heart_rate]", exerciseObj.getAverageHeartRate()));
                holder.tvSportTime.setText(getResources().getString(R.string.txt_minute).replace("[minute]",String.valueOf(sportDuration)));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s'Z'");
                Date date;
                try {
                    date = format.parse(exerciseObj.getTimestamp());
                    System.out.println(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = new Date();
                }

                SimpleDateFormat dayFormatter = new SimpleDateFormat("E"); // the format of your date
                dayFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                SimpleDateFormat timeFormatter = new SimpleDateFormat("h:ma"); // the format of your date
                timeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

                holder.tvDateTime.setText(timeFormatter.format(date));
                holder.tvDateDay.setText(dayFormatter.format(date));
            }
        }

        @Override
        public int getItemCount() {
            return sportsList .size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private CardView cvWorkout;
            private LinearLayout llWorkout;
            private TextView tvTitle;
            private TextView tvAverageHeartRate;
            private TextView tvSportTime;
            private TextView tvDateTime;
            private TextView tvDateDay;


            public MyViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's
                cvWorkout = itemView.findViewById(R.id.cv_workout);
//                llWorkout = itemView.findViewById(R.id.ll_workout);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvAverageHeartRate = itemView.findViewById(R.id.tv_average_heart_rate);
                tvSportTime = itemView.findViewById(R.id.tv_sport_time);
                tvDateTime = itemView.findViewById(R.id.tv_date_time);
                tvDateDay = itemView.findViewById(R.id.tv_date_day);
            }
        }
    }
}
