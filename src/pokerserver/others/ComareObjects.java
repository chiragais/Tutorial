package pokerserver.others;

import java.util.Comparator;



public class ComareObjects implements Comparator <Integer> 
{
	
	@Override
	public int compare(Integer o1, Integer o2) {
		return (o1>o2 ? -1 : (o1==o2 ? 0 : 1));
	}

//	@Override
//	public int compare(Card o1, Card o2) {
//		// TODO Auto-generated method stub
//		return (o1.getValue()>o2.getValue() ? -1 : (o1.getValue()==o2.getValue() ? 0 : 1));
//		
//	}


}
