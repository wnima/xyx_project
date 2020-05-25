package com.game.util;

import data.bean.Account;



public class AccountHelper {
	public static boolean isForbid(Account account) {
		return account.getForbid() == 1;
	}
}
