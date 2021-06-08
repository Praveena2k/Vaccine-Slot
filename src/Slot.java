import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Slot{
    public static HttpURLConnection con;
    public static URL url;
    public static Scanner scan=new Scanner(System.in);
    public static String url1;
    public static String date;
    public static int area_pincode;

    public static void main(String args[]){
        System.out.println("Finding Vaccination Slots Using Pincode:"+ "\n");
        getDetails();
    }

    public static void getDetails(){
      System.out.println("Enter the date (dd-mm-yyyy) : ");
      date=scan.next();
      System.out.println("Enter Your Area Pincode (6 digits) : ");
      area_pincode=scan.nextInt();
      url1="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+area_pincode+"&date="+date;
        //url1="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=600071&date=07-06-2021";
        getApi(url1);
    }

    public static void getApi(String url1){
        BufferedReader reader;
        String line;
        StringBuffer response=new StringBuffer();

        try {

            url = new URL(url1);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status=con.getResponseCode();

            if(status>200){
                reader=new BufferedReader(new InputStreamReader(con.getErrorStream()));
                while((line=reader.readLine())!=null){
                    response.append(line);
                }
                reader.close();
            }else{
                reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
                while((line=reader.readLine())!=null){
                    response.append(line);
                }
                reader.close();
            }

            System.out.println(response+ "\n");
            parse(response.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
    }


    public static void parse(String responseBody){
        JSONObject obj=new JSONObject(responseBody.toString());
        JSONArray arr=new JSONArray(obj.getJSONArray("sessions"));
          if(arr.isEmpty()){
              System.out.println("No Results Found....Think So Vaccine are not available!!!"+ "\n");

          }
          else {

              for (int i = 0; i < arr.length(); i++) {
                  JSONObject album = arr.getJSONObject(i);
                  System.out.println("Vaccine Slot for the given date["+date+"] && Area Pincode ["+area_pincode+"] are : " + "Result[" + (i+1) + "]"+ "\n");
                  System.out.println("Center_ID           :   " + album.getInt("center_id"));
                  System.out.println("Name                :   " + album.getString("name"));
                  System.out.println("Address             :   " + album.getString("address"));
                  System.out.println("Fees Type           :   " + album.getString("fee_type"));
                  System.out.println("Dose1 Availability  :   " + album.getInt("available_capacity_dose1"));
                  System.out.println("Dose2 Availability  :   " + album.getInt("available_capacity_dose2"));
                  System.out.println("Min_Age             :   " + album.getInt("min_age_limit"));
                  System.out.println("Price               :   " + album.getInt("fee"));
                  System.out.println("Vaccine             :   " + album.getString("vaccine"));
                  System.out.println("Slots               :   " + album.getJSONArray("slots")+ "\n"+ "\n");
              }
          }
        System.out.println("Do you want to do for another pincode/date(y/n) : ");
        String flag=scan.next();
        if(flag.equalsIgnoreCase("y")){
            getDetails();
        }else{
            System.out.println("Thanks...Byeeeeee!!!!");
        }
    }


}

