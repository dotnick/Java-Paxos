package com.nick.Paxos.Network;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* Used to Serialize/Deserialize Java Objects 
 * to/from UDP packets
 */

public class SerializationUtil {

	public static byte[] serialize(Object obj) {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		    oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   return baos.toByteArray();
	}
	
	
	public static Object deSerialize(byte[] arr){
		
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(arr));
			obj = ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
