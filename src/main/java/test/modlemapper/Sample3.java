package test.modlemapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
class T1 {
     private String name1;
     //getter setter name1;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class T2 {
     private String name1;
     private String name2;
     private String name3 = "hello";
     //getter setter name1,name2;
}

//https://hikkisan.hatenadiary.org/entry/20121122/1353678142
public class Sample3 {


	public static void main(String... args) {
//	     T1 t1 = new T1();
//	     t1.setName1("hello");
//	     ModelMapper mapper = new ModelMapper();
//	     T2 t2 = mapper.map(t1,T2.class);
//	     System.out.println(t2.getName1());

		T1 t1 = new T1();
	     t1.setName1("hello2");
	     PropertyMap<T1,T2> pmap = new PropertyMap<T1, T2>() {
	          @Override
		  protected void configure() {
		          map().setName2(source.getName1());
		   }
	      };
	     ModelMapper mapper = new ModelMapper();
	     mapper.addMappings(pmap);
	     T2 t2 = mapper.map(t1,T2.class);
	     System.out.println(t2.getName2());
	}

}
