package com.example.lordzsolt.myapplication;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Lordzsolt on 11/15/2015.
 */
public class Downloader extends AsyncTask {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate = null;

    private String _urlString;

    public Downloader(String urlString, AsyncResponse delegate){
        this.delegate = delegate;
        _urlString = urlString;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            HttpURLConnection con = null;
            URL url = new URL(_urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(10000 /* milliseconds */);
            con.setConnectTimeout(15000 /* milliseconds */);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Scanner scanner = new Scanner(con.getInputStream());
            String result = scanner.nextLine();
            scanner.close();
            con.disconnect();
            return result;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    protected void onPostExecute(Object o) {
        if (this.delegate != null) {
            this.delegate.processFinish(o.toString());
        }
    }
}
