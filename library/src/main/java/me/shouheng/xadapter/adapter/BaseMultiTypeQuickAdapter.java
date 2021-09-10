package me.shouheng.xadapter.adapter;

import android.util.SparseIntArray;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Custom typed quick adapter as {@link com.chad.library.adapter.base.BaseMultiItemQuickAdapter}.
 * This adapter use the class type id as the item view type, so you don't need to implement
 * any interfaces and extend any classes.
 *
 * @Author wangshouheng
 * @Time 2021/9/10
 */
public abstract class BaseMultiTypeQuickAdapter <T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    /** layouts indexed with their types */
    private SparseIntArray layouts;

    private static final int DEFAULT_VIEW_TYPE = -0xff;
    public static final int TYPE_NOT_FOUND = -404;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseMultiTypeQuickAdapter(List<T> data) {
        super(data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        T item = mData.get(position);
        if (item != null) {
            return item.getClass().hashCode();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    /**
     * Add one item type. By default the type should be the supported data class's hash code.
     * Also you can also call {@link #addItemType(Class, int)} directly.
     *
     * @param type the type of item
     * @param layoutResId the type layout id
     */
    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }

    /**
     * Add one item type by its class type.
     *
     * @param type the type of item
     * @param layoutResId the layout id.
     */
    protected void addItemType(Class<? extends T> type, @LayoutRes int layoutResId) {
        addItemType(type.hashCode(), layoutResId);
    }

    @Override
    public void remove(@IntRange(from = 0L) int position) {
        if (mData == null || position < 0 || position >= mData.size()) return;

        T entity = mData.get(position);
        if (entity instanceof IExpandable) {
            removeAllChild((IExpandable) entity, position);
        }
        removeDataFromParent(entity);
        super.remove(position);
    }

    /**
     * 移除父控件时，若父控件处于展开状态，则先移除其所有的子控件
     *
     * @param parent         父控件实体
     * @param parentPosition 父控件位置
     */
    protected void removeAllChild(IExpandable parent, int parentPosition) {
        if (parent.isExpanded()) {
            List<MultiItemEntity> chidChilds = parent.getSubItems();
            if (chidChilds == null || chidChilds.size() == 0) return;

            int childSize = chidChilds.size();
            for (int i = 0; i < childSize; i++) {
                remove(parentPosition + 1);
            }
        }
    }

    /**
     * 移除子控件时，移除父控件实体类中相关子控件数据，避免关闭后再次展开数据重现
     *
     * @param child 子控件实体
     */
    protected void removeDataFromParent(T child) {
        int position = getParentPosition(child);
        if (position >= 0) {
            IExpandable parent = (IExpandable) mData.get(position);
            if (parent != child) {
                parent.getSubItems().remove(child);
            }
        }
    }

    /**
     * 该方法用于 IExpandable 树形列表。
     * 如果不存在 Parent，则 return -1。
     *
     * @param position 所处列表的位置
     * @return 父 position 在数据列表中的位置
     */
    public int getParentPositionInAll(int position) {
        List<T> data = getData();
        T item = getItem(position);

        if (isExpandable(item)) {
            IExpandable IExpandable = (IExpandable) item;
            for (int i = position - 1; i >= 0; i--) {
                T entity = data.get(i);
                if (isExpandable(entity) && IExpandable.getLevel() > ((IExpandable) entity).getLevel()) {
                    return i;
                }
            }
        } else {
            for (int i = position - 1; i >= 0; i--) {
                T entity = data.get(i);
                if (isExpandable(entity)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean isExpandable(MultiItemEntity item) {
        return item != null && item instanceof IExpandable;
    }
}

