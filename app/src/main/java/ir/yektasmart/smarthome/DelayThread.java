package ir.yektasmart.smarthome;

import android.os.AsyncTask;
import android.os.Handler;

public class DelayThread extends AsyncTask<Void, Integer , Void> {

    Handler myHandler;
    int delay;
    public DelayThread(Handler handler, int delay)
    {
        this.delay = delay;
        this.myHandler = handler;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            Thread.sleep(delay);
            myHandler.sendEmptyMessage(1);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }
}