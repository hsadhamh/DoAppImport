package factor.labs.indiancalendar.utils.serializer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class JsonSerializer {
	
	ObjectMapper mObjMapper = null;
	
	static JsonSerializer mSerializer = null;
	public static JsonSerializer getInstance(){
		if(mSerializer == null)
			mSerializer = new JsonSerializer();
		return mSerializer;
	}
	
	public JsonSerializer(){
		mObjMapper = new ObjectMapper();
		mObjMapper.setSerializationInclusion(Include.NON_NULL);
	}	
	
	public String SerializeToString(Object obj, boolean printPretty) 
			throws JsonProcessingException, UnsupportedEncodingException{
		
		if(printPretty)
			return mObjMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		
		return mObjMapper.writeValueAsString(obj);
	}
	
	public <T> Object UnserializeToObject(String sJson, Class<T> valueType) 
			throws JsonParseException, JsonMappingException, IOException{
		return mObjMapper.readValue(sJson, valueType);
	}
}
