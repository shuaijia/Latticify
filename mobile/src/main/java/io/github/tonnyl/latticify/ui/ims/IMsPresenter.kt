package io.github.tonnyl.latticify.ui.ims

import android.content.Intent
import android.view.View
import com.airbnb.epoxy.EpoxyModel
import io.github.tonnyl.latticify.data.Channel
import io.github.tonnyl.latticify.data.repository.ConversationsRepository
import io.github.tonnyl.latticify.epoxy.IMModel_
import io.github.tonnyl.latticify.mvp.ListContract
import io.github.tonnyl.latticify.mvp.ListPresenter
import io.github.tonnyl.latticify.ui.chat.ChatActivity
import io.github.tonnyl.latticify.ui.chat.ChatPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by lizhaotailang on 06/10/2017.
 */
class IMsPresenter(mView: ListContract.View) : ListPresenter(mView) {

    override var mCursor: String = ""

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun subscribe() {
        mView.setLoadingIndicator(true)
        fetchData()
    }

    override fun unsubscribe() {
        mCompositeDisposable.clear()
    }

    override fun fetchData() {
        val disposable = ConversationsRepository.list(types = "im,mpim")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.setLoadingIndicator(false)

                    with(it.channels) {
                        if (this.isNotEmpty()) {
                            mView.showData(generateEpoxyModels(this))
                        } else {
                            mView.showEmptyView()
                        }

                    }

                    it.responseMetaData?.let {
                        mCursor = it.nextCursor
                    }
                }, {
                    it.printStackTrace()
                    mView.setLoadingIndicator(false)
                    mView.showErrorView()
                })
        mCompositeDisposable.add(disposable)
    }

    override fun fetchDataOfNextPage() {
        if (mCursor.isNotEmpty()) {
            mView.showLoadingMore(true)
            val disposable = ConversationsRepository.list(cursor = mCursor, types = "im,mpim")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mView.showLoadingMore(false)

                        if (it.channels.isNotEmpty()) {
                            mView.showDataOfNextPage(generateEpoxyModels(it.channels))
                        }

                        it.responseMetaData?.let {
                            mCursor = it.nextCursor
                        }
                    }, {
                        it.printStackTrace()
                        mView.showLoadingMore(false)
                    })
            mCompositeDisposable.add(disposable)
        }
    }

    override fun generateEpoxyModels(dataList: List<*>): Collection<EpoxyModel<*>> =
            dataList.filter { it is Channel && it.isUserDeleted == false }
                    .map { channel ->
                        IMModel_()
                                .channel(channel as Channel)
                                .itemOnClickListener(View.OnClickListener {
                                    mView.gotoActivity(Intent(it.context, ChatActivity::class.java).apply { putExtra(ChatPresenter.KEY_EXTRA_CHANNEL, channel) })
                                })
                    }

}