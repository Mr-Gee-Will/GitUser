package com.woniu.test;

import com.woniu.bean.User;
import com.woniu.dao.DAOFactory;

public class Test {

	public static void main(String[] args) {
		// 法复规划法国恢复规划法规
		User user = new User(3,"11","3113",2);
		DAOFactory.getUserDAO().save(user);
	}

}
