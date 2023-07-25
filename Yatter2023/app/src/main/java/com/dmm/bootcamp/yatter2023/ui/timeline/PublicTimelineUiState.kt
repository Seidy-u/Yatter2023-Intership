import com.dmm.bootcamp.yatter2023.ui.timeline.bindingmodel.StatusBindingModel

data class PublicTimelineUiState(
    val statusList: List<StatusBindingModel>,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
) {
    companion object {
        fun empty(): PublicTimelineUiState = PublicTimelineUiState(
            statusList = emptyList(),
            isLoading = false,
            isRefreshing = false,
        )
    }
}