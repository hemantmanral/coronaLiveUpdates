package com.example.coronaLiveUpdates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_CountryWise extends Fragment {

   private RecyclerView rvCovidCountry;

    ArrayList<CountryWise> covidCountries;
    private static  final String TAG = Fragment_CountryWise.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_countrywise,container,false);

        rvCovidCountry = root.findViewById(R.id.CovidCountry_RecyclerView);
        rvCovidCountry.setLayoutManager(new LinearLayoutManager((getActivity())));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCovidCountry.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.line_drawer));
        rvCovidCountry.addItemDecoration(dividerItemDecoration);

        getDataFromServer();
        return root;
    }

    private void showRecyclerView() {
        CountryAdapter countryAdapter = new CountryAdapter(covidCountries);
        rvCovidCountry.setAdapter(countryAdapter);
    }

    private void getDataFromServer() {
        String url = "https://corona.lmao.ninja/countries";

        covidCountries = new ArrayList<CountryWise>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.i(TAG, "onRespone: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            covidCountries.add(new CountryWise(
                                    data.getString("country"), data.getString("cases"),
                                    data.getString("todayCases"), data.getString("deaths"),
                                    data.getString("todayDeaths"), data.getString("recovered"),
                                    data.getString("active"), data.getString("critical")
                            ));
                        } showRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"onResponse" + error);
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
