package id.pptik.bawaslubatch.features;

import android.os.Environment;
import android.text.Editable;
import android.util.Log;

import com.google.gson.Gson;
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
import java.net.SocketException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class FileTransfer {
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "Nama,File Foto,Tlp,Provinsi,Reg Timestamp,Imei";

    Long tsLong = System.currentTimeMillis()/1000;

    private static final String TAG = null;
    public boolean ftpConnect(String srcFilePath, String Imei, Editable nama, Editable txtTelpText, String kodeProv){
        try {
            String[] prov = kodeProv.split("-");

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

                boolean  status = ftpClient.storeFile("Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg", bis);
                if (status ==true){
                    File root = new File(String.valueOf(Environment.getExternalStorageDirectory())+"/tmpBanwasl");
                    CSVWriter writer =null;
                    try {
                        if (root.mkdir()){
                            Log.d("FolderDone", "Succes Create Folder: "+root);
                            String csv = root+"/Reg-"+ts+"-"+Imei+".csv";
                            writer = new CSVWriter(new FileWriter(csv));

                            List<String []> data = new ArrayList<String[]>();
                            data.add(new String []{"Nama","File Foto","Tlp","Provinsi","Reg Timestamp","Imei"});
                            data.add(new String []{names,"Pemilu/"+prov[0]+"/KTP-"+ts+"_"+Imei+".jpg",telp,prov[0],ts,"-"+Imei});

                            writer.writeAll(data);
                            writer.close();
                            FileInputStream fI = new FileInputStream(csv);
                            BufferedInputStream Bi = new BufferedInputStream(fI);
                            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                           boolean tes = ftpClient.storeFile("Pemilu/",Bi);
                            Log.d("TrueorFalse", "ftpConnect: "+tes);
                        }else{
                        }
                        Log.d("FolderDone", "ftpConnect: Folder Exist");
                        String csv = root+"/Reg-"+ts+"-"+Imei+".csv";
                        writer = new CSVWriter(new FileWriter(csv));

                        List<String []> data = new ArrayList<String[]>();
                        data.add(new String []{"Nama","File Foto","Tlp","Provinsi","Reg Timestamp","Imei"});
                        data.add(new String []{names,"Pemilu/"+prov[0]+"/KTP-"+ts+"-"+Imei+".jpg",telp,prov[0],ts,"-"+Imei});

                        writer.writeAll(data);
                        writer.close();
                        FileInputStream fI = new FileInputStream(csv);
                        BufferedInputStream Bi = new BufferedInputStream(fI);
                        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                        boolean tes = ftpClient.storeFile("Pemilu/",Bi);
                        Log.d("TrueorFalse", "ftpConnect: "+tes);
                    }catch (Exception e){
                        Log.d("FailedCSV", "ftpConnect: Gagal CSV "+e );
                    }

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
        }
        return false;
    }

}
