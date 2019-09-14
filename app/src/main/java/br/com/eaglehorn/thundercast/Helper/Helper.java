package br.com.eaglehorn.thundercast.Helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class Helper {

    private static final String TAG_FILE = "File Download";

    public String releaseDate (String date) {

        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, year);
//        cal.set(Calendar.MONTH, month - 1);
//        cal.set(Calendar.DAY_OF_MONTH, day);

//        return cal.getTime();
        return month(month) + " " + day;
    }

    private String month(int month) {

        ArrayList<String> months = new ArrayList<>();
        months.add("JANUARY");
        months.add("FEBRUARY");
        months.add("MARCH");
        months.add("APRIL");
        months.add("MAY");
        months.add("JUNE");
        months.add("JULY");
        months.add("AUGUST");
        months.add("SEPTEMBER");
        months.add("OCTOBER");
        months.add("NOVEMBER");
        months.add("DECEMBER");

        return months.get(month-1);
    }

    public boolean locateFileInDisk(Context context, String filename) {
//        File file = new File(context.getFilesDir(), filename);
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS),
                filename
        );

        if(file.exists()) {
            //Do something
            return true;
        } else {
            return false;
        }

    }
    public boolean saveFileToDisk(Context context, String filename, ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            // context.getFilesDir() = Internal Storage
//            File file = new File(context.getFilesDir(), filename);

            //External Storage
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS),
                    filename
            );

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG_FILE , fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                Log.d(TAG_FILE, "saveFileToDisk: " + e);
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.d(TAG_FILE, "saveFileToDisk: " + e);
            return false;
        }
    }

}
