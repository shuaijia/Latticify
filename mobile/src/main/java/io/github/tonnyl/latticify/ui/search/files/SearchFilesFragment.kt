package io.github.tonnyl.latticify.ui.search.files

import io.github.tonnyl.latticify.mvp.ListFragment

/**
 * Created by lizhaotailang on 29/12/2017.
 */
class SearchFilesFragment : ListFragment() {

    override var ignoreScrollChange: Boolean = true

    companion object {
        @JvmStatic
        fun newInstance() = SearchFilesFragment()
    }

}