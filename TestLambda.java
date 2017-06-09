package com.db.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.db.entity.Member;
import com.db.inter.UserInter;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;

public class TestLambda {
	
	public static void test(Member s, UserInter inter) {
		inter.sayMessage(s);
	}
	
	static List<Member> list = new ArrayList<> ();

	static {
		list.add(new Member("a", "a", 10, "a"));
		list.add(new Member("c", "a", 11, "a"));
		list.add(new Member("e", "a", 12, "a"));
		list.add(new Member("d", "a", 13, "a"));
		list.add(new Member("f", "a", 14, "a"));
		list.add(new Member("b", "a", 15, "a"));
		list.add(new Member("g", "a", 9, "a"));
		list.add(new Member("j", "a", 8, "a"));
	}
	
	public static void main(String[] args) {
		list.sort(comparing(Member::getFirstName).reversed());
		System.out.println(list.size());
		list.stream().forEach(m -> {
			System.out.println(m);
		});
		list.forEach(System.out::println);
		int b = list.stream().map(m -> m.getAge()).reduce(0, (x, y) -> x + y);
		System.out.println(b);
		Integer i = list.stream()
		.map(m -> m.getAge() + 1).reduce(0, (x, y) -> x + y);
		System.out.println(i);
		test(list.get(0), s -> {
			System.out.println(s);
		});
		
		List<Integer> ints = Arrays.asList(9,3,4,45,6,7,8);
		//ints.sort(Integer::compare);
		Comparator<Integer> comparator = (x,y) -> {return x - y;};
//		ints.sort(comparingInt(Integer::new));
//		ints.sort((x,y) -> {
//			return y - x;
//		});
		ints.sort(comparator.reversed());
		ints.forEach(System.out::println);
	}
	
}
