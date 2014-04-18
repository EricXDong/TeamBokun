package jsonParse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import data.Car;

import java.util.ArrayList;
import java.util.Iterator;

public class CarJsonParser {
	private ArrayList<Car> cars = new ArrayList<Car>();
	
	//	Pass in path of json file
	public CarJsonParser(String jsonPath) {
		//		Configure Gson
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Car.class, new CarDeserializer());
		Gson gson = gsonBuilder.create();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(jsonPath));
			String jsonString = br.readLine();
			if(jsonString == null) {
				System.err.println("Invalid JSON file.");
				System.exit(1);
			}
				
			//	Get array of all json cars
			JsonElement json = new JsonParser().parse(jsonString);
			JsonArray array = json.getAsJsonArray();
			Iterator<JsonElement> it = array.iterator();
			
			//	Iterate through and save all cars
			while(it.hasNext()) {
				JsonElement jsonCar = (JsonElement)it.next();
				
				cars.add(gson.fromJson(jsonCar, Car.class));
			}
		}
		catch(FileNotFoundException e) {
			System.err.println("Error parsing JSON file: " + e.getMessage());
			System.exit(1);
		}
		catch(IOException e) {
			System.err.println("Error parsing JSON file: " + e.getMessage());
			System.exit(1);
		}
		finally {
			try {
				br.close();
			}
			catch(IOException e) {System.err.println("Unknown error: "  + e.getMessage()); }
		}
	}
	
	//	Get all cars
	public ArrayList<Car> getCars() { return cars; }
	
	//	Print all car information
	public void printCars() {
		for(int i = 0; i < cars.size(); i++)
			System.out.println(cars.get(i));
	}
	
	public static void main(String[] args) {
		new CarJsonParser("./ref/test.json").printCars();
	}
}
