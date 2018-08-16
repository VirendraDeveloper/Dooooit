package cl.activaresearch.android_app.Dooit.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.TaskBean;
import cl.activaresearch.android_app.Dooit.servercommunication.ApiClient;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 19 Jun,2018
 */
public class TaskAdapter extends BaseAdapter implements Filterable {
    private final LayoutInflater inflater;
    private Activity mContext;
    private List<TaskBean> originalData = null;
    private List<TaskBean> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();

    public TaskAdapter(Activity mContext, List<TaskBean> taskBeans) {
        this.mContext = mContext;
        this.filteredData = taskBeans;
        this.originalData = taskBeans;
        inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_row, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final TaskBean taskBean = filteredData.get(i);
        mViewHolder.tvTaskName.setText(taskBean.getNombre());
        mViewHolder.tvCategory.setText(taskBean.getCategoria() + "");
        mViewHolder.viewLine.setBackgroundColor(Color.parseColor(taskBean.getColor()));
        mViewHolder.tvPayment.setText(taskBean.getPago() + " - ");
        mViewHolder.tvTime.setText(taskBean.getHoras() + "m - ");
        double roundOff = Math.round(taskBean.getDistancia() * 100.0) / 100.0;
        mViewHolder.tvDistance.setText(roundOff + "km");
        if (taskBean.getFoto() != null) {
            Glide.with(mContext)
                    .load(ApiClient.BASE_URL + taskBean.getFoto())
                    .into(mViewHolder.ivPhoto);
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<TaskBean> list = originalData;

            int count = list.size();
            final ArrayList<TaskBean> nlist = new ArrayList<TaskBean>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                TaskBean taskBean = list.get(i);
                filterableString = taskBean.getNombre();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(taskBean);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<TaskBean>) results.values;
            notifyDataSetChanged();
        }

    }

    private class ViewHolder {
        private TextView tvTaskName, tvCategory, tvTime, tvDistance, tvPayment;
        private ImageView ivPhoto;
        private View viewLine;

        public ViewHolder(View item) {
            tvTaskName = (TextView) item.findViewById(R.id.tv_task_name);
            ivPhoto = (ImageView) item.findViewById(R.id.iv_photo);
            tvCategory = (TextView) item.findViewById(R.id.tv_address);
            tvPayment = (TextView) item.findViewById(R.id.tv_payment);
            viewLine = (View) item.findViewById(R.id.view_line);
            tvTime = (TextView) item.findViewById(R.id.tv_time);
            tvDistance = (TextView) item.findViewById(R.id.tv_distance);
        }
    }
}
