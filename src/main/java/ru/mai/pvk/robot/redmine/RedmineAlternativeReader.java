/*
 * To change this license header, choose License Headers in Project RedmineConnectionProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.mai.pvk.robot.redmine;

import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.internal.RedmineJSONBuilder;
import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.mai.pvk.robot.redmine.data.OurProjectMember;
import ru.mai.pvk.robot.redmine.data.StudentsIssue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        ArrayList<StudentsIssue> issues = parseIssues(getItems("issues", STATUS_CLOSED));
        return issues;
    }

    public ArrayList<String> getJournals(String issueId) {
        ArrayList<String> journals = parseNames(getItems("issues", issueId));
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

    public String createIssue(Issue issue) {
        Header header = new BasicHeader("X-Redmine-API-Key", this.apiKey);
        String body = RedmineJSONBuilder.toSimpleJSON("issue", issue, RedmineJSONBuilder::writeIssue);
        String url = redmineUrl + "/issues.json";

        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(body.getBytes()));
        entity.setContentLength(body.getBytes().length);

        try (CloseableHttpClient client = HttpClients.createSystem()) {
            HttpUriRequest request = RequestBuilder.post()
                    .setUri(url)
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setHeader(header)
                    .setHeader(HttpHeaders.USER_AGENT, "mine")
                    .setEntity(entity)
                    .build();

            HttpResponse response = null;
            String responseJsonStr = null;
            response = client.execute(request);

            responseJsonStr = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))
                    .lines().collect(Collectors.joining("\n"));

            return responseJsonStr;
        } catch (IOException ex) {
            Logger.getLogger(RedmineAlternativeReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    public String createIssueOkHttp(Issue issue) {
        Header header = new BasicHeader("X-Redmine-API-Key", this.apiKey);
        String bodyS = RedmineJSONBuilder.toSimpleJSON("issue", issue, RedmineJSONBuilder::writeIssue);
        String url = redmineUrl + "/issues.json";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"issue\":{\"subject\":\"Разменнная монета\",\"assigned_to_id\":26945,\"is_private\":true,\"project_id\":52589,\"description\":\"В некоторой заморской стране довольно необычная система монет: существуют только монеты 1, 3, 4 песо.\\r\\nВам нужно найти способ как выдать сдачу в _N_ песо используя минимальное число монет.\\r\\nВходной формат данных. Целое число - необходимая сдача.\\r\\nВыходной формат. Минимальное количество монет из набора 1, 3, 4, и пример получившейся суммы, где монеты упорядочены по возрастанию номинала.\\r\\n*Пример 1*\\r\\nВвод:\\r\\n2\\r\\nВывод:\\r\\n2\\r\\n2 = 1 + 1\\r\\n*Пример 2*\\r\\nВвод:\\r\\n34\\r\\nВывод:\\r\\n9\\r\\n34 = 3 + 3 + 4 + 4 + 4 + 4 + 4 + 4 + 4В некоторой заморской стране довольно необычная система монет: существуют только монеты 1, 3, 4 песо.\\r\\nВам нужно найти способ как выдать сдачу в _N_ песо используя минимальное число монет.\\r\\nВходной формат данных. Целое число - необходимая сдача.\\r\\nВыходной формат. Минимальное количество монет из набора 1, 3, 4, и пример получившейся суммы, где монеты упорядочены по возрастанию номинала.\\r\\n*Пример 1*\\r\\nВвод:\\r\\n2\\r\\nВывод:\\r\\n2\\r\\n2 = 1 + 1\\r\\n*Пример 2*\\r\\nВвод:\\r\\n34\\r\\nВывод:\\r\\n9\\r\\n34 = 3 + 3 + 4 + 4 + 4 + 4 + 4 + 4 + 4\",\"fixed_version_id\":17751,\"category_id\":null}}");
        Request request = new Request.Builder()
                .url("https://hostedredmine.com/issues.json")
                .method("POST", body)
                .addHeader("X-Redmine-API-Key", "7bd0ad78c5a96b2434a07fec485eff8a67f76214")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
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
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return responseJsonStr;
    }

    private ArrayList<String> parseNames(String responseStr) {
        ArrayList<String> journal = new ArrayList<>();
        try {
            JSONObject obj = null;
            if (!responseStr.toLowerCase().contains("captcha")) {
                obj = new JSONObject(responseStr);
            } else {
                obj = new JSONObject("{" + "}");
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
                obj = new JSONObject("{}");
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
        if (manager.isEmpty()) {
            manager = "Sergey Politsyn";
        }
        String retVal = manager;

        for (String aName : allNames) {
            if (!aName.equals(manager)) {
                retVal = aName;
                break;
            }
        }

        return retVal;
    }
}
