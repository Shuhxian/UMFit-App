package com.healthy.umfit;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthy.umfit.entity.Patient;
import com.healthy.umfit.utils.OkHttpClientConnection;
import com.healthy.umfit.utils.SharedPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.healthy.umfit.TagName.KEY_PATIENT;
import static com.healthy.umfit.TagName.hostUrl;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotificationsFragment extends Fragment {
    private static final String TAG = NotificationsFragment.class.getSimpleName();

    private SharedPreferencesManager sharedPrefObj;
    private OkHttpClientConnection okHttpClientConnectionObj;
    private Handler handlerObj;
    private Runnable runnableObj;

    private SwipeRefreshLayout srlNotifications, srlNoNotifications;
    private RecyclerView rvNotifications;
    private RelativeLayout rlNotifications;
    private TextView tvNoResultFound;

    private ArrayList<Patient> notificationList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefObj = new SharedPreferencesManager(Objects.requireNonNull(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        findViewById(rootView);
        setOnClick();

        return rootView;
    }

    private void findViewById(View rootView) {
        srlNotifications = rootView.findViewById(R.id.srl_notifications);
        srlNoNotifications = rootView.findViewById(R.id.srl_no_notifications);
        rvNotifications = rootView.findViewById(R.id.rv_notifications);
        rlNotifications = rootView.findViewById(R.id.rl_no_notifications);
        tvNoResultFound = rootView.findViewById(R.id.tv_no_result_found);

        try {
            srlNotifications.setRefreshing(true);
            String url = hostUrl + KEY_PATIENT + "all/5d023f99e2209604bea21a3cs";
            runHttp(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOnClick() {

    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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

    private void runHttp(String url) throws IOException {
        okHttpClientConnectionObj = new OkHttpClientConnection();

        okHttpClientConnectionObj.getRequestWithHeader(url);

        callHandler();

    }

    private void callHandler() {
        handlerObj = new Handler();

        runnableObj = new Runnable() {
            public void run() {
                try {
                    if (notificationList.size() > 0) {
                        handlerObj.removeCallbacks(runnableObj);
                        srlNotifications.setRefreshing(false);
                        displayData();
                    } else {
                        String result = okHttpClientConnectionObj.getResult();
                        Log.d(TAG, "result: " + result);
                        if (!result.equalsIgnoreCase("")) {
                            handleData(result);
                        } else {
                            handlerObj.postDelayed(runnableObj, 5000);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        handlerObj.postDelayed(runnableObj, 5000);
    }

    private void handleData(String result) {
        try {
            JSONObject joResult = new JSONObject(result);
            if(joResult.has("data")) {
                String data = joResult.getString("data");

                if (data != null && !data.equalsIgnoreCase("")) {
                    JSONArray jaWorkout = new JSONArray(data);
                    if (jaWorkout.length() > 0) {
                        for (int i = 0; i < jaWorkout.length(); i++) {
                            JSONObject joWorkout = jaWorkout.getJSONObject(i);

                            Patient patientObj = new Patient(joWorkout);
                            notificationList.add(patientObj);

//                sharedPrefObj.updatePref();

                        }

                        srlNotifications.setVisibility(View.VISIBLE);
                        srlNoNotifications.setVisibility(View.GONE);
                    } else {
                        srlNotifications.setVisibility(View.GONE);
                        srlNoNotifications.setVisibility(View.VISIBLE);

                    }
                }
            }else{
                srlNotifications.setVisibility(View.GONE);
                srlNoNotifications.setVisibility(View.VISIBLE);
            }

            if (handlerObj != null && runnableObj != null) {
                handlerObj.postDelayed(runnableObj, 5000);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvNotifications.setLayoutManager(linearLayoutManager);
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(notificationList);
        rvNotifications.setAdapter(notificationsAdapter);
    }

    public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
        ArrayList<Patient> notificationList;

        public NotificationsAdapter(ArrayList<Patient> notificationList) {
            this.notificationList = notificationList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // infalte the item Layout
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notification_item, parent, false);
            // set the view's size, margins, paddings and layout parameters
            NotificationsAdapter.MyViewHolder vh = new NotificationsAdapter.MyViewHolder(v); // pass the view to View Holder
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Patient patientObj = notificationList.get(position);
            // set the data in items
            holder.tvTitle.setText(patientObj.getTodayStepsCount());
            holder.tvMessage.setText(patientObj.getTodayCaloriesBurned());
            holder.tvDateTime.setText(patientObj.getLastUpdatedCaloriesDateTime());
            // implement setOnClickListener event on item view.
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // display a toast with person name on item click
//                    Toast.makeText(context, personNames.get(position), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private CardView cvNotification;
            private LinearLayout llNotification;
            private TextView tvTitle;
            private TextView tvMessage;
            private TextView tvDateTime;


            public MyViewHolder(View itemView) {
                super(itemView);
                // get the reference of item view's
                cvNotification = itemView.findViewById(R.id.cv_notification);
                llNotification = itemView.findViewById(R.id.ll_notification);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvMessage = itemView.findViewById(R.id.tv_message);
                tvDateTime = itemView.findViewById(R.id.tv_date_day);
            }
        }
    }
}
