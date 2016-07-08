package cn.com.mvtech.selection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FeatureHelper {
	
	
	public static List<Feature> loadFeature(String path) {
		File file = new File(path);
		List<Feature> features = new ArrayList<Feature>();
		try {
			List<String> lines = FileUtils.readLines(file);
			for (String line : lines) {
				Feature feature = new Feature();
				String[] arr = line.split(" ");
				feature.setText(arr[0]);
				feature.setId(Integer.valueOf(arr[1].equals("") ? "0" : arr[1]));
				feature.setDocCount(Integer.valueOf(arr[2]));
				features.add(feature);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return features;
	} 
}
