package com.example.andre.nytreader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Andre on 18/03/2017.
 */

public class Doc implements Serializable {
    public String web_url, snippet, lead_paragraph, source, headline, pub_date, section_name,
            subsection_name, image_url, news_desk;

    Doc(JSONObject ob) {
        try {
            web_url = ob.getString("web_url");
            snippet = ob.getString("snippet");
            lead_paragraph = ob.getString("lead_paragraph");
            source = ob.getString("source");
            headline = ob.getJSONObject("headline").getString("main");
            pub_date = ob.getString("pub_date");
            section_name = ob.getString("section_name");
            subsection_name = ob.getString("subsection_name");
            news_desk = ob.getString("news_desk");

            JSONArray multimedia = ob.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                image_url = multimedia.getJSONObject(0).getString("url");
            } else {
                image_url = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
