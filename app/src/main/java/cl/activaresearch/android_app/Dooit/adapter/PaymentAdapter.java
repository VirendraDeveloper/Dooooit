package cl.activaresearch.android_app.Dooit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.activaresearch.android_app.Dooit.R;
import cl.activaresearch.android_app.Dooit.models.PaymentBean;
import cl.activaresearch.android_app.Dooit.utils.Validation;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 23 Jul,2018
 */
public class PaymentAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private Activity mContext;
    private List<PaymentBean> paymentBeans;

    public PaymentAdapter(Activity mContext) {
        paymentBeans = new ArrayList<>();
        this.mContext = mContext;
        inflater = LayoutInflater.from(this.mContext);
    }

    public void addAllTask(List<PaymentBean> tasks1) {
        paymentBeans.clear();
        paymentBeans.addAll(tasks1);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return paymentBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return paymentBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.payment_row, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final PaymentBean paymentBean = paymentBeans.get(i);
        String str= Validation.getFormattedDate(mContext, paymentBean.getRequestedOn());
        mViewHolder.tvDate.setText(str);
        mViewHolder.tvPayment.setText("$" + paymentBean.getTotal());
        return convertView;
    }

    private class ViewHolder {
        private TextView tvDate, tvPayment;

        public ViewHolder(View item) {
            tvDate = (TextView) item.findViewById(R.id.tv_date);
            tvPayment = (TextView) item.findViewById(R.id.tv_money);
        }
    }
}
