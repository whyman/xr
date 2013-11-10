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

package net.v00d00.xr.fragment;

import java.util.List;

import net.v00d00.xr.R;
import net.v00d00.xr.view.FixedHeightImageView;
import net.v00d00.xr.view.FixedHeightRatioImageView;

import org.xbmc.android.jsonrpc.api.AbstractCall;
import org.xbmc.android.jsonrpc.api.call.AudioLibrary.GetSongDetails;
import org.xbmc.android.jsonrpc.api.call.Player;
import org.xbmc.android.jsonrpc.api.call.Player.GetActivePlayers;
import org.xbmc.android.jsonrpc.api.call.Player.GetActivePlayers.GetActivePlayersResult;
import org.xbmc.android.jsonrpc.api.model.AudioModel.SongDetail;
import org.xbmc.android.jsonrpc.api.model.ListModel;
import org.xbmc.android.jsonrpc.api.model.ListModel.AllItems;
import org.xbmc.android.jsonrpc.api.model.ListModel.BaseItem;
import org.xbmc.android.jsonrpc.io.ApiCallback;
import org.xbmc.android.jsonrpc.io.ConnectionManager.NotificationObserver;
import org.xbmc.android.jsonrpc.notification.PlayerEvent;
import org.xbmc.android.jsonrpc.notification.PlayerEvent.Pause;
import org.xbmc.android.jsonrpc.notification.PlayerEvent.Play;
import org.xbmc.android.jsonrpc.notification.PlayerEvent.Seek;
import org.xbmc.android.jsonrpc.notification.PlayerEvent.SpeedChanged;
import org.xbmc.android.jsonrpc.notification.PlayerEvent.Stop;
import org.xbmc.android.jsonrpc.notification.PlayerObserver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayingBarFragment extends AbstractXRFragment implements NotificationObserver {

	private RelativeLayout bar;
	private TextView title;
	private FixedHeightImageView image;
	private TextView subtitle;
	private FixedHeightRatioImageView bigImage;

	public interface Provider {
		public void setDragView(View view);
	}

	private PlayerObserver playerObserver;

	private class PlayingBarObserver extends PlayerObserver {
		public void onPlay(Play notification) {
			if (notification.data.item.type == PlayerEvent.Item.Type.SONG) {
				GetSongDetails call = new GetSongDetails(notification.data.item.id, SongDetail.DISPLAYARTIST, SongDetail.THUMBNAIL, SongDetail.TITLE);

				getConnectionManager().call(call, new ApiCallback<SongDetail>() {
					@Override
					public void onResponse(AbstractCall<SongDetail> call) {
						SongDetail detail = call.getResult();
						showNowPlaying(detail.title, detail.displayartist, detail.thumbnail);
					}

					@Override
					public void onError(int code, String message, String hint) {
						// TODO Auto-generated method stub

					}

				});
			}
		}
		public void onPause(Pause notification) {
		}
		public void onStop(Stop notification) {
			showNowPlaying("", "", null);
		}
		public void onSpeedChanged(SpeedChanged notification) {
		}
		public void onSeek(Seek notification) {
		}
	}

	private void showNowPlaying(String title, String subtitle, String image) {
		this.title.setText(title);
		this.subtitle.setText(subtitle);
		this.image.setThumbnailPath(image);
		this.bigImage.setThumbnailPath(image);
	}

	public PlayingBarFragment() {};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.playing, container, false);

		bar = (RelativeLayout) view.findViewById(R.id.now_playing_bar);
		image = (FixedHeightImageView) view.findViewById(R.id.now_playing_bar_img);
		title = (TextView) view.findViewById(R.id.now_playing_bar_title);
		subtitle = (TextView) view.findViewById(R.id.now_playing_bar_subtitle);
		bigImage = (FixedHeightRatioImageView) view.findViewById(R.id.now_playing_bar_img_big);

		// Make marquee mode work
		title.setSelected(true);
		subtitle.setSelected(true);

		Activity activity = getActivity();
		if (activity instanceof Provider)
			((Provider) activity).setDragView(bar);
		else
			throw new ClassCastException(activity.toString() + " does not implement ConnectionManagerProvider");

		return view;
	}

	@Override
	protected void load() {
		getConnectionManager().call(new Player.GetActivePlayers(), new ApiCallback<GetActivePlayers.GetActivePlayersResult>() {

			@Override
			public void onResponse(AbstractCall<GetActivePlayersResult> call) {
				List<GetActivePlayersResult> players = call.getResults();
				for (GetActivePlayersResult player : players) {
						Player.GetItem getSongItem = new Player.GetItem(player.playerid, BaseItem.TITLE, BaseItem.DISPLAYARTIST, BaseItem.THUMBNAIL);
						getConnectionManager().call(getSongItem, new ApiCallback<ListModel.AllItems>() {
							@Override
							public void onResponse(AbstractCall<AllItems> call) {
								AllItems items = call.getResult();
								showNowPlaying(items.title, items.displayartist, items.thumbnail);
							}

							@Override
							public void onError(int code, String message, String hint) {
								// TODO Auto-generated method stub

							}
						});
					}
			}

			@Override
			public void onError(int code, String message, String hint) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public PlayerObserver getPlayerObserver() {
		if (playerObserver == null)
			playerObserver = new PlayingBarObserver();

		return playerObserver;
	}

	@Override
	public void onConnected() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(int code, String message, String hint) {
		Context context = getActivity().getApplicationContext();
		CharSequence text = Integer.toString(code) + " " + message + "\n" + hint;
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public CharSequence getTitle() {
		return "Now Playing";
	}
}
