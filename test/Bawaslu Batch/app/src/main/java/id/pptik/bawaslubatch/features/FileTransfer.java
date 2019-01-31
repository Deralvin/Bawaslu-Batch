package id.pptik.bawaslubatch.features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.util.Log;

import com.opencsv.CSVWriter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import id.pptik.bawaslubatch.R;
import id.pptik.bawaslubatch.helpers.SendToRMQ;

public class FileTransfer {

    SendToRMQ sendToRMQ =new SendToRMQ();

    Long tsLong = System.currentTimeMillis()/1000;

    private static final String TAG = null;
    public boolean ftpConnect(String srcFilePath, String Imei, Editable nama, Editable txtTelpText, String kodeProv,Context context){
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
            String telp = String.valueOf(txtTelpText);
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

                TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                @SuppressLint("MissingPermission")
                String mPhoneNumber = "99027262829020";

                boolean  status = ftpClient.storeFile("Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg", bis);
                SharedPreferences pref =context.getSharedPreferences("myToken", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                String randomUUID = UUID.randomUUID().toString();
                editor.putString(String.valueOf(R.string.pref_nama),names);
                editor.putString(String.valueOf(R.string.pref_tlp),telp);
                editor.putString(String.valueOf(R.string.pref_prov),prov[0]);
                editor.putString(String.valueOf(R.string.pref_guid),randomUUID);
                editor.commit();

                JSONObject obj = new JSONObject();
                JSONObject gps = new JSONObject();
                gps.put("LONG","90001928219");
                gps.put("LAT","-8882717229");

                obj.put("MSG_TYPE",0);
                obj.put("CMD_TYPE",0);
                obj.put("TIMESTAMP",ts);
                obj.put("IMEI1",Imei);
                obj.put("IMEI2","");
                obj.put("TLP1","");
                obj.put("TLP2","");
                obj.put("GPS",gps);
                obj.put("NAMA",names);
                obj.put("TLP",telp);
                obj.put("PROVINSI",prov[0]);
                obj.put("KTP","Pemilu/"+prov[0]+"/KTP-"+mPhoneNumber+".jpg");
                obj.put("GUID",pref.getString(String.valueOf(R.string.pref_guid),null));

                sendToRMQ.sendRMQRegister(obj.toString());


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

    public boolean ftpReport(String srcFilePath, Editable comment, Context context,String imei){
        try {
            SharedPreferences pref = context.getSharedPreferences("myToken",0);
            String guid = pref.getString(String.valueOf(R.string.pref_guid),null);
            String tlp = pref.getString(String.valueOf(R.string.pref_tlp),null);
            String prov = pref.getString(String.valueOf(R.string.pref_prov),null);
            String komentar = String.valueOf(comment);
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
                boolean  status = ftpClient.storeFile("Pemilu/"+prov+"/gambar/"+ts+"-"+guid+".jpg", bis);

                JSONObject obj = new JSONObject();
                JSONObject gps = new JSONObject();
                gps.put("LONG","XXXXXXXXXXX");
                gps.put("LAT","XXXXXXXXXX");

                obj.put("MSG_TYPE",1);
                obj.put("CMD_TYPE",0);
                obj.put("TIMESTAMP",ts);
                obj.put("IMEI1",imei);
                obj.put("IMEI2","");
                obj.put("TLP",tlp);
                obj.put("TLP1","");
                obj.put("TLP2","");
                obj.put("GPS",gps);
                obj.put("KOMENTAR",komentar);
                obj.put("FILENAME","Pemilu/"+prov+"/gambar/"+ts+"-"+guid+".jpg");
                obj.put("PROVINSI",prov);
                obj.put("GUID",guid);

                sendToRMQ.sendRMQreport(obj.toString());
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


    public boolean downloadFile() throws IOException {
        boolean result = false;
        File root = new File(Environment.getExternalStorageDirectory() + "/tmpBanwasl/Aduhh.csv");
        Log.d("DicobaKK", "downloadFile: "+root);
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("bawaslu-ftp.pptik.id");
        boolean stass = ftpClient.login("pemilu","pemilu123!");
        Log.d("DicobaKK", "Login: "+stass);
        try {
            ftpClient.connect("bawaslu-ftp.pptik.id");
            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())){
                boolean stas = ftpClient.login("pemilu","pemilu123!");
                Log.d("DicobaKK", "Login: "+stas);
                ftpClient.enterLocalPassiveMode();
                try (BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(root))) {
                    result = ftpClient.retrieveFile("Pemilu/32/Report.csv", out);
                    if (result) {
                        result = true;
                        Log.d("file-->成功从FTP服务器下载","ARGGHHHH");
                    }
                } catch (Exception e) {
                    Log.e("errorssss","sdadas "+e.getMessage() );
                } finally {
                    ftpClient.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("errorssss","sdadas "+e.getMessage() );
        }
        if (null == ftpClient || !ftpClient.isConnected()) {
            return result;
        }

        return result;
    }

    public boolean connectClient() throws IOException {
        FTPClient client =new FTPClient();
        boolean isLoggedIn = true;
        client.setControlEncoding("UTF-8");
        client.connect("bawaslu-ftp.pptik.id", 21);
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.enterLocalPassiveMode();
        client.login("pemilu", "pemilu123!");
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            Log.d("FaildeLoadingLogin", "Negative reply form FTP server, aborting, id was {}:"+ reply);
            //throw new IOException("failed to connect to FTP server");
            isLoggedIn = false;
        }
        return isLoggedIn;
    }
//    public void downloadFiles() {
//        File root =new File(Environment.getExternalStorageDirectory()+"/tmpBanwasl/Aduhh.csv");
//        String src = String.valueOf(root);
//        FTPClient client = new FTPClient();
//        try (BufferedInputStream os = new BufferedInputStream(new FileInputStream(src))) {
//            client.connect("bawaslu-ftp.pptik.id");
//            client.login("admin", "admin123**");
//
//            // Download file from FTP server.
//            boolean status = client.retrieveFile("Pemilu/32/Report.csv", os);
//            os.close();
//            Log.d("Gotcha", "downloadFiles: "+status);
//
//        } catch (IOException e) {
//            e.printStackTrace();  Log.d("Gotcha1", "downloadFiles: "+e);
//        } finally {
//            try {
//                client.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("Gotcha2", "downloadFiles: "+e);
//            }
//        }
//
//    }
    public boolean downloadAndSaveFile(String server, int portNumber,
                                        String user, String password, String filename, File localFile)
            throws IOException {
        FTPClient ftp = null;

        try {
            ftp = new FTPClient();
            try {
                ftp.connect(server, portNumber);
                Log.d("kasih_ibu", "Connected. Reply: " + ftp.getReplyString());

                ftp.login(user, password);
                Log.d("kasih_ibu", "Logged in");
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                Log.d("kasih_ibu", "Downloading");
                ftp.enterLocalPassiveMode();
            }
            catch (Exception ex) {
                Log.d("ibu_marah", "downloadAndSaveFile: "+ex.getMessage());
            }

            OutputStream outputStream = null;
            boolean success = false;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        localFile));
                success = ftp.retrieveFile(filename, outputStream);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }

            return success;
        } finally {
            if (ftp != null) {
                ftp.logout();
                ftp.disconnect();
            }
        }
    }

}
