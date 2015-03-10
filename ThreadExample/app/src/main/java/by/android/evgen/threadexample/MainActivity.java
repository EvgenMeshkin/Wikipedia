package by.android.evgen.threadexample;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public Handler mHandler;
    public boolean FLAG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Channel> chanels = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            chanels.add(new Channel(i, "Title: " + i, "desc: " + i, "image url: " + i));
        }
        Thread write = new Thread(new WriterThread(chanels));
        Thread writeadd = new Thread(new WriterThread(chanels));
        Thread reader1 = new Thread(new ReaderThread(chanels));
        Thread reader2 = new Thread(new ReaderThread(chanels));
        Thread reader3 = new Thread(new ReaderThread(chanels));
        Thread update = new Thread(new UpdateThread(chanels));
        write.start();
        writeadd.start();
        update.start();
        reader1.start();
        reader2.start();
        reader3.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WriterThread implements Runnable {

        private List<Channel> data;
        Handler handler;

        public WriterThread(List<Channel> data){
             handler = new Handler();
             this.data = data;
        }

        @Override
        public void run() {
            FLAG = false;
            Channel chanel = new Channel(data.size(), "Title: " + data.size(), "desc: " + data.size(), "image url: " + data.size());
            data.add(chanel);
            Log.d("Write chanel: ", chanel.getId() + chanel.getTitle() + chanel.getDesc() + chanel.getImageUrl());
            handler.postDelayed(this, 9000);
        }
    }

    private class UpdateThread implements Runnable {

        private List<Channel> data;
        Handler handler;

        public UpdateThread(List<Channel> data){
            handler = new Handler();
            this.data = data;
        }

        @Override
        public void run() {
            for (int i = 0; i < data.size(); i++) {
                data.set(i, new Channel(i, "New Title: " + i, "New desc: " + i, "New image url: " + i) );
            }
            FLAG = true;
            Log.d("Update chanel: ", "true");
            handler.postDelayed(this, 15000);
        }
    }

    private class ReaderThread implements Runnable {

        private List<Channel> data;
        Handler handler;

        public ReaderThread(List<Channel> data){
            handler = new Handler();
            this.data = data;

        }

        @Override
        public void run() {
            if (FLAG) {
                Log.d("Read chanel: ", data.get(data.size() - 1).getId() + data.get(data.size() - 1).getTitle() + data.get(data.size() - 1).getDesc() + data.get(data.size() - 1).getImageUrl());
            }
            handler.postDelayed(this, 12000);
        }
    }


}
