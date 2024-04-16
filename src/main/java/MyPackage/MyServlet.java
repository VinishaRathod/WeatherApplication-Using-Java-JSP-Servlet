package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */

public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//API SETUP
		String apiKey = "4abc537dc798c7ab95a563d4d6bc9a8e";
		String city = request.getParameter("city"); 
		
		
		//Create the URL for the OpenWeatherMap API request
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=4abc537dc798c7ab95a563d4d6bc9a8e";
		
		//API Integration 
		URL url= new URL(apiUrl);
		HttpURLConnection connection= (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		//Reading the data from the network
		InputStream inputstream= connection.getInputStream();
		InputStreamReader reader= new InputStreamReader(inputstream);
		
		//want to store in the String
		StringBuilder responseContent = new StringBuilder();
		
		//input lene k liye from the reader , will create scanner S
		Scanner scanner= new Scanner(reader);
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		
		scanner.close();
		System.out.print(responseContent);
		
		
		Gson gson= new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
		System.out.print(jsonObject);
		
		//Date and Time
		long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimestamp).toString();
		
		//Temperature
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelcius =  (int) (temperatureKelvin-273.15);
		
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind Speed
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//WeatherCondition
		String weatherCondition= jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().toString();
		
		//Set the data as request attributes(for sending to the jsp page)
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelcius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData", responseContent.toString());
		
		connection.disconnect();
		//forward the request to weather.jsp page for rendering
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
	}

}
