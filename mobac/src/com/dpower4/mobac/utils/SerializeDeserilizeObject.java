package com.dpower4.mobac.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;



import com.dpower4.mobac.models.User;

public class SerializeDeserilizeObject {
	
	public static void saveObject(User p, String fileLocation) throws IOException {
		ObjectOutputStream oos = null;
		
			oos = new ObjectOutputStream(new FileOutputStream(new File(
					fileLocation)));

			oos.writeObject(p);
			oos.flush();
			oos.close();
		
	}

	public static Object loadSerializedObject(String fileLocation) throws StreamCorruptedException,
			FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File (fileLocation)));
		Object o = ois.readObject();
		
		ois.close();
		return o;

	}


}
