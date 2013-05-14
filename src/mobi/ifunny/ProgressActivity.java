package mobi.ifunny.;

import mobi.ifunny.R;
import mobi.ifunny.view.DeterminateCircleProgress;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class GifFragment extends Activity{

	private DeterminateCircleProgress dProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.progress_layout, container, false);
		dProgress = (DeterminateCircleProgress) v.findViewById(R.id.gif_determinate_progress);
		showTimer();//test
		return v;
	}

	private void showTimer() {
		dProgress.setVisibility(View.VISIBLE);
		Handler handler = new Handler();
		handler.post(new Runnable() {
			private int localPercent = 0;

			@Override
			public void run() {
				localPercent = ++localPercent % 101;
				dProgress.setPercent(localPercent);
				//
				if (dProgress.getVisibility() != View.GONE) {
					handler.postDelayed(this, 100);
				}
			}
		});
	}

}
