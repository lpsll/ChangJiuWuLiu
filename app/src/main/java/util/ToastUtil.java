package util;

import android.content.Context;
import android.widget.Toast;

import com.htlc.cjwl.util.CommonUtil;

public class ToastUtil {
	public static Toast mToast;

	/**
	 * 立即连续弹吐司
	 * @param mContext
	 * @param msg
	 */
	public static void showToast(final Context mContext, final String msg) {
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
				}
				mToast.setText(msg);
				mToast.show();

			}
		});
	}
	/**
	 * 立即连续弹吐司
	 * @param mContext
	 * @param msgId
	 */
	public static void showToast(final Context mContext, final int msgId) {
		CommonUtil.runOnUIThread(new Runnable() {
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
				}
				mToast.setText(msgId);
				mToast.show();

			}
		});
	}
}
