package com.db.entity;

import com.db.annotation.DBTable;
import com.db.annotation.Id;
import com.db.annotation.SqlInteger;
import com.db.annotation.SqlString;
import com.db.inter.UserInter;
import com.db.annotation.Constraints;

@DBTable(name="t_member")
public class Member{
	@SqlString(value=30, name="first_name")
	private String firstName;
	@SqlString(value=50, name="last_name")
	private String lastName;
	@SqlInteger(name="age")
	private Integer age;
	@Id
	@SqlString(value=30, name="handle", constraints=@Constraints(primaryKey=true))
	private String handle;

	public Member() {
		super();
	}

	public Member(String firstName, String lastName, Integer age, String handle) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.handle = handle;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	@Override
	public String toString() {
		return "Member [firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", handle=" + handle
				+ "]";
	}
}
