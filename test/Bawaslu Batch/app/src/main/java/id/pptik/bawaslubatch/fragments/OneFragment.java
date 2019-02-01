//package id.pptik.bawaslubatch.fragments;
//
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListAdapter;
//import android.widget.SimpleAdapter;
//import android.widget.Toast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import id.pptik.bawaslubatch.Adapter.MyAdapter;
//import id.pptik.bawaslubatch.MainActivity;
//import id.pptik.bawaslubatch.R;
//import id.pptik.bawaslubatch.helpers.HttpHandler;
//import id.pptik.bawaslubatch.m_JSON.JSONDownloader;
//import id.pptik.bawaslubatch.networks.POJO.DataItem;
//import okhttp3.internal.Util;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class OneFragment extends Fragment{
//    String jsonURL="http://bawaslu-ftp.pptik.id:5000/Pemilu/32/report/TOC.json";
//    RecyclerView rv;
//    private List<DataItem> results = new ArrayList<DataItem>();
//    private MainActivity mainActivity;
//    private RecyclerView.Adapter mAdapter;
//    ArrayList<HashMap<String, String>> contactList;
//
//    private static String[] spacecrafts={"Pioneer","Voyager","Casini","Spirit","Challenger"};
//
//
//
//    public OneFragment() {
//        // Required empty public constructor
//    }
//
//    public static OneFragment newInstance(){
//        return new OneFragment();
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.all_report, null);
//        rv =(RecyclerView)rootView.findViewById(R.id.allReport);
////        contactList = new ArrayList<>();
//        mainActivity = ((MainActivity) getActivity());
//        rv.setLayoutManager(new LinearLayoutManager(((MainActivity)getActivity())));
////        new JSONDownloader(((MainActivity)getActivity()),jsonURL,rv);
//
//        // Inflate the layout for this fragment
//        new LoadData().execute();
//        getUser();
//        return rootView;
//    }
//    private class LoadData extends AsyncTask<Void,Void,JSONObject>{
//
//        @Override
//        protected JSONObject doInBackground(Void... voids) {
//            HttpHandler sh = new HttpHandler();
//            String url = "http://bawaslu-ftp.pptik.id:5000/Pemilu/32/report/TOC.json";
//            String jsonStr = sh.makeServiceCall(url);
//            Log.e("DataFafaf", "Response from url: " + jsonStr);
//            if (jsonStr!=null){
//                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    JSONArray rangeData = jsonObj.getJSONArray("data");
//                    for (int i = 0;i<rangeData.length();i++){
//                        JSONObject c = rangeData.getJSONObject(i);
//                        String time = c.getString("TIMESTAMP");
//                        String guid = c.getString("GUID");
//                        String pathJson = c.getString("FILENAME");
//                        Log.e("Data1Masuk", "Waktu : "+time);
//                        Log.e("Data2Masuk", "Waktu : "+guid);
//                        Log.e("Data3Masuk", "Waktu : "+pathJson);
//
//                        HashMap<String, String> contact = new HashMap<>();
//                        contact.put("times", time);
//                        contact.put("guids", guid);
//                        contact.put("paths", pathJson);
//
//
//                    }
//                }catch (Exception e){
//                    Log.e("exeDataFailde", "Response from url: " + e);
//
//                }
//
//            }else {
//                Log.e("GagalDatamasuk", "Couldn't get json from server.");
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            super.onPostExecute(jsonObject);
//            rv.setLayoutManager(new LinearLayoutManager(((MainActivity) getActivity())));
//
//            ListAdapter adapter = new SimpleAdapter(((MainActivity)getActivity()),contactList,R.layout.cardview_post,
//                    new String[]{"times","guids","paths"},new int[]{R.id.card_tgl,R.id.card_tlp,R.id.card_comment});
//            rv.setAdapter((RecyclerView.Adapter) adapter);
//        }
//    }
//
//    public void getUser(){
//        try {
//            Call<id.pptik.bawaslubatch.networks.POJO.Response> call = mainActivity.restServiceInterface.dataItem();
//            call.enqueue(new Callback<id.pptik.bawaslubatch.networks.POJO.Response>() {
//                @Override
//                public void onResponse(Call<id.pptik.bawaslubatch.networks.POJO.Response> call, Response<id.pptik.bawaslubatch.networks.POJO.Response> response) {
//                    Log.d("aduhh123", "onResponse: "+response.body().getData().size());
//
//                    if (response.body().getData().size()>0){
//                        for (int i=0 ; i<response.body().getData().size();i++){
//                            results.add(response.body().getData().get(i));
////                            mAdapter = new My
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<id.pptik.bawaslubatch.networks.POJO.Response> call, Throwable t) {
//                    Log.d("failed123", "onResponse: ");
//                }
//            });
//        }catch (Exception e){
//            Log.d("aduhh123", "failedfCounsme: "+e);
//        }
//    }
//
//}