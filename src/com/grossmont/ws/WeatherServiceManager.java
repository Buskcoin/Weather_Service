package com.grossmont.ws;

// Classes for reading web service.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

// Classes for JSON conversion to java objects using Google's gson.
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Scanner;


public class WeatherServiceManager{
    
    private Weather m_oWeather = null;
    private String m_sWeatherJson;



    // Gets the overall weather JSON string from the 3rd party web service.
    public void callWeatherWebService(String sCity){

    	String sServiceReturnJson = "";

    	try {

            // Call weather API.
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" +
                    sCity + "&appid=1868f2463a960613c0a78b66a99b5e5f&units=imperial");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    sServiceReturnJson += strTemp;
            }

            // sServiceReturnJson now looks something like this:
            /*
            {"coord":{"lon":-116.96,"lat":32.79},
            "weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],
            "base":"cmc stations",
            "main":{"temp":62.65,"pressure":1007.4,"humidity":93,"temp_min":62.65,"temp_max":62.65,"sea_level":1028.19,"grnd_level":1007.4},
            "wind":{"speed":7.29,"deg":310.501},"clouds":{"all":32},"dt":1463026609,
            "sys":{"message":0.0078,"country":"US","sunrise":1463057430,"sunset":1463107097},
            "id":5345529,"name":"El Cajon","cod":200}
            */

            //System.out.println("Returned json:");
            //System.out.println(sServiceReturnJson);
            


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred in callWeatherWebService() in WeatherServiceManager: " + ex.toString());
        }

        m_sWeatherJson = sServiceReturnJson;

        // Turn raw json into java object heirarchy using Google's gson.
        convertJsonToJavaObject();
    }

	// Uses Google's gson library to convert json into filled java objects
	// using the java object heirarchy that you already created.
    private void convertJsonToJavaObject(){

        Gson gson = new GsonBuilder().create();

        m_oWeather = gson.fromJson(m_sWeatherJson, Weather.class);
    }

    // This uses Google's gson library for parsing json.
    public float getCurrentTemp(){

        return m_oWeather.main.temp;
    }
    
    // getCityName Method
    public String getCityName() {
        
       return m_oWeather.name; 
    }
    
    // getHighTemp method
    public float getHighTemp() {
        
        return m_oWeather.main.temp_max;
    }
    
    // getLowTemp method
    public float getLowTemp() {
        
        return m_oWeather.main.temp_min;
    }


    public static void main(String[] args){
        
        Scanner cityInfo = new Scanner(System.in);
        String city1 = "";
        String city2 = "";
        
        
        // Instantiating two instances of the WeatherServiceManager
        WeatherServiceManager oRun1 = new WeatherServiceManager();
        WeatherServiceManager oRun2 = new WeatherServiceManager();
        
        // Asking for user input for first city
        System.out.println("What city would you information for? ");
        city1 = cityInfo.nextLine();
        city1 = city1.replaceAll(" ","");
        oRun1.callWeatherWebService(city1);
        
        // Asking for user input on second city
        System.out.println("What other city would you like information for? ");
        city2 = cityInfo.nextLine();
        city2 = city2.replaceAll(" ", "");
        oRun2.callWeatherWebService(city2);
        
        // Compare the temps between cities
        if (oRun1.getCurrentTemp() > oRun2.getCurrentTemp()) {
            
            System.out.println("The highest current temperature is " + oRun1.getCityName() + " which has a temperature of " + oRun2.getCurrentTemp() + " degrees fahrenheit");
                 
        } else {
            
            System.out.println("The highest current temprature is " + oRun2.getCityName() + " which has a temperature of " + oRun2.getCurrentTemp());
        }
        
        if (oRun1.getHighTemp() - oRun1.getLowTemp() > oRun2.getHighTemp() - oRun2.getLowTemp()) {
            
            System.out.println("The greatest range is " + oRun1.getCityName() + " with a temperature of " + oRun1.getHighTemp() + " degrees fahrenheit");
        } else {
            
            System.out.println("The greatest range is " + oRun1.getCityName() + " with a temperature of " + oRun2.getHighTemp() + " degrees fahrenheit");
        }
        
    }




	// ------------------------------------------------------------------------------------------------------------

    // ***********************************
	// Only included here just as an example of how the raw json
	// could be parsed directly w/o using 3rd party library like gson.
	public float getTempManualParse(){

		String sTemp = "";
		float fTemp;

		// Parse "temp" out of JSON reply.
		int iTempIndex = m_sWeatherJson.indexOf("\"temp\":") + 7;
		sTemp = m_sWeatherJson.substring(iTempIndex);
		sTemp = sTemp.substring(0, sTemp.indexOf(","));
		fTemp = Float.parseFloat(sTemp);

		return fTemp;
	}
    

}
