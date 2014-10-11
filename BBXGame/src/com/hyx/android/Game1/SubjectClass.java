package com.hyx.android.Game1;

import java.util.List;

public class SubjectClass {
	 int id;  
     String name;  
     public List<SubSubjectClass> subclass;
     public List<SubSubjectClass> getSubclass() {
		return subclass;
	}
	public void setSubclass(List<SubSubjectClass> subclass) {
		this.subclass = subclass;
	}
	public int getId() {  
         return id;  
     }  
     public void setId(int id) {  
         this.id = id;  
     }  
     public String getName() {  
         return name;  
     }  
     public void setName(String name) {  
         this.name = name;  
     }     
}


