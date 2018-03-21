package top.liumingyi.menu3dlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    MenuFragment menuFragment = MenuFragment.newInstance();
    ContentFragment contentFragment = ContentFragment.newInstance();

    getSupportFragmentManager().beginTransaction()
        .add(R.id.menu_frame, menuFragment, "Menu")
        .add(R.id.content_frame, contentFragment, "Content")
        .commit();

    final Menu3dLayout menu3dLayout = findViewById(R.id.menu_layout);
    contentFragment.setEventListener(new ContentFragment.OpenEventListener() {
      @Override public void open() {
        menu3dLayout.toggle();
      }
    });
  }
}
