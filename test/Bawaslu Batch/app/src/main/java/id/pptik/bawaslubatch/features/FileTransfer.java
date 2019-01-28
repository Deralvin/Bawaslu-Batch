package id.pptik.bawaslubatch.features;

import android.os.Environment;
import android.text.Editable;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class FileTransfer {

    Long tsLong = System.currentTimeMillis()/1000;

    String noTelp ="085224609423";
    String longitude ="-6.87499";
    String latitude = "107.5281";
    String prov = "32";
    String kota_kab ="73";
    String kel ="02";

    private static final String TAG = null;
    public boolean ftpConnect(String srcFilePath, String Imei){
        try {
            String mBitmap =null;
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("bawaslu-ftp.pptik.id");
            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                boolean status1 = ftpClient.login("pemilu","pemilu123!");
                ftpClient.enterLocalPassiveMode();
                Log.d("Connection success", "ftpConnect: berhasil status = "+status1);

                FileInputStream srcFileStream = new FileInputStream(srcFilePath);
                BufferedInputStream bis = new BufferedInputStream(srcFileStream);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                Gson gson = new Gson();
                String json = gson.toJson(srcFileStream);
                Log.d("FileName", srcFilePath);
                String ts = tsLong.toString();

                boolean  status = ftpClient.storeFile("Pemilu/"+prov+"/"+ts+"_"+Imei+".jpg", bis);


                JSONObject obj = new JSONObject();
                obj.put("Nama File",ts+"_"+Imei+".jpg");
                obj.put("Telephone",noTelp);
                obj.put("IMEI",Imei);
                obj.put("Provinsi",prov);
                obj.put("Kabupaten",kota_kab);
                obj.put("Kelurahan",kel);
                obj.put("Long",longitude);
                obj.put("Lat",latitude);
                obj.put("Komentar","rrsad");
                String onjTo=obj.toString();

                if (status ==true){
                    File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"tmp-bawasluCSV");
                    Log.d("Folder", "ftpConnect: "+fileDir);
                }else{
                    Log.d("RMQERROR", "ftpConnect: Error data RMQ");
                }
                bis.close();
                return status;
            }
        } catch (SocketException e) {
            Log.d("FTP1", "Error: could not connect to socket " + e );
        } catch (IOException e) {
            Log.d("FTP2", "Error: could not connect to host " + e );
        } catch (JSONException e) {
            Log.d("FTP3", "Error: could not connect to host " + e);
        }
        return false;
    }

}
