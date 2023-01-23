package chat.sphinx.discover_tribes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import chat.sphinx.concept_image_loader.Disposable
import chat.sphinx.concept_image_loader.ImageLoader
import chat.sphinx.concept_image_loader.ImageLoaderOptions
import chat.sphinx.concept_network_query_chat.model.TribeDto
import chat.sphinx.dashboard.R
import chat.sphinx.discover_tribes.databinding.LayoutDiscoverTribeListItemHolderBinding
import chat.sphinx.discover_tribes.ui.DiscoverTribesViewModel
import io.matthewnelson.android_feature_viewmodel.util.OnStopSupervisor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoverTribesAdapter(
    private val recyclerView: RecyclerView,
    private val imageLoader: ImageLoader<ImageView>,
    private val lifecycleOwner: LifecycleOwner,
    private val onStopSupervisor: OnStopSupervisor,
    private val viewModel: DiscoverTribesViewModel,
): RecyclerView.Adapter<DiscoverTribesAdapter.DiscoverTribeViewHolder>(), DefaultLifecycleObserver {

    private inner class Diff(
        private val oldList: List<TribeDto>,
        private val newList: List<TribeDto>,
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        @Volatile
        var sameList: Boolean = oldListSize == newListSize

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return try {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]

                val same: Boolean =
                    old.owner_pubkey                 == new.owner_pubkey


                if (sameList) {
                    sameList = same
                }

                same
            } catch (e: IndexOutOfBoundsException) {
                sameList = false
                false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return try {
                val old = oldList[oldItemPosition]
                val new = newList[newItemPosition]

                val same: Boolean =
                    old.name                   == new.name                &&
                            old.description             == new.description

                if (sameList) {
                    sameList = same
                }

                same
            } catch (e: IndexOutOfBoundsException) {
                sameList = false
                false
            }
        }

    }

    private val tribeItems = ArrayList<TribeDto>(listOf())

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        onStopSupervisor.scope.launch(viewModel.mainImmediate) {
            viewModel.discoverTribesStateFlow.collect { list ->

                val discoverTribesList = mutableListOf<TribeDto>()

                list.forEach { tribe ->
                        discoverTribesList.add(tribe)
                    }

                if (tribeItems.isEmpty()) {
                    tribeItems.addAll(discoverTribesList)
                    this@DiscoverTribesAdapter.notifyDataSetChanged()
                } else {

                    val diff = Diff(tribeItems, discoverTribesList)

                    withContext(viewModel.dispatchers.default) {
                        DiffUtil.calculateDiff(diff)
                    }.let { result ->

                        if (!diff.sameList) {
                            tribeItems.clear()
                            tribeItems.addAll(discoverTribesList)
                            result.dispatchUpdatesTo(this@DiscoverTribesAdapter)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return tribeItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverTribesAdapter.DiscoverTribeViewHolder {
        val binding = LayoutDiscoverTribeListItemHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DiscoverTribeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscoverTribesAdapter.DiscoverTribeViewHolder, position: Int) {
        holder.bind(position)
    }

    private val imageLoaderOptions: ImageLoaderOptions by lazy {
        ImageLoaderOptions.Builder()
            .placeholderResId(R.drawable.ic_tribe)
            .build()
    }

    inner class DiscoverTribeViewHolder(
        private val binding: LayoutDiscoverTribeListItemHolderBinding
    ): RecyclerView.ViewHolder(binding.root), DefaultLifecycleObserver {

        private val holderJobs: ArrayList<Job> = ArrayList(2)
        private val disposables: ArrayList<Disposable> = ArrayList(2)

        private var tribe: TribeDto? = null

        init {
            binding.root.setOnClickListener {
                tribe?.let { nnTribe ->
                    lifecycleOwner.lifecycleScope.launch {
                        nnTribe.uuid?.let { nnUUID ->
                            viewModel.handleTribeLink(nnUUID)
                        }
                    }
                }
            }
        }

        fun bind(position: Int) {
            binding.apply {
                val tribeItem: TribeDto = tribeItems.getOrNull(position) ?: let {
                    tribe = null
                    return
                }
                tribe = tribeItem

                for (job in holderJobs) {
                    job.cancel()
                }

                for (disposable in disposables) {
                    disposable.dispose()
                }

                tribeItem.img?.let { imageUrl ->
                    onStopSupervisor.scope.launch(viewModel.mainImmediate) {
                        imageLoader.load(
                            imageViewTribeImage,
                            imageUrl,
                            imageLoaderOptions
                        ).also {
                            disposables.add(it)
                        }
                    }.let { job ->
                        holderJobs.add(job)
                    }
                } ?: run {
                    imageViewTribeImage.setImageDrawable(
                        ContextCompat.getDrawable(root.context, R.drawable.ic_tribe)
                    )
                }

                textViewTribeTitle.text = tribeItem.name
                textViewTribeDescription.text = tribeItem.description
            }
        }

        init {
            lifecycleOwner.lifecycle.addObserver(this)
        }

    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }
}