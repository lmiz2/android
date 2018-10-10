package com.example.songhyeonseok.taos_gps_test;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SongHyeonSeok on 2017-09-21.
 */

public class ParsingBattData {
    String tmp;
    //String regex = "[0-9]: \\p{Alnum}\\p{Alnum} \\p{Alnum}\\p{Alnum} \\p{Alnum}\\p{Alnum} \\p{Alnum}\\p{Alnum} \\p{Alnum}\\p{Alnum} \\p{Alnum}\\p{Alnum} \\p{Alnum}\\p{Alnum}\n?";
    String regex = "[0-9]: (\\p{Alnum}\\p{Alnum} )+\n?";
    String regex2 = "\\p{Alnum}\\p{Alnum}";
    Pattern pattern;
    Matcher matcher;
    final int TYPE_2101 = 2101;
    final int TYPE_2100 = 2100;

    public ParsingBattData() {
    }

    public void setTargetString(String s){
        tmp = s;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(tmp);
    }

    public String getMatches(String s){
        tmp = s;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(tmp);
        StringBuffer stb = new StringBuffer();

        while(matcher.find()){
            Log.d("매칭",matcher.group());
            stb.append(matcher.group()+"\n");
        }
        return stb.toString();
    }

    public String getJSON(String tmp, int type){
        pattern = null;
        matcher = null;
        String result_1 = getMatches(tmp);

        pattern = null;
        matcher = null;
        pattern = Pattern.compile(regex2);
        matcher = pattern.matcher(result_1);
        String[] arr0 = new String[6];
        String[][] arrs = new String[8][7];
        int count = 0;
        int index_row = 0;
        int index_c = 0;
        boolean flag = true;
        while (matcher.find()&&flag){
            if(count <= 5){
                arr0[count] = "\""+matcher.group()+"\"";
                count++;
            }else {
                arrs[index_row][index_c++] = "\""+matcher.group()+"\"";
                if(index_c == 7){
                    index_c = 0;
                    index_row++;
                    if(index_row>7){
                        flag = false;
                    }
                }
            }

        }



        StringBuffer tmpsb = new StringBuffer();
        if(type == TYPE_2101) {
            tmpsb.append("{ \"Arr0\" :{ \"H1\": " + arr0[0] + ",  \"H2\": " + arr0[1] + ", \"A\": " + arr0[2] + ", \"B\": " + arr0[3] + ", \"C\": " + arr0[4] + ", \"D\": " + arr0[5] + " },\n" +
                    "\"Arr1\" :{ \"E\": " + arrs[0][0] + ", \"F\": " + arrs[0][1] + ", \"G\": " + arrs[0][2] + ", \"H\": " + arrs[0][3] + ", \"I\": " + arrs[0][4] + ", \"J\": " + arrs[0][5] + ", \"K\": " + arrs[0][6] + " },\n" +
                    "\"Arr2\" :{ \"L\": " + arrs[1][0] + ", \"M\": " + arrs[1][1] + ", \"N\": " + arrs[1][2] + ", \"O\": " + arrs[1][3] + ", \"P\": " + arrs[1][4] + ", \"Q\": " + arrs[1][5] + ", \"R\": " + arrs[1][6] + " },\n" +
                    "\"Arr3\" :{ \"S\": " + arrs[2][0] + ", \"T\": " + arrs[2][1] + ", \"U\": " + arrs[2][2] + ", \"V\": " + arrs[2][3] + ", \"W\": " + arrs[2][4] + ", \"X\": " + arrs[2][5] + ", \"Y\": " + arrs[2][6] + " },\n" +
                    "\"Arr4\" :{ \"Z\": " + arrs[3][0] + ", \"AA\": " + arrs[3][1] + ", \"AB\": " + arrs[3][2] + ", \"AC\": " + arrs[3][3] + ", \"AD\": " + arrs[3][4] + ", \"AE\": " + arrs[3][5] + ",  \"AF\": " + arrs[3][6] + " },\n" +
                    "\"Arr5\" :{ \"AG\": " + arrs[4][0] + ", \"AH\": " + arrs[4][1] + ", \"AI\": " + arrs[4][2] + ", \"AJ\": " + arrs[4][3] + ", \"AK\": " + arrs[4][4] + ", \"AL\": " + arrs[4][5] + ", \"AM\": " + arrs[4][6] + " },\n" +
                    "\"Arr6\" :{ \"AN\": " + arrs[5][0] + ", \"AO\": " + arrs[5][1] + ", \"AP\": " + arrs[5][2] + ", \"AQ\": " + arrs[5][3] + ", \"AR\": " + arrs[5][4] + ", \"AS\": " + arrs[5][5] + ", \"AT\": " + arrs[5][6] + " },\n" +
                    "\"Arr7\" :{ \"AU\": " + arrs[6][0] + ", \"AV\": " + arrs[6][1] + ", \"AW\": " + arrs[6][2] + ", \"AX\": " + arrs[6][3] + ", \"AY\": " + arrs[6][4] + ", \"AZ\": " + arrs[6][5] + ", \"BA\": " + arrs[6][6] + " },\n" +
                    "\"Arr8\" :{ \"BB\": " + arrs[7][0] + ", \"BC\": " + arrs[7][1] + ", \"BD\": " + arrs[7][2] + ", \"BE\": " + arrs[7][3] + ", \"BF\": " + arrs[7][4] + ", \"BG\": " + arrs[7][5] + ", \"BH\": " + arrs[7][6] + " } }");
        }else if(type == TYPE_2100){
            tmpsb.append("{ \"Arr0\" :{ \"H1\": " + arr0[0] + ",  \"H2\": " + arr0[1] + ", \"A\": " + arr0[2] + ", \"B\": " + arr0[3] + ", \"C\": " + arr0[4] + ", \"D\": " + arr0[5] + " },\n" +
                    "\"Arr1\" :{ \"E\": " + arrs[0][0] + ", \"F\": " + arrs[0][1] + ", \"G\": " + arrs[0][2] + ", \"H\": " + arrs[0][3] + ", \"I\": " + arrs[0][4] + ", \"J\": " + arrs[0][5] + ", \"K\": " + arrs[0][6] + " },\n" +
                    "\"Arr2\" :{ \"L\": " + arrs[1][0] + ", \"M\": " + arrs[1][1] + ", \"N\": " + arrs[1][2] + ", \"O\": " + arrs[1][3] + ", \"P\": " + arrs[1][4] + ", \"Q\": " + arrs[1][5] + ", \"R\": " + arrs[1][6] + " },\n" +
                    "\"Arr3\" :{ \"S\": " + arrs[2][0] + ", \"T\": " + arrs[2][1] + ", \"U\": " + arrs[2][2] + ", \"V\": " + arrs[2][3] + ", \"W\": " + arrs[2][4] + ", \"X\": " + arrs[2][5] + ", \"Y\": " + arrs[2][6] + " } }");
        }

        return tmpsb.toString();
    }


}
