package br.com.eaglehorn.thundercast.Helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import okhttp3.ResponseBody;

public class StorageHelper {

    private static final String TAG = "StorageHelper";

    private Context context;
    private String fileString;
    private String filename;
    private String extension;
    private String directoryName = "images";

    public StorageHelper(Context context) {
        this.context = context;
    }

    public StorageHelper fileString(String file) {
        this.fileString = file;
        return this;
    }

    public StorageHelper fileBody(ResponseBody file) {

        return this;
    }

    public StorageHelper filename(String filename) {
        this.filename = filename;
        return this;
    }

    public StorageHelper extension(String extension) {
        this.extension = extension;
        return this;
    }

    public StorageHelper save() {

        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename + extension
        );

        Log.d(TAG, "save: " + fileString);
        Log.d(TAG, "save: " + filename);
        Log.d(TAG, "save: " + extension);
        Log.d(TAG, "save: " + file.getAbsolutePath());

        OutputStream outputStream = null;

        try {

            file.createNewFile();

            outputStream = new FileOutputStream(file);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.append(fileString);
            outputStreamWriter.close();

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }
}
