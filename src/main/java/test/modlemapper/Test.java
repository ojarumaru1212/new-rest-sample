package test.modlemapper;

import java.io.Serializable;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

import lombok.Data;

public class Test {

	@Data
	public static class Single implements Serializable {
		public String fullname;
		public String common;

		public Single() {

		}

		public Single(String fullname, String common) {
			this.fullname = fullname;
			this.common = common;

		}
	}

	public static class Multi implements Serializable {
		public String firstname;
		public String lastname;
		public String common;

		public Multi() {

		}

		public Multi(String firstname,String lastname,String common) {
			this.firstname = firstname;
			this.lastname = lastname;
			this.common = common;

		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getCommon() {
			return common;
		}

		public void setCommon(String common) {
			this.common = common;
		}

	}

	public void singleToMulti(Single single) {
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
			}
		});

		// Mapping and print result
		System.out.println(mapper.map(single, Multi.class));
	}

	public void multiToSingle(Multi multi) {
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
		System.out.println(mapper.map(multi, Single.class));
	}

	public static void main(String[] args) {
		var sample = new Test();
		sample.singleToMulti(new Single("First Last", "common"));
		sample.multiToSingle(new Multi("First", "Last", "common"));
	}

}
