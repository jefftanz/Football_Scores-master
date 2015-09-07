package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Created by jeff on 9/5/15.
 */
public class MyWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewFactory(getApplicationContext(), intent);
    }

    class WidgetViewFactory implements RemoteViewsFactory {
        Cursor c;
        private int mAppWidgetId;
        private Context mContext;

        public WidgetViewFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        @Override
        public void onCreate() {
            c = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDataSetChanged() {
            c = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI,null,null,null,null);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return c.getCount();
        }

        @Override
        public RemoteViews getViewAt(int i) {

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.my_widget_item);

            if(c!= null && !c.isAfterLast() && c.getCount() >= i && c.moveToPosition(i)){
                String home_name = c.getString(scoresAdapter.COL_HOME);
                String away_name = c.getString(scoresAdapter.COL_AWAY);
                String match_time = c.getString(scoresAdapter.COL_MATCHTIME);
                String score = Utilies.getScores(c.getInt(scoresAdapter.COL_HOME_GOALS),
                        c.getInt(scoresAdapter.COL_AWAY_GOALS));

                rv.setTextViewText(R.id.txtGameName_WidgetItem,home_name + " " + mContext.getString(R.string.vs) + " " + away_name);
                rv.setContentDescription(R.id.txtGameName_WidgetItem, home_name + " " + mContext.getString(R.string.vs) + " " + away_name);
                rv.setTextViewText(R.id.txtMatchTime_WidgetItem, mContext.getString(R.string.time_short) + match_time);
                rv.setContentDescription(R.id.txtMatchTime_WidgetItem, mContext.getString(R.string.time) + match_time);
                rv.setTextViewText(R.id.txtScore_WidgetItem, mContext.getString(R.string.score_short) + score);
                rv.setContentDescription(R.id.txtScore_WidgetItem, mContext.getString(R.string.score) + score);
            }

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            if(c!= null && !c.isAfterLast() && c.getCount() >= i && c.moveToPosition(i)){
                try{
                    return (long)c.getDouble(i);
                }catch(Exception ex){
                    //Log.e("ITEM ERROR", "getItemId " + ex.toString());
                    return 0 ;
                }
            }
            return 0;

        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
