package com.daahae.damoyeo.view.function;

import android.util.Log;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.view.fragment.NMapFragment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class APISearchMap {
    private NMapFragment view;
    public APISearchMap(NMapFragment view) {
        this.view = view;
    }

    public void getMap(String target) {
        String clientId = view.getResources().getString(R.string.NAVER_API_KEY);//애플리케이션 클라이언트 아이디값";
        String clientSecret = view.getResources().getString(R.string.NAVER_API_SECRET);//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(target, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text; // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            Toast.makeText(view.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
