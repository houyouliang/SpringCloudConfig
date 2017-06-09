package com.db.inter;

import com.db.entity.Member;

@FunctionalInterface
public interface UserInter {
	void sayMessage(Member message);
}
