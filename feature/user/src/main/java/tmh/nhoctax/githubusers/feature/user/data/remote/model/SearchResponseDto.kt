package tmh.nhoctax.githubusers.feature.user.data.remote.model

import com.google.gson.annotations.SerializedName

data class SearchResponseDto(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<UserDto>
)