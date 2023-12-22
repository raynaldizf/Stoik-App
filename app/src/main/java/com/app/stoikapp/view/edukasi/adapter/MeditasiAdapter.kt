import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.app.stoikapp.data.model.MeditasiSong
import com.app.stoikapp.databinding.CustomMeditasiBinding
import com.bumptech.glide.Glide

class MeditasiAdapter(private val context: Context, private val meditasiList: MutableList<MeditasiSong>) :
    RecyclerView.Adapter<MeditasiAdapter.ViewHolder>() {

    class ViewHolder(val binding: CustomMeditasiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CustomMeditasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meditasiSong = meditasiList[position]

        Glide.with(holder.itemView.context)
            .load(meditasiSong.banner)
            .into(holder.binding.imageView)

        holder.binding.pencipta.text = meditasiSong.pencipta
        holder.binding.judulLagu.text = meditasiSong.judul
        holder.binding.cardView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("judul", meditasiSong.judul)
            bundle.putString("pencipta", meditasiSong.pencipta)
            bundle.putString("banner", meditasiSong.banner)
            bundle.putString("url", meditasiSong.link_lagu)
            Navigation.findNavController(it).navigate(com.app.stoikapp.R.id.musicPlayFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return meditasiList.size
    }

    fun updateData(newData: List<MeditasiSong>) {
        meditasiList.clear()
        meditasiList.addAll(newData)
        notifyDataSetChanged()
    }
}
