/*

XR - Modern Android XBMC remote
Copyright (C) 2013 Ian Whyman

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package net.v00d00.xr;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.Player;
import org.xbmc.android.jsonrpc.api.call.Player.Open.ItemPlaylistIdPosition;
import org.xbmc.android.jsonrpc.api.call.Playlist;
import org.xbmc.android.jsonrpc.api.model.PlaylistModel.Item;
import org.xbmc.android.jsonrpc.api.model.PlaylistModel.Item.Albumid;
import org.xbmc.android.jsonrpc.io.ApiCallback;
import org.xbmc.android.jsonrpc.io.ConnectionManager;

public class PlaylistHandler {
	public static final int AUDIO_PLAYLIST_ID = 0;
	public static final int VIDEO_PLAYLIST_ID = 1;

	public static void playAlbum(final ConnectionManager cm, final Albumid album, final int startTrack) {
		cm.call(new Playlist.Clear(AUDIO_PLAYLIST_ID), new ApiCallback<String>() {
			@Override
			public void onResponse(AbstractCall<String> call) {
				cm.call(new Playlist.Add(AUDIO_PLAYLIST_ID, new Item(album)), new ApiCallback<String>() {

					@Override
					public void onResponse(AbstractCall<String> call) {
						playAudioPlaylist(cm, startTrack);
					}

					@Override
					public void onError(int code, String message, String hint) {
						// TODO Auto-generated method stub

					}
				});
			}

			@Override
			public void onError(int code, String message, String hint) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void playAudioPlaylist(ConnectionManager cm, int position) {
		cm.call(new Player.Open(new ItemPlaylistIdPosition(AUDIO_PLAYLIST_ID, position)), new ApiCallback<String>() {

			@Override
			public void onResponse(AbstractCall<String> call) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(int code, String message, String hint) {
				// TODO Auto-generated method stub

			}
		});
	}
}
