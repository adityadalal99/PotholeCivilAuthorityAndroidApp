package com.example.potholecivilauthorityandroidapp.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.potholecivilauthorityandroidapp.Adapters.CasesRecyclerViewAdapter;
import com.example.potholecivilauthorityandroidapp.Helpers.NetworkHelper;
import com.example.potholecivilauthorityandroidapp.Interfaces.CaseApi;
import com.example.potholecivilauthorityandroidapp.Models.Case;
import com.example.potholecivilauthorityandroidapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Context context;

    RecyclerView casesRecyclerView;


    CasesRecyclerViewAdapter casesRecyclerViewAdapter;

    List<Case> caseList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        casesRecyclerView = view.findViewById(R.id.homecasesrecyclerviewid);

        casesRecyclerView.setHasFixedSize(true);
        casesRecyclerView.setLayoutManager(new LinearLayoutManager(context));


        Retrofit retrofit = NetworkHelper.getRetrofitInstance(context);

        final CaseApi caseApi = retrofit.create(CaseApi.class);

        Call<List<Case>> caseCall = caseApi.getCases();

        caseCall.enqueue(new Callback<List<Case>>() {
            @Override
            public void onResponse(Call<List<Case>> call, Response<List<Case>> response) {

                if(response.isSuccessful()){
                    if(response.body()!=null && response.body().size() > 0){


                        caseList = response.body();

                        casesRecyclerViewAdapter = new CasesRecyclerViewAdapter(context,HomeFragment.this,caseList);

                        casesRecyclerView.setAdapter(casesRecyclerViewAdapter);


                    }else{
                        Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Case>> call, Throwable t) {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0  && resultCode == Activity.RESULT_OK){

            if(data!=null && data.getExtras().get("data")!=null){

                Toast.makeText(context,"Called",Toast.LENGTH_LONG).show();

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                casesRecyclerViewAdapter.resolveCase(bitmap);
            }


        }
    }
}
