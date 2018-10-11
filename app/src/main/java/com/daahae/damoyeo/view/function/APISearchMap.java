package com.daahae.damoyeo.view.function;

import android.util.Log;
import android.widget.Toast;

import com.daahae.damoyeo.R;
import com.daahae.damoyeo.model.Building;
import com.daahae.damoyeo.model.Position;
import com.daahae.damoyeo.view.fragment.NMapFragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class APISearchMap {
    private NMapFragment view;
    public APISearchMap(NMapFragment view) {
        this.view = view;
    }
    public StringBuilder sb;
    public ArrayList<String> name;
    public ArrayList<Building> buildings;

    public ArrayList<Building> getMap(String target) {
        String clientId = view.getResources().getString(R.string.NAVER_API_KEY);//애플리케이션 클라이언트 아이디값";
        String clientSecret = view.getResources().getString(R.string.NAVER_API_SECRET);//애플리케이션 클라이언트 시크릿값";
        int display = 5;
        try {
            String text = URLEncoder.encode(target, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text  + "&display=" + display + "&"; // json 결과
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
            sb = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine + "\n");
            }
            br.close();
            con.disconnect();
            //System.out.println(sb);

            String data = sb.toString();
            String[] array;
            array = data.split("\"");
            buildings = new ArrayList<Building>();
            //String[] link = new String[display];
            //String[] category = new String[display];
            //String[] description = new String[display];
            //String[] mapx = new String[display];
            //String[] mapy = new String[display];
            int k = 0;
            for (int i = 0; i < array.length; i++) {
                String name ="", tel="", address="";
                double mapX=0, mapY=0;
                if (array[i].equals("title"))
                    name = array[i + 2];
                /*
                if (array[i].equals("link"))
                    link[k] = array[i + 2];
                if (array[i].equals("category"))
                    category[k] = array[i + 2];
                if (array[i].equals("description"))
                    description[k] = array[i + 2];
                    */
                if (array[i].equals("telephone"))
                    tel = array[i + 2];
                if (array[i].equals("address"))
                    address = array[i + 2];
                if (array[i].equals("mapx"))
                    mapX = Double.parseDouble(array[i + 2]);
                if (array[i].equals("mapy")) {
                    mapY = Double.parseDouble(array[i + 2]);
                    k++;
                }

                if(!name.equals("")){
                    Position pos = new Position(mapX, mapY);

                    Building building = new Building(pos, address, 0, pos, name, address, tel);
                    Log.d("err", name);
                    buildings.add(building);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return buildings;
    }
}
