package id.pptik.bawaslubatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import id.pptik.bawaslubatch.Adapter.DataAdapter;
import id.pptik.bawaslubatch.features.FileTransfer;
import id.pptik.bawaslubatch.features.PreviewCapture;
import id.pptik.bawaslubatch.helpers.CSVReaders;
import id.pptik.bawaslubatch.helpers.CameraUtilsReport;
import id.pptik.bawaslubatch.helpers.HttpHandler;
import id.pptik.bawaslubatch.models.Post;
import id.pptik.bawaslubatch.models.dataContent;
//import id.pptik.bawaslubatch.view_models.PostAdapter;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;


public class MainActivity extends AppCompatActivity {

    InputStream inputStream;
    String[] data;
    private FloatingActionButton btnCapturePicture;

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    FileTransfer fileTransfer =new FileTransfer();
//    private PostAdapter pAdapter;
    List<dataContent> listcontent=new ArrayList<>();
    private String csv_download_url = "http://filehosting.pptik.id/Bawaslu-Ftp-Testing/32/73/02/3273021547479080_990000862471858_351756051523997.csv";
    public dataContent content;
    private DataAdapter adapter;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.photostream);

//        pAdapter = new PostAdapter(postList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(pAdapter);


        btnCapturePicture = findViewById(R.id.btnPhoto);
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PreviewCapture.class);
                startActivity(intent);
            }
        });


//        new argh().execute();
//        List<String[]> rows = new ArrayList<>();
//       CSVReaders csvReader = new CSVReaders(getApplicationContext(),Environment.getExternalStorageDirectory()+"/tmpBanwasl/DataReport.csv");
//        try {
//            rows = csvReader.readCSV();
//        } catch (IOException e) {
//            Log.d("FormatDatass", "oii"+e.getMessage());
//        }catch (Exception e){
//            Log.d("FormatDatas", "oii"+e.getMessage());
//        }
//
//        for (int i = 0; i < rows.size(); i++) {
//            Log.d("FormatData", String.format("row %s: %s, %s", i, rows.get(i)[0], rows.get(i)[1]));
//        }
new GetContacts().execute();
    }

    public class argh extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            FileTransfer fileTransfer = new FileTransfer();
            boolean status = false;
            try {
                status = fileTransfer.downloadAndSaveFile("167.205.7.21", 21, "pemilu", "pemilu123!", "/Pemilu/32/DataReport.csv", new File(Environment.getExternalStorageDirectory()+"/tmpBanwasl/DataReport.csv"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return status;

        }
    }

    public class downloadData extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("bawaslu-ftp.pptik.id:5000/Pemilu/32/TOC.json");
            Log.e("WorthIt", "Response from url: " + jsonStr);

            if (jsonStr !=null){
                try {
                    JSONObject obj = new JSONObject(jsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
    }
    public class GetContacts extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("bawaslu-ftp.pptik.id:5000/Pemilu/32/report/TOC.json");

            Log.e("WorthTIsa", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts =jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        content.komentar=c.getString("komentar");
                        content.timestamp=c.getString("timestamp");

                        content.tlp1=c.getString("te");

                        listcontent.add(content);
                    }
                } catch (final JSONException e) {
                    Log.e("MakanBang", "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e("GAGAL", "Couldn't get json from server.");


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


            /**
             * Updating parsed JSON data into ListView
             * */

            recyclerView=findViewById(R.id.photostream);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter=new DataAdapter(getApplicationContext(),listcontent);
            recyclerView.setAdapter(adapter);
        }

    }

}
