package test.modlemapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;

@Data
@NoArgsConstructor
@AllArgsConstructor
class Kind {
	private String kindId;
	private String kindName;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Single {
	private String fullname;
	private String common;
	private List<Kind> kindList;
	private List<String> list;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Multi {
	private String firstname;
	private String lastname;
	private String common;
	private Map<String,String> kindMap;
	private String bits;
}

public class ModelMapperSample {
	private final ObjectMapper objectMapper = new ObjectMapper();

	public void singleToMulti(Single single) throws JsonProcessingException {
		// Model mapper configuration
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(new PropertyMap<Single, Multi>() {
			@Override
			protected void configure() {
				using(new Converter<Single, String>(){
					public String convert(MappingContext<Single, String> context) {
						return context.getSource().getFullname().split(" ")[0];
					}
				}).map(source, destination.getFirstname());

				using(new Converter<Single, String>(){
					public String convert(MappingContext<Single, String> context) {
						return context.getSource().getFullname().split(" ")[1];
					}
				}).map(source, destination.getLastname());

				using(new Converter<Single, Map<String,String>>(){
					public Map<String,String> convert(MappingContext<Single, Map<String,String>> context) {


						return context.getSource().getKindList().stream().collect(Collectors.toMap(Kind::getKindId, Kind::getKindName));
					}
				}).map(source, destination.getKindMap());


				using(new Converter<Single, String>(){
					public String convert(MappingContext<Single, String> context) {

						List<String> list = new ArrayList<String>(5);
						for(String s : context.getSource().getList()) {
							list.add(Integer.parseInt(s), "1");
						}
						return String.join("", list);
					}
				}).map(source, destination.getBits());
			}
		});

		// Mapping and print result
		System.out.println(objectMapper.writeValueAsString(mapper.map(single, Multi.class)));
	}

	public void multiToSingle(Multi multi) throws JsonProcessingException {
		// Model mapper configuration
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(new PropertyMap<Multi, Single>() {
			@Override
			protected void configure() {
				using(new Converter<Multi, String>(){
					public String convert(MappingContext<Multi, String> context) {
						return context.getSource().getFirstname() + " " + context.getSource().getLastname();
					}
				}).map(source, destination.getFullname());
			}
		});

		// Mapping and print result
		System.out.println(objectMapper.writeValueAsString(mapper.map(multi, Single.class)));
	}

	public static void main(String[] args) throws JsonProcessingException {
		var sample = new ModelMapperSample();
		sample.singleToMulti(new Single("First Last", "common", new ArrayList<Kind>(Arrays.asList(new Kind("kindId1","kindName1"),new Kind("kindId2","kindName2"))), new ArrayList<String>(Arrays.asList("1","3"))));
		sample.multiToSingle(new Multi("First", "Last", "common", null, null));
	}
}
