package id.pptik.bawaslubatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import id.pptik.bawaslubatch.features.FileTransfer;
import id.pptik.bawaslubatch.features.PreviewCapture;
import id.pptik.bawaslubatch.helpers.CameraUtilsReport;
import id.pptik.bawaslubatch.models.Post;
import id.pptik.bawaslubatch.view_models.PostAdapter;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;
public class MainActivity extends AppCompatActivity {


    private FloatingActionButton btnCapturePicture;

    private List<Post> postList = new ArrayList<>();
    private RecyclerView recyclerView;
    FileTransfer fileTransfer =new FileTransfer();
    private PostAdapter pAdapter;

    private String csv_download_url = "http://filehosting.pptik.id/Bawaslu-Ftp-Testing/32/73/02/3273021547479080_990000862471858_351756051523997.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String randomUID = UUID.randomUUID().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor =pref.edit();
        editor.putString("GUID",randomUID);
        editor.commit();

        btnCapturePicture = findViewById(R.id.btnPhoto);
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PreviewCapture.class);
                startActivity(intent);
            }
        });
//        File root = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/tmpBanwasl/Testing.csv");
//        String src="Pemilu/32/Report.csv";
//        try {
//            FileTransfer fileTransfer = new FileTransfer();
//            fileTransfer.downloadAndSaveFile("167.205.7.21", 21, "pemilu", "pemilu123!", "/Pemilu/test.csv", new File("test.csv"));
//        } catch (Exception e) {
//            Log.d("LaparDude", "onCreate: "+e.getMessage());
//            Toast.makeText(this, "ERROR KK KU"+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
new argh().execute();


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

}
