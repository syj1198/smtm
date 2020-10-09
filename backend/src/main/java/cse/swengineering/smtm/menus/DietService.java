package cse.swengineering.smtm.menus;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DietService {

    private List<Diet> dietList = new ArrayList<>();

    public DietService() throws IOException {
        getDietInformation();
    }

    public List<Diet> getDietList() {
        return dietList;
    }

    public void setDietList(List<Diet> dietList) {
        this.dietList = dietList;
    }

    public void getDietInformation() throws IOException {
        final String RE_INCLUDE_KOREAN = ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*";
        final String RE_KOREAN = "^[가-힣\\s]*$";
        final String RE_DAY = "[0-9]+\\([가-힣]\\)";

        URL url = new URL("http://dorm.cnu.ac.kr/html/kr/sub03/sub03_0304.html");
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder builder = new StringBuilder();

        // 태그까지 모조리 긁어오기
        while ((line = br.readLine()) != null) {
            if(line.contains("<div class=\"diet_info\">")) {
                builder.append(line);
                line = br.readLine();
                builder.append(line);
            }
            if(line.contains("<table class=\"default_view diet_table\">"))
                while(!(line = br.readLine()).contains("</table>"))
                    if(line.matches(RE_INCLUDE_KOREAN))
                        builder.append(line);
        }

        // <"diet_info">에 시작 날짜와 끝 날짜가 있다
        String rawData = builder.toString();
        // 무의미한 공백 제거
        rawData = rawData.replace("\t", "");
        // 시작 날짜와 끝 날짜 얻기
        int dateIndex = rawData.indexOf("<p>202");
        String stringDate = rawData.substring(dateIndex + 3, dateIndex + 13);

        // <br>태그 기준으로 한 번 잘라서 다듬고
        String[] splitByTag = rawData.split("<br />");
//        for(String str : splitByTag)
//            System.out.println(str);

        // 다시 합친 뒤
        String data = convertObjectArrayToString(splitByTag, ".");
//        System.out.println(data);
        // 날짜별로 다 쪼개기
        String[] splitByDay = data.split(RE_DAY);
//        for(String str : splitByDay)
//            System.out.println(str);
        List<String> strList = new ArrayList<>();

        // 날짜마다
        for(int i=1; i<=7; i++){
            Diet diet = new Diet();
            if(i != 1)
                stringDate = addDay(stringDate);
            diet.setDate(LocalDate.parse(stringDate));
            // 메뉴별로 다 쪼개서
            String[] splitByMenu = splitByDay[i].split("\\.");

            for (String menuStr : splitByMenu){
                if(menuStr.contains("메인A") || menuStr.contains("메인 C") || menuStr.contains("메인C")) { // 메인A or 메인C
//                    diet.getMenus().add(new Menu(menuStr.substring(menuStr.indexOf("메인"), menuStr.indexOf("kcal]") + "kcal]".length())));
                    String mainAndCalories = menuStr.substring(menuStr.indexOf("메인"), menuStr.indexOf("kcal]") + "kcal]".length());
                    mainAndCalories = mainAndCalories.replace(" ", "");
                    System.out.println(mainAndCalories);
                    String replace = mainAndCalories.replace("[", " ");
                    replace = replace.replace("]", "");
                    String[] split = replace.split(" ");
                    String main = split[0];
                    String cal = split[1];
                    strList.add(main);
                    strList.add(cal);
                }
                else if(menuStr.matches(RE_INCLUDE_KOREAN))
                    strList.add(menuStr);
//                    diet.getMenus().add(new Menu(menuStr));
            }
//            dietList.add(diet);
        }
        strList.forEach(System.out::println);
    }

    // string array to string
    private static String convertObjectArrayToString(Object[] arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : arr)
            sb.append(obj.toString()).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }

    private String addDay(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try{
            c.setTime(sdf.parse(date));
        }catch(ParseException e){
            e.printStackTrace();
        }
        //Incrementing the date by 1 day
        c.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(c.getTime());
    }
}