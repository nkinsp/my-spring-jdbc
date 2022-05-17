package spms.sb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.function.Consumer;

import org.springframework.util.StringUtils;

public class AppTest {

	
	public static void readLine(File file,Consumer<String> consum) {
		
		
		if(file.isDirectory()) {
			
//			System.out.println(file.getName());
		
			if(file.getName().endsWith("node_modules") || file.getName().endsWith("build") || "shopms2".equals(file.getName())) {
//				System.out.println(");
				return;
			}
			
			File[] files = file.listFiles();
			for (File fe : files) {
				readLine(fe, consum);
			}
		}
		
		if(file.getName().endsWith(".java")) {
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String s = null;
	            while((s = br.readLine())!=null){
	                consum.accept(s);
	            }
	            br.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
	}
	
	static boolean isfirst = false;
	public static void main(String[] args) {
		
		File file = new File("/Users/yue/works/webapp/ande/");
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/yue/Desktop/9.txt")));
			
			readLine(file, str->{
			
				String string = str.replaceAll("\r\n", "").replaceAll(" ", "");
				if(StringUtils.isEmpty(string)) {
					return;
				}
				
				
				if(string.length() <= 2) {
					return;
				}
				
//				System.out.println(string.length());
				
				
				try {
					if(isfirst) {
						writer.newLine();
					}
					writer.write(string);
					isfirst = true;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			});
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
}
