import java.io.Serializable;

import org.freeshell.zs.common.Encryptor;

public class TestMain {

	public static void main(String[] args) {
		Person person = new Person();
		person.name = "Poo";
		person.address = "BAN";
		perform(person);
	}

	private static void perform(Serializable person) {
		Person person2 = (Person) person;
		System.out.println(person2.name);
		System.out.println(person2.address);
		
		 try {
			System.out.println(AES.encode("rules")); 
			System.out.println(AES.decode("F95430B60488BB87B4FB1AA4E0F968A0"));
			/* byte[] bytes="XYZ9876MNOP123".getBytes();
			 
			 System.out.println(Encryptor.encrypt(bytes, 64, "Poornima", "rules".getBytes()).toString());
			 System.out.println(Encryptor.decrypt(bytes, 64, "Poornima", "rules".getBytes()).toString());*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
