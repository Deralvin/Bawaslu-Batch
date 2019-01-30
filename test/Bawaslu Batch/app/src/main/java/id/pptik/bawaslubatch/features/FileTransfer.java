package id.pptik.bawaslubatch.features;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
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
import java.util.concurrent.TimeoutException;

import id.pptik.bawaslubatch.helpers.SendToRMQ;

public class FileTransfer {

    SendToRMQ sendToRMQ =new SendToRMQ();

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
                data.add(new String []{names,"Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg",telp,prov[0],ts,"-"+Imei});

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

    public boolean ftpReport(String srcFilePath, Editable comment, Context context){
        try {
            SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
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


                String randomUID =pref.getString("GUID",null);
                JSONObject obj = new JSONObject();
                obj.put("NAMA","Dilan");
                obj.put("FILENAME","Pemilu/32/"+ts+"-"+randomUID+".jpg");
                obj.put("PROVINSI","32");
                obj.put("KOMENTAR",komentar);
                obj.put("Reg_TimeStamp",ts);


                sendToRMQ.sendRMQreport(obj.toString());
                boolean  status = ftpClient.storeFile("Pemilu/32/"+ts+"-"+randomUID+".jpg", bis);
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
