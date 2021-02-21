package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ObjectMapperSiriarizzeTest {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ObjectMapper objectMapper = new ObjectMapper();

		User user = new User(1, "theUser");

		try {
			String serialized = objectMapper.writeValueAsString(user);
			System.out.println(serialized);

			UserCustam userCustam = new UserCustam(1, "John", "Doe");
			List<User> users = new ArrayList<User>();
			users.add(user);
			userCustam.setUsers(users);

			String serializedCustam = objectMapper.writeValueAsString(userCustam);
			System.out.println(serializedCustam);

			//アノテーションを無視するマッパー
			//https://living-sun.com/ja/java/492098-is-it-possible-to-ignore-jsonserialize-annotation-java-json-serialization-jackson.html
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(MapperFeature.USE_ANNOTATIONS);
			String serializedCustam2 = mapper.writeValueAsString(userCustam);
			System.out.println(serializedCustam2);

		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	//https://www.tcmobile.jp/dev_blog/programming/jackson%E3%81%A7%E7%8B%AC%E8%87%AA%E3%81%AEjson%E3%82%B7%E3%83%AA%E3%82%A2%E3%83%A9%E3%82%A4%E3%82%BA%E3%82%92%E3%81%99%E3%82%8B/
	//参考URLはスペルミス多いので、注意


	//シリアライザーなしのモデル
	public static class User {
		public int id;
		public String name;

		public User(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
	}

	//独自シリアライザーを利用するモデル
	@JsonSerialize(using = UserSerializer.class)
	public static class UserCustam {
		private int id;
		private String firstName;
		private String lastName;
		private List<User> users;

		public List<User> getUsers() {
			return users;
		}

		public void setUsers(List<User> users) {
			this.users = users;
		}

		public UserCustam(int id, String firstName, String lastName) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public int getId() {
			return id;
		}



		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getFullName() {
			return firstName + " " + lastName;
		}
	}

	//シリアライザー
	public static class UserSerializer extends JsonSerializer<UserCustam> {

		public UserSerializer() {
		}

		@Override
		public void serialize(UserCustam value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeStartObject();
			gen.writeNumberField("id", value.getId());
			gen.writeStringField("fullName", value.getFullName());

			StringBuilder sb = new StringBuilder();
			for(User user : value.getUsers()) {
				sb.append("user.name:" + user.name);
			}
			gen.writeStringField("userList", sb.toString() );
			gen.writeEndObject();
		}

	}

}
