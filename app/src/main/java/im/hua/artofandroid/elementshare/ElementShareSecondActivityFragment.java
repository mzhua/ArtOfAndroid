package im.hua.artofandroid.elementshare;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import im.hua.artofandroid.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ElementShareSecondActivityFragment extends Fragment {

    public ElementShareSecondActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_element_share_sencond, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        Glide.with(this)
                .load("http://www.gaopinimages.com/imagesetsview/244/133100650592.jpg")
                .centerCrop()
                .crossFade()
                .into(imageView);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.jellies));
    }
}
