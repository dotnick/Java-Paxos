package com.nick.Paxos;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* Used to Serialize/Deserialize Java Objects 
 * to/from UDP packets
 */

public class SerializationUtil {

	public static byte[] serialize(Object obj) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		ObjectOutputStream oos = new ObjectOutputStream(baos) ;
	    oos.writeObject(obj);
	    oos.flush();
	    
	   return baos.toByteArray();
	}
	
	
	public static Object deSerialize(byte[] arr) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(arr));
		return ois.readObject();
	}
}
