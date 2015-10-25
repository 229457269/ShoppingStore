package com.zhaoqiang.shoppingstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.zhaoqiang.shoppingstore.adapter.CartAdapter;
import com.zhaoqiang.shoppingstore.com.alipay.sdk.pay.demo.PayDemoActivity;
import com.zhaoqiang.shoppingstore.modle.CartItem;

import java.util.LinkedList;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * Called when the activity is first created.
     */
    private CartAdapter adapter;
    private LinkedList<CartItem> items;
    private ListView listView;

    /**
     * 显示的总金额：
     */
    private TextView txtTotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView) findViewById(R.id.cart_list_view);
        txtTotal = (TextView) findViewById(R.id.cart_total);
        items = new LinkedList<CartItem>();


        for (int i = 0; i < 5; i++) {

            CartItem item = new CartItem();

            item.setProductName("内衣");
            item.setProductPrice(30.0f);
            item.setCount(2);
            items.add(item);
        }

        adapter = new CartAdapter(this, items);
        //由 activity 来监听内部 checkBox 内部的选中事件
        adapter.setItemCheckedListener(this);
        //由 activity
        adapter.setOnCountChangedListener(this);
        //设置adapter的数据变化观察者
        //只要adapter的notifiyDataSetChanged
        adapter.registerDataSetObserver(sumObserver);

        listView.setAdapter(adapter);

//      listView.setOnItemClickListener(this);
    }

    //计算总金额  的观察者  检测adapter
    private DataSetObserver sumObserver = new DataSetObserver() {
        /**
         * 当adapter的notifityDataSetChanged被调用
         * 那么自动回调  该方法
         */
        @Override
        public void onInvalidated() {
        }
        /**
         * 当Adapter调用notifyDataSetInvalidate（）时候回调
         */
        @Override
        public void onChanged() {
            //TODO 计算金额
            int sum = getSum();
            txtTotal.setText(sum + "元");
            super.onChanged();
        }
    };

    //遍历数据   查询数据总额
    private int getSum() {
        int sum = 0;
        for(CartItem item : items){
            boolean isChecked = item.isChecked();
            if(isChecked){
                int count = item.getCount();
                float price = item.getProductPrice();
                sum += count*price;
            }
        }
        return sum;
    }

    @Override
    protected void onDestroy() {
        adapter.unregisterDataSetObserver(sumObserver);
        super.onDestroy();
    }

    /**
     * 点击按钮
     */
    public void btnSwitchEditMode(View view){
        //调用adapter中的事件
        adapter.switchEditMode();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //设置   条目  中checkbox被选中的事件,
        Object tag = buttonView.getTag();
        int position = (Integer)tag;
        CartItem cartItem = items.get(position);
        if (tag != null && tag instanceof  Integer) {
            //数据状态改变  不需要强制notifyFataSet   当滑动时  选中状态不会因为复用而改变
            cartItem.setChecked(isChecked);//设置状态
            //计算数据总额  加上当前数据金额和数量
            int sum = getSum();
            txtTotal.setText(sum+"元");
        }
        //如果选中后  单击设置该项数量为1   金额不变
        if(!isChecked){
            cartItem.setCount(1);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        //点击接口  用于处理 listView内部的按钮的点击
        int id = v.getId();
        Object tag = v.getTag();
        switch (id){
            case R.id.cart_item_inc://数量加 1
                if (tag != null&&tag instanceof  Integer) {
                    Integer position = (Integer) tag;
                    CartItem cartItem = items.get(position);
                    int count = cartItem.getCount();
                    count++;
                    //设置增加数量：
                    cartItem.setCount(count);

                    //强制刷新adapter   更新显示数据
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.cart_item_dec://减一
                if (tag != null&&tag instanceof  Integer) {
                    Integer position = (Integer) tag;
                    CartItem cartItem = items.get(position);
                    int count = cartItem.getCount();
                    count--;
                    if(count>0){
                        //设置增加数量：
                        cartItem.setCount(count);
                        //强制刷新adapter   更新显示数据
                        adapter.notifyDataSetChanged();
                    }else{
                        //对于小于1的情况 可以不处理  也可以是删除条目
                        items.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.cart_item_delete://删除该项
                if (tag != null && tag instanceof  Integer) {
                    final Integer position = (Integer) tag;
                    //使用  对话框  确认信息
                    AlertDialog deleteDialog = new AlertDialog.Builder(this)
                            .setTitle("确认")
                            .setIcon(android.R.drawable.ic_menu_delete)
                            .setMessage("确认删除该项？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean isdelete = items.remove(items.get(position));
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }
                break;
        }
    }

    /**
     *付款调用支付包  接口支付
     */
    public void btnPay(View view){
        //传递选中的参数：
        String prices = new String();
        for (CartItem item : items){
            if (item.isChecked()){
                prices = item.getProductPrice()+"";
            }
        }
        Intent intent = new Intent(MainActivity.this, PayDemoActivity.class);
        intent.putExtra("totalPrices",txtTotal.getText().toString());
        Log.i("info",prices);
        //TODO 传递订单名称  以及钱的总数
        startActivity(intent);
     }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //设置全选事件：
    public void allChecked(View view){
        CheckBox box = (CheckBox) view;
        if (box.isChecked()){
            for(CartItem item : items){
                item.setChecked(true);
                //设置为可编辑模式：
                CartAdapter.listMode = 1;
                adapter.notifyDataSetChanged();
            }
            //        更新适配器
            adapter.notifyDataSetChanged();
        }else{
            for(CartItem item : items){
                item.setChecked(false);
            }
            //        更新适配器
            adapter.notifyDataSetChanged();
        }
    }
}
