package net.v00d00.xr;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.Playlist;
import org.xbmc.android.jsonrpc.api.model.AudioModel.SongDetail;
import org.xbmc.android.jsonrpc.api.model.PlaylistModel;
import org.xbmc.android.jsonrpc.io.ApiCallback;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

public class PlaylistManager {
	private ConnectionManager cm;

	public PlaylistManager(ConnectionManager cm) {
		this.cm = cm;
	}

	public void playSong(SongDetail song) {

	}

}
