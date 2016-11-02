package com.anjuke.android.commonutils;

public class ValidateUtil {
	/**
	 * 验证手机号码格式是否正确，只验证以1开头的11位数字
	 * @param phone
	 * @return
	 */
	public static boolean phoneValidate(String phone) {
		if (phone == null) {
			return false;
		}

		return phone.matches("^1\\d{10}");
	}

	public static boolean isSmsCode(String smsCode) {
		if (smsCode == null) {
			return false;
		}

		return smsCode.matches("^\\d{4}");
	}

}
