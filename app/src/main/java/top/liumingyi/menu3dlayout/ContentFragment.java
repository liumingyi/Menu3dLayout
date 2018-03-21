package top.liumingyi.menu3dlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContentFragment extends Fragment {

  public static ContentFragment newInstance() {
    Bundle args = new Bundle();
    ContentFragment fragment = new ContentFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_content, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    view.findViewById(R.id.switchBtn).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        listener.open();
      }
    });
  }

  public interface OpenEventListener {
    void open();
  }

  OpenEventListener listener;

  public void setEventListener(OpenEventListener listener) {
    this.listener = listener;
  }
}
