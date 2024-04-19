package com.wallet.state;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/state")
public class StateControler {
	public List<Package> packages=new ArrayList<Package>();
	public static void Text(List<Package> list) {
    	String filePath = "output.txt";
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Package element : list) {
            	writer.write("san pham: "+element.getName()+", trang thai hien tai: "+element.getStatus());
                writer.newLine();
                writer.newLine();
            }
            
        } catch (IOException e) {
            System.out.println("An error occurred while writing the List to the file.");
            e.printStackTrace();
        }
    }
	@PostMapping("/add")
    public String add_new_package(@RequestBody Map<String, String>jsonData) {
    	try {
    		String name=jsonData.get("name");
    		Package pkg=new Package();
    		pkg.setName(name);
    		packages.add(pkg);
    		Text(packages);
    		return "them thanh cong";
    	}
    	catch (Exception e) {
			e.printStackTrace();
			return "them that bai";
		}
    }
	@PutMapping("/update")
	public String Delivere(@RequestBody Map<String, String>jsonData) {
		
		try {
			String name=jsonData.get("name");
			for (Package e : packages) {
				if (e.getName().equals(name)) {
					e.getState().next(e);
					Text(packages);
					return "trang thai hien tai : "+ e.getStatus();
				}
			}
			return "khon tim thay son hang";
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return "khong the chuyen doi trang thai";
		}
	}
	
	@PutMapping("/pre")
	public String pre(@RequestBody Map<String, String>jsonData) {
		
		try {
			String name=jsonData.get("name");
			for (Package e : packages) {
				if (e.getName().equals(name)) {
					e.getState().prev(e);
					Text(packages);
					return "trang thai hien tai : "+ e.getStatus();
				}
			}
			return "khon tim thay son hang";
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return "khong the chuyen doi trang thai";
		}
	}
	
	@PutMapping("/no_state")
	public String update_no_state(@RequestBody Map<String, String>jsonData) {
		try {
			String name=jsonData.get("name");
			for (Package e : packages) {
				if (e.getName().equals(name)) {
					if(e.getStatus().equals("Package ordered")) {
						e.setState(new DeliveredState());
					}
					else if(e.getStatus().equals("Package delivered")) {
						e.setState(new ReceivedState());
					}
					else {}
					Text(packages);
					return "trang thai hien tai : "+ e.getStatus();
				}
				
			}
			return "khon tim thay don hang";
			
		}catch (Exception e) {
			e.printStackTrace();
			return "khong the cap nhat trang thai";
		}
	}
	
}
