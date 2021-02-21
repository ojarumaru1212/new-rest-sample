package test;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class Test2 {

	 public static int PRETTY_PRINT_INDENT_FACTOR = 4;
	    public static String TEST_XML_STRING =
	        "<?xml version=\"1.0\" ?>\r\n" +
	        "<obj>\r\n" +
	        "	<User>\r\n" +
	        "		<name>fname</name>\r\n" +
	        "		<id>001</id>\r\n" +
	        "		<array>\r\n" +
	        "			<a_name>aaaaaa</a_name>\r\n" +
	        "			<a_name>bbbbb</a_name>\r\n" +
	        "		</array>\r\n" +
	        "		<opt>\r\n" +
	        "			<optName>aaaaoptaa</optName>\r\n" +
	        "		</opt>\r\n" +
	        "	</User>\r\n" +
	        "</obj>";


	public static void main(String[] args) {
		try {
            JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
            JSONObject map = xmlJSONObj.getJSONObject("obj").getJSONObject("User");

//            String str = map.getString("name");
//            System.out.println(str);
//
//            String str3 = map.optString("array", null);
//            System.out.println(str3);
//
//            String str4= map.optString("ids", null);
//            System.out.println(str4);

//            String str2 = map.getString("array");
//
//            System.out.println(str2);

            System.out.print("obj:\t");
            Object obj = getObj(xmlJSONObj.toMap(), "optName");
            System.out.println(Objects.toString(obj));

//            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
//            System.out.println(jsonPrettyPrintString);
        } catch (JSONException je) {
            System.out.println(je.toString());
        }

	}

	private static Object getObj(Map<String, Object> map, String key) {

//
//		if(map.containsKey(key)) {
//			return map.get(key);
//		}

		for(Entry<String, Object> entry : map.entrySet()) {

			if(entry.getKey().equalsIgnoreCase(key)) {
				return entry.getValue();
			}

			Object value = entry.getValue();
			if(!(value instanceof Map)) {
				continue;
			}
			@SuppressWarnings("unchecked")
			Object subObj = getObj((Map<String, Object>) value, key);

			if(Objects.nonNull(subObj)) {
				return subObj;
			}

		}

		return null;

	}

}
