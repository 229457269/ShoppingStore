package com.zhaoqiang.shoppingstore.adapter;

/**
 * Project_name : ShoppingStore
 * Author : zhaoQiang
 * Email : 229457269@qq.com
 * Create_time : 2015/10/14 | 11:06
 */

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zhaoqiang.shoppingstore.R;
import com.zhaoqiang.shoppingstore.modle.CartItem;

import java.util.List;

/**
 * 基本的购物车列表适配器
 */

public class CartAdapter extends BaseAdapter{

    private Context context;
    private List<CartItem> items;

    //当前  listview 编辑模式 包含：（0:普通模式  1，编辑模式）
    public static int listMode;

    //当某一条目  通过checkBox 选中状态发生变化的时候  回调的接口
    private CompoundButton.OnCheckedChangeListener itemCheckedListener;

    /**
     * 当点击加号  减号  的  时候处理事件
     */
    private View.OnClickListener onCountChangedListener;

    public CartAdapter(Context context, List<CartItem> items) {
        this.context = context;
        this.items = items;
    }

    /**
     * 设置接口 用于回调实现  条目的选中：
     * @param itemCheckedListener
     */
    public void setItemCheckedListener(CompoundButton.OnCheckedChangeListener itemCheckedListener) {
        this.itemCheckedListener = itemCheckedListener;
    }

    /**
     * 数量的调整
     * @param onCountChangedListener
     */
    public void setOnCountChangedListener(View.OnClickListener onCountChangedListener) {
        this.onCountChangedListener = onCountChangedListener;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(items!= null){
            ret = items.size();
        }
        return ret;
    }

    @Override
    public CartItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;

        //1，  视图复用
        if(convertView!=null){
            ret = convertView;
        }else{
            LayoutInflater inflater = LayoutInflater.from(context);
            ret = inflater.inflate(R.layout.cart_item,parent,false);
        }

        //2，viewHolder创建
        ViewHolder holder = (ViewHolder) ret.getTag();
        if (holder == null) {
            holder = new ViewHolder();

            //TODO 设置视图控件
            holder.checkBox = (CheckBox) ret.findViewById(R.id.cart_item_checkbox);

            //TODO 设置CheckBox  选中变化的事件  选中之后，改变CartItem的内容
            //！******  传递  事件监听器
            holder.checkBox.setOnCheckedChangeListener(itemCheckedListener);

            holder.imgIcon = (ImageView) ret.findViewById(R.id.cart_item_product_icon);
            holder.txtProductName = (TextView) ret.findViewById(R.id.cart_item_product_name);
            holder.txtProductPrice = (TextView) ret.findViewById(R.id.cart_item_product_price);

            holder.btnInc = ret.findViewById(R.id.cart_item_inc);
            //加号   按钮的事件处理：
            holder.btnInc.setOnClickListener(onCountChangedListener);

            holder.txtCount = (TextView) ret.findViewById(R.id.cart_item_count);

            holder.btnDec = ret.findViewById(R.id.cart_item_dec);
            //减号按钮的事件
            holder.btnDec.setOnClickListener(onCountChangedListener);

            holder.btnDelete = ret.findViewById(R.id.cart_item_delete);
            //添加删除事件
            holder.btnDelete.setOnClickListener(onCountChangedListener);
            ret.setTag(holder);
        }

        //3，获取数据
        CartItem cartItem = items.get(position);
        //4，绑定数据
        holder.txtProductName.setText(cartItem.getProductName());
        holder.txtCount.setText(items.get(position).getCount()+"");
        holder.txtProductPrice.setText(Float.toString(cartItem.getProductPrice()));

        //设置按钮的 tag：
        holder.btnInc.setTag(position);
        holder.btnDec.setTag(position);
        holder.btnDelete.setTag(position);

        //4.1 根据模式处理checkBox：

            //listView 的规则： 无论任何状态   viewHolder中的所有控件，在每一次getView的时候
            //都必须设置与刷新
        holder.checkBox.setVisibility(View.INVISIBLE);

            //设置checkBox的Tag
        holder.checkBox.setTag(position);

        if(listMode==1){
            //当编辑模式显示的  时候 checkbox  是否选中  是依赖于cartItem的变量的
            boolean isChecked = cartItem.isChecked();//该项被选中
            holder.checkBox.setChecked(isChecked);
            holder.checkBox.setVisibility(View.VISIBLE);//设置可见
            holder.btnDelete.setVisibility(View.VISIBLE);
        }else{
            //非编辑  模式  CheckBox取消选中
            holder.checkBox.setChecked(false);
            holder.btnDelete.setVisibility(View.INVISIBLE);
            //非  编辑模式  数据实体中checkedBox
            holder.checkBox.setVisibility(View.INVISIBLE);//设置不可见
        }
        return ret;
    }

    //该方法  切换内部变量  进行编辑模式的切换 因为listView 显示内容的变化
    //   需要 使用getView方法  name切换模式的时候，让adapter 进行notifitydatasetchanged()
    //强制出发  getView();
    public void switchEditMode(){
        if(listMode==1){
            listMode = 0;
        }else if (listMode==0){
            listMode = 1;
        }
        notifyDataSetChanged();
    }

//帮助类
    private static class ViewHolder{

        public CheckBox checkBox;

        public ImageView imgIcon;

        public TextView txtProductName;

        public TextView txtProductPrice;

        //添加产品  图片  按钮
        public View btnInc;

        //个数
        public TextView txtCount;

        //减少产品
        public View btnDec;

        //删除  删除产品
        public View btnDelete;
    }
}
