package chicxulub.nagaram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by trollham on 10/15/15.
 */
public class ScoreAdapter extends ArrayAdapter<Score> {
    Context context;
    List<Score> scoreList;

    public ScoreAdapter(Context ctx,  List<Score> scoreList){
        super(ctx, R.layout.list_item_scoreboard, scoreList);
        context = ctx;
        this.scoreList = scoreList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_scoreboard, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.list_item_scoreboard_textview_name);
        TextView score = (TextView) convertView.findViewById(R.id.list_item_scoreboard_textview_score);
        Score s = scoreList.get(position);
        name.setText(s.getName());
        score.setText("" + s.getScore());

        return convertView;
    }
}
