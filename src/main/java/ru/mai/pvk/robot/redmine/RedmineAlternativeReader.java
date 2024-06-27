/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.mai.pvk.robot.redmine.data.OurProjectMember;
import ru.mai.pvk.robot.redmine.data.StudentsIssue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author user
 */
public class RedmineAlternativeReader {

    private String redmineUrl;
    private String apiKey;
    private String projectKeyName;
    private final String STATUS_CLOSED = "-5";

    public RedmineAlternativeReader(String redmineUrl, String key) {
        this.redmineUrl = redmineUrl;
        this.apiKey = key;
    }

    public RedmineAlternativeReader(String redmineUrl, String apiKey, String projectKeyName) {
        this.redmineUrl = redmineUrl;
        this.apiKey = apiKey;
        this.projectKeyName = projectKeyName;
    }

    public ArrayList<StudentsIssue> getAllClosedIssues() {
        ArrayList<StudentsIssue> issues = parseIssues(getItems( "issues", STATUS_CLOSED));
        return issues;
    }

    public ArrayList<String> getJournals(String issueId) {
        ArrayList<String> journals = parseNames(getItems( "issues", issueId));
        return journals;
    }

    public ArrayList<OurProjectMember> getAllProjectUsers() {
        if (projectKeyName.isBlank()) {
            return null;
        }
        this.redmineUrl += "/projects/" + projectKeyName;
        ArrayList<OurProjectMember> ourProjectMembers = parseMembers(getItems("memberships"));
        return ourProjectMembers;
    }

    public OurProjectMember getOurProjectMember(String name) {
        return getAllProjectUsers().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name)).findFirst().get();
    }

    private String getItems(String whatToGet) {
        return getItems(whatToGet, "");
    }

    private String getItems(String whatToGet, String issueId) {
        OkHttpClient client = new OkHttpClient();

        String requestStr = redmineUrl + "/" + whatToGet;
        if (!issueId.isBlank() && !issueId.equals(STATUS_CLOSED)) {
            requestStr += "/" + issueId;
        }
        requestStr += ".json";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(requestStr).newBuilder();
        if (whatToGet.equalsIgnoreCase("issues")) {
            urlBuilder.addQueryParameter("include", "journals");
            urlBuilder.addQueryParameter("project_id", this.projectKeyName);
        }
        
        if (issueId.equals(STATUS_CLOSED)) {
            urlBuilder.addQueryParameter("status_id", "5");
        }

        urlBuilder.addQueryParameter("limit", "100");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("X-Redmine-API-Key", this.apiKey)
                .header("Content-Type", "application/json")
                .build();

        Response response = null;
        String responseJsonStr = "";
        try {
            response = client.newCall(request).execute();
            responseJsonStr = response.body().string();
        } catch (IOException ex) {
            Logger.getLogger(RedmineAlternativeReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseJsonStr;
    }

    private ArrayList<String> parseNames(String responseStr) {
        ArrayList<String> journal = new ArrayList<String>();
        try {
            JSONObject obj = null;
            if (!responseStr.toLowerCase().contains("captcha")) {
                obj = new JSONObject(responseStr);
            } else {
                obj = new JSONObject("{" + "" + "}");
            }
            JSONArray arr = obj.getJSONObject("issue").getJSONArray("journals");
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).getJSONObject("user").getString("name");
                journal.add(name);
            }
        } catch (JSONException ex) {
            //Logger.getLogger(RedmineJournalsReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return journal;
    }

    private ArrayList<OurProjectMember> parseMembers(String responseStr) {
        ArrayList<OurProjectMember> ourMembers = new ArrayList<OurProjectMember>();
        try {
            JSONObject obj = null;
            if (!responseStr.toLowerCase().contains("captcha")) {
                obj = new JSONObject(responseStr);
            } else {
                obj = new JSONObject("{" + "" + "}");
            }
            JSONArray arr = obj.getJSONArray("memberships");
            for (int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).getJSONObject("user").getString("name");
                String id = arr.getJSONObject(i).getJSONObject("user").getString("id");
                OurProjectMember member = new OurProjectMember();
                member.setId(Integer.parseInt(id));
                member.setName(name);
                ourMembers.add(member);
            }
        } catch (JSONException ex) {
            Logger.getLogger(RedmineAlternativeReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ourMembers;
    }

    private ArrayList<StudentsIssue> parseIssues(String issues) {
        //ToDO: decide if this method is really required.
        return null;
    }

    public String getStudentsName(int issueId, String manager) {
        ArrayList<String> allNames = this.getJournals(String.valueOf(issueId));
        if (manager.equals("")) {
            manager = "Sergey Politsyn";
        }
        String retVal = manager;

        for (int i = 0; i < allNames.size(); i++) {
            if (!allNames.get(i).equals(manager)) {
                retVal = allNames.get(i);
                break;
            }
        }

        return retVal;
    }
}
