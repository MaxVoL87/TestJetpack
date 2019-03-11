package com.example.testjetpack.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.testjetpack.R
import kotlin.reflect.KFunction1


abstract class BaseRecyclerAdapter : RecyclerView.Adapter<BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>>() {

    abstract val HOLDERS: Map<Int, Pair<Int, KFunction1<@ParameterName(name = "itemView") View, BaseRecyclerItemViewHolder<out ViewDataBinding, out BaseRecyclerItemViewModel>>>>

    var itemViewModels: MutableList<out BaseRecyclerItemViewModel> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onItemClickListener: OnItemClickListener<BaseRecyclerItemViewModel>? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<BaseRecyclerItemViewModel>) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener<T: BaseRecyclerItemViewModel> {
        fun onItemClick(position: Int, item: T)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel> {
        val view: View = LayoutInflater.from(parent.context).inflate(HOLDERS[viewType]?.first ?: R.layout.item_error, parent, false)
        return BaseRecyclerItemViewHolder.castToBaseSafe(HOLDERS[viewType]?.second?.invoke(view)) ?: throw CastExeption(BaseRecyclerItemViewModel::class.java)
    }

    override fun onBindViewHolder(holder: BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>, position: Int) {
        val model = itemViewModels[position]
        holder.bind(model)?.root?.setOnClickListener {
            onItemClickListener?.onItemClick(position, model)
        }
    }

    override fun onViewRecycled(holder: BaseRecyclerItemViewHolder<ViewDataBinding, BaseRecyclerItemViewModel>) {
        holder.unbind()
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return itemViewModels.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewModels[position].itemViewType
    }
}