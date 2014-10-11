package com.hyx.android.Game1;

import java.io.IOException;
import java.util.List;

import com.hyx.android.Game1.R;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MusicPlayer {

	// private MediaPlayer mBgVoice = null;
	private MediaPlayer mBombVoice = null;
	// private MediaPlayer mRightVoice = null;
	private MediaPlayer mWrongVoice = null;
	private MediaPlayer mVoice = null;
	private boolean mIsMute = false;

	private MediaPlayer mIntroduceVoice = null;

	public static boolean mIsPlayFinished = true;

	public MusicPlayer(Context context) {
		// mBgVoice = MediaPlayer.create(context,R.raw.bg);
		mBombVoice = MediaPlayer.create(context, R.raw.bomb);
		// mRightVoice = MediaPlayer.create(context,R.raw.right);
		mWrongVoice = MediaPlayer.create(context, R.raw.wrong);
		mIntroduceVoice = MediaPlayer.create(context, R.raw.intro);
		mVoice = new MediaPlayer();
	}

	public void playVoice(AssetManager am, String filename) {
		try {
			beforePlayVoice();

			AssetFileDescriptor afd = am.openFd("audio/" + filename);
			mVoice = new MediaPlayer();
			mVoice.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			mVoice.prepare();

			mIsPlayFinished = false;
			mVoice.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVoice.release();
					mIsPlayFinished = true;
					afterPlayVoice();
				}

			});

			mVoice.start();
		} catch (IllegalArgumentException e) {
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	public void playFaviteVoice(AssetManager am, String filename) {
		try {
			beforePlayVoice();

			AssetFileDescriptor afd = am.openFd("audio/" + filename);
			mVoice = new MediaPlayer();
			mVoice.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			mVoice.prepare();

			mIsPlayFinished = false;
			mVoice.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVoice.release();
					mIsPlayFinished = true;
				}

			});

			mVoice.start();
		} catch (IllegalArgumentException e) {
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	public void playVoice(String filename) {
		try {

			beforePlayVoice();

			mVoice = new MediaPlayer();
			mVoice.setDataSource(filename);
			mVoice.prepare();

			mIsPlayFinished = false;
			mVoice.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVoice.release();
					mIsPlayFinished = true;
					afterPlayVoice();
				}

			});

			mVoice.start();

		} catch (IllegalArgumentException e) {
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}
	
	public void playFavoriteVoice(String filename) {
		try {

			beforePlayVoice();

			mVoice = new MediaPlayer();
			mVoice.setDataSource(filename);
			mVoice.prepare();

			mIsPlayFinished = false;
			mVoice.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVoice.release();
					mIsPlayFinished = true;
				}

			});

			mVoice.start();

		} catch (IllegalArgumentException e) {
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	public void playVoiceList(final AssetManager am,
			final List<String> filenames, final int index) {
		try {
			if (filenames.size() <= index)
				return;

			beforePlayVoice();

			String filename = filenames.get(index);
			AssetFileDescriptor afd = am.openFd("audio/" + filename);
			mVoice = new MediaPlayer();
			mVoice.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			mVoice.prepare();
			mVoice.start();
			mVoice.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVoice.release();
					int i = index + 1;
					playVoiceList(am, filenames, i);
				}

			});

		} catch (IllegalArgumentException e) {
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	public void playVoiceList(final List<String> filenames, final int index) {
		try {
			if (filenames.size() <= index)
				return;

			String filename = filenames.get(index);
			mVoice = new MediaPlayer();
			mVoice.setDataSource(filename);
			mVoice.prepare();
			mVoice.start();
			mVoice.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mVoice.release();
					int i = index + 1;
					playVoiceList(filenames, i);
				}

			});

		} catch (IllegalArgumentException e) {
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
	}

	public void StopVoice() {
		/*
		 * if(mVoice.isPlaying()) { mVoice.pause(); }
		 */
		mVoice.release();

		mIsPlayFinished = true;

	}

	public void beforePlayVoice() {
		MyGameActivity.instance.stopRecord();
	}

	public void afterPlayVoice() {
		MyGameActivity.instance.afterPlayAudio();
		MyGameActivity.instance.startRecord();
	}

	/*
	 * public void playBgVoice() { if(mIsMute) return;
	 * 
	 * //if(!Config.soundToggle())return;
	 * 
	 * mBgVoice.setLooping(true);
	 * mBgVoice.setAudioStreamType(AudioManager.STREAM_MUSIC); try {
	 * mBgVoice.prepare(); } catch (IllegalStateException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); }
	 * mBgVoice.start(); }
	 * 
	 * 
	 * public void StopBgVoice(){ if(mBgVoice.isPlaying()) { mBgVoice.pause(); }
	 * }
	 */

	public void playBombVoice() {
		if (mIsMute) {
			return;
		}
		try {
			mBombVoice.start();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void playRightVoice() {
		if (mIsMute)
			return;
		// mRightVoice.start();
	}

	public void playWrongVoice() {
		if (mIsMute)
			return;

		try {
			mWrongVoice.start();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void playIntroduceVoice() {
		if (mIsMute)
			return;
		
		try {
			mIntroduceVoice.start();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void setMute(boolean b) {
		mIsMute = b;
	}

	public void free() {
		mVoice.release();
		mBombVoice.release();
		// mRightVoice.release();
		mWrongVoice.release();
		mIntroduceVoice.release();
	}

}
