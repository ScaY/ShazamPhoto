package fr.isen.shazamphoto.utils;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.isen.shazamphoto.database.Monument;
import fr.isen.shazamphoto.ui.UnidentifiedMonument;

public class AddMonument extends AsyncTask<Monument, Void, String> {

    private HttpClient httpclient = new DefaultHttpClient();
    private HttpPost httppost = new HttpPost("http://"+ConfigurationShazam.IP_SERVER+"/shazam/api.php");
    private UnidentifiedMonument home;
    private HttpResponse response;

    public AddMonument(UnidentifiedMonument home) {
        this.home = home;
    }

    public String doInBackground(Monument... monuments) {
        Monument monument = monuments[0];
        try {
            StringEntity entity = new StringEntity("monument=" + monument.toJSON().toString(), "UTF8");
            entity.setContentType("application/x-www-form-urlencoded");
            httppost.setEntity(entity);

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            // Toast.makeText(home, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            //Toast.makeText(home, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return monument.getName().toString();
    }

    public void onPostExecute(String result2) {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject jsonResponse = new JSONObject(result.toString());
        } catch (Exception e) {
        }

    }
}
