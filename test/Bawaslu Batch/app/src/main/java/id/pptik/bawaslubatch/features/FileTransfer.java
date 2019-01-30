package id.pptik.bawaslubatch.features;

import android.content.Intent;
import android.os.Environment;
import android.text.Editable;
import android.util.Log;

import com.opencsv.CSVWriter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import id.pptik.bawaslubatch.helpers.SendToRMQ;

public class FileTransfer {
    SendToRMQ sendToRMQ =new SendToRMQ();
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "Nama,File Foto,Tlp,Provinsi,Reg Timestamp,Imei";

    Long tsLong = System.currentTimeMillis()/1000;

    private static final String TAG = null;
    public boolean ftpConnect(String srcFilePath, String Imei, Editable nama, Editable txtTelpText, String kodeProv){
        try {
            String[] prov = kodeProv.split("-");

            File root = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/tmpBanwasl");
            CSVWriter writer =null;
            try {
                if (root.mkdir()){
                    Log.d("FolderDone", "Succes Create Folder: "+root);
                }else{
                    Log.d("FolderDone", "Succes exist Folder: "+root);
                }
            }catch (Exception e){
                Log.d("Arrgghghg", "ftpConnect: "+e);
            }


            String names = String.valueOf(nama);
            String telp = "-"+String.valueOf(txtTelpText);
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
                String ts = tsLong.toString();


                String csv = root+"/Reg-"+ts+"-"+Imei+".csv";
                writer = new CSVWriter(new FileWriter(csv));

                List<String []> data = new ArrayList<String[]>();
                data.add(new String []{"Nama","File Foto","Tlp","Provinsi","Reg Timestamp","Imei"});
                data.add(new String []{names,"Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg",telp,ts,"-"+Imei});

                writer.writeAll(data);
                writer.close();

                FileInputStream sc = new FileInputStream(csv);
                BufferedInputStream bif = new BufferedInputStream(sc);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                JSONObject obj = new JSONObject();
                obj.put("Nama",names);
                obj.put("File_ktp","Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg");
                obj.put("Tlp",telp);
                obj.put("Reg_TimeStamp",ts);
                obj.put("Imei",Imei);

                sendToRMQ.sendRMQFan(obj.toString());
                boolean  status = ftpClient.storeFile("Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg", bis);
                boolean result = ftpClient.storeFile("Pemilu/"+prov[0]+"/Reg-"+ts+"-"+Imei+".csv",bif);

////                boolean sendData =sendCSV(data);
//                Log.d("StoreCSVExist", "ftpConnect: "+sendData);

                bif.close();
                bis.close();


                return status;
            }
        } catch (SocketException e) {
            Log.d("FTP1", "Error: could not connect to socket " + e );
        } catch (IOException e) {
            Log.d("FTP2", "Error: could not connect to host " + e );
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("FTP3", "Error: could not connect to host " + e );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean sendCSV(String src){
        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect("bawaslu-ftp.pptik.id");

            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                boolean status1 = ftpClient.login("pemilu","pemilu123!");
                ftpClient.enterLocalPassiveMode();
                Log.d("ConnectionSuccessCSV", "ftpConnect: berhasil status = "+status1);



            }
        }catch (Exception e){

        }

        return false;
    }

}
