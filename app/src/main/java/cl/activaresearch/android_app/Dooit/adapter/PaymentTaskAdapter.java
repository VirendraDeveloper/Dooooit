package cl.activaresearch.android_app.Dooit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.PaymentDetails;
import cl.activaresearch.android_app.Dooit.utils.Validation;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 24 Jul,2018
 */
public class PaymentTaskAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Activity mContext;
    private List<PaymentDetails.TasksBean> filteredData = null;

    public PaymentTaskAdapter(Activity mContext, List<PaymentDetails.TasksBean> taskBeans) {
        this.mContext = mContext;
        this.filteredData = taskBeans;
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
            convertView = inflater.inflate(R.layout.payment_details_row, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final PaymentDetails.TasksBean taskBean = filteredData.get(i);
        mViewHolder.tvTaskName.setText(taskBean.getName());
        mViewHolder.tvDate.setText(Validation.getFormattedDate(mContext, taskBean.getValidatedOn()));
        mViewHolder.tvPayment.setText("$"+taskBean.getPayment());
        return convertView;
    }

    private class ViewHolder {
        private TextView tvTaskName, tvDate, tvPayment;

        public ViewHolder(View item) {
            tvDate = (TextView) item.findViewById(R.id.tv_date);
            tvTaskName = (TextView) item.findViewById(R.id.tv_task_name);
            tvPayment = (TextView) item.findViewById(R.id.tv_bal);
        }
    }
}
