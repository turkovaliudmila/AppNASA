package ru.geekbrains.appnasa.material.ui.earth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_picture_of_earth.view.*
import ru.geekbrains.appnasa.R

class EarthRecyclerAdapter : RecyclerView.Adapter<EarthRecyclerAdapter.EarthViewHolder>() {

    private var data: List<PictureOfTheEarthItem> = arrayListOf()

    fun setData(data: List<PictureOfTheEarthItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthViewHolder {
        return EarthViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_picture_of_earth, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: EarthViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class EarthViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: PictureOfTheEarthItem) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.url.text = data.pathToImage
                //itemView.image_view_earth.load(data.pathToImage) //{
//                    lifecycle(context)
//                    error(R.drawable.ic_baseline_file_download_off_24)
//                    placeholder(R.drawable.ic_baseline_file_download_24)
//                }
            }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: PictureOfTheEarthItem)
    }

}
