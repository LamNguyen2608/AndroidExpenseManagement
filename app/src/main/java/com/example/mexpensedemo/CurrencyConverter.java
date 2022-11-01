package com.example.mexpensedemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CurrencyConverter extends AppCompatDialogFragment {


    private ArrayList<String> currencyName;
    private AutoCompleteTextView enterFrom, enterTo;
    private EditText enterAmount;
    private final String apiKey = "FaPo0bqMMtMvmSnevGBH50gURMohefdp";
    private String str_from, str_to, endpoint;
    private View expenseView;
    String[] symbols = {"USD","EUR", "VND", "JPY", "KRW", "INR", "CAD", "SGD", "GBP"};


    public CurrencyConverter(View view) {
        // Required empty public constructor
        expenseView = view;
    }

    public interface OnInputListener {
        void sendInput(String input);
    }
    public OnInputListener mOnInputListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAttachToParentFragment(getParentFragment());
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_currency_converter, null);

        enterFrom = view.findViewById(R.id.dropdown_from_field);
        enterTo = view.findViewById(R.id.dropdown_to_field);
        enterAmount = view.findViewById(R.id.convert_amount);

        //Drop down value
        ArrayAdapter<String> arrayAdapterSymbols = new ArrayAdapter<String>(getActivity(), R.layout.list_item, symbols);
        enterFrom.setAdapter(arrayAdapterSymbols);
        enterTo.setAdapter(arrayAdapterSymbols);
        enterFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_from = adapterView.getItemAtPosition(i).toString();
                Log.d("From ==>", str_from);
            }
        });
        enterTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                str_to = adapterView.getItemAtPosition(i).toString();
                Log.d("To ==>", str_to);
            }
        });

        builder.setView(view)
                .setTitle("Edit this expense")
                .setPositiveButton("Convert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                          endpoint = "https://api.apilayer.com/exchangerates_data/convert?to="
                                + str_to + "&from="
                                + str_from +"&amount="
                                + enterAmount.getText().toString();
                          Log.d("url", "===>" + endpoint);
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        JsonObjectRequest convert = new JsonObjectRequest(
                                Request.Method.GET,
                                endpoint,
                                null,
                                new Response.Listener<JSONObject>()
                                {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // response
                                        Log.d("Response API", "==>" + response.toString());
                                        try {
                                            mOnInputListener.sendInput(String.valueOf(response.getDouble("result")));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("ERROR","error => "+error.toString());
                                    }
                                }
                        ){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  params = new HashMap<String, String>();
                                params.put("apikey", apiKey);
                                return params;
                            }
                        };
                        requestQueue.add(convert);
                    }
                });

        return builder.create();
    }

    public void onAttachToParentFragment(Fragment fragment)
    {
        try
        {
            mOnInputListener = (OnInputListener)fragment;

        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    fragment.toString() + " must implement OnPlayerSelectionSetListener");
        }
    }
}