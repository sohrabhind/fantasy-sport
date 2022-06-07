package com.hindbyte.redfun.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.hindbyte.redfun.R;
import com.hindbyte.redfun.library.SwipyRefreshLayout;
import com.hindbyte.redfun.library.SwipyRefreshLayoutDirection;
import com.hindbyte.redfun.matches.MatchAdapter;
import com.hindbyte.redfun.matches.MatchModel;
import com.hindbyte.redfun.utils.ToastWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchesFragment extends Fragment {

    private MatchAdapter matchAdapter;
    private List<MatchModel> matchList = new ArrayList<>();
    private Context mContext;
    private SwipyRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MatchModel matchModel;

    public static MatchesFragment newInstance() {
        MatchesFragment myFragment = new MatchesFragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        return rootView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        matchAdapter = new MatchAdapter(matchList, mContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, 0));
        recyclerView.setAdapter(matchAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        getTopFeed();
        swipeRefreshLayout.setOnRefreshListener(direction -> {
            if (direction == SwipyRefreshLayoutDirection.TOP) {
                getTopFeed();
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getTopFeed() {
        String tag_string_req = "req_reg";
        StringRequest strReq = new StringRequest(Request.Method.POST, "http://hindbyte.com/redfun/matches/matches.php", response -> {
            swipeRefreshLayout.setRefreshing(false);
            try {
                matchList.clear();
                matchAdapter.notifyDataSetChanged();
                JSONObject jObj = new JSONObject(response);
                JSONArray jArray = jObj.getJSONArray("item");
                for (int i=0; i < jArray.length(); i++) {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    String id = oneObject.getString("id");
                    String teamA = oneObject.getString("teamA");
                    String flagA = oneObject.getString("flagA");
                    String teamB = oneObject.getString("teamB");
                    String flagB = oneObject.getString("flagB");
                    String series = oneObject.getString("series");
                    String time = oneObject.getString("time");
                    String status = oneObject.getString("status");
                    matchModel = new MatchModel();
                    matchModel.setId(id);
                    matchModel.setSeries(series);
                    matchModel.setTeamA(teamA);
                    matchModel.setFlagA(flagA);
                    matchModel.setTeamB(teamB);
                    matchModel.setFlagB(flagB);
                    matchModel.setTime(time);
                    matchModel.setStatus(status);
                    matchModel.setViewType(1);
                    matchList.add(matchModel);
                    MatchAdapter.handlerMatch.removeCallbacksAndMessages(null);
                    matchAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            swipeRefreshLayout.setRefreshing(false);
            ToastWindow.makeText(mContext, "Internet/Server Problem: Please try after some time", 1500);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("status", "open");
                return params;
            }
        };
        strReq.setShouldCache(false);
        com.hindbyte.redfun.utils.Request.addToRequestQueue(mContext, strReq, tag_string_req);
    }
}