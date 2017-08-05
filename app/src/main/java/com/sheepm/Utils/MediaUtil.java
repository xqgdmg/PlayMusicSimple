package com.sheepm.Utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.sheepm.bean.MusicInfo;

public class MediaUtil {

	/*
	 * 获取本地音乐
	 */
	public static List<MusicInfo> getMp3Infos(Context context) {
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		int mId = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
		int mTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
		int mArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
		int mAlbum = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
		int mAlbumID = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
		int mDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
		int mSize = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
		int mUrl = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
		int mIsMusic = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);

		List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
		for (int i = 0, p = cursor.getCount(); i < p; i++) {
			cursor.moveToNext();
			MusicInfo musicInfo = new MusicInfo();
			long id = cursor.getLong(mId);
			String title = cursor.getString(mTitle);
			String artist = cursor.getString(mArtist);
			String album = cursor.getString(mAlbum);
			long albumId = cursor.getInt(mAlbumID);
			long duration = cursor.getLong(mDuration);
			long size = cursor.getLong(mSize);
			String url = cursor.getString(mUrl);
			int isMusic = cursor.getInt(mIsMusic);
			if (isMusic != 0 && url.matches(".*\\.mp3$")) {
				Log.d("song", "id:"+id+" title: "+ title + " artist:" + artist+ " album:" +album + " size:" +size );
				musicInfo.setId(id);
				musicInfo.setTitle(title);
				musicInfo.setArtist(artist);
				musicInfo.setAlbum(album);
				musicInfo.setAlbumId(albumId);
				musicInfo.setDuration(duration);
				musicInfo.setSize(size);
				musicInfo.setUrl(url);
				musicInfos.add(musicInfo);
			}
		}
		cursor.close();
		return musicInfos;
	}


}
