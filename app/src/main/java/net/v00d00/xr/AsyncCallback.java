package net.v00d00.xr;

/**
 * Created by ian on 09/10/14.
 */
public interface AsyncCallback {
	void onSuccess(Object result);

	void onFailure(String error);
}
