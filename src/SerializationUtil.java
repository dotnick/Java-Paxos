import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SerializationUtil {

	public static byte[] serialize(Object obj) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream() ;
		ObjectOutputStream oos = new ObjectOutputStream(baos) ;
	    oos.writeObject(obj);
	    oos.close();
	    
	   return baos.toByteArray();
	}
	
	
	public static Object deSerialize(byte[] arr) throws IOException, ClassNotFoundException{
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(arr));
		return ois.readObject();
	}
}
