import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class list{
	public static void main(String[] args) 
	{
	    List<String> listA = new ArrayList<String>();
	    listA.add("And");
	    listA.add("Go");
	    listA.add("Go");
	    listA.add("Come");
	    listA.add("Come");
	    listA.add("NO");
	    listA.add("YES");
	    listA.add("Wel");
	    listA.add("Come");
	    Set<String> Strings =  findDuplicates(listA);
	    List<String> list = findDuplicatesIndex(listA,Strings);
	    System.out.println(list);
	    
	   
	   
	}
	
	private static List<String> findDuplicatesIndex(List<String> listA, Set<String> strings) {
	StringBuilder indexTodelete = new StringBuilder();
	for(String Duplicate: strings)
	{ 
		
		Boolean firstOccurance = false;
		for(int index = 0 ; index <listA.size(); index++){
			if(!firstOccurance){
				if(Duplicate.equals(listA.get(index)))
					firstOccurance = true;
			}else{
				if(Duplicate.equals(listA.get(index)))
						indexTodelete.append(index + ",") ;
			}
		}
		
	}
	indexTodelete.deleteCharAt(indexTodelete.length()-1);
	List<String> values = Arrays.asList(indexTodelete.toString().split(","));
	return values;
	}

	public static Set<String> findDuplicates(List<String> listContainingDuplicates)
	{ 
	  final Set<String> setToReturn = new HashSet(); 
	  final Set<String> set1 = new HashSet();

	  for (String yourInt : listContainingDuplicates)
	  {
	   if (!set1.add(yourInt))
	   {
	    setToReturn.add(yourInt);
	   }
	  }
	  return setToReturn;
	}
	
	
	
}